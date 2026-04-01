package top.mc506lw.rebar.endfield_industry.content.cloudstorage

import org.bukkit.Bukkit
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
    
    private val storageLocks = ConcurrentHashMap<UUID, ReentrantLock>()
    private val storageData = ConcurrentHashMap<UUID, CloudStorageData>()
    
    private var defaultMaxCapacity: Long = 1000000L
    private val itemLimits = ConcurrentHashMap<String, Long>()
    private val globalWhitelist = ConcurrentHashMap.newKeySet<String>()
    
    data class CloudStorageData(
        val gridId: UUID,
        val items: ConcurrentHashMap<String, AtomicLong> = ConcurrentHashMap(),
        val totalCapacity: AtomicLong = AtomicLong(0),
        var maxCapacity: Long = 1000000L
    )
    
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
    
    private fun getLock(gridId: UUID): ReentrantLock {
        return storageLocks.computeIfAbsent(gridId) { ReentrantLock() }
    }
    
    fun getOrCreateStorage(gridId: UUID): CloudStorageData {
        return storageData.computeIfAbsent(gridId) { 
            CloudStorageData(gridId, maxCapacity = defaultMaxCapacity).also { data ->
                CloudStorageDatabase.loadStorageData(gridId, data)
            }
        }
    }
    
    fun getStorage(gridId: UUID): CloudStorageData? {
        return storageData[gridId]
    }
    
    fun hasStorage(gridId: UUID): Boolean {
        return storageData.containsKey(gridId)
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
    
    fun isOverLimit(gridId: UUID, itemKey: String): Boolean {
        val storage = getStorage(gridId) ?: return false
        val currentAmount = storage.items[itemKey]?.get() ?: 0
        val limit = getItemLimit(itemKey)
        return currentAmount >= limit
    }
    
    fun insertItem(gridId: UUID, item: ItemStack, amount: Long = item.amount.toLong()): InsertResult {
        val itemKey = generateItemKey(item)
        
        if (!isAllowed(item)) {
            return InsertResult.NOT_IN_WHITELIST
        }
        
        val lock = getLock(gridId)
        return lock.withLock {
            val storage = getOrCreateStorage(gridId)
            
            if (isOverLimit(gridId, itemKey)) {
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
            
            InsertResult.SUCCESS(canAdd)
        }
    }
    
    fun extractItem(gridId: UUID, itemKey: String, amount: Long): ExtractResult {
        val lock = getLock(gridId)
        return lock.withLock {
            val storage = getStorage(gridId) ?: return@withLock ExtractResult.STORAGE_NOT_FOUND
            
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
            
            ExtractResult.SUCCESS(toExtract)
        }
    }
    
    fun getItemAmount(gridId: UUID, itemKey: String): Long {
        val storage = getStorage(gridId) ?: return 0
        return storage.items[itemKey]?.get() ?: 0
    }
    
    fun getItemsPage(gridId: UUID, page: Int): List<Pair<String, Long>> {
        val storage = getStorage(gridId) ?: return emptyList()
        
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
    
    fun getTotalPages(gridId: UUID): Int {
        val storage = getStorage(gridId) ?: return 0
        val itemCount = storage.items.entries.count { it.value.get() > 0 }
        return (itemCount + ITEMS_PER_PAGE - 1) / ITEMS_PER_PAGE
    }
    
    fun getStorageInfo(gridId: UUID): StorageInfo {
        val storage = getStorage(gridId) ?: return StorageInfo(0, 0, 0)
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
        logger.info("[CloudStorage] 开始保存所有数据")
        
        for ((gridId, storage) in storageData) {
            try {
                CloudStorageDatabase.saveStorageData(gridId, storage)
            } catch (e: Exception) {
                logger.severe("[CloudStorage] 保存仓库 $gridId 失败: ${e.message}")
            }
        }
        
        logger.info("[CloudStorage] 所有数据保存完成")
    }
    
    fun deleteStorage(gridId: UUID) {
        val lock = getLock(gridId)
        lock.withLock {
            storageData.remove(gridId)
            storageLocks.remove(gridId)
            CloudStorageDatabase.deleteStorageData(gridId)
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
