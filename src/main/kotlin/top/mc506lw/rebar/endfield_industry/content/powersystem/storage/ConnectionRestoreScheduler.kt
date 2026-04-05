package top.mc506lw.rebar.endfield_industry.content.powersystem.storage

import io.github.pylonmc.rebar.block.BlockStorage
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.world.WorldLoadEvent
import top.mc506lw.rebar.endfield_industry.EndfieldIndustry
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerDevice
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

object ConnectionRestoreScheduler : Listener {
    
    private val logger = EndfieldIndustry.instance.logger
    
    enum class ConnectionState {
        PENDING,
        SOURCE_READY,
        TARGET_READY,
        READY,
        RESTORED,
        FAILED
    }
    
    data class PendingConnection(
        val connectionData: PowerSystemStorage.ConnectionData,
        var state: ConnectionState = ConnectionState.PENDING,
        var sourceLoaded: Boolean = false,
        var targetLoaded: Boolean = false,
        var restoreAttempted: Boolean = false,
        var failCount: Int = 0
    )
    
    private val pendingConnections = ConcurrentHashMap<String, PendingConnection>()
    private val deviceConnections = ConcurrentHashMap<String, MutableSet<String>>()
    
    fun initialize() {
        Bukkit.getPluginManager().registerEvents(this, EndfieldIndustry.instance)
        logger.info("[ConnectionRestoreScheduler] 连接恢复调度器已初始化")
    }
    
    fun registerPendingConnection(connData: PowerSystemStorage.ConnectionData) {
        val key = generateConnectionKey(connData)
        
        if (pendingConnections.containsKey(key)) {
            return
        }
        
        val pending = PendingConnection(connData)
        pendingConnections[key] = pending
        
        val sourceKey = "${connData.sourceWorldUid}:${connData.sourceX}:${connData.sourceY}:${connData.sourceZ}"
        val targetKey = "${connData.targetWorldUid}:${connData.targetX}:${connData.targetY}:${connData.targetZ}"
        
        deviceConnections.computeIfAbsent(sourceKey) { ConcurrentHashMap.newKeySet() }.add(key)
        deviceConnections.computeIfAbsent(targetKey) { ConcurrentHashMap.newKeySet() }.add(key)
    }
    
    fun onDeviceLoad(device: PowerDevice) {
        val loc = device.block.location
        val world = loc.world ?: return
        val deviceKey = "${world.uid}:${loc.blockX}:${loc.blockY}:${loc.blockZ}"
        
        val connKeys = deviceConnections[deviceKey]?.toSet() ?: return
        
        for (key in connKeys) {
            val pending = pendingConnections[key] ?: continue
            
            val isSource = isSourceDevice(pending.connectionData, world.uid, loc.blockX, loc.blockY, loc.blockZ)
            
            if (isSource) {
                pending.sourceLoaded = true
            } else {
                pending.targetLoaded = true
            }
            
            updateConnectionState(pending)
            
            if (pending.state == ConnectionState.READY) {
                scheduleRestore(pending, key, device)
            }
        }
    }
    
    fun onDeviceUnload(device: PowerDevice) {
    }
    
    private fun updateConnectionState(pending: PendingConnection) {
        when {
            pending.sourceLoaded && pending.targetLoaded -> {
                pending.state = ConnectionState.READY
            }
            pending.sourceLoaded -> {
                pending.state = ConnectionState.SOURCE_READY
            }
            pending.targetLoaded -> {
                pending.state = ConnectionState.TARGET_READY
            }
        }
    }
    
    private fun scheduleRestore(pending: PendingConnection, key: String, loadedDevice: PowerDevice) {
        if (pending.restoreAttempted) {
            return
        }
        pending.restoreAttempted = true
        
        Bukkit.getScheduler().runTaskLater(EndfieldIndustry.instance, Runnable {
            attemptRestore(pending, key)
        }, 5L)
    }
    
    private fun attemptRestore(pending: PendingConnection, key: String) {
        val connData = pending.connectionData
        
        if (connData.sourceWorldUid != connData.targetWorldUid) {
            markFailed(pending, key, "跨世界连接不支持")
            return
        }
        
        val world = Bukkit.getWorld(connData.sourceWorldUid)
        if (world == null) {
            markFailed(pending, key, "目标世界未加载")
            return
        }
        
        val sourceBlock = world.getBlockAt(connData.sourceX, connData.sourceY, connData.sourceZ)
        val targetBlock = world.getBlockAt(connData.targetX, connData.targetY, connData.targetZ)
        
        val sourceRebarBlock = BlockStorage.get(sourceBlock)
        val targetRebarBlock = BlockStorage.get(targetBlock)
        
        if (sourceRebarBlock == null || sourceRebarBlock !is PowerDevice) {
            markFailed(pending, key, "源设备不存在")
            return
        }
        
        if (targetRebarBlock == null || targetRebarBlock !is PowerDevice) {
            markFailed(pending, key, "目标设备不存在")
            return
        }
        
        val sourceDevice = sourceRebarBlock
        
        if (!world.isChunkLoaded(connData.targetX shr 4, connData.targetZ shr 4)) {
            pending.restoreAttempted = false
            pending.state = ConnectionState.SOURCE_READY
            logger.info("[ConnectionRestoreScheduler] 目标区块未加载，延迟恢复: $key")
            return
        }
        
        try {
            sourceDevice.triggerConnectionRestore()
            
            pending.state = ConnectionState.RESTORED
            logger.info("[ConnectionRestoreScheduler] 成功触发连接恢复: $key")
            
            removePendingConnection(key)
        } catch (e: Exception) {
            pending.failCount++
            
            if (pending.failCount < 3) {
                pending.restoreAttempted = false
                pending.state = ConnectionState.READY
                
                Bukkit.getScheduler().runTaskLater(EndfieldIndustry.instance, Runnable {
                    attemptRestore(pending, key)
                }, 20L * (pending.failCount + 1).toLong())
                
                logger.warning("[ConnectionRestoreScheduler] 恢复失败，重试 (${pending.failCount}/3): ${e.message}")
            } else {
                markFailed(pending, key, "超过最大重试次数")
            }
        }
    }
    
    private fun markFailed(pending: PendingConnection, key: String, reason: String) {
        pending.state = ConnectionState.FAILED
        logger.warning("[ConnectionRestoreScheduler] 恢复失败: $key - 原因: $reason")
        
        pending.failCount++
        if (pending.failCount >= 5) {
            removePendingConnection(key)
        }
    }
    
    private fun removePendingConnection(key: String) {
        val pending = pendingConnections.remove(key) ?: return
        
        val sourceKey = "${pending.connectionData.sourceWorldUid}:${pending.connectionData.sourceX}:${pending.connectionData.sourceY}:${pending.connectionData.sourceZ}"
        val targetKey = "${pending.connectionData.targetWorldUid}:${pending.connectionData.targetX}:${pending.connectionData.targetY}:${pending.connectionData.targetZ}"
        
        deviceConnections[sourceKey]?.remove(key)
        deviceConnections[targetKey]?.remove(key)
    }
    
    private fun isSourceDevice(connData: PowerSystemStorage.ConnectionData, worldUid: UUID, x: Int, y: Int, z: Int): Boolean {
        return connData.sourceWorldUid == worldUid && 
               connData.sourceX == x && 
               connData.sourceY == y && 
               connData.sourceZ == z
    }
    
    private fun generateConnectionKey(connData: PowerSystemStorage.ConnectionData): String {
        val coords = listOf(
            Triple(connData.sourceWorldUid, connData.sourceX, Pair(connData.sourceY, connData.sourceZ)),
            Triple(connData.targetWorldUid, connData.targetX, Pair(connData.targetY, connData.targetZ))
        ).sortedBy { "${it.first}:${it.second}:${it.third.first}:${it.third.second}" }
        
        val first = coords[0]
        val second = coords[1]
        return "${first.first}:${first.second}:${first.third.first}:${first.third.second}-${second.first}:${second.second}:${second.third.first}:${second.third.second}"
    }
    
    fun getPendingCount(): Int = pendingConnections.size
    
    fun getStateStats(): Map<ConnectionState, Int> {
        return pendingConnections.values.groupingBy { it.state }.eachCount()
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    fun onWorldLoad(event: WorldLoadEvent) {
        logger.info("[ConnectionRestoreScheduler] 世界加载: ${event.world.name}，检查待恢复连接")
    }
}
