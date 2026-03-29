package top.mc506lw.rebar.endfield_industry.content.machines

import io.github.pylonmc.rebar.block.RebarBlock
import io.github.pylonmc.rebar.block.context.BlockCreateContext
import org.bukkit.block.Block
import org.bukkit.persistence.PersistentDataContainer

open class MachineComponent : RebarBlock {
    
    constructor(block: Block, context: BlockCreateContext) : super(block, context)
    
    @Suppress("unused")
    constructor(block: Block, pdc: PersistentDataContainer) : super(block, pdc)
}
