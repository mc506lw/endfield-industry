package top.mc506lw.rebar.endfield_industry.content.powersystem.gui

import io.github.pylonmc.rebar.i18n.RebarArgument
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import io.github.pylonmc.rebar.util.gui.GuiItems
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.RelayDiffuser
import xyz.xenondevs.invui.Click
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.item.AbstractItem
import xyz.xenondevs.invui.item.ItemProvider

class RelayGui(private val diffuserDevice: RelayDiffuser) {

    fun createGui(): Gui {
        return Gui.builder()
            .setStructure(
                "# # # # # # # # #",
                "# f # i # g # p #",
                "# # # # # # # # #"
            )
            .addIngredient('#', GuiItems.background())
            .addIngredient('f', StatusItem(diffuserDevice))
            .addIngredient('i', GridCapacityItem(diffuserDevice))
            .addIngredient('g', GridIdItem(diffuserDevice))
            .addIngredient('p', GridPowerItem(diffuserDevice))
            .build()
    }

    private class StatusItem(private val diffuser: RelayDiffuser) : AbstractItem() {
        override fun getItemProvider(viewer: Player): ItemProvider {
            val formed = diffuser.isFormedAndFullyLoaded()
            return if (formed) {
                ItemStackBuilder.of(Material.GREEN_STAINED_GLASS_PANE)
                    .name(Component.translatable("endfield-industry.gui.relay.status_complete"))
            } else {
                ItemStackBuilder.of(Material.RED_STAINED_GLASS_PANE)
                    .name(Component.translatable("endfield-industry.gui.relay.status_incomplete"))
            }
        }

        override fun handleClick(clickType: ClickType, player: Player, click: Click) {}
    }

    private class GridCapacityItem(private val diffuser: RelayDiffuser) : AbstractItem() {
        override fun getItemProvider(viewer: Player): ItemProvider {
            val grid = diffuser.getGrid()
            return if (grid != null) {
                val statusKey = if (grid.isOverloaded) 
                    "endfield-industry.gui.power_grid.status_overloaded" 
                else 
                    "endfield-industry.gui.power_grid.status_normal"
                ItemStackBuilder.of(Material.LIME_STAINED_GLASS_PANE)
                    .name(Component.translatable("endfield-industry.gui.power_grid.status_display")
                        .arguments(RebarArgument.of("status", Component.translatable(statusKey))))
                    .lore(listOf(
                        Component.translatable("endfield-industry.gui.power_grid.total_capacity")
                            .arguments(RebarArgument.of("value", grid.totalCapacity)),
                        Component.translatable("endfield-industry.gui.power_grid.used_capacity")
                            .arguments(RebarArgument.of("value", grid.usedCapacity)),
                        Component.translatable("endfield-industry.gui.power_grid.available_capacity")
                            .arguments(RebarArgument.of("value", grid.availableCapacity))
                    ))
            } else {
                ItemStackBuilder.of(Material.GRAY_STAINED_GLASS_PANE)
                    .name(Component.translatable("endfield-industry.gui.power_grid.not_connected"))
            }
        }

        override fun handleClick(clickType: ClickType, player: Player, click: Click) {}
    }

    private class GridIdItem(private val diffuser: RelayDiffuser) : AbstractItem() {
        override fun getItemProvider(viewer: Player): ItemProvider {
            val grid = diffuser.getGrid()
            return if (grid != null) {
                ItemStackBuilder.of(Material.REDSTONE_BLOCK)
                    .name(Component.translatable("endfield-industry.gui.power_grid.grid_id")
                        .arguments(RebarArgument.of("id", grid.gridId.toString().substring(0, 8))))
            } else {
                ItemStackBuilder.of(Material.REDSTONE_BLOCK)
                    .name(Component.translatable("endfield-industry.gui.power_grid.grid_id_not_connected"))
            }
        }

        override fun handleClick(clickType: ClickType, player: Player, click: Click) {}
    }
    
    private class GridPowerItem(private val diffuser: RelayDiffuser) : AbstractItem() {
        override fun getItemProvider(viewer: Player): ItemProvider {
            val grid = diffuser.getGrid()
            return if (grid != null) {
                val deviceCount = grid.getDevices().size
                val consumerCount = grid.getConsumers().size
                ItemStackBuilder.of(Material.COPPER_BLOCK)
                    .name(Component.translatable("endfield-industry.gui.power_grid.devices"))
                    .lore(listOf(
                        Component.translatable("endfield-industry.gui.power_grid.supply_devices")
                            .arguments(RebarArgument.of("count", deviceCount)),
                        Component.translatable("endfield-industry.gui.power_grid.consumer_devices")
                            .arguments(RebarArgument.of("count", consumerCount))
                    ))
            } else {
                ItemStackBuilder.of(Material.COPPER_BLOCK)
                    .name(Component.translatable("endfield-industry.gui.power_grid.devices"))
                    .lore(listOf(Component.translatable("endfield-industry.gui.power_grid.no_devices")))
            }
        }

        override fun handleClick(clickType: ClickType, player: Player, click: Click) {}
    }
}
