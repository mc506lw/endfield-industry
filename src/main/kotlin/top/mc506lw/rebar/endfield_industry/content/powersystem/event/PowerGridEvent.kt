package top.mc506lw.rebar.endfield_industry.content.powersystem.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import top.mc506lw.rebar.endfield_industry.content.powersystem.PowerGrid

open class PowerGridEvent(val grid: PowerGrid) : Event() {

    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLERS
    }

    override fun getHandlers(): HandlerList = HANDLERS
}
