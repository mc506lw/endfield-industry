package top.mc506lw.rebar.endfield_industry

import io.github.pylonmc.rebar.block.RebarBlock
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import io.papermc.paper.datacomponent.DataComponentTypes
import net.kyori.adventure.text.Component
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
        .set(DataComponentTypes.ITEM_NAME, Component.translatable("endfield-industry.item.seed_extractor.name"))

    @JvmField
    var PLANTER_CONTROLLER: ItemStackBuilder = ItemStackBuilder.rebar(Material.SMOKER, EndfieldIndustryKeys.PLANTER)
        .set(DataComponentTypes.ITEM_NAME, Component.translatable("endfield-industry.item.planter.name"))

    @JvmField
    var RELAY_BASE_BUILDER: ItemStackBuilder = ItemStackBuilder.rebar(Material.FURNACE, EndfieldIndustryKeys.RELAY_BASE)
        .set(DataComponentTypes.ITEM_NAME, Component.translatable("endfield-industry.item.relay_base.name"))

    @JvmField
    var RELAY_DIFFUSER_BUILDER: ItemStackBuilder = ItemStackBuilder.rebar(Material.FURNACE, EndfieldIndustryKeys.RELAY_DIFFUSER)
        .set(DataComponentTypes.ITEM_NAME, Component.translatable("endfield-industry.item.relay_diffuser.name"))

    @JvmField
    var POWER_STATION_BASE_BUILDER: ItemStackBuilder = ItemStackBuilder.rebar(Material.FURNACE, EndfieldIndustryKeys.POWER_STATION_BASE)
        .set(DataComponentTypes.ITEM_NAME, Component.translatable("endfield-industry.item.power_station_base.name"))

    @JvmField
    var POWER_STATION_EMITTER_BUILDER: ItemStackBuilder = ItemStackBuilder.rebar(Material.FURNACE, EndfieldIndustryKeys.POWER_STATION_EMITTER)
        .set(DataComponentTypes.ITEM_NAME, Component.translatable("endfield-industry.item.power_station_emitter.name"))

    @JvmField
    var PROTOCOL_CORE_CONTROLLER_BUILDER: ItemStackBuilder = ItemStackBuilder.rebar(Material.BEACON, EndfieldIndustryKeys.PROTOCOL_CORE_CONTROLLER)
        .set(DataComponentTypes.ITEM_NAME, Component.translatable("endfield-industry.item.protocol_core_controller.name"))

    fun initialize() {
        SEED_EXTRACTOR_CONTROLLER = ItemStackBuilder.rebar(Material.FURNACE, EndfieldIndustryKeys.SEED_EXTRACTOR)
            .set(DataComponentTypes.ITEM_NAME, Component.translatable("endfield-industry.item.seed_extractor.name"))
        PLANTER_CONTROLLER = ItemStackBuilder.rebar(Material.SMOKER, EndfieldIndustryKeys.PLANTER)
            .set(DataComponentTypes.ITEM_NAME, Component.translatable("endfield-industry.item.planter.name"))

        RebarBlock.register(EndfieldIndustryKeys.SEED_EXTRACTOR, Material.FURNACE, SeedExtractorController::class.java)
        RebarBlock.register(EndfieldIndustryKeys.PLANTER, Material.SMOKER, PlanterController::class.java)

        RELAY_BASE_BUILDER = ItemStackBuilder.rebar(Material.FURNACE, EndfieldIndustryKeys.RELAY_BASE)
            .set(DataComponentTypes.ITEM_NAME, Component.translatable("endfield-industry.item.relay_base.name"))
        RELAY_DIFFUSER_BUILDER = ItemStackBuilder.rebar(Material.FURNACE, EndfieldIndustryKeys.RELAY_DIFFUSER)
            .set(DataComponentTypes.ITEM_NAME, Component.translatable("endfield-industry.item.relay_diffuser.name"))
        POWER_STATION_BASE_BUILDER = ItemStackBuilder.rebar(Material.FURNACE, EndfieldIndustryKeys.POWER_STATION_BASE)
            .set(DataComponentTypes.ITEM_NAME, Component.translatable("endfield-industry.item.power_station_base.name"))
        POWER_STATION_EMITTER_BUILDER = ItemStackBuilder.rebar(Material.FURNACE, EndfieldIndustryKeys.POWER_STATION_EMITTER)
            .set(DataComponentTypes.ITEM_NAME, Component.translatable("endfield-industry.item.power_station_emitter.name"))
        PROTOCOL_CORE_CONTROLLER_BUILDER = ItemStackBuilder.rebar(Material.BEACON, EndfieldIndustryKeys.PROTOCOL_CORE_CONTROLLER)
            .set(DataComponentTypes.ITEM_NAME, Component.translatable("endfield-industry.item.protocol_core_controller.name"))

        RebarBlock.register(EndfieldIndustryKeys.RELAY_BASE, Material.FURNACE, RelayBase::class.java)
        RebarBlock.register(EndfieldIndustryKeys.RELAY_DIFFUSER, Material.FURNACE, RelayDiffuser::class.java)
        RebarBlock.register(EndfieldIndustryKeys.POWER_STATION_BASE, Material.FURNACE, PowerStationBase::class.java)
        RebarBlock.register(EndfieldIndustryKeys.POWER_STATION_EMITTER, Material.FURNACE, PowerStationEmitter::class.java)
        RebarBlock.register(EndfieldIndustryKeys.PROTOCOL_CORE_CONTROLLER, Material.BEACON, ProtocolCoreController::class.java)
    }
}
