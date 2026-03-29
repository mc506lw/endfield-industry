package top.mc506lw.rebar.endfield_industry.content.powersystem.connection.wirepath

import io.github.pylonmc.rebar.i18n.RebarArgument
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerDevice
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.RelayDiffuser
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerStationEmitter
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.ProtocolCoreController
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

object WirePathManager {

    private val sessions: MutableMap<UUID, WirePathSession> = ConcurrentHashMap()
    
    var relayMaxDistance: Double = 30.0
    var powerStationMaxDistance: Double = 80.0

    fun startPath(player: Player, device: PowerDevice): WirePathSession? {
        if (sessions.containsKey(player.uniqueId)) {
            endPath(player)
        }
        
        val maxDistance = getMaxDistance(device)
        if (maxDistance <= 0) {
            player.sendActionBar(
                Component.translatable("endfield-industry.message.wirepath.cannot_start")
            )
            return null
        }
        
        val session = WirePathSession(player, device, maxDistance)
        session.initialize()
        sessions[player.uniqueId] = session
        
        player.sendActionBar(
            Component.translatable("endfield-industry.message.wirepath.started")
                .arguments(RebarArgument.of("max", maxDistance.toInt()))
        )
        
        return session
    }

    fun endPath(player: Player): WirePathSession? {
        val session = sessions.remove(player.uniqueId)
        session?.cleanup()
        
        if (session != null && player.isOnline) {
            player.sendActionBar(
                Component.translatable("endfield-industry.message.wirepath.ended")
            )
        }
        
        return session
    }

    fun getSession(player: Player): WirePathSession? = sessions[player.uniqueId]

    fun hasActiveSession(player: Player): Boolean = sessions.containsKey(player.uniqueId)

    fun updatePath(player: Player): Boolean {
        val session = sessions[player.uniqueId] ?: return false
        
        if (!session.updatePlayerPosition()) {
            if (session.isOverMaxDistance) {
                player.sendActionBar(
                    Component.translatable("endfield-industry.message.wirepath.over_distance_auto_end")
                        .arguments(RebarArgument.of("max", session.pathLength.toInt()))
                )
            }
            endPath(player)
            return false
        }
        
        return true
    }

    fun getPathLength(player: Player): Double {
        return sessions[player.uniqueId]?.pathLength ?: 0.0
    }

    fun getFullPath(player: Player): List<org.bukkit.Location>? {
        return sessions[player.uniqueId]?.getFullPath()
    }

    fun getStartDevice(player: Player): PowerDevice? {
        return sessions[player.uniqueId]?.startDevice
    }

    fun getTailLocation(player: Player): org.bukkit.Location? {
        return sessions[player.uniqueId]?.getTailLocation()
    }

    fun cleanupAll() {
        for (session in sessions.values) {
            session.cleanup()
        }
        sessions.clear()
    }

    fun cleanupPlayer(player: Player) {
        endPath(player)
    }

    private fun getMaxDistance(device: PowerDevice): Double {
        return when (device) {
            is RelayDiffuser -> relayMaxDistance
            is PowerStationEmitter -> powerStationMaxDistance
            is ProtocolCoreController -> relayMaxDistance
            else -> 0.0
        }
    }

    fun getActiveSessionCount(): Int = sessions.size

    fun getActivePlayers(): Set<UUID> = sessions.keys.toSet()
}
