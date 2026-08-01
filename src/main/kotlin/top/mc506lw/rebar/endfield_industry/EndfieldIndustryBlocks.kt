package top.mc506lw.rebar.endfield_industry

import io.github.pylonmc.rebar.block.RebarBlock
import org.bukkit.Material
import top.mc506lw.rebar.endfield_industry.content.machines.PlanterController
import top.mc506lw.rebar.endfield_industry.content.machines.SeedExtractorController
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerStationBase
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerStationEmitter
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.ProtocolCoreController
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.RelayBase
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.RelayDiffuser

object EndfieldIndustryBlocks {

    fun initialize() {
        RebarBlock.register(EndfieldIndustryKeys.SEED_EXTRACTOR, Material.FURNACE, SeedExtractorController::class.java)
        RebarBlock.register(EndfieldIndustryKeys.PLANTER, Material.SMOKER, PlanterController::class.java)

        RebarBlock.register(EndfieldIndustryKeys.RELAY_BASE, Material.FURNACE, RelayBase::class.java)
        RebarBlock.register(EndfieldIndustryKeys.RELAY_DIFFUSER, Material.FURNACE, RelayDiffuser::class.java)
        RebarBlock.register(EndfieldIndustryKeys.POWER_STATION_BASE, Material.FURNACE, PowerStationBase::class.java)
        RebarBlock.register(EndfieldIndustryKeys.POWER_STATION_EMITTER, Material.FURNACE, PowerStationEmitter::class.java)
        RebarBlock.register(EndfieldIndustryKeys.PROTOCOL_CORE_CONTROLLER, Material.BEACON, ProtocolCoreController::class.java)
    }
}
