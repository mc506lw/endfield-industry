package top.mc506lw.rebar.endfield_industry.content.powersystem.devices

import io.github.pylonmc.rebar.block.BlockStorage
import io.github.pylonmc.rebar.block.context.BlockCreateContext
import io.github.pylonmc.rebar.block.interfaces.GuiRebarBlock
import io.github.pylonmc.rebar.block.interfaces.SimpleRebarMultiblock
import io.github.pylonmc.rebar.util.position.position
import io.github.pylonmc.rebar.waila.WailaDisplay
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataContainer
import org.joml.Vector3i
import top.mc506lw.rebar.endfield_industry.content.powersystem.PowerSystem
import top.mc506lw.rebar.endfield_industry.content.powersystem.gui.ProtocolCoreGui
import xyz.xenondevs.invui.gui.Gui

class ProtocolCoreController : PowerDevice, GuiRebarBlock, SimpleRebarMultiblock {

    constructor(block: Block, context: BlockCreateContext) : super(block, context)

    @Suppress("unused")
    constructor(block: Block, pdc: PersistentDataContainer) : super(block, pdc)

    override val components: Map<Vector3i, SimpleRebarMultiblock.MultiblockComponent>
        get() {
            val map = mutableMapOf<Vector3i, SimpleRebarMultiblock.MultiblockComponent>()
            for (x in -2..2) {
                for (z in -2..1) {
                    if (x == 0 && z == 0) {
                        continue
                    }
                    map[Vector3i(x, -1, z)] = SimpleRebarMultiblock.MultiblockComponent.of(Material.IRON_BLOCK)
                }
            }
            return map
        }

    override fun onMultiblockFormed() {
        super<SimpleRebarMultiblock>.onMultiblockFormed()
    }

    override fun createGui(): Gui {
        return ProtocolCoreGui(this).createGui()
    }

    override fun getPowerContribution(): Int = if (isFormedAndFullyLoaded()) 200 else 0

    override fun getWaila(player: Player): WailaDisplay {
        return if (isFormedAndFullyLoaded()) {
            WailaDisplay.of(this, player)
        } else {
            WailaDisplay.of(this, player)
                .addWithoutSeperator(Component.translatable("endfield-industry.message.structure_incomplete"))
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
