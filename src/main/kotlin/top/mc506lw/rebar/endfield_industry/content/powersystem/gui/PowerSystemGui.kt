package top.mc506lw.rebar.endfield_industry.content.powersystem.gui

import io.github.pylonmc.rebar.i18n.RebarArgument
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import top.mc506lw.rebar.endfield_industry.content.powersystem.PowerGrid
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerDevice
import xyz.xenondevs.invui.gui.Gui

abstract class PowerSystemGui(protected val device: PowerDevice, protected val player: Player) {

    abstract fun createGui(): Gui

    protected fun getGrid(): PowerGrid? = device.getGrid()

    protected fun getGridCapacityText(): Component {
        val grid = getGrid() ?: return Component.translatable("endfield-industry.gui.power_grid.not_connected")
        return Component.translatable("endfield-industry.gui.power_grid.grid_capacity")
            .arguments(RebarArgument.of("value", grid.totalCapacity - grid.usedCapacity))
    }

    protected fun getDeviceCountText(): Component {
        val grid = getGrid() ?: return Component.translatable("endfield-industry.gui.power_grid.not_connected")
        return Component.translatable("endfield-industry.gui.power_grid.device_count")
            .arguments(RebarArgument.of("count", grid.getDevices().size))
    }

    protected fun getConnectionStatusText(): Component {
        val grid = getGrid() ?: return Component.translatable("endfield-industry.gui.power_grid.connection_status_not_connected")
        return Component.translatable("endfield-industry.gui.power_grid.connection_status")
            .arguments(RebarArgument.of("id", grid.gridId.toString().substring(0, 8)))
    }
    
    protected fun getGridIdText(): Component {
        val grid = getGrid() ?: return Component.translatable("endfield-industry.gui.power_grid.grid_id_not_connected")
        return Component.translatable("endfield-industry.gui.power_grid.grid_id")
            .arguments(RebarArgument.of("id", grid.gridId.toString().substring(0, 8)))
    }
}
