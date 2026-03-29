package top.mc506lw.rebar.endfield_industry.content.powersystem.devices

import io.github.pylonmc.rebar.util.position.BlockPosition
import io.github.pylonmc.rebar.waila.WailaDisplay
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import io.github.pylonmc.rebar.block.context.BlockCreateContext

class PowerStationBase : PowerDevice {
    
    constructor(block: Block, context: BlockCreateContext) : super(block, context)
    
    @Suppress("unused")
    constructor(block: Block, pdc: PersistentDataContainer) : super(block, pdc)

    private val center: BlockPosition = BlockPosition(block)

    override fun getPowerContribution(): Int = 0

    override fun getWaila(player: Player): WailaDisplay {
        return WailaDisplay(defaultWailaTranslationKey)
    }
}
