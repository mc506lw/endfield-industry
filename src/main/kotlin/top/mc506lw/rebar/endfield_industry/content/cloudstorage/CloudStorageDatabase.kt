package top.mc506lw.rebar.endfield_industry.content.cloudstorage

import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.world.WorldLoadEvent
import org.bukkit.event.world.WorldSaveEvent
import top.mc506lw.rebar.endfield_industry.EndfieldIndustry
import java.io.File
import java.sql.*
import java.util.concurrent.atomic.AtomicLong

object CloudStorageDatabase : Listener {
    
    private val logger = EndfieldIndustry.instance.logger
    
    private const val DB_VERSION = 2
    private var connection: Connection? = null
    private var primaryWorldName: String? = null
    private var dataLoaded = false
    
    @Volatile
    private var isDirty = false
    
    fun markDirty() {
        isDirty = true
    }
    
    fun initialize() {
        logger.info("[CloudStorageDatabase] 初始化数据库系统")
        
        Bukkit.getPluginManager().registerEvents(this, EndfieldIndustry.instance)
        
        val worlds = Bukkit.getWorlds()
        if (worlds.isNotEmpty()) {
            primaryWorldName = worlds[0].name
            logger.info("[CloudStorageDatabase] 主世界: $primaryWorldName，立即加载数据库")
            initDatabase()
            dataLoaded = true
        } else {
            logger.info("[CloudStorageDatabase] 世界未加载，等待WorldLoadEvent")
        }
        
        logger.info("[CloudStorageDatabase] 数据库系统初始化完成")
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    fun onWorldLoad(event: WorldLoadEvent) {
        if (!dataLoaded) {
            primaryWorldName = event.world.name
            logger.info("[CloudStorageDatabase] 检测到世界加载: ${event.world.name}，开始初始化数据库")
            initDatabase()
            dataLoaded = true
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    fun onWorldSave(event: WorldSaveEvent) {
        if (event.world.name == primaryWorldName && isDirty) {
            logger.info("[CloudStorageDatabase] 世界保存事件触发，执行保存（数据已更改）")
            CloudStorage.saveAllData()
            isDirty = false
        }
    }
    
    private fun stopAutoSave() {
    }
    
    private fun getPrimaryWorld(): World? {
        val worldName = primaryWorldName
        if (worldName != null) {
            return Bukkit.getWorld(worldName)
        }
        return Bukkit.getWorlds().firstOrNull()
    }
    
    private fun getDataFolder(): File {
        val world = getPrimaryWorld()
            ?: throw IllegalStateException("No primary world found")
        val dataFolder = File(world.worldFolder, "data/endfield-industry/cloudstorage")
        if (!dataFolder.exists()) {
            dataFolder.mkdirs()
            logger.info("[CloudStorageDatabase] 创建数据文件夹: ${dataFolder.absolutePath}")
        }
        return dataFolder
    }
    
    private fun getDatabaseFile(): File {
        return File(getDataFolder(), "cloud_storage")
    }
    
    @Synchronized
    private fun getConnection(): Connection {
        val existing = connection
        if (existing != null && !existing.isClosed) {
            return existing
        }
        
        val dbFile = getDatabaseFile()
        val absolutePath = dbFile.absolutePath.replace("\\", "/")
        val url = "jdbc:h2:$absolutePath;MODE=MySQL;AUTO_SERVER=TRUE"
        
        logger.info("[CloudStorageDatabase] 连接数据库: $url")
        
        try {
            Class.forName("org.h2.Driver")
        } catch (e: ClassNotFoundException) {
            logger.severe("[CloudStorageDatabase] H2驱动未找到，请确认依赖已正确添加: ${e.message}")
            throw e
        }
        
        try {
            connection = DriverManager.getConnection(url, "sa", "")
            logger.info("[CloudStorageDatabase] 数据库连接成功")
        } catch (e: SQLException) {
            logger.severe("[CloudStorageDatabase] 数据库连接失败: ${e.message}")
            throw e
        }
        
        return connection!!
    }
    
    private fun initDatabase() {
        try {
            val conn = getConnection()
            
            conn.createStatement().use { stmt ->
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS storage_items (
                        location_key VARCHAR(128) NOT NULL,
                        item_key VARCHAR(512) NOT NULL,
                        amount BIGINT NOT NULL DEFAULT 0,
                        PRIMARY KEY (location_key, item_key)
                    )
                """)
                
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS storage_meta (
                        location_key VARCHAR(128) PRIMARY KEY,
                        total_capacity BIGINT NOT NULL DEFAULT 0,
                        max_capacity BIGINT NOT NULL DEFAULT 1000000
                    )
                """)
                
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS db_version (
                        version INT NOT NULL
                    )
                """)
                
                val rs = stmt.executeQuery("SELECT COUNT(*) AS cnt FROM db_version")
                rs.next()
                val currentVersion = if (rs.getInt("cnt") == 0) {
                    0
                } else {
                    val versionRs = stmt.executeQuery("SELECT version FROM db_version")
                    if (versionRs.next()) versionRs.getInt("version") else 0
                }
                
                if (currentVersion < DB_VERSION) {
                    migrateDatabase(conn, currentVersion, DB_VERSION)
                }
            }
            
            val itemCount = queryItemCount(conn)
            logger.info("[CloudStorageDatabase] 数据库表初始化完成，当前存储记录数: $itemCount")
        } catch (e: Exception) {
            logger.severe("[CloudStorageDatabase] 数据库初始化失败: ${e.message}")
            e.printStackTrace()
        }
    }
    
    private fun migrateDatabase(conn: Connection, fromVersion: Int, toVersion: Int) {
        logger.info("[CloudStorageDatabase] 执行数据库迁移: $fromVersion -> $toVersion")
        
        if (fromVersion < 2) {
            try {
                conn.createStatement().use { stmt ->
                    stmt.execute("DROP TABLE IF EXISTS storage_items")
                    stmt.execute("DROP TABLE IF EXISTS storage_meta")
                    
                    stmt.execute("""
                        CREATE TABLE IF NOT EXISTS storage_items (
                            location_key VARCHAR(128) NOT NULL,
                            item_key VARCHAR(512) NOT NULL,
                            amount BIGINT NOT NULL DEFAULT 0,
                            PRIMARY KEY (location_key, item_key)
                        )
                    """)
                    
                    stmt.execute("""
                        CREATE TABLE IF NOT EXISTS storage_meta (
                            location_key VARCHAR(128) PRIMARY KEY,
                            total_capacity BIGINT NOT NULL DEFAULT 0,
                            max_capacity BIGINT NOT NULL DEFAULT 1000000
                        )
                    """)
                }
                logger.info("[CloudStorageDatabase] 数据库结构已更新为v2（使用位置键）")
            } catch (e: Exception) {
                logger.warning("[CloudStorageDatabase] 数据库迁移失败: ${e.message}")
            }
        }
        
        conn.createStatement().use { stmt ->
            stmt.execute("DELETE FROM db_version")
            stmt.executeUpdate("INSERT INTO db_version (version) VALUES ($toVersion)")
        }
        
        logger.info("[CloudStorageDatabase] 数据库迁移完成")
    }
    
    private fun queryItemCount(conn: Connection): Int {
        conn.prepareStatement("SELECT COUNT(*) AS cnt FROM storage_items").use { stmt ->
            val rs = stmt.executeQuery()
            return if (rs.next()) rs.getInt("cnt") else 0
        }
    }
    
    fun loadStorageData(locationKey: String, storage: CloudStorage.CloudStorageData) {
        try {
            val conn = getConnection()
            
            conn.prepareStatement("SELECT total_capacity, max_capacity FROM storage_meta WHERE location_key = ?").use { stmt ->
                stmt.setString(1, locationKey)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    val totalCap = rs.getLong("total_capacity")
                    val maxCap = rs.getLong("max_capacity")
                    storage.totalCapacity.set(totalCap)
                    storage.maxCapacity = maxCap
                    logger.info("[CloudStorageDatabase] 加载仓库元数据: location=$locationKey, totalCapacity=$totalCap, maxCapacity=$maxCap")
                } else {
                    logger.info("[CloudStorageDatabase] 仓库无元数据记录(新仓库): location=$locationKey")
                }
            }
            
            conn.prepareStatement("SELECT item_key, amount FROM storage_items WHERE location_key = ?").use { stmt ->
                stmt.setString(1, locationKey)
                val rs = stmt.executeQuery()
                var itemCount = 0
                var totalAmount = 0L
                while (rs.next()) {
                    val itemKey = rs.getString("item_key")
                    val amount = rs.getLong("amount")
                    if (amount > 0) {
                        storage.items[itemKey] = AtomicLong(amount)
                        itemCount++
                        totalAmount += amount
                    }
                }
                if (itemCount > 0) {
                    logger.info("[CloudStorageDatabase] 加载仓库物品: location=$locationKey, 物品种类=$itemCount, 总数量=$totalAmount")
                }
            }
        } catch (e: Exception) {
            logger.warning("[CloudStorageDatabase] 加载仓库 $locationKey 失败: ${e.message}")
            e.printStackTrace()
        }
    }
    
    fun saveStorageData(locationKey: String, storage: CloudStorage.CloudStorageData) {
        try {
            val conn = getConnection()
            
            conn.autoCommit = false
            
            try {
                conn.prepareStatement(
                    "MERGE INTO storage_meta (location_key, total_capacity, max_capacity) VALUES (?, ?, ?)"
                ).use { stmt ->
                    stmt.setString(1, locationKey)
                    stmt.setLong(2, storage.totalCapacity.get())
                    stmt.setLong(3, storage.maxCapacity)
                    stmt.executeUpdate()
                }
                
                conn.prepareStatement("DELETE FROM storage_items WHERE location_key = ?").use { stmt ->
                    stmt.setString(1, locationKey)
                    stmt.executeUpdate()
                }
                
                var savedItems = 0
                var savedAmount = 0L
                
                conn.prepareStatement(
                    "INSERT INTO storage_items (location_key, item_key, amount) VALUES (?, ?, ?)"
                ).use { stmt ->
                    for ((itemKey, amount) in storage.items) {
                        val amt = amount.get()
                        if (amt > 0) {
                            stmt.setString(1, locationKey)
                            stmt.setString(2, itemKey)
                            stmt.setLong(3, amt)
                            stmt.addBatch()
                            savedItems++
                            savedAmount += amt
                        }
                    }
                    if (savedItems > 0) {
                        stmt.executeBatch()
                    }
                }
                
                conn.commit()
                logger.info("[CloudStorageDatabase] 保存仓库成功: location=$locationKey, 物品=$savedItems, 总量=$savedAmount")
            } catch (e: Exception) {
                try {
                    conn.rollback()
                } catch (ex: SQLException) {
                    // ignore rollback failure
                }
                throw e
            } finally {
                conn.autoCommit = true
            }
        } catch (e: Exception) {
            logger.warning("[CloudStorageDatabase] 保存仓库 $locationKey 失败: ${e.message}")
            e.printStackTrace()
        }
    }
    
    fun deleteStorageData(locationKey: String) {
        try {
            val conn = getConnection()
            
            conn.autoCommit = false
            
            try {
                conn.prepareStatement("DELETE FROM storage_meta WHERE location_key = ?").use { stmt ->
                    stmt.setString(1, locationKey)
                    stmt.executeUpdate()
                }
                
                conn.prepareStatement("DELETE FROM storage_items WHERE location_key = ?").use { stmt ->
                    stmt.setString(1, locationKey)
                    stmt.executeUpdate()
                }
                
                conn.commit()
                logger.info("[CloudStorageDatabase] 删除仓库数据成功: location=$locationKey")
            } catch (e: Exception) {
                try {
                    conn.rollback()
                } catch (ex: SQLException) {
                    // ignore
                }
                throw e
            } finally {
                conn.autoCommit = true
            }
        } catch (e: Exception) {
            logger.warning("[CloudStorageDatabase] 删除仓库 $locationKey 失败: ${e.message}")
        }
    }
    
    fun shutdown() {
        logger.info("[CloudStorageDatabase] 关闭数据库连接")
        
        try {
            CloudStorage.saveAllData()
            logger.info("[CloudStorageDatabase] 最终保存完成")
        } catch (e: Exception) {
            logger.warning("[CloudStorageDatabase] 最终保存失败: ${e.message}")
        }
        
        try {
            connection?.close()
            connection = null
        } catch (e: Exception) {
            logger.warning("[CloudStorageDatabase] 关闭数据库连接失败: ${e.message}")
        }
    }
}
