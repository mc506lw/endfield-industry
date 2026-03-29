package top.mc506lw.rebar.endfield_industry.content.powersystem.util

import io.github.pylonmc.rebar.block.BlockStorage
import org.bukkit.Location
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerDevice

object PowerUtils {

    @JvmStatic
    fun isPowerDevice(location: Location?): Boolean {
        if (location == null || location.world == null) {
            return false
        }
        val block = location.block
        return BlockStorage.get(block) is PowerDevice
    }

    @JvmStatic
    fun getPowerDevice(location: Location?): PowerDevice? {
        if (!isPowerDevice(location)) {
            return null
        }
        return BlockStorage.get(location!!.block) as PowerDevice
    }

    @JvmStatic
    fun isConnected(device1: PowerDevice?, device2: PowerDevice?): Boolean {
        if (device1 == null || device2 == null) {
            return false
        }
        return device1.getGrid() != null && device1.getGrid() == device2.getGrid()
    }
}
