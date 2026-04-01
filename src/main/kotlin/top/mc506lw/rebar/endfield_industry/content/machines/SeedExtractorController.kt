package top.mc506lw.rebar.endfield_industry.content.machines

import io.github.pylonmc.rebar.block.RebarBlock
import io.github.pylonmc.rebar.block.base.*
import io.github.pylonmc.rebar.block.context.BlockBreakContext
import io.github.pylonmc.rebar.block.context.BlockCreateContext
import io.github.pylonmc.rebar.util.position.position
import io.github.pylonmc.rebar.i18n.RebarArgument
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import io.github.pylonmc.rebar.logistics.LogisticGroupType
import io.github.pylonmc.rebar.util.MachineUpdateReason
import io.github.pylonmc.rebar.util.gui.GuiItems
import io.github.pylonmc.rebar.waila.WailaDisplay
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.joml.Vector3i
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerConsumerDevice
import top.mc506lw.rebar.endfield_industry.recipes.SeedExtractorRecipe
import xyz.xenondevs.invui.Click
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.inventory.VirtualInventory
import xyz.xenondevs.invui.item.AbstractItem
import xyz.xenondevs.invui.item.ItemProvider

class SeedExtractorController : PowerConsumerDevice,
    RebarGuiBlock, RebarSimpleMultiblock, RebarTickingBlock, RebarVirtualInventoryBlock, RebarCargoBlock {

    constructor(block: Block, context: BlockCreateContext) : super(block, context)
    
    @Suppress("unused")
    constructor(block: Block, pdc: PersistentDataContainer) : super(block, pdc)

    override val powerConsumption: Int = 1

    private val inputInventory = VirtualInventory(1)
    private val outputInventory = VirtualInventory(1)

    private val statusItem = StatusItem()

    override val components: Map<Vector3i, RebarSimpleMultiblock.MultiblockComponent>
        get() {
            val map = mutableMapOf<Vector3i, RebarSimpleMultiblock.MultiblockComponent>()
            
            for (x in -2..2) {
                for (z in -2..2) {
                    map[Vector3i(x, -1, z)] = RebarSimpleMultiblock.VanillaMultiblockComponent(Material.STONE_BRICKS)
                }
            }
            
            for (x in -1..1) {
                for (z in -1..1) {
                    if (x == 0 && z == 0) continue
                    map[Vector3i(x, 0, z)] = RebarSimpleMultiblock.VanillaMultiblockComponent(Material.IRON_BLOCK)
                }
            }
            
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

    override fun postInitialise() {
        createLogisticGroup("input", LogisticGroupType.INPUT, inputInventory)
        createLogisticGroup("output", LogisticGroupType.OUTPUT, outputInventory)
    }
    
    override fun onMultiblockFormed() {
        super<RebarSimpleMultiblock>.onMultiblockFormed()
        tryConnectToNearbyPowerStation()
    }

    override fun getVirtualInventories(): Map<String, VirtualInventory> {
        return mapOf(
            "input" to inputInventory,
            "output" to outputInventory
        )
    }

    override fun tick() {
        if (!isFormedAndFullyLoaded()) return
        if (!isPowered) return

        val input = inputInventory.getItem(0) ?: return
        
        val recipe = SeedExtractorRecipe.RECIPE_TYPE.recipes.find { recipe ->
            recipe.input.matches(input)
        } ?: return

        if (input.amount >= recipe.input.amount) {
            inputInventory.setItem(MachineUpdateReason(), 0, input.subtract())
            val output = recipe.output.clone()
            outputInventory.addItem(MachineUpdateReason(), output)
        }
    }
    
    override fun onBreak(drops: MutableList<ItemStack>, context: BlockBreakContext) {
        super<PowerConsumerDevice>.onBreak(drops, context)
    }

    override fun createGui(): Gui {
        return Gui.builder()
            .setStructure(
                "# # # # # # # # #",
                "# # i # s # c # #",
                "# # # # # # # # #"
            )
            .addIngredient('#', GuiItems.background())
            .addIngredient('i', inputInventory)
            .addIngredient('c', outputInventory)
            .addIngredient('s', statusItem)
            .build()
    }

    private inner class StatusItem : AbstractItem() {
        override fun getItemProvider(viewer: Player): ItemProvider {
            return when {
                !isFormedAndFullyLoaded() -> {
                    ItemStackBuilder.of(Material.RED_STAINED_GLASS_PANE)
                        .name(Component.translatable("endfield-industry.gui.machine.status_incomplete"))
                }
                getConnectedGrid() == null -> {
                    ItemStackBuilder.of(Material.YELLOW_STAINED_GLASS_PANE)
                        .name(Component.translatable("endfield-industry.gui.machine.not_connected"))
                        .lore(listOf(Component.translatable("endfield-industry.gui.machine.need_power_station_range")))
                }
                !isPowered -> {
                    ItemStackBuilder.of(Material.ORANGE_STAINED_GLASS_PANE)
                        .name(Component.translatable("endfield-industry.gui.machine.power_low"))
                        .lore(listOf(Component.translatable("endfield-industry.gui.machine.grid_overloaded")))
                }
                else -> {
                    ItemStackBuilder.of(Material.GREEN_STAINED_GLASS_PANE)
                        .name(Component.translatable("endfield-industry.gui.machine.running"))
                        .lore(listOf(Component.translatable("endfield-industry.gui.machine.power_consumption")
                            .arguments(RebarArgument.of("value", powerConsumption))))
                }
            }
        }

        override fun handleClick(clickType: ClickType, player: Player, click: Click) {}
    }

    override fun getWaila(player: Player): WailaDisplay {
        val suffixKey = when {
            !isFormedAndFullyLoaded() -> "endfield-industry.waila.structure_incomplete"
            getConnectedGrid() == null -> "endfield-industry.waila.not_connected"
            !isPowered -> "endfield-industry.waila.power_low"
            else -> null
        }
        return if (suffixKey != null) {
            WailaDisplay(defaultWailaTranslationKey.append(Component.translatable(suffixKey)))
        } else {
            WailaDisplay(defaultWailaTranslationKey)
        }
    }
}
