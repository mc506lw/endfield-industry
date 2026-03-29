package top.mc506lw.rebar.endfield_industry.content.powersystem.storage

import io.github.pylonmc.rebar.block.BlockStorage
import org.bukkit.Bukkit
import org.bukkit.World
import top.mc506lw.rebar.endfield_industry.EndfieldIndustry
import top.mc506lw.rebar.endfield_industry.content.powersystem.PowerGrid
import top.mc506lw.rebar.endfield_industry.content.powersystem.PowerGridManager
import top.mc506lw.rebar.endfield_industry.content.powersystem.PowerSystem
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerDevice
import java.util.UUID

object DataConsistencyValidator {
    
    private val logger = EndfieldIndustry.instance.logger
    
    data class ValidationResult(
        val valid: Boolean,
        val errors: List<String>,
        val warnings: List<String>,
        val stats: Map<String, Int>
    )
    
    fun validateAndRepair(): ValidationResult {
        logger.info("[DataConsistencyValidator] 开始数据一致性验证...")
        
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()
        val stats = mutableMapOf<String, Int>()
        
        val gridManager = PowerGridManager.getInstance()
        val allGrids = gridManager.getAllGrids()
        
        stats["total_grids"] = allGrids.size
        
        var totalDevices = 0
        var totalConsumers = 0
        var orphanDevices = 0
        var invalidConnections = 0
        
        for ((gridId, grid) in allGrids) {
            val devices = grid.getDevices()
            val consumers = grid.getConsumers()
            
            totalDevices += devices.size
            totalConsumers += consumers.size
            
            for (device in devices) {
                if (device.getGrid() != grid) {
                    errors.add("设备 ${device.block.location} 的电网引用不一致")
                    device.setGrid(grid)
                }
            }
            
            if (devices.isEmpty() && consumers.isEmpty()) {
                warnings.add("电网 $gridId 没有任何设备或消费者")
            }
        }
        
        stats["total_devices"] = totalDevices
        stats["total_consumers"] = totalConsumers
        
        val allConnections = PowerSystem.connectionManager.getAllConnections()
        var totalConnections = 0
        
        for ((key, connections) in allConnections) {
            for (conn in connections) {
                totalConnections++
                
                val sourceGrid = conn.source.getGrid()
                val targetGrid = conn.target.getGrid()
                
                if (sourceGrid != targetGrid) {
                    errors.add("连接 ${conn.sourceLocation} -> ${conn.targetLocation} 的两端设备不在同一电网")
                    
                    if (sourceGrid != null && targetGrid != null && sourceGrid != targetGrid) {
                        PowerSystem.gridManager.mergeGrids(sourceGrid.gridId, targetGrid.gridId)
                        logger.info("[DataConsistencyValidator] 自动合并电网 ${sourceGrid.gridId} 和 ${targetGrid.gridId}")
                    }
                }
                
                if (!conn.hasWireDisplay()) {
                    warnings.add("连接 ${conn.sourceLocation} -> ${conn.targetLocation} 没有电线显示，正在重建")
                    conn.createWireDisplay()
                }
            }
        }
        
        stats["total_connections"] = totalConnections
        stats["orphan_devices"] = orphanDevices
        stats["invalid_connections"] = invalidConnections
        
        val pendingConns = PowerSystemStorage.hasPendingConnections()
        if (pendingConns) {
            warnings.add("存在待恢复的连接数据，可能在下次区块加载时恢复")
        }
        
        val valid = errors.isEmpty()
        
        if (valid) {
            logger.info("[DataConsistencyValidator] 数据一致性验证通过")
        } else {
            logger.warning("[DataConsistencyValidator] 数据一致性验证发现 ${errors.size} 个错误，已尝试自动修复")
        }
        
        if (warnings.isNotEmpty()) {
            logger.info("[DataConsistencyValidator] 发现 ${warnings.size} 个警告")
        }
        
        logStats(stats)
        
        return ValidationResult(valid, errors, warnings, stats)
    }
    
    private fun logStats(stats: Map<String, Int>) {
        logger.info("[DataConsistencyValidator] 统计信息:")
        logger.info("  - 电网总数: ${stats["total_grids"] ?: 0}")
        logger.info("  - 设备总数: ${stats["total_devices"] ?: 0}")
        logger.info("  - 消费者总数: ${stats["total_consumers"] ?: 0}")
        logger.info("  - 连接总数: ${stats["total_connections"] ?: 0}")
    }
    
    fun validateGridIntegrity(gridId: UUID): Boolean {
        val grid = PowerGridManager.getInstance().getGrid(gridId) ?: return false
        
        val devices = grid.getDevices()
        val calculatedCapacity = devices.sumOf { it.getPowerContribution() }
        
        if (calculatedCapacity != grid.totalCapacity) {
            logger.warning("[DataConsistencyValidator] 电网 $gridId 容量不一致: 记录=${grid.totalCapacity}, 计算=$calculatedCapacity")
            return false
        }
        
        return true
    }
    
    fun cleanupOrphanGrids(): Int {
        if (PowerSystemStorage.hasPendingConnections()) {
            logger.info("[DataConsistencyValidator] 存在待恢复连接，跳过空电网清理")
            return 0
        }
        
        val gridManager = PowerGridManager.getInstance()
        val allGrids = gridManager.getAllGrids().toMap()
        var cleanedCount = 0
        
        for ((gridId, grid) in allGrids) {
            if (grid.getDevices().isEmpty() && grid.getConsumers().isEmpty()) {
                gridManager.removeGrid(gridId)
                cleanedCount++
                logger.info("[DataConsistencyValidator] 清理空电网: $gridId")
            }
        }
        
        return cleanedCount
    }
    
    fun runStartupValidation() {
        Bukkit.getScheduler().runTaskLater(EndfieldIndustry.instance, Runnable {
            logger.info("[DataConsistencyValidator] 执行启动时数据验证...")
            val result = validateAndRepair()
            
            if (!result.valid) {
                logger.warning("[DataConsistencyValidator] 启动验证发现问题，已尝试自动修复")
                result.errors.forEach { logger.warning("  错误: $it") }
            }
            
            result.warnings.forEach { logger.info("  警告: $it") }
            
            val cleaned = cleanupOrphanGrids()
            if (cleaned > 0) {
                logger.info("[DataConsistencyValidator] 清理了 $cleaned 个空电网")
            }
        }, 200L)
    }
}
