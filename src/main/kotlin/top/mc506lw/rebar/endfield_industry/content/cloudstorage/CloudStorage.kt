package top.mc506lw.rebar.endfield_industry.content.cloudstorage

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import top.mc506lw.rebar.endfield_industry.EndfieldIndustry
import top.mc506lw.rebar.endfield_industry.EndfieldIndustryItems
import java.io.File
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

object CloudStorage {
    
    private val logger = EndfieldIndustry.instance.logger
    
    const val ITEMS_PER_PAGE = 45
    
    private val storageLocks = ConcurrentHashMap<String, ReentrantLock>()
    private val storageData = ConcurrentHashMap<String, CloudStorageData>()
    
    private var defaultMaxCapacity: Long = 1000000L
    private val itemLimits = ConcurrentHashMap<String, Long>()
    private val globalWhitelist = ConcurrentHashMap.newKeySet<String>()
    
    data class CloudStorageData(
        val locationKey: String,
        val items: ConcurrentHashMap<String, AtomicLong> = ConcurrentHashMap(),
        val totalCapacity: AtomicLong = AtomicLong(0),
        var maxCapacity: Long = 1000000L
    )
    
    fun generateLocationKey(location: Location): String {
        return "${location.world?.name ?: "unknown"}:${location.blockX}:${location.blockY}:${location.blockZ}"
    }
    
    fun parseLocationKey(key: String): Location? {
        val parts = key.split(":")
        if (parts.size != 4) return null
        val world = Bukkit.getWorld(parts[0]) ?: return null
        val x = parts[1].toIntOrNull() ?: return null
        val y = parts[2].toIntOrNull() ?: return null
        val z = parts[3].toIntOrNull() ?: return null
        return Location(world, x.toDouble(), y.toDouble(), z.toDouble())
    }
    
    fun initialize() {
        logger.info("[CloudStorage] 初始化云仓库系统")
        loadConfig()
        CloudStorageDatabase.initialize()
        logger.info("[CloudStorage] 云仓库系统初始化完成")
    }
    
    private fun loadConfig() {
        val configFile = File(EndfieldIndustry.instance.dataFolder, "settings/cloud_storage.yml")
        if (!configFile.exists()) {
            EndfieldIndustry.instance.saveResource("settings/cloud_storage.yml", false)
        }
        
        val config = YamlConfiguration.loadConfiguration(configFile)
        
        defaultMaxCapacity = config.getLong("settings.default_max_capacity", 1000000L)
        
        itemLimits.clear()
        val limitsSection = config.getConfigurationSection("item_limits")
        if (limitsSection != null) {
            for (key in limitsSection.getKeys(false)) {
                itemLimits[key] = limitsSection.getLong(key)
            }
        }
        
        globalWhitelist.clear()
        val whitelistList = config.getStringList("whitelist")
        for (itemKey in whitelistList) {
            globalWhitelist.add(itemKey)
        }
        
        registerPluginItems()
        
        logger.info("[CloudStorage] 配置加载完成: 默认容量=$defaultMaxCapacity, 物品上限=${itemLimits.size}, 白名单=${globalWhitelist.size}")
    }
    
    private fun registerPluginItems() {
        var addedCount = 0
        
        for (field in EndfieldIndustryItems::class.java.declaredFields) {
            try {
                field.isAccessible = true
                val item = field.get(null) as? ItemStack ?: continue
                
                val rebarItem = io.github.pylonmc.rebar.item.RebarItem.fromStack(item)
                if (rebarItem != null) {
                    val key = rebarItem.key
                    val itemKey = "rebar:${key.namespace}:${key.key}"
                    if (globalWhitelist.add(itemKey)) {
                        addedCount++
                    }
                }
            } catch (e: Exception) {
                // Ignore
            }
        }
        
        if (addedCount > 0) {
            logger.info("[CloudStorage] 自动添加 $addedCount 个插件物品到白名单")
        }
    }
    
    fun reloadConfig() {
        loadConfig()
    }
    
    fun shutdown() {
        logger.info("[CloudStorage] 关闭云仓库系统")
        saveAllData()
        CloudStorageDatabase.shutdown()
        storageData.clear()
        storageLocks.clear()
        logger.info("[CloudStorage] 云仓库系统已关闭")
    }
    
    private fun getLock(locationKey: String): ReentrantLock {
        return storageLocks.computeIfAbsent(locationKey) { ReentrantLock() }
    }
    
    fun getOrCreateStorage(locationKey: String): CloudStorageData {
        return storageData.computeIfAbsent(locationKey) { 
            CloudStorageData(locationKey, maxCapacity = defaultMaxCapacity).also { data ->
                CloudStorageDatabase.loadStorageData(locationKey, data)
            }
        }
    }
    
    fun getStorage(locationKey: String): CloudStorageData? {
        return storageData[locationKey]
    }
    
    fun hasStorage(locationKey: String): Boolean {
        return storageData.containsKey(locationKey)
    }
    
    fun generateItemKey(item: ItemStack): String {
        val rebarItem = io.github.pylonmc.rebar.item.RebarItem.fromStack(item)
        
        if (rebarItem != null) {
            val key = rebarItem.key
            return "rebar:${key.namespace}:${key.key}"
        }
        
        return "minecraft:${item.type.name}"
    }
    
    fun getItemLimit(itemKey: String): Long {
        return itemLimits[itemKey] ?: Long.MAX_VALUE
    }
    
    fun isOverLimit(locationKey: String, itemKey: String): Boolean {
        val storage = getOrCreateStorage(locationKey)
        val currentAmount = storage.items[itemKey]?.get() ?: 0
        val limit = getItemLimit(itemKey)
        return currentAmount >= limit
    }
    
    fun insertItem(locationKey: String, item: ItemStack, amount: Long = item.amount.toLong()): InsertResult {
        val itemKey = generateItemKey(item)
        
        if (!isAllowed(item)) {
            return InsertResult.NOT_IN_WHITELIST
        }
        
        val lock = getLock(locationKey)
        return lock.withLock {
            val storage = getOrCreateStorage(locationKey)
            
            if (isOverLimit(locationKey, itemKey)) {
                return@withLock InsertResult.OVER_LIMIT
            }
            
            val currentTotal = storage.totalCapacity.get()
            if (currentTotal + amount > storage.maxCapacity) {
                return@withLock InsertResult.CAPACITY_FULL
            }
            
            val limit = getItemLimit(itemKey)
            val currentAmount = storage.items[itemKey]?.get() ?: 0
            
            val canAdd = if (limit == Long.MAX_VALUE) {
                amount
            } else {
                minOf(amount, limit - currentAmount)
            }
            
            if (canAdd <= 0) {
                return@withLock InsertResult.OVER_LIMIT
            }
            
            val currentAmountAtomic = storage.items.computeIfAbsent(itemKey) { AtomicLong(0) }
            currentAmountAtomic.addAndGet(canAdd)
            storage.totalCapacity.addAndGet(canAdd)
            
            CloudStorageDatabase.markDirty()
            
            InsertResult.SUCCESS(canAdd)
        }
    }
    
    fun extractItem(locationKey: String, itemKey: String, amount: Long): ExtractResult {
        val lock = getLock(locationKey)
        return lock.withLock {
            val storage = getStorage(locationKey) ?: return@withLock ExtractResult.STORAGE_NOT_FOUND
            
            val currentAmount = storage.items[itemKey] ?: return@withLock ExtractResult.ITEM_NOT_FOUND
            val available = currentAmount.get()
            
            if (available <= 0) {
                storage.items.remove(itemKey)
                return@withLock ExtractResult.ITEM_NOT_FOUND
            }
            
            val toExtract = minOf(amount, available)
            currentAmount.addAndGet(-toExtract)
            storage.totalCapacity.addAndGet(-toExtract)
            
            if (currentAmount.get() <= 0) {
                storage.items.remove(itemKey)
            }
            
            CloudStorageDatabase.markDirty()
            
            ExtractResult.SUCCESS(toExtract)
        }
    }
    
    fun getItemAmount(locationKey: String, itemKey: String): Long {
        val storage = getOrCreateStorage(locationKey)
        return storage.items[itemKey]?.get() ?: 0
    }
    
    fun getItemsPage(locationKey: String, page: Int): List<Pair<String, Long>> {
        val storage = getOrCreateStorage(locationKey)
        
        val allItems = storage.items.entries
            .map { it.key to it.value.get() }
            .filter { it.second > 0 }
            .sortedByDescending { it.second }
        
        val startIndex = page * ITEMS_PER_PAGE
        if (startIndex >= allItems.size) {
            return emptyList()
        }
        
        val endIndex = minOf(startIndex + ITEMS_PER_PAGE, allItems.size)
        return allItems.subList(startIndex, endIndex)
    }
    
    fun getTotalPages(locationKey: String): Int {
        val storage = getOrCreateStorage(locationKey)
        val itemCount = storage.items.entries.count { it.value.get() > 0 }
        return (itemCount + ITEMS_PER_PAGE - 1) / ITEMS_PER_PAGE
    }
    
    fun getStorageInfo(locationKey: String): StorageInfo {
        val storage = getOrCreateStorage(locationKey)
        return StorageInfo(
            totalItems = storage.items.entries.count { it.value.get() > 0 },
            totalAmount = storage.totalCapacity.get(),
            maxCapacity = storage.maxCapacity
        )
    }
    
    fun isAllowed(item: ItemStack): Boolean {
        val itemKey = generateItemKey(item)
        return globalWhitelist.contains(itemKey) || globalWhitelist.contains("minecraft:${item.type.name}")
    }
    
    fun addToWhitelist(itemKey: String): Boolean {
        return globalWhitelist.add(itemKey)
    }
    
    fun removeFromWhitelist(itemKey: String): Boolean {
        return globalWhitelist.remove(itemKey)
    }
    
    fun getWhitelist(): Set<String> {
        return globalWhitelist.toSet()
    }
    
    fun saveAllData() {
        val dataCount = storageData.size
        logger.info("[CloudStorage] 开始保存所有数据，仓库数量: $dataCount")
        
        if (dataCount == 0) {
            logger.info("[CloudStorage] 没有需要保存的仓库数据")
            return
        }
        
        var successCount = 0
        for ((locationKey, storage) in storageData) {
            try {
                val itemCount = storage.items.size
                val totalAmount = storage.totalCapacity.get()
                CloudStorageDatabase.saveStorageData(locationKey, storage)
                successCount++
                logger.fine("[CloudStorage] 仓库 $locationKey 保存成功: $itemCount 种物品, 总量 $totalAmount")
            } catch (e: Exception) {
                logger.severe("[CloudStorage] 保存仓库 $locationKey 失败: ${e.message}")
                e.printStackTrace()
            }
        }
        
        logger.info("[CloudStorage] 所有数据保存完成: $successCount/$dataCount 个仓库")
    }
    
    fun deleteStorage(locationKey: String) {
        val lock = getLock(locationKey)
        lock.withLock {
            storageData.remove(locationKey)
            storageLocks.remove(locationKey)
            CloudStorageDatabase.deleteStorageData(locationKey)
        }
    }
    
    sealed class InsertResult {
        data class SUCCESS(val amount: Long) : InsertResult()
        object NOT_IN_WHITELIST : InsertResult()
        object CAPACITY_FULL : InsertResult()
        object OVER_LIMIT : InsertResult()
    }
    
    sealed class ExtractResult {
        data class SUCCESS(val amount: Long) : ExtractResult()
        object STORAGE_NOT_FOUND : ExtractResult()
        object ITEM_NOT_FOUND : ExtractResult()
    }
    
    data class StorageInfo(
        val totalItems: Int,
        val totalAmount: Long,
        val maxCapacity: Long
    )
}
