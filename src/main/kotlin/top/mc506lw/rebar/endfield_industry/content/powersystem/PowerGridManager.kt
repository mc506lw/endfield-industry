package top.mc506lw.rebar.endfield_industry.content.powersystem

import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerDevice
import top.mc506lw.rebar.endfield_industry.content.powersystem.event.PowerGridOverloadEvent
import top.mc506lw.rebar.endfield_industry.content.powersystem.event.PowerGridStableEvent
import top.mc506lw.rebar.endfield_industry.content.powersystem.storage.PowerSystemStorage
import java.util.UUID

class PowerGridManager(private val config: PowerSystemConfig) {

    private val grids: MutableMap<UUID, PowerGrid> = mutableMapOf()

    companion object {
        private var instance: PowerGridManager? = null

        fun getInstance(): PowerGridManager {
            return instance ?: throw IllegalStateException("PowerGridManager not initialized")
        }
    }

    init {
        instance = this
    }

    fun createGrid(): PowerGrid {
        val grid = PowerGrid()
        grids[grid.gridId] = grid
        PowerSystemStorage.markGridDirty(grid.gridId)
        return grid
    }
    
    fun createGridWithId(gridId: UUID): PowerGrid {
        val grid = PowerGrid(gridId)
        grids[grid.gridId] = grid
        return grid
    }

    fun getGrid(gridId: UUID): PowerGrid? = grids[gridId]
    
    fun getAllGrids(): Map<UUID, PowerGrid> = grids.toMap()

    fun removeGrid(gridId: UUID) {
        grids.remove(gridId)
        PowerSystemStorage.markGridDirty(gridId)
    }

    fun mergeGrids(gridId1: UUID, gridId2: UUID) {
        val grid1 = grids[gridId1]
        val grid2 = grids[gridId2]
        
        if (grid1 != null && grid2 != null && grid1 != grid2) {
            grid1.mergeGrid(grid2)
            
            for (device in grid2.getDevices()) {
                device.setGrid(grid1)
            }
            
            grids.remove(gridId2)
            PowerSystemStorage.markGridDirty(gridId2)
        }
    }

    fun broadcastOverload(grid: PowerGrid) {
        val event = PowerGridOverloadEvent(grid)
        event.callEvent()
    }

    fun broadcastStable(grid: PowerGrid) {
        val event = PowerGridStableEvent(grid)
        event.callEvent()
    }

    fun getConfig(): PowerSystemConfig = config
}
