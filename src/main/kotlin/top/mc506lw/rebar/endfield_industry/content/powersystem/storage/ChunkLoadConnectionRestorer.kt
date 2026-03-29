package top.mc506lw.rebar.endfield_industry.content.powersystem.storage

import io.github.pylonmc.rebar.block.BlockStorage
import io.github.pylonmc.rebar.event.RebarChunkBlocksLoadEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import top.mc506lw.rebar.endfield_industry.EndfieldIndustry
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerDevice

class ChunkLoadConnectionRestorer : Listener {
    
    private val logger = EndfieldIndustry.instance.logger
    
    @EventHandler(priority = EventPriority.MONITOR)
    fun onChunkBlocksLoad(event: RebarChunkBlocksLoadEvent) {
        val chunk = event.chunk
        val world = chunk.world
        
        if (!PowerSystemStorage.hasPendingConnections()) {
            return
        }
        
        Bukkit.getScheduler().runTaskLater(EndfieldIndustry.instance, Runnable {
            checkDevicesInChunk(chunk.x, chunk.z, world.uid)
        }, 10L)
    }
    
    private fun checkDevicesInChunk(chunkX: Int, chunkZ: Int, worldUid: java.util.UUID) {
        val world = Bukkit.getWorld(worldUid) ?: return
        
        val startX = chunkX shl 4
        val startZ = chunkZ shl 4
        
        var devicesWithPending = 0
        
        for (x in startX until startX + 16) {
            for (z in startZ until startZ + 16) {
                for (y in world.minHeight until world.maxHeight) {
                    val block = world.getBlockAt(x, y, z)
                    val rebarBlock = BlockStorage.get(block)
                    
                    if (rebarBlock is PowerDevice) {
                        val pendingConns = PowerSystemStorage.getPendingConnectionsForDevice(rebarBlock)
                        if (pendingConns.isNotEmpty()) {
                            devicesWithPending++
                        }
                    }
                }
            }
        }
        
        if (devicesWithPending > 0) {
            logger.info("[ChunkLoadConnectionRestorer] 区块($chunkX, $chunkZ)加载，发现 $devicesWithPending 个设备有待恢复连接")
        }
    }
    
    companion object {
        fun register() {
            Bukkit.getPluginManager().registerEvents(
                ChunkLoadConnectionRestorer(),
                EndfieldIndustry.instance
            )
            EndfieldIndustry.instance.logger.info("[ChunkLoadConnectionRestorer] 区块加载连接恢复器已注册")
        }
    }
}
