package top.mc506lw.rebar.endfield_industry.content.powersystem.gui

import org.bukkit.entity.Player
import top.mc506lw.rebar.endfield_industry.content.powersystem.PowerGrid
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerDevice
import xyz.xenondevs.invui.gui.Gui

abstract class PowerSystemGui(protected val device: PowerDevice, protected val player: Player) {

    abstract fun createGui(): Gui

    protected fun getGrid(): PowerGrid? = device.getGrid()

    protected fun getGridCapacityText(): String {
        val grid = getGrid() ?: return "未连接电网"
        return "电网电量: ${grid.totalCapacity - grid.usedCapacity}"
    }

    protected fun getDeviceCountText(): String {
        val grid = getGrid() ?: return "连接设备: 0"
        return "连接设备: ${grid.getDevices().size}"
    }

    protected fun getConnectionStatusText(): String {
        val grid = getGrid() ?: return "连接状态: 未连接"
        return String.format("连接状态: 已连接 (%s)", grid.gridId.toString().substring(0, 8))
    }
    
    protected fun getGridIdText(): String {
        val grid = getGrid() ?: return "电网ID: 未连接"
        return "电网ID: ${grid.gridId.toString().substring(0, 8)}"
    }
}
