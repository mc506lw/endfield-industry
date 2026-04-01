package top.mc506lw.rebar.endfield_industry.content.powersystem.gui

import org.bukkit.Bukkit
import top.mc506lw.rebar.endfield_industry.EndfieldIndustry
import top.mc506lw.rebar.endfield_industry.content.powersystem.PowerGrid
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerDevice
import top.mc506lw.rebar.endfield_industry.content.powersystem.PowerConsumer
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

object PowerGridCacheManager {
    
    private val gridDataCache = ConcurrentHashMap<UUID, CachedGridData>()
    private val lastUpdateTime = ConcurrentHashMap<UUID, AtomicLong>()
    private const val CACHE_TTL_MS = 1000L
    private var updateTaskId: Int = -1
    
    data class CachedGridData(
        val gridId: UUID,
        val totalCapacity: Int,
        val usedCapacity: Int,
        val availableCapacity: Int,
        val isOverloaded: Boolean,
        val deviceCount: Int,
        val consumerCount: Int,
        val deviceTypes: Map<String, Int>,
        val timestamp: Long = System.currentTimeMillis()
    )
    
    fun startUpdateTask() {
        if (updateTaskId != -1) return
        
        updateTaskId = Bukkit.getScheduler().runTaskTimerAsynchronously(
            EndfieldIndustry.instance,
            Runnable { updateAllCaches() },
            20L,
            20L
        ).taskId
    }
    
    fun stopUpdateTask() {
        if (updateTaskId != -1) {
            Bukkit.getScheduler().cancelTask(updateTaskId)
            updateTaskId = -1
        }
    }
    
    private fun updateAllCaches() {
        val gridManager = try {
            top.mc506lw.rebar.endfield_industry.content.powersystem.PowerGridManager.getInstance()
        } catch (e: IllegalStateException) {
            return
        }
        
        for ((gridId, grid) in gridManager.getAllGrids()) {
            try {
                val cachedData = computeGridData(grid)
                gridDataCache[gridId] = cachedData
            } catch (e: Exception) {
                EndfieldIndustry.instance.logger.warning("Error caching grid $gridId: ${e.message}")
            }
        }
    }
    
    private fun computeGridData(grid: PowerGrid): CachedGridData {
        val devices = grid.getDevices()
        val consumers = grid.getConsumers()
        
        val deviceTypes = mutableMapOf<String, Int>()
        for (device in devices) {
            val typeName = device.javaClass.simpleName
            deviceTypes[typeName] = deviceTypes.getOrDefault(typeName, 0) + 1
        }
        
        for (consumer in consumers) {
            val typeName = consumer.javaClass.simpleName
            deviceTypes[typeName] = deviceTypes.getOrDefault(typeName, 0) + 1
        }
        
        return CachedGridData(
            gridId = grid.gridId,
            totalCapacity = grid.totalCapacity,
            usedCapacity = grid.usedCapacity,
            availableCapacity = grid.availableCapacity,
            isOverloaded = grid.isOverloaded,
            deviceCount = devices.size,
            consumerCount = consumers.size,
            deviceTypes = deviceTypes
        )
    }
    
    fun getCachedData(gridId: UUID): CachedGridData? {
        val cached = gridDataCache[gridId]
        if (cached != null && System.currentTimeMillis() - cached.timestamp < CACHE_TTL_MS) {
            return cached
        }
        return null
    }
    
    fun getCachedDataOrCompute(grid: PowerGrid): CachedGridData {
        val cached = getCachedData(grid.gridId)
        if (cached != null) return cached
        
        val computed = computeGridData(grid)
        gridDataCache[grid.gridId] = computed
        return computed
    }
    
    fun invalidateCache(gridId: UUID) {
        gridDataCache.remove(gridId)
        lastUpdateTime.remove(gridId)
    }
    
    fun clearAllCaches() {
        gridDataCache.clear()
        lastUpdateTime.clear()
    }
}
