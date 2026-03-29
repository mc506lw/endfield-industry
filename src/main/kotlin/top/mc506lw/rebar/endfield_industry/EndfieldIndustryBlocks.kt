package top.mc506lw.rebar.endfield_industry

import io.github.pylonmc.rebar.block.RebarBlock
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import org.bukkit.Material
import top.mc506lw.rebar.endfield_industry.content.machines.PlanterController
import top.mc506lw.rebar.endfield_industry.content.machines.SeedExtractorController
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerStationBase
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerStationEmitter
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.ProtocolCoreController
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.RelayBase
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.RelayDiffuser

object EndfieldIndustryBlocks {

    @JvmField
    var SEED_EXTRACTOR_CONTROLLER: ItemStackBuilder = ItemStackBuilder.rebar(Material.FURNACE, EndfieldIndustryKeys.SEED_EXTRACTOR)

    @JvmField
    var PLANTER_CONTROLLER: ItemStackBuilder = ItemStackBuilder.rebar(Material.SMOKER, EndfieldIndustryKeys.PLANTER)

    @JvmField
    var RELAY_BASE_BUILDER: ItemStackBuilder = ItemStackBuilder.rebar(Material.FURNACE, EndfieldIndustryKeys.RELAY_BASE)

    @JvmField
    var RELAY_DIFFUSER_BUILDER: ItemStackBuilder = ItemStackBuilder.rebar(Material.FURNACE, EndfieldIndustryKeys.RELAY_DIFFUSER)

    @JvmField
    var POWER_STATION_BASE_BUILDER: ItemStackBuilder = ItemStackBuilder.rebar(Material.FURNACE, EndfieldIndustryKeys.POWER_STATION_BASE)

    @JvmField
    var POWER_STATION_EMITTER_BUILDER: ItemStackBuilder = ItemStackBuilder.rebar(Material.FURNACE, EndfieldIndustryKeys.POWER_STATION_EMITTER)

    @JvmField
    var PROTOCOL_CORE_CONTROLLER_BUILDER: ItemStackBuilder = ItemStackBuilder.rebar(Material.BEACON, EndfieldIndustryKeys.PROTOCOL_CORE_CONTROLLER)

    fun initialize() {
        SEED_EXTRACTOR_CONTROLLER = ItemStackBuilder.rebar(Material.FURNACE, EndfieldIndustryKeys.SEED_EXTRACTOR)
        PLANTER_CONTROLLER = ItemStackBuilder.rebar(Material.SMOKER, EndfieldIndustryKeys.PLANTER)

        RebarBlock.register(EndfieldIndustryKeys.SEED_EXTRACTOR, Material.FURNACE, SeedExtractorController::class.java)
        RebarBlock.register(EndfieldIndustryKeys.PLANTER, Material.SMOKER, PlanterController::class.java)

        RELAY_BASE_BUILDER = ItemStackBuilder.rebar(Material.FURNACE, EndfieldIndustryKeys.RELAY_BASE)
        RELAY_DIFFUSER_BUILDER = ItemStackBuilder.rebar(Material.FURNACE, EndfieldIndustryKeys.RELAY_DIFFUSER)
        POWER_STATION_BASE_BUILDER = ItemStackBuilder.rebar(Material.FURNACE, EndfieldIndustryKeys.POWER_STATION_BASE)
        POWER_STATION_EMITTER_BUILDER = ItemStackBuilder.rebar(Material.FURNACE, EndfieldIndustryKeys.POWER_STATION_EMITTER)
        PROTOCOL_CORE_CONTROLLER_BUILDER = ItemStackBuilder.rebar(Material.BEACON, EndfieldIndustryKeys.PROTOCOL_CORE_CONTROLLER)

        RebarBlock.register(EndfieldIndustryKeys.RELAY_BASE, Material.FURNACE, RelayBase::class.java)
        RebarBlock.register(EndfieldIndustryKeys.RELAY_DIFFUSER, Material.FURNACE, RelayDiffuser::class.java)
        RebarBlock.register(EndfieldIndustryKeys.POWER_STATION_BASE, Material.FURNACE, PowerStationBase::class.java)
        RebarBlock.register(EndfieldIndustryKeys.POWER_STATION_EMITTER, Material.FURNACE, PowerStationEmitter::class.java)
        RebarBlock.register(EndfieldIndustryKeys.PROTOCOL_CORE_CONTROLLER, Material.BEACON, ProtocolCoreController::class.java)
    }
}
