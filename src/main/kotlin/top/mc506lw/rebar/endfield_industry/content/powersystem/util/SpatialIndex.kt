package top.mc506lw.rebar.endfield_industry.content.powersystem.util

import org.bukkit.Location
import org.bukkit.World
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerDevice

class SpatialIndex(private val chunkSize: Int = 16) {

    private val index: MutableMap<String, MutableList<PowerDevice>> = mutableMapOf()

    fun addDevice(device: PowerDevice) {
        val key = getChunkKey(device.block.location)
        index.computeIfAbsent(key) { mutableListOf() }.add(device)
    }

    fun removeDevice(device: PowerDevice) {
        val key = getChunkKey(device.block.location)
        val devices = index[key]
        if (devices != null) {
            devices.remove(device)
            if (devices.isEmpty()) {
                index.remove(key)
            }
        }
    }

    fun getDevicesInRange(center: Location, range: Int): List<PowerDevice> {
        val result = mutableListOf<PowerDevice>()
        
        val minChunkX = (center.blockX - range) / chunkSize
        val maxChunkX = (center.blockX + range) / chunkSize
        val minChunkZ = (center.blockZ - range) / chunkSize
        val maxChunkZ = (center.blockZ + range) / chunkSize
        
        val world = center.world!!
        
        for (chunkX in minChunkX..maxChunkX) {
            for (chunkZ in minChunkZ..maxChunkZ) {
                val key = getChunkKey(world, chunkX, chunkZ)
                val devices = index[key]
                
                if (devices != null) {
                    for (device in devices) {
                        if (isInRange(center, device.block.location, range)) {
                            result.add(device)
                        }
                    }
                }
            }
        }
        
        return result
    }

    private fun isInRange(center: Location, target: Location, range: Int): Boolean {
        if (center.world != target.world) {
            return false
        }
        
        val dx = Math.abs(center.blockX - target.blockX)
        val dz = Math.abs(center.blockZ - target.blockZ)
        
        return dx <= range && dz <= range
    }

    private fun getChunkKey(location: Location): String {
        val chunkX = location.blockX / chunkSize
        val chunkZ = location.blockZ / chunkSize
        return getChunkKey(location.world!!, chunkX, chunkZ)
    }

    private fun getChunkKey(world: World, chunkX: Int, chunkZ: Int): String {
        return "${world.name}:$chunkX:$chunkZ"
    }

    fun clear() {
        index.clear()
    }
}
