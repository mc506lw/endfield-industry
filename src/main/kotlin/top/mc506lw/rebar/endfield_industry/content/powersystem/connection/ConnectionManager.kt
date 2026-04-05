package top.mc506lw.rebar.endfield_industry.content.powersystem.connection

import io.github.pylonmc.rebar.block.BlockStorage
import io.github.pylonmc.rebar.i18n.RebarArgument
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Player
import top.mc506lw.rebar.endfield_industry.content.powersystem.PowerGrid
import top.mc506lw.rebar.endfield_industry.content.powersystem.PowerSystem
import top.mc506lw.rebar.endfield_industry.content.powersystem.PowerSystemConfig
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerConsumerDevice
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerDevice
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.ProtocolCoreController
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.RelayDiffuser
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerStationEmitter
import top.mc506lw.rebar.endfield_industry.content.powersystem.event.PowerDeviceConnectEvent
import top.mc506lw.rebar.endfield_industry.content.powersystem.storage.PowerSystemStorage
import top.mc506lw.rebar.endfield_industry.content.powersystem.connection.wirepath.WirePathManager
import top.mc506lw.rebar.endfield_industry.content.powersystem.connection.wirepath.WirePathSession
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.sqrt

class ConnectionManager(private val config: PowerSystemConfig) {

    private val connectingPlayers: MutableMap<UUID, PowerDevice> = ConcurrentHashMap()
    private val pathCache: PathCache = PathCache()
    private val deviceConnections: MutableMap<String, MutableSet<PowerConnection>> = ConcurrentHashMap()

    init {
        WirePathManager.relayMaxDistance = config.relayConnectionDistance.toDouble()
        WirePathManager.powerStationMaxDistance = config.powerStationConnectionDistance.toDouble()
    }

    fun startConnection(player: Player, device: PowerDevice) {
        connectingPlayers[player.uniqueId] = device
        
        WirePathManager.startPath(player, device)
    }

    fun endConnection(player: Player) {
        connectingPlayers.remove(player.uniqueId)
        
        WirePathManager.endPath(player)
    }

    fun isConnecting(player: Player): Boolean = connectingPlayers.containsKey(player.uniqueId)

    fun getConnectingDevice(player: Player): PowerDevice? = connectingPlayers[player.uniqueId]

    fun tryConnect(player: Player, targetDevice: PowerDevice): PowerConnection? {
        val sourceDevice = connectingPlayers[player.uniqueId] ?: return null
        
        if (sourceDevice == targetDevice) {
            endConnection(player)
            return null
        }

        if (areConnected(sourceDevice, targetDevice)) {
            player.sendActionBar(Component.translatable("endfield-industry.message.connection.already_connected"))
            return null
        }

        val maxDistance = getMaxDistance(sourceDevice, targetDevice)
        if (maxDistance <= 0) {
            player.sendActionBar(Component.translatable("endfield-industry.message.connection.cannot_connect"))
            return null
        }

        val session = WirePathManager.getSession(player)
        if (session != null && session.isOverMaxDistance) {
            player.sendActionBar(Component.translatable("endfield-industry.message.connection.too_far")
                .arguments(RebarArgument.of("distance", (session.pathLength - maxDistance).toInt())))
            return null
        }

        val pathLength = WirePathManager.getPathLength(player)
        val actualDistance = if (pathLength > 0) {
            pathLength.toInt()
        } else {
            calculateDistance(sourceDevice, targetDevice)
        }

        if (actualDistance > maxDistance) {
            player.sendActionBar(Component.translatable("endfield-industry.message.connection.too_far")
                .arguments(RebarArgument.of("distance", actualDistance - maxDistance)))
            return null
        }

        val pathLocations = WirePathManager.getFullPath(player)
        val connection = createConnectionWithPath(sourceDevice, targetDevice, pathLocations, actualDistance)
        if (connection != null) {
            endConnection(player)
            player.sendActionBar(Component.translatable("endfield-industry.message.connection.success")
                .arguments(RebarArgument.of("distance", actualDistance)))
        }
        return connection
    }

    fun areConnected(device1: PowerDevice, device2: PowerDevice): Boolean {
        val connections1 = getConnections(device1)
        
        for (conn in connections1) {
            if (conn.source == device2 || conn.target == device2) {
                return true
            }
        }
        
        return false
    }

    private fun getDeviceKey(device: PowerDevice): String {
        val loc = device.block.location
        val world = loc.world ?: return "unknown:0:0:0"
        return "${world.uid}:${loc.blockX}:${loc.blockY}:${loc.blockZ}"
    }

    private fun getMaxDistance(source: PowerDevice, target: PowerDevice): Int {
        val sourceDistance = getDeviceMaxDistance(source)
        val targetDistance = getDeviceMaxDistance(target)
        
        if (sourceDistance <= 0 || targetDistance <= 0) {
            return 0
        }
        
        return maxOf(sourceDistance, targetDistance)
    }

    private fun getDeviceMaxDistance(device: PowerDevice): Int {
        return when (device) {
            is RelayDiffuser -> config.relayConnectionDistance
            is PowerStationEmitter -> config.powerStationConnectionDistance
            is ProtocolCoreController -> config.relayConnectionDistance
            else -> 0
        }
    }

    private fun calculateDistance(source: PowerDevice, target: PowerDevice): Int {
        val sourceLoc = source.block.location
        val targetLoc = target.block.location
        
        val dx = sourceLoc.x - targetLoc.x
        val dy = sourceLoc.y - targetLoc.y
        val dz = sourceLoc.z - targetLoc.z
        
        return sqrt(dx * dx + dy * dy + dz * dz).toInt()
    }

    private fun createConnectionWithPath(
        source: PowerDevice, 
        target: PowerDevice, 
        pathLocations: List<Location>?,
        distance: Int
    ): PowerConnection? {
        val path = if (pathLocations != null && pathLocations.isNotEmpty()) {
            pathLocations.map { it.block }.toMutableList()
        } else {
            val simplePath = mutableListOf<Block>()
            simplePath.add(source.block)
            simplePath.add(target.block)
            simplePath
        }
        
        val connection = PowerConnection(source, target, path, distance)
        
        addConnection(source, connection)
        addConnection(target, connection)
        
        val sourceGrid = source.getGrid()
        val targetGrid = target.getGrid()
        var finalGrid: PowerGrid? = null
        
        when {
            sourceGrid == null && targetGrid == null -> {
                val newGrid = PowerSystem.gridManager.createGrid()
                source.connectToGrid(newGrid)
                target.connectToGrid(newGrid)
                finalGrid = newGrid
            }
            sourceGrid == null -> {
                source.connectToGrid(targetGrid!!)
                finalGrid = targetGrid
            }
            targetGrid == null -> {
                target.connectToGrid(sourceGrid!!)
                finalGrid = sourceGrid
            }
            sourceGrid != targetGrid -> {
                PowerSystem.gridManager.mergeGrids(sourceGrid.gridId, targetGrid.gridId)
                finalGrid = sourceGrid
            }
            else -> {
                finalGrid = sourceGrid
            }
        }
        
        connection.createWireDisplay()
        
        val station = when {
            source is PowerStationEmitter -> source
            target is PowerStationEmitter -> target
            else -> null
        }
        
        if (station != null && finalGrid != null) {
            notifyNearbyConsumersForStation(station, finalGrid)
        }
        
        PowerSystemStorage.markConnectionsDirty()
        
        val event = PowerDeviceConnectEvent(source, target, connection)
        event.callEvent()
        
        return connection
    }

    private fun createConnection(source: PowerDevice, target: PowerDevice, distance: Int): PowerConnection? {
        return createConnectionWithPath(source, target, null, distance)
    }
    
    private fun notifyNearbyConsumersForStation(station: PowerStationEmitter, grid: PowerGrid) {
        val range = PowerSystem.config.powerStationSupplyRange
        val halfRange = range / 2
        val center = station.block
        
        for (dx in -halfRange..halfRange) {
            for (dz in -halfRange..halfRange) {
                val pos = center.location.add(dx.toDouble(), 0.0, dz.toDouble()).block
                val rebarBlock = BlockStorage.get(pos)
                
                if (rebarBlock is PowerConsumerDevice) {
                    if (rebarBlock.getConnectedGrid() == null) {
                        rebarBlock.connectToGrid(grid)
                    }
                }
            }
        }
    }

    private fun addConnection(device: PowerDevice, connection: PowerConnection) {
        val key = getDeviceKey(device)
        deviceConnections.computeIfAbsent(key) { ConcurrentHashMap.newKeySet() }.add(connection)
    }

    fun getConnections(device: PowerDevice): Set<PowerConnection> {
        val key = getDeviceKey(device)
        return deviceConnections[key]?.toSet() ?: emptySet()
    }
    
    fun getAllConnections(): Map<String, Set<PowerConnection>> {
        return deviceConnections.mapValues { it.value.toSet() }
    }

    fun removeConnection(connection: PowerConnection) {
        connection.removeWireDisplay()
        
        val sourceKey = getDeviceKey(connection.source)
        val targetKey = getDeviceKey(connection.target)
        
        deviceConnections[sourceKey]?.remove(connection)
        deviceConnections[targetKey]?.remove(connection)
        
        PowerSystemStorage.markConnectionsDirty()
    }
    
    fun restoreConnection(connection: PowerConnection) {
        addConnection(connection.source, connection)
        addConnection(connection.target, connection)
        connection.createWireDisplay()
    }
    
    fun clearAllConnections() {
        for (connections in deviceConnections.values) {
            for (conn in connections) {
                conn.removeWireDisplay()
            }
        }
        deviceConnections.clear()
    }

    fun getPathCache(): PathCache = pathCache

    fun updatePath(player: Player): Boolean {
        val result = WirePathManager.updatePath(player)
        if (!result) {
            connectingPlayers.remove(player.uniqueId)
        }
        return result
    }

    fun getPathLength(player: Player): Double {
        return WirePathManager.getPathLength(player)
    }

    fun cleanupAllPaths() {
        WirePathManager.cleanupAll()
    }

    fun cleanupPlayerPath(player: Player) {
        WirePathManager.cleanupPlayer(player)
    }
}
