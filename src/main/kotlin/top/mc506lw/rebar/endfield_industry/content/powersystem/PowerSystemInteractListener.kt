package top.mc506lw.rebar.endfield_industry.content.powersystem

import io.github.pylonmc.rebar.block.BlockStorage
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import top.mc506lw.rebar.endfield_industry.EndfieldIndustry
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerDevice
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.RelayDiffuser
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerStationEmitter
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.ProtocolCoreController

class PowerSystemInteractListener(private val plugin: EndfieldIndustry) : Listener {

    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) {
            return
        }
        
        val block = event.clickedBlock ?: return
        val player = event.player
        
        val rebarBlock = BlockStorage.get(block)
        
        if (PowerSystem.connectionManager.isConnecting(player)) {
            if (rebarBlock is PowerDevice) {
                val targetDevice = rebarBlock
                PowerSystem.connectionManager.tryConnect(player, targetDevice)
                event.isCancelled = true
                return
            }
        }
        
        when (rebarBlock) {
            is RelayDiffuser -> rebarBlock.onInteract(event)
            is PowerStationEmitter -> rebarBlock.onInteract(event)
            is ProtocolCoreController -> rebarBlock.onInteract(event)
        }
    }
}
