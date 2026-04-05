package top.mc506lw.rebar.endfield_industry.content.powersystem.devices

import io.github.pylonmc.rebar.block.BlockStorage
import io.github.pylonmc.rebar.block.base.RebarBreakHandler
import io.github.pylonmc.rebar.block.context.BlockBreakContext
import io.github.pylonmc.rebar.block.context.BlockCreateContext
import org.bukkit.Bukkit
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import top.mc506lw.rebar.endfield_industry.EndfieldIndustry
import top.mc506lw.rebar.endfield_industry.EndfieldIndustryKeys
import top.mc506lw.rebar.endfield_industry.content.powersystem.PowerGrid
import top.mc506lw.rebar.endfield_industry.content.powersystem.PowerSystem
import top.mc506lw.rebar.endfield_industry.content.machines.MachineComponent
import top.mc506lw.rebar.endfield_industry.content.powersystem.connection.PowerConnection
import top.mc506lw.rebar.endfield_industry.content.powersystem.event.PowerDeviceDisconnectEvent
import top.mc506lw.rebar.endfield_industry.content.powersystem.storage.PowerSystemStorage
import java.util.UUID

abstract class PowerDevice : MachineComponent, RebarBreakHandler {
    
    private var grid: PowerGrid? = null
    private var previousPowerContribution: Int = 0
    private var pendingGridId: UUID? = null
    private var connectionRestored: Boolean = false
    
    constructor(block: Block, context: BlockCreateContext) : super(block, context)
    
    @Suppress("unused")
    constructor(block: Block, pdc: PersistentDataContainer) : super(block, pdc) {
        val gridIdMost = pdc.get(EndfieldIndustryKeys.key("grid_id_most"), PersistentDataType.LONG)
        val gridIdLeast = pdc.get(EndfieldIndustryKeys.key("grid_id_least"), PersistentDataType.LONG)
        if (gridIdMost != null && gridIdLeast != null) {
            pendingGridId = UUID(gridIdMost, gridIdLeast)
        }
    }
    
    override fun postLoad() {
        super.postLoad()
        
        Bukkit.getScheduler().runTaskLater(EndfieldIndustry.instance, Runnable {
            restoreConnectionsFromStorage()
        }, 20L)
    }
    
    fun triggerConnectionRestore() {
        restoreConnectionsFromStorage()
    }
    
    private fun restoreConnectionsFromStorage() {
        if (connectionRestored) {
            return
        }
        connectionRestored = true
        
        val pendingConnections = PowerSystemStorage.getPendingConnectionsForDevice(this)
        
        if (pendingConnections.isEmpty()) {
            return
        }
        
        val myLoc = block.location
        val world = myLoc.world ?: return
        
        EndfieldIndustry.instance.logger.info(
            "[PowerDevice] 从存储恢复 ${pendingConnections.size} 个连接，位置: (${myLoc.blockX}, ${myLoc.blockY}, ${myLoc.blockZ})"
        )
        
        val restoredConnectionKeys = mutableSetOf<String>()
        
        for (connData in pendingConnections) {
            val targetWorldUid = connData.targetWorldUid
            val targetX = connData.targetX
            val targetY = connData.targetY
            val targetZ = connData.targetZ
            val distance = connData.distance
            
            if (world.uid != targetWorldUid) {
                continue
            }
            
            val chunkX = targetX shr 4
            val chunkZ = targetZ shr 4
            
            if (!world.isChunkLoaded(chunkX, chunkZ)) {
                EndfieldIndustry.instance.logger.info(
                    "[PowerDevice] 目标区块($chunkX, $chunkZ)未加载，跳过连接到($targetX, $targetY, $targetZ)"
                )
                continue
            }
            
            val targetBlock = world.getBlockAt(targetX, targetY, targetZ)
            val targetRebarBlock = BlockStorage.get(targetBlock)
            
            if (targetRebarBlock == null) {
                EndfieldIndustry.instance.logger.info(
                    "[PowerDevice] 目标设备不存在（BlockStorage返回null），位置: ($targetX, $targetY, $targetZ)"
                )
                continue
            }
            
            if (targetRebarBlock !is PowerDevice) {
                EndfieldIndustry.instance.logger.info(
                    "[PowerDevice] 目标不是PowerDevice（类型: ${targetRebarBlock.javaClass.simpleName}），跳过"
                )
                continue
            }
            
            val targetDevice = targetRebarBlock
            
            if (targetDevice == this) {
                continue
            }
            
            if (PowerSystem.connectionManager.areConnected(this, targetDevice)) {
                restoredConnectionKeys.add(getConnectionKey(connData))
                continue
            }
            
            if (!shouldRestoreConnection(targetDevice)) {
                continue
            }
            
            val path = listOf(block, targetBlock)
            val connection = PowerConnection(this, targetDevice, path, distance)
            
            PowerSystem.connectionManager.restoreConnection(connection)
            
            restoreGridConnection(targetDevice)
            
            restoredConnectionKeys.add(getConnectionKey(connData))
            
            EndfieldIndustry.instance.logger.info(
                "[PowerDevice] 成功恢复连接到 ($targetX, $targetY, $targetZ)"
            )
        }
        
        if (restoredConnectionKeys.isNotEmpty()) {
            PowerSystemStorage.removeRestoredConnections(restoredConnectionKeys)
        }
    }
    
    private fun getConnectionKey(connData: PowerSystemStorage.ConnectionData): String {
        val coords = listOf(
            Triple(connData.sourceWorldUid, connData.sourceX, connData.sourceY to connData.sourceZ),
            Triple(connData.targetWorldUid, connData.targetX, connData.targetY to connData.targetZ)
        ).sortedBy { "${it.first}:${it.second}:${it.third.first}:${it.third.second}" }
        
        val first = coords[0]
        val second = coords[1]
        return "${first.first}:${first.second}:${first.third.first}:${first.third.second}-${second.first}:${second.second}:${second.third.first}:${second.third.second}"
    }
    
    private fun shouldRestoreConnection(other: PowerDevice): Boolean {
        val myLoc = block.location
        val otherLoc = other.block.location
        
        if (myLoc.world != otherLoc.world) {
            return myLoc.world?.uid?.compareTo(otherLoc.world?.uid ?: return true)!! < 0
        }
        
        if (myLoc.blockX != otherLoc.blockX) return myLoc.blockX < otherLoc.blockX
        if (myLoc.blockY != otherLoc.blockY) return myLoc.blockY < otherLoc.blockY
        if (myLoc.blockZ != otherLoc.blockZ) return myLoc.blockZ < otherLoc.blockZ
        
        return false
    }
    
    private fun restoreGridConnection(other: PowerDevice) {
        val myGrid = getGrid()
        val otherGrid = other.getGrid()
        
        when {
            myGrid == null && otherGrid == null -> {
                val newGrid = PowerSystem.gridManager.createGrid()
                connectToGrid(newGrid)
                other.connectToGrid(newGrid)
            }
            myGrid == null -> {
                connectToGrid(otherGrid!!)
            }
            otherGrid == null -> {
                other.connectToGrid(myGrid)
            }
            myGrid != otherGrid -> {
                PowerSystem.gridManager.mergeGrids(myGrid.gridId, otherGrid.gridId)
            }
        }
        
        val finalGrid = getGrid() ?: other.getGrid()
        if (finalGrid != null) {
            val station = when {
                this is PowerStationEmitter -> this
                other is PowerStationEmitter -> other
                else -> null
            }
            
            if (station != null) {
                notifyNearbyConsumersForStation(station, finalGrid)
            }
        }
    }
    
    private fun notifyNearbyConsumersForStation(station: PowerStationEmitter, grid: PowerGrid) {
        val range = PowerSystem.config.powerStationSupplyRange
        val halfRange = range / 2
        val center = station.block
        
        for (dx in -halfRange..halfRange) {
            for (dz in -halfRange..halfRange) {
                val pos = center.location.add(dx.toDouble(), 0.0, dz.toDouble()).block
                val rebarBlock = BlockStorage.get(pos)
                
                if (rebarBlock is PowerConsumerDevice) {
                    if (rebarBlock.getConnectedGrid() == null) {
                        rebarBlock.connectToGrid(grid)
                    }
                }
            }
        }
    }

    fun getGrid(): PowerGrid? = grid

    fun setGrid(grid: PowerGrid?) {
        this.grid = grid
    }
    
    fun getPendingGridId(): UUID? = pendingGridId
    
    fun clearPendingGridId() {
        pendingGridId = null
    }

    fun getGridId(): UUID? = grid?.gridId

    abstract fun getPowerContribution(): Int

    fun getPreviousPowerContribution(): Int = previousPowerContribution

    fun updatePowerContribution() {
        grid?.let {
            previousPowerContribution = getPowerContribution()
            it.updateDevice(this)
        }
    }

    fun connectToGrid(grid: PowerGrid) {
        if (this.grid != null && this.grid != grid) {
            this.grid?.removeDevice(this)
        }
        this.grid = grid
        previousPowerContribution = getPowerContribution()
        grid.addDevice(this)
    }

    fun disconnectFromGrid() {
        grid?.let {
            it.removeDevice(this)
            grid = null
            previousPowerContribution = 0
        }
    }
    
    override fun write(pdc: PersistentDataContainer) {
        val gridId = grid?.gridId
        if (gridId != null) {
            pdc.set(EndfieldIndustryKeys.key("grid_id_most"), PersistentDataType.LONG, gridId.mostSignificantBits)
            pdc.set(EndfieldIndustryKeys.key("grid_id_least"), PersistentDataType.LONG, gridId.leastSignificantBits)
        }
    }

    override fun onBreak(drops: MutableList<ItemStack>, context: BlockBreakContext) {
        val connections = PowerSystem.connectionManager.getConnections(this).toList()
        for (conn in connections) {
            PowerSystem.connectionManager.removeConnection(conn)
        }
        
        if (getGrid() != null) {
            disconnectFromGrid()
        }
        
        val disconnectEvent = PowerDeviceDisconnectEvent(this)
        disconnectEvent.callEvent()
    }
}
