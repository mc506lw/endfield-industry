package top.mc506lw.rebar.endfield_industry.content.powersystem.devices

import io.github.pylonmc.rebar.block.RebarBlock
import io.github.pylonmc.rebar.block.base.RebarGuiBlock
import io.github.pylonmc.rebar.block.base.RebarSimpleMultiblock
import io.github.pylonmc.rebar.block.context.BlockCreateContext
import io.github.pylonmc.rebar.util.position.position
import io.github.pylonmc.rebar.waila.WailaDisplay
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataContainer
import org.joml.Vector3i
import top.mc506lw.rebar.endfield_industry.EndfieldIndustryKeys
import top.mc506lw.rebar.endfield_industry.content.powersystem.PowerSystem
import top.mc506lw.rebar.endfield_industry.content.powersystem.gui.RelayGui
import xyz.xenondevs.invui.gui.Gui

class RelayDiffuser : PowerDevice, RebarGuiBlock, RebarSimpleMultiblock {
    
    constructor(block: Block, context: BlockCreateContext) : super(block, context)
    
    @Suppress("unused")
    constructor(block: Block, pdc: PersistentDataContainer) : super(block, pdc)

    override val components: Map<Vector3i, RebarSimpleMultiblock.MultiblockComponent>
        get() {
            val map = mutableMapOf<Vector3i, RebarSimpleMultiblock.MultiblockComponent>()
            
            for (x in -1..1) {
                for (z in -1..1) {
                    map[Vector3i(x, -2, z)] = RebarSimpleMultiblock.VanillaMultiblockComponent(Material.STONE_BRICKS)
                }
            }
            
            map[Vector3i(0, -1, 0)] = RebarSimpleMultiblock.RebarMultiblockComponent(EndfieldIndustryKeys.RELAY_BASE)
            
            return map
        }

    override fun checkFormed(): Boolean {
        val block = (this as RebarBlock).block
        val formed = validStructures().any { struct ->
            struct.all { (offset, component) ->
                component.matches((block.position + offset).block)
            }
        }
        updateGhostBlockColors()
        return formed
    }

    override fun createGui(): Gui {
        return RelayGui(this).createGui()
    }

    override fun getPowerContribution(): Int = 0

    override fun getWaila(player: Player): WailaDisplay {
        return if (isFormedAndFullyLoaded()) {
            WailaDisplay(defaultWailaTranslationKey)
        } else {
            WailaDisplay(defaultWailaTranslationKey.append(Component.translatable("endfield-industry.message.structure_incomplete")))
        }
    }

    fun onInteract(event: PlayerInteractEvent) {
        if (!event.player.isSneaking) {
            return
        }
        
        if (!isFormedAndFullyLoaded()) {
            event.player.sendMessage(Component.translatable("endfield-industry.message.structure_incomplete"))
            event.isCancelled = true
            return
        }
        PowerSystem.connectionManager.startConnection(event.player, this)
        event.isCancelled = true
    }
}
