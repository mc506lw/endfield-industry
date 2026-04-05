package top.mc506lw.rebar.endfield_industry.content.powersystem.storage

import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.world.WorldLoadEvent
import org.bukkit.event.world.WorldSaveEvent
import top.mc506lw.rebar.endfield_industry.EndfieldIndustry
import top.mc506lw.rebar.endfield_industry.content.powersystem.PowerGrid
import top.mc506lw.rebar.endfield_industry.content.powersystem.PowerGridManager
import top.mc506lw.rebar.endfield_industry.content.powersystem.PowerSystem
import top.mc506lw.rebar.endfield_industry.content.powersystem.connection.PowerConnection
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerDevice
import java.io.*
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

object PowerSystemStorage : Listener {
    
    private val logger = EndfieldIndustry.instance.logger
    
    private const val MAGIC_POWER = 0x504F5745
    private const val VERSION = 3
    private const val BACKUP_COUNT = 5
    
    private val gridsDirty = AtomicBoolean(false)
    private val connectionsDirty = AtomicBoolean(false)
    private val dirtyGrids = ConcurrentHashMap.newKeySet<UUID>()
    
    private var saveTaskId: Int = -1
    private var dataLoaded = false
    
    private val saveLock = ReentrantReadWriteLock()
    
    private var primaryWorldName: String? = null
    
    private val pendingConnections: MutableMap<String, MutableList<ConnectionData>> = ConcurrentHashMap()
    
    fun initialize() {
        logger.info("[PowerSystemStorage] 初始化统一存储系统 v$VERSION")
        
        Bukkit.getPluginManager().registerEvents(this, EndfieldIndustry.instance)
        
        val worlds = Bukkit.getWorlds()
        if (worlds.isNotEmpty()) {
            primaryWorldName = worlds[0].name
            logger.info("[PowerSystemStorage] 主世界: $primaryWorldName，立即加载数据")
            loadAllData()
            startAutoSaveTask()
            dataLoaded = true
            
            DataConsistencyValidator.runStartupValidation()
        } else {
            logger.info("[PowerSystemStorage] 世界未加载，等待WorldLoadEvent")
        }
        
        logger.info("[PowerSystemStorage] 存储系统初始化完成")
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    fun onWorldLoad(event: WorldLoadEvent) {
        if (!dataLoaded) {
            primaryWorldName = event.world.name
            logger.info("[PowerSystemStorage] 检测到世界加载: ${event.world.name}，开始加载数据")
            loadAllData()
            startAutoSaveTask()
            dataLoaded = true
            
            DataConsistencyValidator.runStartupValidation()
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    fun onWorldSave(event: WorldSaveEvent) {
        if (event.world.name == primaryWorldName) {
            logger.info("[PowerSystemStorage] 世界保存事件触发，执行增量保存")
            saveDirtyData()
        }
    }
    
    fun shutdown() {
        logger.info("[PowerSystemStorage] 关闭存储系统")
        stopAutoSaveTask()
        if (dataLoaded) {
            saveAll()
        }
        pendingConnections.clear()
        logger.info("[PowerSystemStorage] 存储系统已关闭")
    }
    
    private fun startAutoSaveTask() {
        saveTaskId = Bukkit.getScheduler().runTaskTimer(
            EndfieldIndustry.instance,
            Runnable { saveDirtyData() },
            20L * 60,
            20L * 60
        ).taskId
        logger.info("[PowerSystemStorage] 自动保存任务已启动，任务ID: $saveTaskId，间隔: 60秒")
    }
    
    private fun stopAutoSaveTask() {
        if (saveTaskId != -1) {
            Bukkit.getScheduler().cancelTask(saveTaskId)
            logger.info("[PowerSystemStorage] 自动保存任务已停止，任务ID: $saveTaskId")
            saveTaskId = -1
        }
    }
    
    fun markGridDirty(gridId: UUID) {
        dirtyGrids.add(gridId)
        gridsDirty.set(true)
    }
    
    fun markAllGridsDirty() {
        gridsDirty.set(true)
    }
    
    fun markConnectionsDirty() {
        connectionsDirty.set(true)
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
        val dataFolder = File(world.worldFolder, "data/endfield-industry/powersystem")
        if (!dataFolder.exists()) {
            dataFolder.mkdirs()
            logger.info("[PowerSystemStorage] 创建数据文件夹: ${dataFolder.absolutePath}")
        }
        return dataFolder
    }
    
    private fun getMainDataFile(): File {
        return File(getDataFolder(), "power_system.dat")
    }
    
    private fun getBackupFile(index: Int): File {
        return File(getDataFolder(), "power_system.dat.bak$index")
    }
    
    fun saveAll() {
        logger.info("[PowerSystemStorage] 开始保存所有数据")
        saveDataInternal(null, null)
        logger.info("[PowerSystemStorage] 所有数据保存完成")
    }
    
    private fun loadAllData() {
        logger.info("[PowerSystemStorage] 开始加载数据")
        loadDataFromFile()
        logger.info("[PowerSystemStorage] 数据加载完成")
    }
    
    private fun saveDirtyData() {
        val hasGridsDirty = gridsDirty.get()
        val hasConnectionsDirty = connectionsDirty.get()
        
        if (hasGridsDirty || hasConnectionsDirty) {
            val dirtyGridIds = if (hasGridsDirty) {
                val set = dirtyGrids.toSet()
                dirtyGrids.clear()
                set
            } else null
            
            val dirtyConnections = if (hasConnectionsDirty) {
                PowerSystem.connectionManager.getAllConnections()
            } else null
            
            saveDataInternal(dirtyGridIds, dirtyConnections)
            
            if (hasGridsDirty) gridsDirty.set(false)
            if (hasConnectionsDirty) connectionsDirty.set(false)
        }
    }
    
    private fun saveDataInternal(dirtyGridIds: Set<UUID>?, dirtyConnections: Map<String, Set<PowerConnection>>?) {
        saveLock.write {
            try {
                val file = getMainDataFile()
                
                val allGrids = PowerGridManager.getInstance().getAllGrids()
                
                val gridsToSave = if (dirtyGridIds == null || dirtyGridIds.isEmpty()) {
                    allGrids.values.toList()
                } else {
                    allGrids.filterKeys { it in dirtyGridIds }.values.toList()
                }
                
                val nonEmptyGrids = gridsToSave.filter { grid ->
                    grid.getDevices().isNotEmpty() || grid.getConsumers().isNotEmpty()
                }
                
                val emptyGridCount = gridsToSave.size - nonEmptyGrids.size
                if (emptyGridCount > 0) {
                    logger.info("[PowerSystemStorage] 过滤掉 $emptyGridCount 个空电网")
                }
                
                val connectionsToSave = if (dirtyConnections == null) {
                    PowerSystem.connectionManager.getAllConnections()
                } else {
                    dirtyConnections
                }
                
                val connectionList = connectionsToSave.values
                    .flatten()
                    .distinctBy { "${it.sourceLocation}-${it.targetLocation}" }
                
                if (nonEmptyGrids.isEmpty() && connectionList.isEmpty() && !file.exists()) {
                    return
                }
                
                rotateBackups()
                
                val gridDataList = nonEmptyGrids.map { createGridData(it) }
                val connectionDataList = connectionList.map { createConnectionData(it) }
                
                val tempFile = File(file.parentFile, "power_system.dat.tmp")
                
                DataOutputStream(BufferedOutputStream(FileOutputStream(tempFile))).use { out ->
                    out.writeInt(MAGIC_POWER)
                    out.writeInt(VERSION)
                    
                    out.writeInt(gridDataList.size)
                    for (data in gridDataList) {
                        writeGridData(out, data)
                    }
                    
                    out.writeInt(connectionDataList.size)
                    for (data in connectionDataList) {
                        writeConnectionData(out, data)
                    }
                    
                    val checksum = calculateChecksumFromData(gridDataList, connectionDataList)
                    out.writeInt(checksum)
                }
                
                if (file.exists()) {
                    file.delete()
                }
                tempFile.renameTo(file)
                
                logger.info("[PowerSystemStorage] 成功保存 ${gridDataList.size} 个电网, ${connectionDataList.size} 个连接")
            } catch (e: Exception) {
                logger.severe("[PowerSystemStorage] 保存数据失败: ${e.message}")
                e.printStackTrace()
            }
        }
    }
    
    private fun rotateBackups() {
        val dataFolder = getDataFolder()
        
        for (i in BACKUP_COUNT - 1 downTo 1) {
            val oldBackup = File(dataFolder, "power_system.dat.bak$i")
            val newBackup = File(dataFolder, "power_system.dat.bak${i + 1}")
            if (oldBackup.exists()) {
                if (newBackup.exists()) {
                    newBackup.delete()
                }
                oldBackup.renameTo(newBackup)
            }
        }
        
        val currentFile = getMainDataFile()
        if (currentFile.exists()) {
            val firstBackup = File(dataFolder, "power_system.dat.bak1")
            currentFile.copyTo(firstBackup, overwrite = true)
        }
    }
    
    private fun loadDataFromFile(): Boolean {
        val file = getMainDataFile()
        return tryLoadFromFile(file) || tryLoadFromBackups()
    }
    
    private fun tryLoadFromFile(file: File): Boolean {
        if (!file.exists()) {
            logger.info("[PowerSystemStorage] 数据文件不存在: ${file.name}")
            return false
        }
        
        try {
            DataInputStream(BufferedInputStream(FileInputStream(file))).use { input ->
                val magic = input.readInt()
                
                if (magic != MAGIC_POWER) {
                    logger.warning("[PowerSystemStorage] 数据文件魔数不匹配: ${file.name}")
                    return false
                }
                
                val version = input.readInt()
                
                if (version > VERSION) {
                    logger.warning("[PowerSystemStorage] 数据文件版本 $version 高于当前版本 $VERSION，可能存在兼容性问题")
                }
                
                val gridCount = input.readInt()
                val gridDataList = mutableListOf<GridData>()
                
                for (i in 0 until gridCount) {
                    val data = readGridData(input, version)
                    gridDataList.add(data)
                }
                
                val connectionCount = if (version >= 3) input.readInt() else 0
                val connectionDataList = mutableListOf<ConnectionData>()
                
                for (i in 0 until connectionCount) {
                    val data = readConnectionData(input, version)
                    connectionDataList.add(data)
                }
                
                val storedChecksum = if (version >= 2) input.readInt() else 0
                val calculatedChecksum = if (version >= 2) {
                    calculateChecksumFromData(gridDataList, connectionDataList)
                } else 0
                
                if (version >= 2 && storedChecksum != calculatedChecksum) {
                    logger.warning("[PowerSystemStorage] 校验和不匹配: 存储=$storedChecksum, 计算=$calculatedChecksum，文件: ${file.name}")
                    logger.warning("[PowerSystemStorage] 跳过校验检查，继续加载数据...")
                }
                
                for (data in gridDataList) {
                    val grid = PowerGridManager.getInstance().createGridWithId(data.gridId)
                    grid.restoreState(data.totalCapacity, data.usedCapacity, data.wasOverloaded)
                }
                
                for (data in connectionDataList) {
                    val sourceKey = "${data.sourceWorldUid}:${data.sourceX}:${data.sourceY}:${data.sourceZ}"
                    val targetKey = "${data.targetWorldUid}:${data.targetX}:${data.targetY}:${data.targetZ}"
                    
                    pendingConnections.computeIfAbsent(sourceKey) { mutableListOf() }.add(data)
                    pendingConnections.computeIfAbsent(targetKey) { mutableListOf() }.add(data)
                }
                
                logger.info("[PowerSystemStorage] 成功从 ${file.name} 加载 $gridCount 个电网, $connectionCount 个连接")
                return true
            }
        } catch (e: Exception) {
            logger.severe("[PowerSystemStorage] 加载数据失败 (${file.name}): ${e.message}")
            e.printStackTrace()
            return false
        }
    }
    
    private fun tryLoadFromBackups(): Boolean {
        for (i in 1..BACKUP_COUNT) {
            val backupFile = getBackupFile(i)
            if (tryLoadFromFile(backupFile)) {
                logger.info("[PowerSystemStorage] 从备份文件 ${backupFile.name} 恢复数据")
                return true
            }
        }
        
        logger.warning("[PowerSystemStorage] 所有备份文件都无法加载，将创建新数据")
        return false
    }
    
    private fun calculateChecksumFromData(gridDataList: List<GridData>, connectionDataList: List<ConnectionData>): Int {
        var checksum = 0
        for (data in gridDataList) {
            checksum = checksum * 31 + data.gridId.hashCode()
            checksum = checksum * 31 + data.totalCapacity
            checksum = checksum * 31 + data.usedCapacity
        }
        for (data in connectionDataList) {
            checksum = checksum * 31 + data.sourceWorldUid.hashCode()
            checksum = checksum * 31 + data.sourceX
            checksum = checksum * 31 + data.sourceY
            checksum = checksum * 31 + data.sourceZ
            checksum = checksum * 31 + data.targetWorldUid.hashCode()
            checksum = checksum * 31 + data.targetX
            checksum = checksum * 31 + data.targetY
            checksum = checksum * 31 + data.targetZ
            checksum = checksum * 31 + data.distance
        }
        return checksum
    }
    
    fun getPendingConnectionsForDevice(device: PowerDevice): List<ConnectionData> {
        val loc = device.block.location
        val world = loc.world ?: return emptyList()
        val key = "${world.uid}:${loc.blockX}:${loc.blockY}:${loc.blockZ}"
        return pendingConnections[key] ?: emptyList()
    }
    
    fun removePendingConnectionsForDevice(device: PowerDevice) {
        val loc = device.block.location
        val world = loc.world ?: return
        val key = "${world.uid}:${loc.blockX}:${loc.blockY}:${loc.blockZ}"
        pendingConnections.remove(key)
    }
    
    fun removeRestoredConnections(connectionKeys: Set<String>) {
        for ((deviceKey, connList) in pendingConnections.toList()) {
            val remaining = connList.filter { connData ->
                val connKey = getConnectionKey(connData)
                connKey !in connectionKeys
            }
            
            if (remaining.isEmpty()) {
                pendingConnections.remove(deviceKey)
            } else if (remaining.size < connList.size) {
                pendingConnections[deviceKey] = remaining.toMutableList()
            }
        }
    }
    
    private fun getConnectionKey(data: ConnectionData): String {
        val coords = listOf(
            Triple(data.sourceWorldUid, data.sourceX, data.sourceY to data.sourceZ),
            Triple(data.targetWorldUid, data.targetX, data.targetY to data.targetZ)
        ).sortedBy { "${it.first}:${it.second}:${it.third.first}:${it.third.second}" }
        
        val first = coords[0]
        val second = coords[1]
        return "${first.first}:${first.second}:${first.third.first}:${first.third.second}-${second.first}:${second.second}:${second.third.first}:${second.third.second}"
    }
    
    fun hasPendingConnections(): Boolean = pendingConnections.isNotEmpty()
    
    private fun writeGridData(out: DataOutput, data: GridData) {
        writeUUID(out, data.gridId)
        out.writeInt(data.totalCapacity)
        out.writeInt(data.usedCapacity)
        out.writeBoolean(data.wasOverloaded)
        out.writeInt(data.deviceCount)
        out.writeInt(data.consumerCount)
        out.writeInt(data.reserved.size)
        for (b in data.reserved) {
            out.writeByte(b.toInt())
        }
    }
    
    private fun readGridData(input: DataInput, version: Int): GridData {
        val gridId = readUUID(input)
        val totalCapacity = input.readInt()
        val usedCapacity = input.readInt()
        val wasOverloaded = if (version >= 2) input.readBoolean() else usedCapacity > totalCapacity
        val deviceCount = input.readInt()
        val consumerCount = input.readInt()
        val reservedSize = if (version >= 1) input.readInt() else 16
        val reserved = ByteArray(reservedSize)
        input.readFully(reserved)
        
        return GridData(
            gridId = gridId,
            totalCapacity = totalCapacity,
            usedCapacity = usedCapacity,
            wasOverloaded = wasOverloaded,
            deviceCount = deviceCount,
            consumerCount = consumerCount,
            reserved = reserved
        )
    }
    
    private fun writeConnectionData(out: DataOutput, data: ConnectionData) {
        writeUUID(out, data.sourceWorldUid)
        out.writeInt(data.sourceX)
        out.writeInt(data.sourceY)
        out.writeInt(data.sourceZ)
        writeUUID(out, data.targetWorldUid)
        out.writeInt(data.targetX)
        out.writeInt(data.targetY)
        out.writeInt(data.targetZ)
        out.writeInt(data.distance)
    }
    
    private fun readConnectionData(input: DataInput, version: Int): ConnectionData {
        val sourceWorldUid = readUUID(input)
        val sourceX = input.readInt()
        val sourceY = input.readInt()
        val sourceZ = input.readInt()
        val targetWorldUid = readUUID(input)
        val targetX = input.readInt()
        val targetY = input.readInt()
        val targetZ = input.readInt()
        val distance = input.readInt()
        
        return ConnectionData(
            sourceWorldUid = sourceWorldUid,
            sourceX = sourceX,
            sourceY = sourceY,
            sourceZ = sourceZ,
            targetWorldUid = targetWorldUid,
            targetX = targetX,
            targetY = targetY,
            targetZ = targetZ,
            distance = distance
        )
    }
    
    private fun writeUUID(out: DataOutput, uuid: UUID) {
        out.writeLong(uuid.mostSignificantBits)
        out.writeLong(uuid.leastSignificantBits)
    }
    
    private fun readUUID(input: DataInput): UUID {
        val most = input.readLong()
        val least = input.readLong()
        return UUID(most, least)
    }
    
    private fun createGridData(grid: PowerGrid): GridData {
        return GridData(
            gridId = grid.gridId,
            totalCapacity = grid.totalCapacity,
            usedCapacity = grid.usedCapacity,
            wasOverloaded = grid.getWasOverloaded(),
            deviceCount = grid.getDevices().size,
            consumerCount = grid.getConsumers().size,
            reserved = ByteArray(16)
        )
    }
    
    private fun createConnectionData(conn: PowerConnection): ConnectionData {
        val sourceLoc = conn.sourceLocation
        val targetLoc = conn.targetLocation
        
        return ConnectionData(
            sourceWorldUid = sourceLoc.world?.uid ?: UUID(0, 0),
            sourceX = sourceLoc.blockX,
            sourceY = sourceLoc.blockY,
            sourceZ = sourceLoc.blockZ,
            targetWorldUid = targetLoc.world?.uid ?: UUID(0, 0),
            targetX = targetLoc.blockX,
            targetY = targetLoc.blockY,
            targetZ = targetLoc.blockZ,
            distance = conn.distance
        )
    }
    
    data class GridData(
        val gridId: UUID,
        val totalCapacity: Int,
        val usedCapacity: Int,
        val wasOverloaded: Boolean,
        val deviceCount: Int,
        val consumerCount: Int,
        val reserved: ByteArray
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as GridData
            return gridId == other.gridId
        }
        
        override fun hashCode(): Int = gridId.hashCode()
    }
    
    data class ConnectionData(
        val sourceWorldUid: UUID,
        val sourceX: Int,
        val sourceY: Int,
        val sourceZ: Int,
        val targetWorldUid: UUID,
        val targetX: Int,
        val targetY: Int,
        val targetZ: Int,
        val distance: Int
    )
}
