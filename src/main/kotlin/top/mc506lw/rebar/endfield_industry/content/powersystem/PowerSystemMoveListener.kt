package top.mc506lw.rebar.endfield_industry.content.powersystem

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import top.mc506lw.rebar.endfield_industry.EndfieldIndustry

class PowerSystemMoveListener(plugin: EndfieldIndustry) : Listener {

    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (PowerSystem.connectionManager.isConnecting(event.player)) {
            PowerSystem.connectionManager.updateDistanceDisplay(event.player)
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        if (PowerSystem.connectionManager.isConnecting(event.player)) {
            PowerSystem.connectionManager.endConnection(event.player)
        }
    }
}
