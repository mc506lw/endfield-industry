package top.mc506lw.rebar.endfield_industry.content.powersystem

import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerDevice
import top.mc506lw.rebar.endfield_industry.content.powersystem.storage.PowerSystemStorage
import java.util.UUID

class PowerGrid(val gridId: UUID = UUID.randomUUID()) {

    private val devices: MutableSet<PowerDevice> = mutableSetOf()
    private val consumers: MutableSet<PowerConsumer> = mutableSetOf()
    
    var totalCapacity: Int = 0
        private set
    var usedCapacity: Int = 0
        private set
    
    val availableCapacity: Int
        get() = totalCapacity - usedCapacity

    val isOverloaded: Boolean
        get() = usedCapacity > totalCapacity

    private var wasOverloaded: Boolean = false

    fun getDevices(): Set<PowerDevice> = devices.toSet()
    
    fun getConsumers(): Set<PowerConsumer> = consumers.toSet()
    
    fun restoreState(totalCapacity: Int, usedCapacity: Int, wasOverloaded: Boolean) {
        this.totalCapacity = totalCapacity
        this.usedCapacity = usedCapacity
        this.wasOverloaded = wasOverloaded
        markDirty()
    }
    
    fun getWasOverloaded(): Boolean = wasOverloaded

    fun addDevice(device: PowerDevice) {
        if (devices.add(device)) {
            totalCapacity += device.getPowerContribution()
            checkOverloadState()
            markDirty()
        }
    }

    fun removeDevice(device: PowerDevice) {
        if (devices.remove(device)) {
            totalCapacity -= device.getPreviousPowerContribution()
            checkOverloadState()
            markDirty()
        }
    }

    fun updateDevice(device: PowerDevice) {
        if (devices.contains(device)) {
            val oldContribution = device.getPreviousPowerContribution()
            val newContribution = device.getPowerContribution()
            totalCapacity += (newContribution - oldContribution)
            checkOverloadState()
            markDirty()
        }
    }
    
    fun addConsumer(consumer: PowerConsumer) {
        if (consumers.add(consumer)) {
            usedCapacity += consumer.powerConsumption
            consumer.onPowerConnected()
            checkOverloadState()
            markDirty()
        }
    }
    
    fun removeConsumer(consumer: PowerConsumer) {
        if (consumers.remove(consumer)) {
            usedCapacity -= consumer.powerConsumption
            consumer.onPowerDisconnected()
            checkOverloadState()
            markDirty()
        }
    }

    fun mergeGrid(other: PowerGrid) {
        for (device in other.getDevices()) {
            device.setGrid(this)
            this.devices.add(device)
        }
        for (consumer in other.getConsumers()) {
            this.consumers.add(consumer)
        }
        this.totalCapacity += other.totalCapacity
        this.usedCapacity += other.usedCapacity
        checkOverloadState()
        markDirty()
    }

    private fun checkOverloadState() {
        val isOverloaded = isOverloaded
        if (isOverloaded && !wasOverloaded) {
            PowerGridManager.getInstance().broadcastOverload(this)
            notifyAllConsumers(false)
        } else if (!isOverloaded && wasOverloaded) {
            PowerGridManager.getInstance().broadcastStable(this)
            notifyAllConsumers(true)
        }
        wasOverloaded = isOverloaded
    }
    
    private fun notifyAllConsumers(powered: Boolean) {
        for (consumer in consumers) {
            if (powered) {
                consumer.onPowerConnected()
            } else {
                consumer.onPowerDisconnected()
            }
        }
    }
    
    private fun markDirty() {
        PowerSystemStorage.markGridDirty(gridId)
    }
}
