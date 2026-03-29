package top.mc506lw.rebar.endfield_industry.content.powersystem.connection

import org.bukkit.Location
import org.bukkit.block.Block
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerDevice

class PowerConnection(
    val source: PowerDevice,
    val target: PowerDevice,
    val path: List<Block>,
    val distance: Int
) {
    private var wireDisplay: WireDisplay? = null

    val sourceLocation: Location
        get() = source.block.location

    val targetLocation: Location
        get() = target.block.location

    fun createWireDisplay() {
        if (wireDisplay != null && wireDisplay?.isValid() == true) return
        
        val start = source.block.location.toCenterLocation()
        val end = target.block.location.toCenterLocation()
        
        wireDisplay = WireDisplay.create(start, end)
    }

    fun removeWireDisplay() {
        wireDisplay?.remove()
        wireDisplay = null
    }

    fun hasWireDisplay(): Boolean = wireDisplay != null && wireDisplay?.isValid() == true
}
