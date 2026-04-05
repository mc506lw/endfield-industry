package top.mc506lw.rebar.endfield_industry.content.powersystem

import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerDevice
import top.mc506lw.rebar.endfield_industry.content.powersystem.storage.PowerSystemStorage
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class PowerGrid(val gridId: UUID = UUID.randomUUID()) {

    private val devices: MutableSet<PowerDevice> = ConcurrentHashMap.newKeySet()
    private val consumers: MutableSet<PowerConsumer> = ConcurrentHashMap.newKeySet()
    
    @Volatile
    var totalCapacity: Int = 0
        private set
    
    @Volatile
    var usedCapacity: Int = 0
        private set
    
    val availableCapacity: Int
        get() = totalCapacity - usedCapacity

    val isOverloaded: Boolean
        get() = usedCapacity > totalCapacity

    @Volatile
    private var wasOverloaded: Boolean = false
    
    @Volatile
    var isDirty: Boolean = false
        private set

    fun getDevices(): Set<PowerDevice> = devices.toSet()
    
    fun getConsumers(): Set<PowerConsumer> = consumers.toSet()
    
    fun getProtocolCoreLocation(): org.bukkit.Location? {
        for (device in devices) {
            if (device::class.simpleName == "ProtocolCoreController") {
                return device.block.location
            }
        }
        return null
    }
    
    fun restoreState(totalCapacity: Int, usedCapacity: Int, wasOverloaded: Boolean) {
        synchronized(this) {
            this.totalCapacity = totalCapacity
            this.usedCapacity = usedCapacity
            this.wasOverloaded = wasOverloaded
            markDirty()
        }
    }
    
    fun getWasOverloaded(): Boolean = wasOverloaded

    fun addDevice(device: PowerDevice): Boolean {
        synchronized(this) {
            if (devices.add(device)) {
                totalCapacity += device.getPowerContribution()
                checkOverloadState()
                markDirty()
                return true
            }
            return false
        }
    }

    fun removeDevice(device: PowerDevice): Boolean {
        synchronized(this) {
            if (devices.remove(device)) {
                totalCapacity -= device.getPreviousPowerContribution()
                
                if (devices.isEmpty()) {
                    val consumersCopy = consumers.toList()
                    consumers.clear()
                    for (consumer in consumersCopy) {
                        consumer.onGridDestroyed()
                    }
                    usedCapacity = 0
                    
                    PowerGridManager.getInstance().removeGrid(gridId)
                }
                
                checkOverloadState()
                markDirty()
                return true
            }
            return false
        }
    }

    fun updateDevice(device: PowerDevice): Boolean {
        synchronized(this) {
            if (devices.contains(device)) {
                val oldContribution = device.getPreviousPowerContribution()
                val newContribution = device.getPowerContribution()
                totalCapacity += (newContribution - oldContribution)
                checkOverloadState()
                markDirty()
                return true
            }
            return false
        }
    }
    
    fun addConsumer(consumer: PowerConsumer): Boolean {
        synchronized(this) {
            if (consumers.add(consumer)) {
                usedCapacity += consumer.powerConsumption
                consumer.onPowerConnected()
                checkOverloadState()
                markDirty()
                return true
            }
            return false
        }
    }
    
    fun removeConsumer(consumer: PowerConsumer): Boolean {
        synchronized(this) {
            if (consumers.remove(consumer)) {
                usedCapacity -= consumer.powerConsumption
                consumer.onPowerDisconnected()
                checkOverloadState()
                markDirty()
                return true
            }
            return false
        }
    }

    fun mergeGrid(other: PowerGrid) {
        synchronized(this) {
            synchronized(other) {
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
        }
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
        val consumersCopy = consumers.toList()
        for (consumer in consumersCopy) {
            if (powered) {
                consumer.onPowerConnected()
            } else {
                consumer.onPowerDisconnected()
            }
        }
    }
    
    private fun markDirty() {
        isDirty = true
        PowerSystemStorage.markGridDirty(gridId)
    }
    
    fun clearDirty() {
        isDirty = false
    }
}
