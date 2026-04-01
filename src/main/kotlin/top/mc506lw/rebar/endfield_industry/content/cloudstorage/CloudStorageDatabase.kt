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
import java.util.UUID
import java.util.concurrent.atomic.AtomicLong

object CloudStorageDatabase : Listener {
    
    private val logger = EndfieldIndustry.instance.logger
    
    private const val DB_VERSION = 1
    private var connection: Connection? = null
    private var primaryWorldName: String? = null
    private var dataLoaded = false
    
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
        if (event.world.name == primaryWorldName) {
            logger.info("[CloudStorageDatabase] 世界保存事件触发，执行保存")
            CloudStorage.saveAllData()
        }
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
    
    private fun getConnection(): Connection {
        val conn = connection
        if (conn != null && !conn.isClosed) {
            return conn
        }
        
        val dbFile = getDatabaseFile()
        val url = "jdbc:h2:${dbFile.absolutePath};MODE=MySQL;AUTO_SERVER=TRUE"
        
        Class.forName("org.h2.Driver")
        connection = DriverManager.getConnection(url, "sa", "")
        return connection!!
    }
    
    private fun initDatabase() {
        try {
            val conn = getConnection()
            
            conn.createStatement().use { stmt ->
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS storage_items (
                        grid_id UUID NOT NULL,
                        item_key VARCHAR(512) NOT NULL,
                        amount BIGINT NOT NULL DEFAULT 0,
                        PRIMARY KEY (grid_id, item_key)
                    )
                """)
                
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS storage_meta (
                        grid_id UUID PRIMARY KEY,
                        total_capacity BIGINT NOT NULL DEFAULT 0,
                        max_capacity BIGINT NOT NULL DEFAULT 1000000
                    )
                """)
                
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS db_version (
                        version INT NOT NULL
                    )
                """)
                
                val rs = stmt.executeQuery("SELECT version FROM db_version LIMIT 1")
                if (!rs.next()) {
                    stmt.execute("INSERT INTO db_version (version) VALUES ($DB_VERSION)")
                }
            }
            
            logger.info("[CloudStorageDatabase] 数据库表初始化完成")
        } catch (e: Exception) {
            logger.severe("[CloudStorageDatabase] 数据库初始化失败: ${e.message}")
            e.printStackTrace()
        }
    }
    
    fun loadStorageData(gridId: UUID, storage: CloudStorage.CloudStorageData) {
        try {
            val conn = getConnection()
            
            conn.prepareStatement("SELECT total_capacity, max_capacity FROM storage_meta WHERE grid_id = ?").use { stmt ->
                stmt.setString(1, gridId.toString())
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    storage.totalCapacity.set(rs.getLong("total_capacity"))
                    storage.maxCapacity = rs.getLong("max_capacity")
                }
            }
            
            conn.prepareStatement("SELECT item_key, amount FROM storage_items WHERE grid_id = ?").use { stmt ->
                stmt.setString(1, gridId.toString())
                val rs = stmt.executeQuery()
                while (rs.next()) {
                    val itemKey = rs.getString("item_key")
                    val amount = rs.getLong("amount")
                    if (amount > 0) {
                        storage.items[itemKey] = AtomicLong(amount)
                    }
                }
            }
            
            logger.info("[CloudStorageDatabase] 加载仓库 $gridId 数据: ${storage.items.size} 种物品, 总量 ${storage.totalCapacity.get()}")
        } catch (e: Exception) {
            logger.warning("[CloudStorageDatabase] 加载仓库 $gridId 失败: ${e.message}")
        }
    }
    
    fun saveStorageData(gridId: UUID, storage: CloudStorage.CloudStorageData) {
        try {
            val conn = getConnection()
            
            conn.prepareStatement(
                "MERGE INTO storage_meta (grid_id, total_capacity, max_capacity) VALUES (?, ?, ?)"
            ).use { stmt ->
                stmt.setString(1, gridId.toString())
                stmt.setLong(2, storage.totalCapacity.get())
                stmt.setLong(3, storage.maxCapacity)
                stmt.execute()
            }
            
            conn.prepareStatement("DELETE FROM storage_items WHERE grid_id = ?").use { stmt ->
                stmt.setString(1, gridId.toString())
                stmt.execute()
            }
            
            conn.prepareStatement(
                "INSERT INTO storage_items (grid_id, item_key, amount) VALUES (?, ?, ?)"
            ).use { stmt ->
                for ((itemKey, amount) in storage.items) {
                    val amt = amount.get()
                    if (amt > 0) {
                        stmt.setString(1, gridId.toString())
                        stmt.setString(2, itemKey)
                        stmt.setLong(3, amt)
                        stmt.addBatch()
                    }
                }
                stmt.executeBatch()
            }
            
            logger.fine("[CloudStorageDatabase] 保存仓库 $gridId 数据完成")
        } catch (e: Exception) {
            logger.warning("[CloudStorageDatabase] 保存仓库 $gridId 失败: ${e.message}")
        }
    }
    
    fun deleteStorageData(gridId: UUID) {
        try {
            val conn = getConnection()
            
            conn.prepareStatement("DELETE FROM storage_meta WHERE grid_id = ?").use { stmt ->
                stmt.setString(1, gridId.toString())
                stmt.execute()
            }
            
            conn.prepareStatement("DELETE FROM storage_items WHERE grid_id = ?").use { stmt ->
                stmt.setString(1, gridId.toString())
                stmt.execute()
            }
            
            logger.info("[CloudStorageDatabase] 删除仓库 $gridId 数据完成")
        } catch (e: Exception) {
            logger.warning("[CloudStorageDatabase] 删除仓库 $gridId 失败: ${e.message}")
        }
    }
    
    fun shutdown() {
        logger.info("[CloudStorageDatabase] 关闭数据库连接")
        try {
            connection?.close()
            connection = null
        } catch (e: Exception) {
            logger.warning("[CloudStorageDatabase] 关闭数据库连接失败: ${e.message}")
        }
    }
}
