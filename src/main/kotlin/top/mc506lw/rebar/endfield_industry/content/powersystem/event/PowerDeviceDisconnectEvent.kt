package top.mc506lw.rebar.endfield_industry.content.powersystem.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerConsumerDevice
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerDevice

class PowerDeviceDisconnectEvent(val device: PowerDevice?) : Event() {
    
    constructor(consumer: PowerConsumerDevice) : this(null) {
        this.consumer = consumer
    }

    private var consumer: PowerConsumerDevice? = null
    
    fun getConsumer(): PowerConsumerDevice? = consumer

    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLERS
    }

    override fun getHandlers(): HandlerList = HANDLERS
}
