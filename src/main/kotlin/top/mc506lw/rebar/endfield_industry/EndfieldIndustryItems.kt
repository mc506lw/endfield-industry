package top.mc506lw.rebar.endfield_industry

import io.github.pylonmc.rebar.item.RebarItem
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import io.papermc.paper.datacomponent.DataComponentTypes
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack

/**
 * 插件全部物品的定义与注册。
 *
 * 每个物品以模板 [ItemStack] 形式定义（名称/描述来自语言文件，纹理通过 ITEM_MODEL 数据组件指定），
 * 在 [initialize] 中统一注册为 Rebar 物品；机器物品额外绑定对应方块键。
 * 手册页面的展示由 Pages 模块另行添加。
 */
object EndfieldIndustryItems {

    private fun item(material: Material, key: NamespacedKey): ItemStack =
        ItemStackBuilder.rebar(ItemStack.of(material), key).build()

    private fun item(material: Material, key: NamespacedKey, model: Material): ItemStack =
        ItemStackBuilder.rebar(ItemStack.of(material), key)
            .set(DataComponentTypes.ITEM_MODEL, model.key)
            .build()

    // region 机器（同时作为方块物品，注册时绑定方块键）

    @JvmField
    val SEED_EXTRACTOR: ItemStack = item(Material.FURNACE, EndfieldIndustryKeys.SEED_EXTRACTOR)

    @JvmField
    val PLANTER: ItemStack = item(Material.SMOKER, EndfieldIndustryKeys.PLANTER)

    @JvmField
    val RELAY_BASE: ItemStack = item(Material.FURNACE, EndfieldIndustryKeys.RELAY_BASE)

    @JvmField
    val RELAY_DIFFUSER: ItemStack = item(Material.FURNACE, EndfieldIndustryKeys.RELAY_DIFFUSER)

    @JvmField
    val POWER_STATION_BASE: ItemStack = item(Material.FURNACE, EndfieldIndustryKeys.POWER_STATION_BASE)

    @JvmField
    val POWER_STATION_EMITTER: ItemStack = item(Material.FURNACE, EndfieldIndustryKeys.POWER_STATION_EMITTER)

    @JvmField
    val PROTOCOL_CORE_CONTROLLER: ItemStack = item(Material.BEACON, EndfieldIndustryKeys.PROTOCOL_CORE_CONTROLLER)

    // endregion

    // region 植物与作物

    @JvmField
    val BUCKWHEAT: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.BUCKWHEAT, Material.POPPY)

    @JvmField
    val GRAY_WHEAT: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.GRAY_WHEAT, Material.WHEAT)

    @JvmField
    val KETONE_BUSH: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.KETONE_BUSH, Material.DEAD_BUSH)

    @JvmField
    val CITRUS: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.CITRUS, Material.ORANGE_DYE)

    @JvmField
    val JIN_GRASS: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.JIN_GRASS, Material.SHORT_GRASS)

    @JvmField
    val BUD_NEEDLE: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.BUD_NEEDLE, Material.SWEET_BERRIES)

    @JvmField
    val GOLD_STONE_RICE: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.GOLD_STONE_RICE, Material.WHEAT)

    @JvmField
    val JADE_LEAF_GINSENG: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.JADE_LEAF_GINSENG, Material.CARROT)

    @JvmField
    val SAND_LEAF: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.SAND_LEAF, Material.DEAD_BUSH)

    @JvmField
    val FIRE_BUCKWHEAT: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.FIRE_BUCKWHEAT, Material.RED_TULIP)

    @JvmField
    val DARK_SILVER_CITRUS: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.DARK_SILVER_CITRUS, Material.ORANGE_DYE)

    @JvmField
    val LOG: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.LOG, Material.OAK_LOG)

    @JvmField
    val FLUFFY_JIN_GRASS: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.FLUFFY_JIN_GRASS, Material.SHORT_GRASS)

    @JvmField
    val THORN_BUD_NEEDLE: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.THORN_BUD_NEEDLE, Material.SWEET_BERRIES)

    // endregion

    // region 种子

    @JvmField
    val BUCKWHEAT_SEED: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.BUCKWHEAT_SEED, Material.WHEAT_SEEDS)

    @JvmField
    val GRAY_WHEAT_SEED: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.GRAY_WHEAT_SEED, Material.WHEAT_SEEDS)

    @JvmField
    val KETONE_TREE_SEED: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.KETONE_TREE_SEED, Material.OAK_SAPLING)

    @JvmField
    val CITRUS_SEED: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.CITRUS_SEED, Material.ORANGE_DYE)

    @JvmField
    val JIN_GRASS_SEED: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.JIN_GRASS_SEED, Material.WHEAT_SEEDS)

    @JvmField
    val BUD_NEEDLE_SEED: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.BUD_NEEDLE_SEED, Material.WHEAT_SEEDS)

    @JvmField
    val GOLD_STONE_RICE_SEED: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.GOLD_STONE_RICE_SEED, Material.WHEAT_SEEDS)

    @JvmField
    val JADE_LEAF_GINSENG_SEED: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.JADE_LEAF_GINSENG_SEED, Material.WHEAT_SEEDS)

    @JvmField
    val SAND_LEAF_SEED: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.SAND_LEAF_SEED, Material.WHEAT_SEEDS)

    // endregion

    // region 菌类与特殊植物

    @JvmField
    val LIGHT_RED_PILLAR_FUNGUS: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.LIGHT_RED_PILLAR_FUNGUS, Material.RED_MUSHROOM)

    @JvmField
    val MID_RED_PILLAR_FUNGUS: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.MID_RED_PILLAR_FUNGUS, Material.RED_MUSHROOM)

    @JvmField
    val HEAVY_RED_PILLAR_FUNGUS: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.HEAVY_RED_PILLAR_FUNGUS, Material.RED_MUSHROOM)

    @JvmField
    val BLOOD_FUNGUS: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.BLOOD_FUNGUS, Material.NETHER_WART)

    @JvmField
    val STAR_GATE_FUNGUS: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.STAR_GATE_FUNGUS, Material.BROWN_MUSHROOM)

    @JvmField
    val CRYSTALIZED_TOOTHED_LEAF: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.CRYSTALIZED_TOOTHED_LEAF, Material.DIAMOND)

    @JvmField
    val PURE_CRYSTAL_TOOTHED_LEAF: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.PURE_CRYSTAL_TOOTHED_LEAF, Material.DIAMOND)

    @JvmField
    val ULTIMATE_CRYSTAL_TOOTHED_LEAF: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.ULTIMATE_CRYSTAL_TOOTHED_LEAF, Material.DIAMOND)

    // endregion

    // region 材料（块）

    @JvmField
    val STABLE_CARBON_BLOCK: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.STABLE_CARBON_BLOCK, Material.COAL_BLOCK)

    @JvmField
    val CARBON_BLOCK: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.CARBON_BLOCK, Material.COAL)

    @JvmField
    val STEEL_BLOCK: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.STEEL_BLOCK, Material.IRON_BLOCK)

    @JvmField
    val BLUE_IRON_BLOCK: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.BLUE_IRON_BLOCK, Material.LAPIS_BLOCK)

    @JvmField
    val DENSE_CRYSTAL: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.DENSE_CRYSTAL, Material.DIAMOND)

    // endregion

    // region 材料（粉末）

    @JvmField
    val SAND_LEAF_POWDER: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.SAND_LEAF_POWDER, Material.SAND)

    @JvmField
    val DENSE_ORIGINIUM_POWDER: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.DENSE_ORIGINIUM_POWDER, Material.GLOWSTONE_DUST)

    @JvmField
    val DENSE_CRYSTAL_POWDER: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.DENSE_CRYSTAL_POWDER, Material.DIAMOND)

    @JvmField
    val HIGH_CRYSTAL_POWDER: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.HIGH_CRYSTAL_POWDER, Material.AMETHYST_SHARD)

    @JvmField
    val DENSE_BLUE_IRON_POWDER: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.DENSE_BLUE_IRON_POWDER, Material.LAPIS_LAZULI)

    @JvmField
    val DENSE_CARBON_POWDER: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.DENSE_CARBON_POWDER, Material.GUNPOWDER)

    @JvmField
    val ORIGINIUM_POWDER: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.ORIGINIUM_POWDER, Material.GLOWSTONE_DUST)

    @JvmField
    val BLUE_IRON_POWDER: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.BLUE_IRON_POWDER, Material.LAPIS_LAZULI)

    @JvmField
    val AMETHYST_POWDER: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.AMETHYST_POWDER, Material.AMETHYST_SHARD)

    @JvmField
    val CARBON_POWDER: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.CARBON_POWDER, Material.GUNPOWDER)

    @JvmField
    val KETONE_BUSH_POWDER: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.KETONE_BUSH_POWDER, Material.SAND)

    @JvmField
    val BUCKWHEAT_POWDER: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.BUCKWHEAT_POWDER, Material.SUGAR)

    @JvmField
    val CITRUS_POWDER: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.CITRUS_POWDER, Material.ORANGE_DYE)

    @JvmField
    val JIN_GRASS_POWDER: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.JIN_GRASS_POWDER, Material.SHORT_GRASS)

    @JvmField
    val BUD_NEEDLE_POWDER: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.BUD_NEEDLE_POWDER, Material.SWEET_BERRIES)

    @JvmField
    val FLUFFY_JIN_GRASS_POWDER: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.FLUFFY_JIN_GRASS_POWDER, Material.SHORT_GRASS)

    @JvmField
    val THORN_BUD_NEEDLE_POWDER: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.THORN_BUD_NEEDLE_POWDER, Material.SWEET_BERRIES)

    @JvmField
    val GRASS_SEED_POWDER: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.GRASS_SEED_POWDER, Material.SHORT_GRASS)

    @JvmField
    val FINE_GROUND_BUCKWHEAT_POWDER: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.FINE_GROUND_BUCKWHEAT_POWDER, Material.SUGAR)

    @JvmField
    val FINE_GROUND_CITRUS_POWDER: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.FINE_GROUND_CITRUS_POWDER, Material.ORANGE_DYE)

    @JvmField
    val CRYSTAL_SHELL_POWDER: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.CRYSTAL_SHELL_POWDER, Material.QUARTZ)

    // endregion

    // region 材料（矿石与天然物）

    @JvmField
    val BLUE_IRON_ORE: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.BLUE_IRON_ORE, Material.LAPIS_ORE)

    @JvmField
    val AMETHYST_ORE: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.AMETHYST_ORE, Material.AMETHYST_CLUSTER)

    @JvmField
    val ORIGINIUM_ORE: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.ORIGINIUM_ORE, Material.GLOWSTONE)

    @JvmField
    val LIGHT_DIM_STONE: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.LIGHT_DIM_STONE, Material.ECHO_SHARD)

    @JvmField
    val MID_DIM_STONE: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.MID_DIM_STONE, Material.ECHO_SHARD)

    @JvmField
    val HARD_FRAGRANT_STONE: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.HARD_FRAGRANT_STONE, Material.STONE)

    @JvmField
    val HIGH_ENERGY_FRAGRANT_STONE: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.HIGH_ENERGY_FRAGRANT_STONE, Material.GLOWSTONE_DUST)

    @JvmField
    val XI_RANG: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.XI_RANG, Material.DIRT)

    @JvmField
    val EMBEDDED_CRYSTAL_JADE: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.EMBEDDED_CRYSTAL_JADE, Material.EMERALD)

    @JvmField
    val CRYSTAL_SHELL: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.CRYSTAL_SHELL, Material.QUARTZ)

    @JvmField
    val CARTILAGE_FRAGMENT: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.CARTILAGE_FRAGMENT, Material.BONE_MEAL)

    @JvmField
    val GLOW_SHELL_BUG: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.GLOW_SHELL_BUG, Material.GLOW_INK_SAC)

    @JvmField
    val BEAST_MEAT: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.BEAST_MEAT, Material.BEEF)

    @JvmField
    val INDUSTRIAL_EXPLOSIVE: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.INDUSTRIAL_EXPLOSIVE, Material.TNT_MINECART)

    // endregion

    // region 零件

    @JvmField
    val HIGH_CRYSTAL_PART: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.HIGH_CRYSTAL_PART, Material.QUARTZ)

    @JvmField
    val STEEL_PART: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.STEEL_PART, Material.IRON_NUGGET)

    @JvmField
    val IRON_PART: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.IRON_PART, Material.IRON_NUGGET)

    @JvmField
    val AMETHYST_PART: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.AMETHYST_PART, Material.AMETHYST_SHARD)

    @JvmField
    val HIGH_CRYSTAL_FIBER: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.HIGH_CRYSTAL_FIBER, Material.STRING)

    @JvmField
    val AMETHYST_FIBER: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.AMETHYST_FIBER, Material.AMETHYST_SHARD)

    @JvmField
    val HIGH_CRYSTAL_BOTTLE: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.HIGH_CRYSTAL_BOTTLE, Material.GLASS_BOTTLE)

    @JvmField
    val STEEL_BOTTLE: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.STEEL_BOTTLE, Material.IRON_INGOT)

    @JvmField
    val AMETHYST_BOTTLE: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.AMETHYST_BOTTLE, Material.GLASS_BOTTLE)

    @JvmField
    val BLUE_IRON_BOTTLE: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.BLUE_IRON_BOTTLE, Material.GLASS_BOTTLE)

    @JvmField
    val AMETHYST_EQUIPMENT_PART: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.AMETHYST_EQUIPMENT_PART, Material.AMETHYST_SHARD)

    @JvmField
    val BLUE_IRON_EQUIPMENT_PART: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.BLUE_IRON_EQUIPMENT_PART, Material.LAPIS_LAZULI)

    @JvmField
    val HIGH_CRYSTAL_EQUIPMENT_PART: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.HIGH_CRYSTAL_EQUIPMENT_PART, Material.AMETHYST_SHARD)

    @JvmField
    val XI_RANG_EQUIPMENT_PART: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.XI_RANG_EQUIPMENT_PART, Material.DIRT)

    // endregion

    // region 药品

    @JvmField
    val LARGE_BUD_SPRAY: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.LARGE_BUD_SPRAY, Material.POTION)

    @JvmField
    val BUCKWHEAT_MEDICINE_POWDER: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.BUCKWHEAT_MEDICINE_POWDER, Material.SUGAR)

    @JvmField
    val CITRUS_DRAFT: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.CITRUS_DRAFT, Material.POTION)

    @JvmField
    val BUCKWHEAT_CAPSULE: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.BUCKWHEAT_CAPSULE, Material.SUNFLOWER)

    @JvmField
    val QUALITY_BUCKWHEAT_CAPSULE: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.QUALITY_BUCKWHEAT_CAPSULE, Material.SUNFLOWER)

    @JvmField
    val CITRUS_CAN: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.CITRUS_CAN, Material.ORANGE_DYE)

    @JvmField
    val QUALITY_CITRUS_CAN: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.QUALITY_CITRUS_CAN, Material.ORANGE_DYE)

    @JvmField
    val SMALL_BUCKWHEAT_RESTORATION_AGENT: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.SMALL_BUCKWHEAT_RESTORATION_AGENT, Material.POTION)

    @JvmField
    val SMALL_CITRUS_DRAFT: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.SMALL_CITRUS_DRAFT, Material.POTION)

    @JvmField
    val SELECT_BUCKWHEAT_CAPSULE: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.SELECT_BUCKWHEAT_CAPSULE, Material.SUNFLOWER)

    @JvmField
    val SELECT_CITRUS_CAN: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.SELECT_CITRUS_CAN, Material.ORANGE_DYE)

    @JvmField
    val SELECT_CITRUS_DRAFT: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.SELECT_CITRUS_DRAFT, Material.POTION)

    @JvmField
    val SELECT_BUCKWHEAT_RESTORATION_AGENT: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.SELECT_BUCKWHEAT_RESTORATION_AGENT, Material.POTION)

    @JvmField
    val LARGE_BUCKWHEAT_RESTORATION_AGENT: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.LARGE_BUCKWHEAT_RESTORATION_AGENT, Material.POTION)

    // endregion

    // region 产品

    @JvmField
    val HIGH_CAP_VALLEY_BATTERY: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.HIGH_CAP_VALLEY_BATTERY, Material.REDSTONE_BLOCK)

    @JvmField
    val MID_CAP_VALLEY_BATTERY: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.MID_CAP_VALLEY_BATTERY, Material.REDSTONE)

    @JvmField
    val LOW_CAP_VALLEY_BATTERY: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.LOW_CAP_VALLEY_BATTERY, Material.REDSTONE)

    @JvmField
    val LOW_CAP_WULING_BATTERY: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.LOW_CAP_WULING_BATTERY, Material.REDSTONE)

    @JvmField
    val VALLEY_DISPATCH_TICKET: ItemStack = item(Material.CLAY_BALL, EndfieldIndustryKeys.VALLEY_DISPATCH_TICKET, Material.PAPER)

    @JvmField
    val GOLD_TICKET: ItemStack = item(Material.PAPER, EndfieldIndustryKeys.GOLD_TICKET, Material.GOLD_INGOT)

    // endregion

    /**
     * 注册全部物品；机器物品绑定对应方块键。
     */
    fun initialize() {
        // 机器（绑定方块）
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

        // 植物与作物
        RebarItem.register(RebarItem::class.java, BUCKWHEAT)
        EndfieldIndustryPages.PLANTS.addItem(BUCKWHEAT)
        RebarItem.register(RebarItem::class.java, GRAY_WHEAT)
        EndfieldIndustryPages.PLANTS.addItem(GRAY_WHEAT)
        RebarItem.register(RebarItem::class.java, KETONE_BUSH)
        EndfieldIndustryPages.PLANTS.addItem(KETONE_BUSH)
        RebarItem.register(RebarItem::class.java, CITRUS)
        EndfieldIndustryPages.PLANTS.addItem(CITRUS)
        RebarItem.register(RebarItem::class.java, JIN_GRASS)
        EndfieldIndustryPages.PLANTS.addItem(JIN_GRASS)
        RebarItem.register(RebarItem::class.java, BUD_NEEDLE)
        EndfieldIndustryPages.PLANTS.addItem(BUD_NEEDLE)
        RebarItem.register(RebarItem::class.java, GOLD_STONE_RICE)
        EndfieldIndustryPages.PLANTS.addItem(GOLD_STONE_RICE)
        RebarItem.register(RebarItem::class.java, JADE_LEAF_GINSENG)
        EndfieldIndustryPages.PLANTS.addItem(JADE_LEAF_GINSENG)
        RebarItem.register(RebarItem::class.java, SAND_LEAF)
        EndfieldIndustryPages.PLANTS.addItem(SAND_LEAF)
        RebarItem.register(RebarItem::class.java, FIRE_BUCKWHEAT)
        EndfieldIndustryPages.PLANTS.addItem(FIRE_BUCKWHEAT)
        RebarItem.register(RebarItem::class.java, DARK_SILVER_CITRUS)
        EndfieldIndustryPages.PLANTS.addItem(DARK_SILVER_CITRUS)
        RebarItem.register(RebarItem::class.java, LOG)
        EndfieldIndustryPages.MATERIALS.addItem(LOG)
        RebarItem.register(RebarItem::class.java, FLUFFY_JIN_GRASS)
        EndfieldIndustryPages.PLANTS.addItem(FLUFFY_JIN_GRASS)
        RebarItem.register(RebarItem::class.java, THORN_BUD_NEEDLE)
        EndfieldIndustryPages.PLANTS.addItem(THORN_BUD_NEEDLE)

        // 种子
        RebarItem.register(RebarItem::class.java, BUCKWHEAT_SEED)
        EndfieldIndustryPages.PLANTS.addItem(BUCKWHEAT_SEED)
        RebarItem.register(RebarItem::class.java, GRAY_WHEAT_SEED)
        EndfieldIndustryPages.PLANTS.addItem(GRAY_WHEAT_SEED)
        RebarItem.register(RebarItem::class.java, KETONE_TREE_SEED)
        EndfieldIndustryPages.PLANTS.addItem(KETONE_TREE_SEED)
        RebarItem.register(RebarItem::class.java, CITRUS_SEED)
        EndfieldIndustryPages.PLANTS.addItem(CITRUS_SEED)
        RebarItem.register(RebarItem::class.java, JIN_GRASS_SEED)
        EndfieldIndustryPages.PLANTS.addItem(JIN_GRASS_SEED)
        RebarItem.register(RebarItem::class.java, BUD_NEEDLE_SEED)
        EndfieldIndustryPages.PLANTS.addItem(BUD_NEEDLE_SEED)
        RebarItem.register(RebarItem::class.java, GOLD_STONE_RICE_SEED)
        EndfieldIndustryPages.PLANTS.addItem(GOLD_STONE_RICE_SEED)
        RebarItem.register(RebarItem::class.java, JADE_LEAF_GINSENG_SEED)
        EndfieldIndustryPages.PLANTS.addItem(JADE_LEAF_GINSENG_SEED)
        RebarItem.register(RebarItem::class.java, SAND_LEAF_SEED)
        EndfieldIndustryPages.PLANTS.addItem(SAND_LEAF_SEED)

        // 菌类与特殊植物
        RebarItem.register(RebarItem::class.java, LIGHT_RED_PILLAR_FUNGUS)
        EndfieldIndustryPages.MATERIALS.addItem(LIGHT_RED_PILLAR_FUNGUS)
        RebarItem.register(RebarItem::class.java, MID_RED_PILLAR_FUNGUS)
        EndfieldIndustryPages.MATERIALS.addItem(MID_RED_PILLAR_FUNGUS)
        RebarItem.register(RebarItem::class.java, HEAVY_RED_PILLAR_FUNGUS)
        EndfieldIndustryPages.MATERIALS.addItem(HEAVY_RED_PILLAR_FUNGUS)
        RebarItem.register(RebarItem::class.java, BLOOD_FUNGUS)
        EndfieldIndustryPages.MATERIALS.addItem(BLOOD_FUNGUS)
        RebarItem.register(RebarItem::class.java, STAR_GATE_FUNGUS)
        EndfieldIndustryPages.PLANTS.addItem(STAR_GATE_FUNGUS)
        RebarItem.register(RebarItem::class.java, CRYSTALIZED_TOOTHED_LEAF)
        EndfieldIndustryPages.MATERIALS.addItem(CRYSTALIZED_TOOTHED_LEAF)
        RebarItem.register(RebarItem::class.java, PURE_CRYSTAL_TOOTHED_LEAF)
        EndfieldIndustryPages.MATERIALS.addItem(PURE_CRYSTAL_TOOTHED_LEAF)
        RebarItem.register(RebarItem::class.java, ULTIMATE_CRYSTAL_TOOTHED_LEAF)
        EndfieldIndustryPages.MATERIALS.addItem(ULTIMATE_CRYSTAL_TOOTHED_LEAF)

        // 材料（块）
        RebarItem.register(RebarItem::class.java, STABLE_CARBON_BLOCK)
        EndfieldIndustryPages.MATERIALS.addItem(STABLE_CARBON_BLOCK)
        RebarItem.register(RebarItem::class.java, CARBON_BLOCK)
        EndfieldIndustryPages.MATERIALS.addItem(CARBON_BLOCK)
        RebarItem.register(RebarItem::class.java, STEEL_BLOCK)
        EndfieldIndustryPages.MATERIALS.addItem(STEEL_BLOCK)
        RebarItem.register(RebarItem::class.java, BLUE_IRON_BLOCK)
        EndfieldIndustryPages.MATERIALS.addItem(BLUE_IRON_BLOCK)
        RebarItem.register(RebarItem::class.java, DENSE_CRYSTAL)
        EndfieldIndustryPages.MATERIALS.addItem(DENSE_CRYSTAL)

        // 材料（粉末）
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
        RebarItem.register(RebarItem::class.java, ORIGINIUM_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(ORIGINIUM_POWDER)
        RebarItem.register(RebarItem::class.java, BLUE_IRON_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(BLUE_IRON_POWDER)
        RebarItem.register(RebarItem::class.java, AMETHYST_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(AMETHYST_POWDER)
        RebarItem.register(RebarItem::class.java, CARBON_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(CARBON_POWDER)
        RebarItem.register(RebarItem::class.java, KETONE_BUSH_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(KETONE_BUSH_POWDER)
        RebarItem.register(RebarItem::class.java, BUCKWHEAT_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(BUCKWHEAT_POWDER)
        RebarItem.register(RebarItem::class.java, CITRUS_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(CITRUS_POWDER)
        RebarItem.register(RebarItem::class.java, JIN_GRASS_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(JIN_GRASS_POWDER)
        RebarItem.register(RebarItem::class.java, BUD_NEEDLE_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(BUD_NEEDLE_POWDER)
        RebarItem.register(RebarItem::class.java, FLUFFY_JIN_GRASS_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(FLUFFY_JIN_GRASS_POWDER)
        RebarItem.register(RebarItem::class.java, THORN_BUD_NEEDLE_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(THORN_BUD_NEEDLE_POWDER)
        RebarItem.register(RebarItem::class.java, GRASS_SEED_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(GRASS_SEED_POWDER)
        RebarItem.register(RebarItem::class.java, FINE_GROUND_BUCKWHEAT_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(FINE_GROUND_BUCKWHEAT_POWDER)
        RebarItem.register(RebarItem::class.java, FINE_GROUND_CITRUS_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(FINE_GROUND_CITRUS_POWDER)
        RebarItem.register(RebarItem::class.java, CRYSTAL_SHELL_POWDER)
        EndfieldIndustryPages.MATERIALS.addItem(CRYSTAL_SHELL_POWDER)

        // 材料（矿石与天然物）
        RebarItem.register(RebarItem::class.java, BLUE_IRON_ORE)
        EndfieldIndustryPages.MATERIALS.addItem(BLUE_IRON_ORE)
        RebarItem.register(RebarItem::class.java, AMETHYST_ORE)
        EndfieldIndustryPages.MATERIALS.addItem(AMETHYST_ORE)
        RebarItem.register(RebarItem::class.java, ORIGINIUM_ORE)
        EndfieldIndustryPages.MATERIALS.addItem(ORIGINIUM_ORE)
        RebarItem.register(RebarItem::class.java, LIGHT_DIM_STONE)
        EndfieldIndustryPages.MATERIALS.addItem(LIGHT_DIM_STONE)
        RebarItem.register(RebarItem::class.java, MID_DIM_STONE)
        EndfieldIndustryPages.MATERIALS.addItem(MID_DIM_STONE)
        RebarItem.register(RebarItem::class.java, HARD_FRAGRANT_STONE)
        EndfieldIndustryPages.MATERIALS.addItem(HARD_FRAGRANT_STONE)
        RebarItem.register(RebarItem::class.java, HIGH_ENERGY_FRAGRANT_STONE)
        EndfieldIndustryPages.MATERIALS.addItem(HIGH_ENERGY_FRAGRANT_STONE)
        RebarItem.register(RebarItem::class.java, XI_RANG)
        EndfieldIndustryPages.MATERIALS.addItem(XI_RANG)
        RebarItem.register(RebarItem::class.java, EMBEDDED_CRYSTAL_JADE)
        EndfieldIndustryPages.PRODUCTS.addItem(EMBEDDED_CRYSTAL_JADE)
        RebarItem.register(RebarItem::class.java, CRYSTAL_SHELL)
        EndfieldIndustryPages.MATERIALS.addItem(CRYSTAL_SHELL)
        RebarItem.register(RebarItem::class.java, CARTILAGE_FRAGMENT)
        EndfieldIndustryPages.MATERIALS.addItem(CARTILAGE_FRAGMENT)
        RebarItem.register(RebarItem::class.java, GLOW_SHELL_BUG)
        EndfieldIndustryPages.MATERIALS.addItem(GLOW_SHELL_BUG)
        RebarItem.register(RebarItem::class.java, BEAST_MEAT)
        EndfieldIndustryPages.MATERIALS.addItem(BEAST_MEAT)
        RebarItem.register(RebarItem::class.java, INDUSTRIAL_EXPLOSIVE)
        EndfieldIndustryPages.PRODUCTS.addItem(INDUSTRIAL_EXPLOSIVE)

        // 零件
        RebarItem.register(RebarItem::class.java, HIGH_CRYSTAL_PART)
        EndfieldIndustryPages.COMPONENTS.addItem(HIGH_CRYSTAL_PART)
        RebarItem.register(RebarItem::class.java, STEEL_PART)
        EndfieldIndustryPages.COMPONENTS.addItem(STEEL_PART)
        RebarItem.register(RebarItem::class.java, IRON_PART)
        EndfieldIndustryPages.COMPONENTS.addItem(IRON_PART)
        RebarItem.register(RebarItem::class.java, AMETHYST_PART)
        EndfieldIndustryPages.COMPONENTS.addItem(AMETHYST_PART)
        RebarItem.register(RebarItem::class.java, HIGH_CRYSTAL_FIBER)
        EndfieldIndustryPages.COMPONENTS.addItem(HIGH_CRYSTAL_FIBER)
        RebarItem.register(RebarItem::class.java, AMETHYST_FIBER)
        EndfieldIndustryPages.MATERIALS.addItem(AMETHYST_FIBER)
        RebarItem.register(RebarItem::class.java, HIGH_CRYSTAL_BOTTLE)
        EndfieldIndustryPages.COMPONENTS.addItem(HIGH_CRYSTAL_BOTTLE)
        RebarItem.register(RebarItem::class.java, STEEL_BOTTLE)
        EndfieldIndustryPages.COMPONENTS.addItem(STEEL_BOTTLE)
        RebarItem.register(RebarItem::class.java, AMETHYST_BOTTLE)
        EndfieldIndustryPages.COMPONENTS.addItem(AMETHYST_BOTTLE)
        RebarItem.register(RebarItem::class.java, BLUE_IRON_BOTTLE)
        EndfieldIndustryPages.COMPONENTS.addItem(BLUE_IRON_BOTTLE)
        RebarItem.register(RebarItem::class.java, AMETHYST_EQUIPMENT_PART)
        EndfieldIndustryPages.COMPONENTS.addItem(AMETHYST_EQUIPMENT_PART)
        RebarItem.register(RebarItem::class.java, BLUE_IRON_EQUIPMENT_PART)
        EndfieldIndustryPages.COMPONENTS.addItem(BLUE_IRON_EQUIPMENT_PART)
        RebarItem.register(RebarItem::class.java, HIGH_CRYSTAL_EQUIPMENT_PART)
        EndfieldIndustryPages.COMPONENTS.addItem(HIGH_CRYSTAL_EQUIPMENT_PART)
        RebarItem.register(RebarItem::class.java, XI_RANG_EQUIPMENT_PART)
        EndfieldIndustryPages.COMPONENTS.addItem(XI_RANG_EQUIPMENT_PART)

        // 药品
        RebarItem.register(RebarItem::class.java, LARGE_BUD_SPRAY)
        EndfieldIndustryPages.MEDICINES.addItem(LARGE_BUD_SPRAY)
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
        RebarItem.register(RebarItem::class.java, SELECT_CITRUS_DRAFT)
        EndfieldIndustryPages.MEDICINES.addItem(SELECT_CITRUS_DRAFT)
        RebarItem.register(RebarItem::class.java, SELECT_BUCKWHEAT_RESTORATION_AGENT)
        EndfieldIndustryPages.MEDICINES.addItem(SELECT_BUCKWHEAT_RESTORATION_AGENT)
        RebarItem.register(RebarItem::class.java, LARGE_BUCKWHEAT_RESTORATION_AGENT)
        EndfieldIndustryPages.MEDICINES.addItem(LARGE_BUCKWHEAT_RESTORATION_AGENT)

        // 产品
        RebarItem.register(RebarItem::class.java, HIGH_CAP_VALLEY_BATTERY)
        EndfieldIndustryPages.PRODUCTS.addItem(HIGH_CAP_VALLEY_BATTERY)
        RebarItem.register(RebarItem::class.java, MID_CAP_VALLEY_BATTERY)
        EndfieldIndustryPages.PRODUCTS.addItem(MID_CAP_VALLEY_BATTERY)
        RebarItem.register(RebarItem::class.java, LOW_CAP_VALLEY_BATTERY)
        EndfieldIndustryPages.PRODUCTS.addItem(LOW_CAP_VALLEY_BATTERY)
        RebarItem.register(RebarItem::class.java, LOW_CAP_WULING_BATTERY)
        EndfieldIndustryPages.PRODUCTS.addItem(LOW_CAP_WULING_BATTERY)
        RebarItem.register(RebarItem::class.java, VALLEY_DISPATCH_TICKET)
        EndfieldIndustryPages.PRODUCTS.addItem(VALLEY_DISPATCH_TICKET)
        RebarItem.register(RebarItem::class.java, GOLD_TICKET)
        EndfieldIndustryPages.PRODUCTS.addItem(GOLD_TICKET)
    }
}
