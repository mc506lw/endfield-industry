package top.mc506lw.rebar.endfield_industry.content.powersystem.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import top.mc506lw.rebar.endfield_industry.content.powersystem.connection.PowerConnection
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerDevice

class PowerDeviceConnectEvent(
    val source: PowerDevice,
    val target: PowerDevice,
    val connection: PowerConnection
) : Event() {

    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLERS
    }

    override fun getHandlers(): HandlerList = HANDLERS
}
