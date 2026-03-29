package top.mc506lw.rebar.endfield_industry

import io.github.pylonmc.rebar.item.RebarItem
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import io.papermc.paper.datacomponent.DataComponentTypes
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object EndfieldIndustryItems {

    @JvmField
    val HIGH_CRYSTAL_PART: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.HIGH_CRYSTAL_PART)
        .set(DataComponentTypes.ITEM_MODEL, Material.QUARTZ.key)
        .build()

    @JvmField
    val STEEL_PART: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.STEEL_PART)
        .set(DataComponentTypes.ITEM_MODEL, Material.IRON_NUGGET.key)
        .build()

    @JvmField
    val STABLE_CARBON_BLOCK: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.STABLE_CARBON_BLOCK)
        .set(DataComponentTypes.ITEM_MODEL, Material.COAL_BLOCK.key)
        .build()

    @JvmField
    val SAND_LEAF_POWDER: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.SAND_LEAF_POWDER)
        .set(DataComponentTypes.ITEM_MODEL, Material.SAND.key)
        .build()

    @JvmField
    val DENSE_ORIGINIUM_POWDER: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.DENSE_ORIGINIUM_POWDER)
        .set(DataComponentTypes.ITEM_MODEL, Material.GLOWSTONE_DUST.key)
        .build()

    @JvmField
    val DENSE_CRYSTAL_POWDER: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.DENSE_CRYSTAL_POWDER)
        .set(DataComponentTypes.ITEM_MODEL, Material.DIAMOND.key)
        .build()

    @JvmField
    val HIGH_CRYSTAL_POWDER: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.HIGH_CRYSTAL_POWDER)
        .set(DataComponentTypes.ITEM_MODEL, Material.AMETHYST_SHARD.key)
        .build()

    @JvmField
    val DENSE_BLUE_IRON_POWDER: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.DENSE_BLUE_IRON_POWDER)
        .set(DataComponentTypes.ITEM_MODEL, Material.LAPIS_LAZULI.key)
        .build()

    @JvmField
    val DENSE_CARBON_POWDER: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.DENSE_CARBON_POWDER)
        .set(DataComponentTypes.ITEM_MODEL, Material.GUNPOWDER.key)
        .build()

    @JvmField
    val HIGH_CAP_VALLEY_BATTERY: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.HIGH_CAP_VALLEY_BATTERY)
        .set(DataComponentTypes.ITEM_MODEL, Material.REDSTONE_BLOCK.key)
        .build()

    @JvmField
    val LARGE_BUD_SPRAY: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.LARGE_BUD_SPRAY)
        .set(DataComponentTypes.ITEM_MODEL, Material.POTION.key)
        .build()

    @JvmField
    val HIGH_CRYSTAL_BOTTLE: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.HIGH_CRYSTAL_BOTTLE)
        .set(DataComponentTypes.ITEM_MODEL, Material.GLASS_BOTTLE.key)
        .build()

    @JvmField
    val STEEL_BOTTLE: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.STEEL_BOTTLE)
        .set(DataComponentTypes.ITEM_MODEL, Material.IRON_INGOT.key)
        .build()

    @JvmField
    val FINE_GROUND_BUCKWHEAT_POWDER: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.FINE_GROUND_BUCKWHEAT_POWDER)
        .set(DataComponentTypes.ITEM_MODEL, Material.SUGAR.key)
        .build()

    @JvmField
    val FINE_GROUND_CITRUS_POWDER: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.FINE_GROUND_CITRUS_POWDER)
        .set(DataComponentTypes.ITEM_MODEL, Material.ORANGE_DYE.key)
        .build()

    @JvmField
    val STEEL_BLOCK: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.STEEL_BLOCK)
        .set(DataComponentTypes.ITEM_MODEL, Material.IRON_BLOCK.key)
        .build()

    @JvmField
    val HIGH_CRYSTAL_FIBER: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.HIGH_CRYSTAL_FIBER)
        .set(DataComponentTypes.ITEM_MODEL, Material.STRING.key)
        .build()

    @JvmField
    val DENSE_CRYSTAL: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.DENSE_CRYSTAL)
        .set(DataComponentTypes.ITEM_MODEL, Material.DIAMOND.key)
        .build()

    @JvmField
    val BUCKWHEAT: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.BUCKWHEAT)
        .set(DataComponentTypes.ITEM_MODEL, Material.POPPY.key)
        .build()

    @JvmField
    val BUCKWHEAT_SEED: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.BUCKWHEAT_SEED)
        .set(DataComponentTypes.ITEM_MODEL, Material.WHEAT_SEEDS.key)
        .build()

    @JvmField
    val SEED_EXTRACTOR: ItemStack = ItemStackBuilder
        .rebar(Material.FURNACE, EndfieldIndustryKeys.SEED_EXTRACTOR)
        .build()

    @JvmField
    val PLANTER: ItemStack = ItemStackBuilder
        .rebar(Material.SMOKER, EndfieldIndustryKeys.PLANTER)
        .build()

    @JvmField
    val RELAY_BASE: ItemStack = ItemStackBuilder
        .rebar(Material.FURNACE, EndfieldIndustryKeys.RELAY_BASE)
        .build()

    @JvmField
    val RELAY_DIFFUSER: ItemStack = ItemStackBuilder
        .rebar(Material.FURNACE, EndfieldIndustryKeys.RELAY_DIFFUSER)
        .build()

    @JvmField
    val POWER_STATION_BASE: ItemStack = ItemStackBuilder
        .rebar(Material.FURNACE, EndfieldIndustryKeys.POWER_STATION_BASE)
        .build()

    @JvmField
    val POWER_STATION_EMITTER: ItemStack = ItemStackBuilder
        .rebar(Material.FURNACE, EndfieldIndustryKeys.POWER_STATION_EMITTER)
        .build()

    @JvmField
    val PROTOCOL_CORE_CONTROLLER: ItemStack = ItemStackBuilder
        .rebar(Material.BEACON, EndfieldIndustryKeys.PROTOCOL_CORE_CONTROLLER)
        .build()

    @JvmField
    val LIGHT_DIM_STONE: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.LIGHT_DIM_STONE)
        .set(DataComponentTypes.ITEM_MODEL, Material.ECHO_SHARD.key)
        .build()

    @JvmField
    val CARTILAGE_FRAGMENT: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.CARTILAGE_FRAGMENT)
        .set(DataComponentTypes.ITEM_MODEL, Material.BONE_MEAL.key)
        .build()

    @JvmField
    val CARBON_BLOCK: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.CARBON_BLOCK)
        .set(DataComponentTypes.ITEM_MODEL, Material.COAL.key)
        .build()

    @JvmField
    val AMETHYST_FIBER: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.AMETHYST_FIBER)
        .set(DataComponentTypes.ITEM_MODEL, Material.AMETHYST_SHARD.key)
        .build()

    @JvmField
    val BLUE_IRON_BLOCK: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.BLUE_IRON_BLOCK)
        .set(DataComponentTypes.ITEM_MODEL, Material.LAPIS_BLOCK.key)
        .build()

    @JvmField
    val CARBON_POWDER: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.CARBON_POWDER)
        .set(DataComponentTypes.ITEM_MODEL, Material.GUNPOWDER.key)
        .build()

    @JvmField
    val INDUSTRIAL_EXPLOSIVE: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.INDUSTRIAL_EXPLOSIVE)
        .set(DataComponentTypes.ITEM_MODEL, Material.TNT_MINECART.key)
        .build()

    @JvmField
    val GRAY_WHEAT_SEED: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.GRAY_WHEAT_SEED)
        .set(DataComponentTypes.ITEM_MODEL, Material.WHEAT_SEEDS.key)
        .build()

    @JvmField
    val GRAY_WHEAT: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.GRAY_WHEAT)
        .set(DataComponentTypes.ITEM_MODEL, Material.WHEAT.key)
        .build()

    @JvmField
    val KETONE_TREE_SEED: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.KETONE_TREE_SEED)
        .set(DataComponentTypes.ITEM_MODEL, Material.OAK_SAPLING.key)
        .build()

    @JvmField
    val CITRUS_SEED: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.CITRUS_SEED)
        .set(DataComponentTypes.ITEM_MODEL, Material.ORANGE_DYE.key)
        .build()

    @JvmField
    val BLUE_IRON_ORE: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.BLUE_IRON_ORE)
        .set(DataComponentTypes.ITEM_MODEL, Material.LAPIS_ORE.key)
        .build()

    @JvmField
    val GLOW_SHELL_BUG: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.GLOW_SHELL_BUG)
        .set(DataComponentTypes.ITEM_MODEL, Material.GLOW_INK_SAC.key)
        .build()

    @JvmField
    val AMETHYST_POWDER: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.AMETHYST_POWDER)
        .set(DataComponentTypes.ITEM_MODEL, Material.AMETHYST_SHARD.key)
        .build()

    @JvmField
    val GRASS_SEED_POWDER: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.GRASS_SEED_POWDER)
        .set(DataComponentTypes.ITEM_MODEL, Material.SHORT_GRASS.key)
        .build()

    @JvmField
    val VALLEY_DISPATCH_TICKET: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.VALLEY_DISPATCH_TICKET)
        .set(DataComponentTypes.ITEM_MODEL, Material.PAPER.key)
        .build()

    @JvmField
    val CRYSTAL_SHELL: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.CRYSTAL_SHELL)
        .set(DataComponentTypes.ITEM_MODEL, Material.QUARTZ.key)
        .build()

    @JvmField
    val BEAST_MEAT: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.BEAST_MEAT)
        .set(DataComponentTypes.ITEM_MODEL, Material.BEEF.key)
        .build()

    @JvmField
    val HIGH_ENERGY_FRAGRANT_STONE: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.HIGH_ENERGY_FRAGRANT_STONE)
        .set(DataComponentTypes.ITEM_MODEL, Material.GLOWSTONE_DUST.key)
        .build()

    @JvmField
    val HARD_FRAGRANT_STONE: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.HARD_FRAGRANT_STONE)
        .set(DataComponentTypes.ITEM_MODEL, Material.STONE.key)
        .build()

    @JvmField
    val CRYSTAL_SHELL_POWDER: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.CRYSTAL_SHELL_POWDER)
        .set(DataComponentTypes.ITEM_MODEL, Material.QUARTZ.key)
        .build()

    @JvmField
    val LIGHT_RED_PILLAR_FUNGUS: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.LIGHT_RED_PILLAR_FUNGUS)
        .set(DataComponentTypes.ITEM_MODEL, Material.RED_MUSHROOM.key)
        .build()

    @JvmField
    val CRYSTALIZED_TOOTHED_LEAF: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.CRYSTALIZED_TOOTHED_LEAF)
        .set(DataComponentTypes.ITEM_MODEL, Material.DIAMOND.key)
        .build()

    @JvmField
    val FIRE_BUCKWHEAT: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.FIRE_BUCKWHEAT)
        .set(DataComponentTypes.ITEM_MODEL, Material.RED_TULIP.key)
        .build()

    @JvmField
    val DARK_SILVER_CITRUS: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.DARK_SILVER_CITRUS)
        .set(DataComponentTypes.ITEM_MODEL, Material.ORANGE_DYE.key)
        .build()

    @JvmField
    val LOG: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.LOG)
        .set(DataComponentTypes.ITEM_MODEL, Material.OAK_LOG.key)
        .build()

    @JvmField
    val KETONE_BUSH: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.KETONE_BUSH)
        .set(DataComponentTypes.ITEM_MODEL, Material.DEAD_BUSH.key)
        .build()

    @JvmField
    val ORIGINIUM_POWDER: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.ORIGINIUM_POWDER)
        .set(DataComponentTypes.ITEM_MODEL, Material.GLOWSTONE_DUST.key)
        .build()

    @JvmField
    val AMETHYST_ORE: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.AMETHYST_ORE)
        .set(DataComponentTypes.ITEM_MODEL, Material.AMETHYST_CLUSTER.key)
        .build()

    @JvmField
    val BLUE_IRON_POWDER: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.BLUE_IRON_POWDER)
        .set(DataComponentTypes.ITEM_MODEL, Material.LAPIS_LAZULI.key)
        .build()

    @JvmField
    val KETONE_BUSH_POWDER: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.KETONE_BUSH_POWDER)
        .set(DataComponentTypes.ITEM_MODEL, Material.SAND.key)
        .build()

    @JvmField
    val AMETHYST_BOTTLE: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.AMETHYST_BOTTLE)
        .set(DataComponentTypes.ITEM_MODEL, Material.GLASS_BOTTLE.key)
        .build()

    @JvmField
    val BLUE_IRON_BOTTLE: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.BLUE_IRON_BOTTLE)
        .set(DataComponentTypes.ITEM_MODEL, Material.GLASS_BOTTLE.key)
        .build()

    @JvmField
    val IRON_PART: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.IRON_PART)
        .set(DataComponentTypes.ITEM_MODEL, Material.IRON_NUGGET.key)
        .build()

    @JvmField
    val AMETHYST_EQUIPMENT_PART: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.AMETHYST_EQUIPMENT_PART)
        .set(DataComponentTypes.ITEM_MODEL, Material.AMETHYST_SHARD.key)
        .build()

    @JvmField
    val BLUE_IRON_EQUIPMENT_PART: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.BLUE_IRON_EQUIPMENT_PART)
        .set(DataComponentTypes.ITEM_MODEL, Material.LAPIS_LAZULI.key)
        .build()

    @JvmField
    val LOW_CAP_VALLEY_BATTERY: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.LOW_CAP_VALLEY_BATTERY)
        .set(DataComponentTypes.ITEM_MODEL, Material.REDSTONE.key)
        .build()

    @JvmField
    val AMETHYST_PART: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.AMETHYST_PART)
        .set(DataComponentTypes.ITEM_MODEL, Material.AMETHYST_SHARD.key)
        .build()

    @JvmField
    val MID_CAP_VALLEY_BATTERY: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.MID_CAP_VALLEY_BATTERY)
        .set(DataComponentTypes.ITEM_MODEL, Material.REDSTONE.key)
        .build()

    @JvmField
    val BUCKWHEAT_POWDER: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.BUCKWHEAT_POWDER)
        .set(DataComponentTypes.ITEM_MODEL, Material.SUGAR.key)
        .build()

    @JvmField
    val CITRUS_POWDER: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.CITRUS_POWDER)
        .set(DataComponentTypes.ITEM_MODEL, Material.ORANGE_DYE.key)
        .build()

    @JvmField
    val MID_DIM_STONE: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.MID_DIM_STONE)
        .set(DataComponentTypes.ITEM_MODEL, Material.ECHO_SHARD.key)
        .build()

    @JvmField
    val BUCKWHEAT_MEDICINE_POWDER: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.BUCKWHEAT_MEDICINE_POWDER)
        .set(DataComponentTypes.ITEM_MODEL, Material.SUGAR.key)
        .build()

    @JvmField
    val CITRUS_DRAFT: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.CITRUS_DRAFT)
        .set(DataComponentTypes.ITEM_MODEL, Material.POTION.key)
        .build()

    @JvmField
    val BUCKWHEAT_CAPSULE: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.BUCKWHEAT_CAPSULE)
        .set(DataComponentTypes.ITEM_MODEL, Material.SUNFLOWER.key)
        .build()

    @JvmField
    val QUALITY_BUCKWHEAT_CAPSULE: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.QUALITY_BUCKWHEAT_CAPSULE)
        .set(DataComponentTypes.ITEM_MODEL, Material.SUNFLOWER.key)
        .build()

    @JvmField
    val CITRUS_CAN: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.CITRUS_CAN)
        .set(DataComponentTypes.ITEM_MODEL, Material.ORANGE_DYE.key)
        .build()

    @JvmField
    val QUALITY_CITRUS_CAN: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.QUALITY_CITRUS_CAN)
        .set(DataComponentTypes.ITEM_MODEL, Material.ORANGE_DYE.key)
        .build()

    @JvmField
    val SMALL_BUCKWHEAT_RESTORATION_AGENT: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.SMALL_BUCKWHEAT_RESTORATION_AGENT)
        .set(DataComponentTypes.ITEM_MODEL, Material.POTION.key)
        .build()

    @JvmField
    val SMALL_CITRUS_DRAFT: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.SMALL_CITRUS_DRAFT)
        .set(DataComponentTypes.ITEM_MODEL, Material.POTION.key)
        .build()

    @JvmField
    val SELECT_BUCKWHEAT_CAPSULE: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.SELECT_BUCKWHEAT_CAPSULE)
        .set(DataComponentTypes.ITEM_MODEL, Material.SUNFLOWER.key)
        .build()

    @JvmField
    val SELECT_CITRUS_CAN: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.SELECT_CITRUS_CAN)
        .set(DataComponentTypes.ITEM_MODEL, Material.ORANGE_DYE.key)
        .build()

    @JvmField
    val HEAVY_RED_PILLAR_FUNGUS: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.HEAVY_RED_PILLAR_FUNGUS)
        .set(DataComponentTypes.ITEM_MODEL, Material.RED_MUSHROOM.key)
        .build()

    @JvmField
    val PURE_CRYSTAL_TOOTHED_LEAF: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.PURE_CRYSTAL_TOOTHED_LEAF)
        .set(DataComponentTypes.ITEM_MODEL, Material.DIAMOND.key)
        .build()

    @JvmField
    val SELECT_CITRUS_DRAFT: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.SELECT_CITRUS_DRAFT)
        .set(DataComponentTypes.ITEM_MODEL, Material.POTION.key)
        .build()

    @JvmField
    val SELECT_BUCKWHEAT_RESTORATION_AGENT: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.SELECT_BUCKWHEAT_RESTORATION_AGENT)
        .set(DataComponentTypes.ITEM_MODEL, Material.POTION.key)
        .build()

    @JvmField
    val JIN_GRASS_SEED: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.JIN_GRASS_SEED)
        .set(DataComponentTypes.ITEM_MODEL, Material.WHEAT_SEEDS.key)
        .build()

    @JvmField
    val LARGE_BUCKWHEAT_RESTORATION_AGENT: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.LARGE_BUCKWHEAT_RESTORATION_AGENT)
        .set(DataComponentTypes.ITEM_MODEL, Material.POTION.key)
        .build()

    @JvmField
    val BUD_NEEDLE_SEED: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.BUD_NEEDLE_SEED)
        .set(DataComponentTypes.ITEM_MODEL, Material.WHEAT_SEEDS.key)
        .build()

    @JvmField
    val FLUFFY_JIN_GRASS: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.FLUFFY_JIN_GRASS)
        .set(DataComponentTypes.ITEM_MODEL, Material.SHORT_GRASS.key)
        .build()

    @JvmField
    val THORN_BUD_NEEDLE: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.THORN_BUD_NEEDLE)
        .set(DataComponentTypes.ITEM_MODEL, Material.SWEET_BERRIES.key)
        .build()

    @JvmField
    val GOLD_STONE_RICE: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.GOLD_STONE_RICE)
        .set(DataComponentTypes.ITEM_MODEL, Material.WHEAT.key)
        .build()

    @JvmField
    val GOLD_STONE_RICE_SEED: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.GOLD_STONE_RICE_SEED)
        .set(DataComponentTypes.ITEM_MODEL, Material.WHEAT_SEEDS.key)
        .build()

    @JvmField
    val BUD_NEEDLE: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.BUD_NEEDLE)
        .set(DataComponentTypes.ITEM_MODEL, Material.SWEET_BERRIES.key)
        .build()

    @JvmField
    val JADE_LEAF_GINSENG: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.JADE_LEAF_GINSENG)
        .set(DataComponentTypes.ITEM_MODEL, Material.CARROT.key)
        .build()

    @JvmField
    val JADE_LEAF_GINSENG_SEED: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.JADE_LEAF_GINSENG_SEED)
        .set(DataComponentTypes.ITEM_MODEL, Material.WHEAT_SEEDS.key)
        .build()

    @JvmField
    val JIN_GRASS_POWDER: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.JIN_GRASS_POWDER)
        .set(DataComponentTypes.ITEM_MODEL, Material.SHORT_GRASS.key)
        .build()

    @JvmField
    val BUD_NEEDLE_POWDER: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.BUD_NEEDLE_POWDER)
        .set(DataComponentTypes.ITEM_MODEL, Material.SWEET_BERRIES.key)
        .build()

    @JvmField
    val FLUFFY_JIN_GRASS_POWDER: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.FLUFFY_JIN_GRASS_POWDER)
        .set(DataComponentTypes.ITEM_MODEL, Material.SHORT_GRASS.key)
        .build()

    @JvmField
    val THORN_BUD_NEEDLE_POWDER: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.THORN_BUD_NEEDLE_POWDER)
        .set(DataComponentTypes.ITEM_MODEL, Material.SWEET_BERRIES.key)
        .build()

    @JvmField
    val MID_RED_PILLAR_FUNGUS: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.MID_RED_PILLAR_FUNGUS)
        .set(DataComponentTypes.ITEM_MODEL, Material.RED_MUSHROOM.key)
        .build()

    @JvmField
    val BLOOD_FUNGUS: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.BLOOD_FUNGUS)
        .set(DataComponentTypes.ITEM_MODEL, Material.NETHER_WART.key)
        .build()

    @JvmField
    val ULTIMATE_CRYSTAL_TOOTHED_LEAF: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.ULTIMATE_CRYSTAL_TOOTHED_LEAF)
        .set(DataComponentTypes.ITEM_MODEL, Material.DIAMOND.key)
        .build()

    @JvmField
    val LOW_CAP_WULING_BATTERY: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.LOW_CAP_WULING_BATTERY)
        .set(DataComponentTypes.ITEM_MODEL, Material.REDSTONE.key)
        .build()

    @JvmField
    val XI_RANG: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.XI_RANG)
        .set(DataComponentTypes.ITEM_MODEL, Material.DIRT.key)
        .build()

    @JvmField
    val SAND_LEAF: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.SAND_LEAF)
        .set(DataComponentTypes.ITEM_MODEL, Material.DEAD_BUSH.key)
        .build()

    @JvmField
    val JIN_GRASS: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.JIN_GRASS)
        .set(DataComponentTypes.ITEM_MODEL, Material.SHORT_GRASS.key)
        .build()

    @JvmField
    val CITRUS: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.CITRUS)
        .set(DataComponentTypes.ITEM_MODEL, Material.ORANGE_DYE.key)
        .build()

    @JvmField
    val ORIGINIUM_ORE: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.ORIGINIUM_ORE)
        .set(DataComponentTypes.ITEM_MODEL, Material.GLOWSTONE.key)
        .build()

    @JvmField
    val HIGH_CRYSTAL_EQUIPMENT_PART: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.HIGH_CRYSTAL_EQUIPMENT_PART)
        .set(DataComponentTypes.ITEM_MODEL, Material.AMETHYST_SHARD.key)
        .build()

    @JvmField
    val XI_RANG_EQUIPMENT_PART: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.XI_RANG_EQUIPMENT_PART)
        .set(DataComponentTypes.ITEM_MODEL, Material.DIRT.key)
        .build()

    @JvmField
    val SAND_LEAF_SEED: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.SAND_LEAF_SEED)
        .set(DataComponentTypes.ITEM_MODEL, Material.WHEAT_SEEDS.key)
        .build()

    @JvmField
    val STAR_GATE_FUNGUS: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.STAR_GATE_FUNGUS)
        .set(DataComponentTypes.ITEM_MODEL, Material.BROWN_MUSHROOM.key)
        .build()

    @JvmField
    val EMBEDDED_CRYSTAL_JADE: ItemStack = ItemStackBuilder
        .rebar(Material.CLAY_BALL, EndfieldIndustryKeys.EMBEDDED_CRYSTAL_JADE)
        .set(DataComponentTypes.ITEM_MODEL, Material.EMERALD.key)
        .build()

    @JvmField
    val GOLD_TICKET: ItemStack = ItemStackBuilder
        .rebar(Material.PAPER, EndfieldIndustryKeys.GOLD_TICKET)
        .set(DataComponentTypes.ITEM_MODEL, Material.GOLD_INGOT.key)
        .build()

    init {
        RebarItem.register(RebarItem::class.java, HIGH_CRYSTAL_PART)
        EndfieldIndustryPages.COMPONENTS.addItem(HIGH_CRYSTAL_PART)

        RebarItem.register(RebarItem::class.java, STEEL_PART)
        EndfieldIndustryPages.COMPONENTS.addItem(STEEL_PART)

        RebarItem.register(RebarItem::class.java, STABLE_CARBON_BLOCK)
        EndfieldIndustryPages.MATERIALS.addItem(STABLE_CARBON_BLOCK)

        RebarItem.register(RebarItem::class.java, SAND_LEAF_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(SAND_LEAF_POWDER)

        RebarItem.register(RebarItem::class.java, DENSE_ORIGINIUM_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(DENSE_ORIGINIUM_POWDER)

        RebarItem.register(RebarItem::class.java, DENSE_CRYSTAL_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(DENSE_CRYSTAL_POWDER)

        RebarItem.register(RebarItem::class.java, HIGH_CRYSTAL_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(HIGH_CRYSTAL_POWDER)

        RebarItem.register(RebarItem::class.java, DENSE_BLUE_IRON_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(DENSE_BLUE_IRON_POWDER)

        RebarItem.register(RebarItem::class.java, DENSE_CARBON_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(DENSE_CARBON_POWDER)

        RebarItem.register(RebarItem::class.java, HIGH_CAP_VALLEY_BATTERY)
        EndfieldIndustryPages.PRODUCTS.addItem(HIGH_CAP_VALLEY_BATTERY)

        RebarItem.register(RebarItem::class.java, LARGE_BUD_SPRAY)
        EndfieldIndustryPages.MEDICINES.addItem(LARGE_BUD_SPRAY)

        RebarItem.register(RebarItem::class.java, HIGH_CRYSTAL_BOTTLE)
        EndfieldIndustryPages.COMPONENTS.addItem(HIGH_CRYSTAL_BOTTLE)

        RebarItem.register(RebarItem::class.java, STEEL_BOTTLE)
        EndfieldIndustryPages.COMPONENTS.addItem(STEEL_BOTTLE)

        RebarItem.register(RebarItem::class.java, FINE_GROUND_BUCKWHEAT_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(FINE_GROUND_BUCKWHEAT_POWDER)

        RebarItem.register(RebarItem::class.java, FINE_GROUND_CITRUS_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(FINE_GROUND_CITRUS_POWDER)

        RebarItem.register(RebarItem::class.java, STEEL_BLOCK)
        EndfieldIndustryPages.MATERIALS.addItem(STEEL_BLOCK)

        RebarItem.register(RebarItem::class.java, HIGH_CRYSTAL_FIBER)
        EndfieldIndustryPages.COMPONENTS.addItem(HIGH_CRYSTAL_FIBER)

        RebarItem.register(RebarItem::class.java, DENSE_CRYSTAL)
        EndfieldIndustryPages.MATERIALS.addItem(DENSE_CRYSTAL)

        RebarItem.register(RebarItem::class.java, BUCKWHEAT)
        EndfieldIndustryPages.PLANTS.addItem(BUCKWHEAT)

        RebarItem.register(RebarItem::class.java, BUCKWHEAT_SEED)
        EndfieldIndustryPages.PLANTS.addItem(BUCKWHEAT_SEED)

        RebarItem.register(RebarItem::class.java, SEED_EXTRACTOR, EndfieldIndustryKeys.SEED_EXTRACTOR)
        EndfieldIndustryPages.MACHINES.addItem(SEED_EXTRACTOR)

        RebarItem.register(RebarItem::class.java, PLANTER, EndfieldIndustryKeys.PLANTER)
        EndfieldIndustryPages.MACHINES.addItem(PLANTER)

        RebarItem.register(RebarItem::class.java, RELAY_BASE, EndfieldIndustryKeys.RELAY_BASE)
        EndfieldIndustryPages.MACHINES.addItem(RELAY_BASE)

        RebarItem.register(RebarItem::class.java, RELAY_DIFFUSER, EndfieldIndustryKeys.RELAY_DIFFUSER)
        EndfieldIndustryPages.MACHINES.addItem(RELAY_DIFFUSER)

        RebarItem.register(RebarItem::class.java, POWER_STATION_BASE, EndfieldIndustryKeys.POWER_STATION_BASE)
        EndfieldIndustryPages.MACHINES.addItem(POWER_STATION_BASE)

        RebarItem.register(RebarItem::class.java, POWER_STATION_EMITTER, EndfieldIndustryKeys.POWER_STATION_EMITTER)
        EndfieldIndustryPages.MACHINES.addItem(POWER_STATION_EMITTER)

        RebarItem.register(RebarItem::class.java, PROTOCOL_CORE_CONTROLLER, EndfieldIndustryKeys.PROTOCOL_CORE_CONTROLLER)
        EndfieldIndustryPages.MACHINES.addItem(PROTOCOL_CORE_CONTROLLER)

        RebarItem.register(RebarItem::class.java, LIGHT_DIM_STONE)
        EndfieldIndustryPages.MATERIALS.addItem(LIGHT_DIM_STONE)

        RebarItem.register(RebarItem::class.java, CARTILAGE_FRAGMENT)
        EndfieldIndustryPages.MATERIALS.addItem(CARTILAGE_FRAGMENT)

        RebarItem.register(RebarItem::class.java, CARBON_BLOCK)
        EndfieldIndustryPages.MATERIALS.addItem(CARBON_BLOCK)

        RebarItem.register(RebarItem::class.java, AMETHYST_FIBER)
        EndfieldIndustryPages.MATERIALS.addItem(AMETHYST_FIBER)

        RebarItem.register(RebarItem::class.java, BLUE_IRON_BLOCK)
        EndfieldIndustryPages.MATERIALS.addItem(BLUE_IRON_BLOCK)

        RebarItem.register(RebarItem::class.java, CARBON_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(CARBON_POWDER)

        RebarItem.register(RebarItem::class.java, INDUSTRIAL_EXPLOSIVE)
        EndfieldIndustryPages.PRODUCTS.addItem(INDUSTRIAL_EXPLOSIVE)

        RebarItem.register(RebarItem::class.java, GRAY_WHEAT_SEED)
        EndfieldIndustryPages.PLANTS.addItem(GRAY_WHEAT_SEED)

        RebarItem.register(RebarItem::class.java, GRAY_WHEAT)
        EndfieldIndustryPages.PLANTS.addItem(GRAY_WHEAT)

        RebarItem.register(RebarItem::class.java, KETONE_TREE_SEED)
        EndfieldIndustryPages.PLANTS.addItem(KETONE_TREE_SEED)

        RebarItem.register(RebarItem::class.java, CITRUS_SEED)
        EndfieldIndustryPages.PLANTS.addItem(CITRUS_SEED)

        RebarItem.register(RebarItem::class.java, BLUE_IRON_ORE)
        EndfieldIndustryPages.MATERIALS.addItem(BLUE_IRON_ORE)

        RebarItem.register(RebarItem::class.java, GLOW_SHELL_BUG)
        EndfieldIndustryPages.MATERIALS.addItem(GLOW_SHELL_BUG)

        RebarItem.register(RebarItem::class.java, AMETHYST_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(AMETHYST_POWDER)

        RebarItem.register(RebarItem::class.java, GRASS_SEED_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(GRASS_SEED_POWDER)

        RebarItem.register(RebarItem::class.java, VALLEY_DISPATCH_TICKET)
        EndfieldIndustryPages.PRODUCTS.addItem(VALLEY_DISPATCH_TICKET)

        RebarItem.register(RebarItem::class.java, CRYSTAL_SHELL)
        EndfieldIndustryPages.MATERIALS.addItem(CRYSTAL_SHELL)

        RebarItem.register(RebarItem::class.java, BEAST_MEAT)
        EndfieldIndustryPages.MATERIALS.addItem(BEAST_MEAT)

        RebarItem.register(RebarItem::class.java, HIGH_ENERGY_FRAGRANT_STONE)
        EndfieldIndustryPages.MATERIALS.addItem(HIGH_ENERGY_FRAGRANT_STONE)

        RebarItem.register(RebarItem::class.java, HARD_FRAGRANT_STONE)
        EndfieldIndustryPages.MATERIALS.addItem(HARD_FRAGRANT_STONE)

        RebarItem.register(RebarItem::class.java, CRYSTAL_SHELL_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(CRYSTAL_SHELL_POWDER)

        RebarItem.register(RebarItem::class.java, LIGHT_RED_PILLAR_FUNGUS)
        EndfieldIndustryPages.MATERIALS.addItem(LIGHT_RED_PILLAR_FUNGUS)

        RebarItem.register(RebarItem::class.java, CRYSTALIZED_TOOTHED_LEAF)
        EndfieldIndustryPages.MATERIALS.addItem(CRYSTALIZED_TOOTHED_LEAF)

        RebarItem.register(RebarItem::class.java, FIRE_BUCKWHEAT)
        EndfieldIndustryPages.PLANTS.addItem(FIRE_BUCKWHEAT)

        RebarItem.register(RebarItem::class.java, DARK_SILVER_CITRUS)
        EndfieldIndustryPages.PLANTS.addItem(DARK_SILVER_CITRUS)

        RebarItem.register(RebarItem::class.java, LOG)
        EndfieldIndustryPages.MATERIALS.addItem(LOG)

        RebarItem.register(RebarItem::class.java, KETONE_BUSH)
        EndfieldIndustryPages.PLANTS.addItem(KETONE_BUSH)

        RebarItem.register(RebarItem::class.java, ORIGINIUM_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(ORIGINIUM_POWDER)

        RebarItem.register(RebarItem::class.java, AMETHYST_ORE)
        EndfieldIndustryPages.MATERIALS.addItem(AMETHYST_ORE)

        RebarItem.register(RebarItem::class.java, BLUE_IRON_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(BLUE_IRON_POWDER)

        RebarItem.register(RebarItem::class.java, KETONE_BUSH_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(KETONE_BUSH_POWDER)

        RebarItem.register(RebarItem::class.java, AMETHYST_BOTTLE)
        EndfieldIndustryPages.COMPONENTS.addItem(AMETHYST_BOTTLE)

        RebarItem.register(RebarItem::class.java, BLUE_IRON_BOTTLE)
        EndfieldIndustryPages.COMPONENTS.addItem(BLUE_IRON_BOTTLE)

        RebarItem.register(RebarItem::class.java, IRON_PART)
        EndfieldIndustryPages.COMPONENTS.addItem(IRON_PART)

        RebarItem.register(RebarItem::class.java, AMETHYST_EQUIPMENT_PART)
        EndfieldIndustryPages.COMPONENTS.addItem(AMETHYST_EQUIPMENT_PART)

        RebarItem.register(RebarItem::class.java, BLUE_IRON_EQUIPMENT_PART)
        EndfieldIndustryPages.COMPONENTS.addItem(BLUE_IRON_EQUIPMENT_PART)

        RebarItem.register(RebarItem::class.java, LOW_CAP_VALLEY_BATTERY)
        EndfieldIndustryPages.PRODUCTS.addItem(LOW_CAP_VALLEY_BATTERY)

        RebarItem.register(RebarItem::class.java, AMETHYST_PART)
        EndfieldIndustryPages.COMPONENTS.addItem(AMETHYST_PART)

        RebarItem.register(RebarItem::class.java, MID_CAP_VALLEY_BATTERY)
        EndfieldIndustryPages.PRODUCTS.addItem(MID_CAP_VALLEY_BATTERY)

        RebarItem.register(RebarItem::class.java, BUCKWHEAT_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(BUCKWHEAT_POWDER)

        RebarItem.register(RebarItem::class.java, CITRUS_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(CITRUS_POWDER)

        RebarItem.register(RebarItem::class.java, MID_DIM_STONE)
        EndfieldIndustryPages.MATERIALS.addItem(MID_DIM_STONE)

        RebarItem.register(RebarItem::class.java, BUCKWHEAT_MEDICINE_POWDER)
        EndfieldIndustryPages.MEDICINES.addItem(BUCKWHEAT_MEDICINE_POWDER)

        RebarItem.register(RebarItem::class.java, CITRUS_DRAFT)
        EndfieldIndustryPages.MEDICINES.addItem(CITRUS_DRAFT)

        RebarItem.register(RebarItem::class.java, BUCKWHEAT_CAPSULE)
        EndfieldIndustryPages.MEDICINES.addItem(BUCKWHEAT_CAPSULE)

        RebarItem.register(RebarItem::class.java, QUALITY_BUCKWHEAT_CAPSULE)
        EndfieldIndustryPages.MEDICINES.addItem(QUALITY_BUCKWHEAT_CAPSULE)

        RebarItem.register(RebarItem::class.java, CITRUS_CAN)
        EndfieldIndustryPages.MEDICINES.addItem(CITRUS_CAN)

        RebarItem.register(RebarItem::class.java, QUALITY_CITRUS_CAN)
        EndfieldIndustryPages.MEDICINES.addItem(QUALITY_CITRUS_CAN)

        RebarItem.register(RebarItem::class.java, SMALL_BUCKWHEAT_RESTORATION_AGENT)
        EndfieldIndustryPages.MEDICINES.addItem(SMALL_BUCKWHEAT_RESTORATION_AGENT)

        RebarItem.register(RebarItem::class.java, SMALL_CITRUS_DRAFT)
        EndfieldIndustryPages.MEDICINES.addItem(SMALL_CITRUS_DRAFT)

        RebarItem.register(RebarItem::class.java, SELECT_BUCKWHEAT_CAPSULE)
        EndfieldIndustryPages.MEDICINES.addItem(SELECT_BUCKWHEAT_CAPSULE)

        RebarItem.register(RebarItem::class.java, SELECT_CITRUS_CAN)
        EndfieldIndustryPages.MEDICINES.addItem(SELECT_CITRUS_CAN)

        RebarItem.register(RebarItem::class.java, HEAVY_RED_PILLAR_FUNGUS)
        EndfieldIndustryPages.MATERIALS.addItem(HEAVY_RED_PILLAR_FUNGUS)

        RebarItem.register(RebarItem::class.java, PURE_CRYSTAL_TOOTHED_LEAF)
        EndfieldIndustryPages.MATERIALS.addItem(PURE_CRYSTAL_TOOTHED_LEAF)

        RebarItem.register(RebarItem::class.java, SELECT_CITRUS_DRAFT)
        EndfieldIndustryPages.MEDICINES.addItem(SELECT_CITRUS_DRAFT)

        RebarItem.register(RebarItem::class.java, SELECT_BUCKWHEAT_RESTORATION_AGENT)
        EndfieldIndustryPages.MEDICINES.addItem(SELECT_BUCKWHEAT_RESTORATION_AGENT)

        RebarItem.register(RebarItem::class.java, JIN_GRASS_SEED)
        EndfieldIndustryPages.PLANTS.addItem(JIN_GRASS_SEED)

        RebarItem.register(RebarItem::class.java, LARGE_BUCKWHEAT_RESTORATION_AGENT)
        EndfieldIndustryPages.MEDICINES.addItem(LARGE_BUCKWHEAT_RESTORATION_AGENT)

        RebarItem.register(RebarItem::class.java, BUD_NEEDLE_SEED)
        EndfieldIndustryPages.PLANTS.addItem(BUD_NEEDLE_SEED)

        RebarItem.register(RebarItem::class.java, FLUFFY_JIN_GRASS)
        EndfieldIndustryPages.PLANTS.addItem(FLUFFY_JIN_GRASS)

        RebarItem.register(RebarItem::class.java, THORN_BUD_NEEDLE)
        EndfieldIndustryPages.PLANTS.addItem(THORN_BUD_NEEDLE)

        RebarItem.register(RebarItem::class.java, GOLD_STONE_RICE)
        EndfieldIndustryPages.PLANTS.addItem(GOLD_STONE_RICE)

        RebarItem.register(RebarItem::class.java, GOLD_STONE_RICE_SEED)
        EndfieldIndustryPages.PLANTS.addItem(GOLD_STONE_RICE_SEED)

        RebarItem.register(RebarItem::class.java, BUD_NEEDLE)
        EndfieldIndustryPages.PLANTS.addItem(BUD_NEEDLE)

        RebarItem.register(RebarItem::class.java, JADE_LEAF_GINSENG)
        EndfieldIndustryPages.PLANTS.addItem(JADE_LEAF_GINSENG)

        RebarItem.register(RebarItem::class.java, JADE_LEAF_GINSENG_SEED)
        EndfieldIndustryPages.PLANTS.addItem(JADE_LEAF_GINSENG_SEED)

        RebarItem.register(RebarItem::class.java, JIN_GRASS_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(JIN_GRASS_POWDER)

        RebarItem.register(RebarItem::class.java, BUD_NEEDLE_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(BUD_NEEDLE_POWDER)

        RebarItem.register(RebarItem::class.java, FLUFFY_JIN_GRASS_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(FLUFFY_JIN_GRASS_POWDER)

        RebarItem.register(RebarItem::class.java, THORN_BUD_NEEDLE_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(THORN_BUD_NEEDLE_POWDER)

        RebarItem.register(RebarItem::class.java, MID_RED_PILLAR_FUNGUS)
        EndfieldIndustryPages.MATERIALS.addItem(MID_RED_PILLAR_FUNGUS)

        RebarItem.register(RebarItem::class.java, BLOOD_FUNGUS)
        EndfieldIndustryPages.MATERIALS.addItem(BLOOD_FUNGUS)

        RebarItem.register(RebarItem::class.java, ULTIMATE_CRYSTAL_TOOTHED_LEAF)
        EndfieldIndustryPages.MATERIALS.addItem(ULTIMATE_CRYSTAL_TOOTHED_LEAF)

        RebarItem.register(RebarItem::class.java, LOW_CAP_WULING_BATTERY)
        EndfieldIndustryPages.PRODUCTS.addItem(LOW_CAP_WULING_BATTERY)

        RebarItem.register(RebarItem::class.java, XI_RANG)
        EndfieldIndustryPages.MATERIALS.addItem(XI_RANG)

        RebarItem.register(RebarItem::class.java, SAND_LEAF)
        EndfieldIndustryPages.PLANTS.addItem(SAND_LEAF)

        RebarItem.register(RebarItem::class.java, JIN_GRASS)
        EndfieldIndustryPages.PLANTS.addItem(JIN_GRASS)

        RebarItem.register(RebarItem::class.java, CITRUS)
        EndfieldIndustryPages.PLANTS.addItem(CITRUS)

        RebarItem.register(RebarItem::class.java, ORIGINIUM_ORE)
        EndfieldIndustryPages.MATERIALS.addItem(ORIGINIUM_ORE)

        RebarItem.register(RebarItem::class.java, HIGH_CRYSTAL_EQUIPMENT_PART)
        EndfieldIndustryPages.COMPONENTS.addItem(HIGH_CRYSTAL_EQUIPMENT_PART)

        RebarItem.register(RebarItem::class.java, XI_RANG_EQUIPMENT_PART)
        EndfieldIndustryPages.COMPONENTS.addItem(XI_RANG_EQUIPMENT_PART)

        RebarItem.register(RebarItem::class.java, SAND_LEAF_SEED)
        EndfieldIndustryPages.PLANTS.addItem(SAND_LEAF_SEED)

        RebarItem.register(RebarItem::class.java, STAR_GATE_FUNGUS)
        EndfieldIndustryPages.PLANTS.addItem(STAR_GATE_FUNGUS)

        RebarItem.register(RebarItem::class.java, EMBEDDED_CRYSTAL_JADE)
        EndfieldIndustryPages.PRODUCTS.addItem(EMBEDDED_CRYSTAL_JADE)

        RebarItem.register(RebarItem::class.java, GOLD_TICKET)
        EndfieldIndustryPages.PRODUCTS.addItem(GOLD_TICKET)
    }

    fun initialize() {
    }
}
