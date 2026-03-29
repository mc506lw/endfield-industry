package top.mc506lw.rebar.endfield_industry.content.powersystem.devices

import io.github.pylonmc.rebar.block.BlockStorage
import io.github.pylonmc.rebar.block.base.RebarBreakHandler
import io.github.pylonmc.rebar.block.base.RebarSimpleMultiblock
import io.github.pylonmc.rebar.block.context.BlockBreakContext
import io.github.pylonmc.rebar.block.context.BlockCreateContext
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import top.mc506lw.rebar.endfield_industry.EndfieldIndustryKeys
import top.mc506lw.rebar.endfield_industry.content.machines.MachineComponent
import top.mc506lw.rebar.endfield_industry.content.powersystem.PowerConsumer
import top.mc506lw.rebar.endfield_industry.content.powersystem.PowerGrid
import top.mc506lw.rebar.endfield_industry.content.powersystem.PowerSystem
import top.mc506lw.rebar.endfield_industry.content.powersystem.event.PowerDeviceDisconnectEvent
import java.util.UUID

abstract class PowerConsumerDevice : MachineComponent, PowerConsumer, RebarBreakHandler {
    
    constructor(block: Block, context: BlockCreateContext) : super(block, context)
    
    @Suppress("unused")
    constructor(block: Block, pdc: PersistentDataContainer) : super(block, pdc) {
        val gridIdMost = pdc.get(EndfieldIndustryKeys.key("grid_id_most"), PersistentDataType.LONG)
        val gridIdLeast = pdc.get(EndfieldIndustryKeys.key("grid_id_least"), PersistentDataType.LONG)
        if (gridIdMost != null && gridIdLeast != null) {
            val gridId = UUID(gridIdMost, gridIdLeast)
            pendingGridId = gridId
        }
    }

    private var connectedGrid: PowerGrid? = null
    override var isPowered: Boolean = false
    private var pendingGridId: UUID? = null

    fun getConnectedGrid(): PowerGrid? = connectedGrid
    
    fun getPendingGridId(): UUID? = pendingGridId
    
    fun clearPendingGridId() {
        pendingGridId = null
    }

    fun connectToGrid(grid: PowerGrid) {
        if (this.connectedGrid != null && this.connectedGrid != grid) {
            disconnectFromGrid()
        }
        this.connectedGrid = grid
        grid.addConsumer(this)
    }

    fun disconnectFromGrid() {
        connectedGrid?.let {
            it.removeConsumer(this)
            connectedGrid = null
        }
    }
    
    override fun write(pdc: PersistentDataContainer) {
        val gridId = connectedGrid?.gridId
        if (gridId != null) {
            pdc.set(EndfieldIndustryKeys.key("grid_id_most"), PersistentDataType.LONG, gridId.mostSignificantBits)
            pdc.set(EndfieldIndustryKeys.key("grid_id_least"), PersistentDataType.LONG, gridId.leastSignificantBits)
        }
    }
    
    fun tryConnectToNearbyPowerStation(): Boolean {
        val range = PowerSystem.config.powerStationSupplyRange
        val halfRange = range / 2
        
        for (dx in -halfRange..halfRange) {
            for (dz in -halfRange..halfRange) {
                val pos = block.location.add(dx.toDouble(), 0.0, dz.toDouble()).block
                val rebarBlock = BlockStorage.get(pos)
                
                if (rebarBlock is PowerStationEmitter) {
                    if (!rebarBlock.isFormedAndFullyLoaded()) continue
                    
                    val grid = rebarBlock.getGrid()
                    if (grid != null) {
                        connectToGrid(grid)
                        return true
                    }
                }
            }
        }
        return false
    }

    override fun onBreak(drops: MutableList<ItemStack>, context: BlockBreakContext) {
        disconnectFromGrid()
        
        val disconnectEvent = PowerDeviceDisconnectEvent(this)
        disconnectEvent.callEvent()
    }
}
