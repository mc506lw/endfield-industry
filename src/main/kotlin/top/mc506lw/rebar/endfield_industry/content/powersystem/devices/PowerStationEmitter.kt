package top.mc506lw.rebar.endfield_industry.content.powersystem.devices

import io.github.pylonmc.rebar.block.BlockStorage
import io.github.pylonmc.rebar.block.RebarBlock
import io.github.pylonmc.rebar.block.base.RebarGuiBlock
import io.github.pylonmc.rebar.block.base.RebarSimpleMultiblock
import io.github.pylonmc.rebar.block.context.BlockCreateContext
import io.github.pylonmc.rebar.util.position.position
import io.github.pylonmc.rebar.waila.WailaDisplay
import net.kyori.adventure.text.Component
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataContainer
import org.joml.Vector3i
import top.mc506lw.rebar.endfield_industry.EndfieldIndustryKeys
import top.mc506lw.rebar.endfield_industry.content.powersystem.PowerGrid
import top.mc506lw.rebar.endfield_industry.content.powersystem.PowerSystem
import top.mc506lw.rebar.endfield_industry.content.powersystem.gui.PowerStationGui
import xyz.xenondevs.invui.gui.Gui

class PowerStationEmitter : PowerDevice, RebarGuiBlock, RebarSimpleMultiblock {
    
    constructor(block: Block, context: BlockCreateContext) : super(block, context)
    
    @Suppress("unused")
    constructor(block: Block, pdc: PersistentDataContainer) : super(block, pdc)

    override val components: Map<Vector3i, RebarSimpleMultiblock.MultiblockComponent>
        get() = mapOf(
            Vector3i(0, -1, 0) to RebarSimpleMultiblock.RebarMultiblockComponent(EndfieldIndustryKeys.POWER_STATION_BASE)
        )

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

    override fun onMultiblockFormed() {
        super<RebarSimpleMultiblock>.onMultiblockFormed()
        connectNearbyDevices()
        notifyNearbyConsumers()
    }

    private fun connectNearbyDevices() {
        val range = PowerSystem.config.powerStationSupplyRange
        val halfRange = range / 2
        val center = getMultiblockBlock(Vector3i(0, -1, 0))

        var myGrid: PowerGrid? = getGrid()
        
        for (x in -halfRange..halfRange) {
            for (z in -halfRange..halfRange) {
                if (x == 0 && z == 0) {
                    continue
                }
                
                val pos = center.location.add(x.toDouble(), 0.0, z.toDouble()).block
                
                val rebarBlock = BlockStorage.get(pos)
                if (rebarBlock is PowerDevice) {
                    val device = rebarBlock
                    val deviceGrid = device.getGrid()
                    
                    if (deviceGrid == null) {
                        if (myGrid == null) {
                            myGrid = PowerSystem.gridManager.createGrid()
                            connectToGrid(myGrid)
                        }
                        device.connectToGrid(myGrid)
                    } else if (deviceGrid != myGrid) {
                        if (myGrid == null) {
                            connectToGrid(deviceGrid)
                            myGrid = deviceGrid
                        } else {
                            PowerSystem.gridManager.mergeGrids(myGrid.gridId, deviceGrid.gridId)
                        }
                    }
                }
            }
        }
    }
    
    private fun notifyNearbyConsumers() {
        val range = PowerSystem.config.powerStationSupplyRange
        val halfRange = range / 2
        val center = getMultiblockBlock(Vector3i(0, -1, 0))
        
        val grid = getGrid()
        if (grid == null) {
            return
        }

        for (x in -halfRange..halfRange) {
            for (z in -halfRange..halfRange) {
                val pos = center.location.add(x.toDouble(), 0.0, z.toDouble()).block
                val rebarBlock = BlockStorage.get(pos)
                
                if (rebarBlock is PowerConsumerDevice) {
                    if (rebarBlock.getConnectedGrid() == null) {
                        rebarBlock.connectToGrid(grid)
                    }
                }
            }
        }
    }

    override fun createGui(): Gui {
        return PowerStationGui(this).createGui()
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
