package top.mc506lw.rebar.endfield_industry.content.powersystem.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import top.mc506lw.rebar.endfield_industry.content.powersystem.PowerGrid

class PowerGridMergeEvent(
    val survivingGrid: PowerGrid,
    val absorbedGrid: PowerGrid
) : Event() {

    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLERS
    }

    override fun getHandlers(): HandlerList = HANDLERS
}
