package top.mc506lw.rebar.endfield_industry

import org.bukkit.NamespacedKey

/**
 * 插件内全部内容注册键（NamespaceKey）的唯一出处。
 *
 * 所有物品、方块、配方类型、手册页面的键都必须在此集中定义，
 * 其余模块一律通过引用本对象的常量使用，避免硬编码字符串。
 */
object EndfieldIndustryKeys {

    /** 生成命名空间键，内容 id 形如 `endfield-industry:<key>`。 */
    @JvmStatic
    fun key(key: String): NamespacedKey = EndfieldIndustry.key(key)

    // region 配方类型

    /** 种子提取机配方类型 */
    @JvmField
    val SEED_EXTRACTOR_RECIPE: NamespacedKey = key("seed_extractor")

    /** 种植机配方类型 */
    @JvmField
    val PLANTER_RECIPE: NamespacedKey = key("planter")

    // endregion

    // region 机器

    /** 种子提取机（多方块结构） */
    @JvmField
    val SEED_EXTRACTOR: NamespacedKey = key("seed_extractor")

    /** 种植机（多方块结构） */
    @JvmField
    val PLANTER: NamespacedKey = key("planter")

    /** 继电器底座 */
    @JvmField
    val RELAY_BASE: NamespacedKey = key("relay_base")

    /** 继电器扩散器 */
    @JvmField
    val RELAY_DIFFUSER: NamespacedKey = key("relay_diffuser")

    /** 发电站底座 */
    @JvmField
    val POWER_STATION_BASE: NamespacedKey = key("power_station_base")

    /** 发电站发射器 */
    @JvmField
    val POWER_STATION_EMITTER: NamespacedKey = key("power_station_emitter")

    /** 协议核心控制器 */
    @JvmField
    val PROTOCOL_CORE_CONTROLLER: NamespacedKey = key("protocol_core_controller")

    // endregion

    // region 植物与作物

    /** 荞麦 */
    @JvmField
    val BUCKWHEAT: NamespacedKey = key("buckwheat")

    /** 灰麦 */
    @JvmField
    val GRAY_WHEAT: NamespacedKey = key("gray_wheat")

    /** 酮丛 */
    @JvmField
    val KETONE_BUSH: NamespacedKey = key("ketone_bush")

    /** 银柑橘 */
    @JvmField
    val CITRUS: NamespacedKey = key("citrus")

    /** 烬草 */
    @JvmField
    val JIN_GRASS: NamespacedKey = key("jin_grass")

    /** 芽针 */
    @JvmField
    val BUD_NEEDLE: NamespacedKey = key("bud_needle")

    /** 金稻 */
    @JvmField
    val GOLD_STONE_RICE: NamespacedKey = key("gold_stone_rice")

    /** 玉叶参 */
    @JvmField
    val JADE_LEAF_GINSENG: NamespacedKey = key("jade_leaf_ginseng")

    /** 沙叶 */
    @JvmField
    val SAND_LEAF: NamespacedKey = key("sand_leaf")

    /** 火荞麦 */
    @JvmField
    val FIRE_BUCKWHEAT: NamespacedKey = key("fire_buckwheat")

    /** 暗银柑橘 */
    @JvmField
    val DARK_SILVER_CITRUS: NamespacedKey = key("dark_silver_citrus")

    /** 木材 */
    @JvmField
    val LOG: NamespacedKey = key("log")

    // endregion

    // region 种子

    /** 荞麦种子 */
    @JvmField
    val BUCKWHEAT_SEED: NamespacedKey = key("buckwheat_seed")

    /** 灰麦种子 */
    @JvmField
    val GRAY_WHEAT_SEED: NamespacedKey = key("gray_wheat_seed")

    /** 酮树种子 */
    @JvmField
    val KETONE_TREE_SEED: NamespacedKey = key("ketone_tree_seed")

    /** 柑橘种子 */
    @JvmField
    val CITRUS_SEED: NamespacedKey = key("citrus_seed")

    /** 烬草种子 */
    @JvmField
    val JIN_GRASS_SEED: NamespacedKey = key("jin_grass_seed")

    /** 芽针种子 */
    @JvmField
    val BUD_NEEDLE_SEED: NamespacedKey = key("bud_needle_seed")

    /** 金稻种子 */
    @JvmField
    val GOLD_STONE_RICE_SEED: NamespacedKey = key("gold_stone_rice_seed")

    /** 玉叶参种子 */
    @JvmField
    val JADE_LEAF_GINSENG_SEED: NamespacedKey = key("jade_leaf_ginseng_seed")

    /** 沙叶种子 */
    @JvmField
    val SAND_LEAF_SEED: NamespacedKey = key("sand_leaf_seed")

    // endregion

    // region 菌类与特殊植物

    /** 浅红柱菇 */
    @JvmField
    val LIGHT_RED_PILLAR_FUNGUS: NamespacedKey = key("light_red_pillar_fungus")

    /** 中红柱菇 */
    @JvmField
    val MID_RED_PILLAR_FUNGUS: NamespacedKey = key("mid_red_pillar_fungus")

    /** 重红柱菇 */
    @JvmField
    val HEAVY_RED_PILLAR_FUNGUS: NamespacedKey = key("heavy_red_pillar_fungus")

    /** 血菇 */
    @JvmField
    val BLOOD_FUNGUS: NamespacedKey = key("blood_fungus")

    /** 星门菇 */
    @JvmField
    val STAR_GATE_FUNGUS: NamespacedKey = key("star_gate_fungus")

    /** 晶化锯齿叶 */
    @JvmField
    val CRYSTALIZED_TOOTHED_LEAF: NamespacedKey = key("crystalized_toothed_leaf")

    /** 纯晶锯齿叶 */
    @JvmField
    val PURE_CRYSTAL_TOOTHED_LEAF: NamespacedKey = key("pure_crystal_toothed_leaf")

    /** 终极晶锯齿叶 */
    @JvmField
    val ULTIMATE_CRYSTAL_TOOTHED_LEAF: NamespacedKey = key("ultimate_crystal_toothed_leaf")

    /** 绒烬草 */
    @JvmField
    val FLUFFY_JIN_GRASS: NamespacedKey = key("fluffy_jin_grass")

    /** 棘芽针 */
    @JvmField
    val THORN_BUD_NEEDLE: NamespacedKey = key("thorn_bud_needle")

    // endregion

    // region 材料（块）

    /** 稳定碳块 */
    @JvmField
    val STABLE_CARBON_BLOCK: NamespacedKey = key("stable_carbon_block")

    /** 碳块 */
    @JvmField
    val CARBON_BLOCK: NamespacedKey = key("carbon_block")

    /** 钢块 */
    @JvmField
    val STEEL_BLOCK: NamespacedKey = key("steel_block")

    /** 蓝铁块 */
    @JvmField
    val BLUE_IRON_BLOCK: NamespacedKey = key("blue_iron_block")

    /** 致密结晶 */
    @JvmField
    val DENSE_CRYSTAL: NamespacedKey = key("dense_crystal")

    // endregion

    // region 材料（粉末）

    /** 沙叶粉 */
    @JvmField
    val SAND_LEAF_POWDER: NamespacedKey = key("sand_leaf_powder")

    /** 致密源石粉末 */
    @JvmField
    val DENSE_ORIGINIUM_POWDER: NamespacedKey = key("dense_originium_powder")

    /** 致密结晶粉末 */
    @JvmField
    val DENSE_CRYSTAL_POWDER: NamespacedKey = key("dense_crystal_powder")

    /** 高纯结晶粉末 */
    @JvmField
    val HIGH_CRYSTAL_POWDER: NamespacedKey = key("high_crystal_powder")

    /** 致密蓝铁粉末 */
    @JvmField
    val DENSE_BLUE_IRON_POWDER: NamespacedKey = key("dense_blue_iron_powder")

    /** 致密碳粉 */
    @JvmField
    val DENSE_CARBON_POWDER: NamespacedKey = key("dense_carbon_powder")

    /** 源石粉末 */
    @JvmField
    val ORIGINIUM_POWDER: NamespacedKey = key("originium_powder")

    /** 蓝铁粉末 */
    @JvmField
    val BLUE_IRON_POWDER: NamespacedKey = key("blue_iron_powder")

    /** 紫晶粉末 */
    @JvmField
    val AMETHYST_POWDER: NamespacedKey = key("amethyst_powder")

    /** 碳粉 */
    @JvmField
    val CARBON_POWDER: NamespacedKey = key("carbon_powder")

    /** 酮丛粉末 */
    @JvmField
    val KETONE_BUSH_POWDER: NamespacedKey = key("ketone_bush_powder")

    /** 荞麦粉末 */
    @JvmField
    val BUCKWHEAT_POWDER: NamespacedKey = key("buckwheat_powder")

    /** 柑橘粉末 */
    @JvmField
    val CITRUS_POWDER: NamespacedKey = key("citrus_powder")

    /** 烬草粉末 */
    @JvmField
    val JIN_GRASS_POWDER: NamespacedKey = key("jin_grass_powder")

    /** 芽针粉末 */
    @JvmField
    val BUD_NEEDLE_POWDER: NamespacedKey = key("bud_needle_powder")

    /** 绒烬草粉末 */
    @JvmField
    val FLUFFY_JIN_GRASS_POWDER: NamespacedKey = key("fluffy_jin_grass_powder")

    /** 棘芽针粉末 */
    @JvmField
    val THORN_BUD_NEEDLE_POWDER: NamespacedKey = key("thorn_bud_needle_powder")

    /** 草籽粉末 */
    @JvmField
    val GRASS_SEED_POWDER: NamespacedKey = key("grass_seed_powder")

    /** 精磨荞麦粉 */
    @JvmField
    val FINE_GROUND_BUCKWHEAT_POWDER: NamespacedKey = key("fine_ground_buckwheat_powder")

    /** 精磨柑橘粉 */
    @JvmField
    val FINE_GROUND_CITRUS_POWDER: NamespacedKey = key("fine_ground_citrus_powder")

    /** 晶壳粉末 */
    @JvmField
    val CRYSTAL_SHELL_POWDER: NamespacedKey = key("crystal_shell_powder")

    // endregion

    // region 材料（矿石与天然物）

    /** 蓝铁矿石 */
    @JvmField
    val BLUE_IRON_ORE: NamespacedKey = key("blue_iron_ore")

    /** 紫晶矿石 */
    @JvmField
    val AMETHYST_ORE: NamespacedKey = key("amethyst_ore")

    /** 源石矿石 */
    @JvmField
    val ORIGINIUM_ORE: NamespacedKey = key("originium_ore")

    /** 浅暗石 */
    @JvmField
    val LIGHT_DIM_STONE: NamespacedKey = key("light_dim_stone")

    /** 中暗石 */
    @JvmField
    val MID_DIM_STONE: NamespacedKey = key("mid_dim_stone")

    /** 硬香石 */
    @JvmField
    val HARD_FRAGRANT_STONE: NamespacedKey = key("hard_fragrant_stone")

    /** 高能香石 */
    @JvmField
    val HIGH_ENERGY_FRAGRANT_STONE: NamespacedKey = key("high_energy_fragrant_stone")

    /** 夕壤 */
    @JvmField
    val XI_RANG: NamespacedKey = key("xi_rang")

    /** 嵌晶翡翠 */
    @JvmField
    val EMBEDDED_CRYSTAL_JADE: NamespacedKey = key("embedded_crystal_jade")

    /** 晶壳 */
    @JvmField
    val CRYSTAL_SHELL: NamespacedKey = key("crystal_shell")

    /** 软骨碎片 */
    @JvmField
    val CARTILAGE_FRAGMENT: NamespacedKey = key("cartilage_fragment")

    /** 荧光壳虫 */
    @JvmField
    val GLOW_SHELL_BUG: NamespacedKey = key("glow_shell_bug")

    /** 兽肉 */
    @JvmField
    val BEAST_MEAT: NamespacedKey = key("beast_meat")

    /** 工业炸药 */
    @JvmField
    val INDUSTRIAL_EXPLOSIVE: NamespacedKey = key("industrial_explosive")

    // endregion

    // region 零件

    /** 高结晶零件 */
    @JvmField
    val HIGH_CRYSTAL_PART: NamespacedKey = key("high_crystal_part")

    /** 钢制零件 */
    @JvmField
    val STEEL_PART: NamespacedKey = key("steel_part")

    /** 铁制零件 */
    @JvmField
    val IRON_PART: NamespacedKey = key("iron_part")

    /** 紫晶零件 */
    @JvmField
    val AMETHYST_PART: NamespacedKey = key("amethyst_part")

    /** 高结晶纤维 */
    @JvmField
    val HIGH_CRYSTAL_FIBER: NamespacedKey = key("high_crystal_fiber")

    /** 紫晶纤维 */
    @JvmField
    val AMETHYST_FIBER: NamespacedKey = key("amethyst_fiber")

    /** 高结晶瓶 */
    @JvmField
    val HIGH_CRYSTAL_BOTTLE: NamespacedKey = key("high_crystal_bottle")

    /** 钢瓶 */
    @JvmField
    val STEEL_BOTTLE: NamespacedKey = key("steel_bottle")

    /** 紫晶瓶 */
    @JvmField
    val AMETHYST_BOTTLE: NamespacedKey = key("amethyst_bottle")

    /** 蓝铁瓶 */
    @JvmField
    val BLUE_IRON_BOTTLE: NamespacedKey = key("blue_iron_bottle")

    /** 紫晶装备零件 */
    @JvmField
    val AMETHYST_EQUIPMENT_PART: NamespacedKey = key("amethyst_equipment_part")

    /** 蓝铁装备零件 */
    @JvmField
    val BLUE_IRON_EQUIPMENT_PART: NamespacedKey = key("blue_iron_equipment_part")

    /** 高结晶装备零件 */
    @JvmField
    val HIGH_CRYSTAL_EQUIPMENT_PART: NamespacedKey = key("high_crystal_equipment_part")

    /** 夕壤装备零件 */
    @JvmField
    val XI_RANG_EQUIPMENT_PART: NamespacedKey = key("xi_rang_equipment_part")

    // endregion

    // region 药品

    /** 大芽喷剂 */
    @JvmField
    val LARGE_BUD_SPRAY: NamespacedKey = key("large_bud_spray")

    /** 荞麦药用粉末 */
    @JvmField
    val BUCKWHEAT_MEDICINE_POWDER: NamespacedKey = key("buckwheat_medicine_powder")

    /** 柑橘药剂 */
    @JvmField
    val CITRUS_DRAFT: NamespacedKey = key("citrus_draft")

    /** 荞麦胶囊 */
    @JvmField
    val BUCKWHEAT_CAPSULE: NamespacedKey = key("buckwheat_capsule")

    /** 优质荞麦胶囊 */
    @JvmField
    val QUALITY_BUCKWHEAT_CAPSULE: NamespacedKey = key("quality_buckwheat_capsule")

    /** 柑橘罐 */
    @JvmField
    val CITRUS_CAN: NamespacedKey = key("citrus_can")

    /** 优质柑橘罐 */
    @JvmField
    val QUALITY_CITRUS_CAN: NamespacedKey = key("quality_citrus_can")

    /** 小型荞麦复原剂 */
    @JvmField
    val SMALL_BUCKWHEAT_RESTORATION_AGENT: NamespacedKey = key("small_buckwheat_restoration_agent")

    /** 小型柑橘药剂 */
    @JvmField
    val SMALL_CITRUS_DRAFT: NamespacedKey = key("small_citrus_draft")

    /** 精选荞麦胶囊 */
    @JvmField
    val SELECT_BUCKWHEAT_CAPSULE: NamespacedKey = key("select_buckwheat_capsule")

    /** 精选柑橘罐 */
    @JvmField
    val SELECT_CITRUS_CAN: NamespacedKey = key("select_citrus_can")

    /** 精选柑橘药剂 */
    @JvmField
    val SELECT_CITRUS_DRAFT: NamespacedKey = key("select_citrus_draft")

    /** 精选荞麦复原剂 */
    @JvmField
    val SELECT_BUCKWHEAT_RESTORATION_AGENT: NamespacedKey = key("select_buckwheat_restoration_agent")

    /** 大型荞麦复原剂 */
    @JvmField
    val LARGE_BUCKWHEAT_RESTORATION_AGENT: NamespacedKey = key("large_buckwheat_restoration_agent")

    // endregion

    // region 产品

    /** 高容量谷地电池 */
    @JvmField
    val HIGH_CAP_VALLEY_BATTERY: NamespacedKey = key("high_cap_valley_battery")

    /** 中容量谷地电池 */
    @JvmField
    val MID_CAP_VALLEY_BATTERY: NamespacedKey = key("mid_cap_valley_battery")

    /** 低容量谷地电池 */
    @JvmField
    val LOW_CAP_VALLEY_BATTERY: NamespacedKey = key("low_cap_valley_battery")

    /** 低容量武陵电池 */
    @JvmField
    val LOW_CAP_WULING_BATTERY: NamespacedKey = key("low_cap_wuling_battery")

    /** 谷地调度券 */
    @JvmField
    val VALLEY_DISPATCH_TICKET: NamespacedKey = key("valley_dispatch_ticket")

    /** 金票 */
    @JvmField
    val GOLD_TICKET: NamespacedKey = key("gold_ticket")

    // endregion

    // region 电力系统

    /** 电线显示实体标记 */
    @JvmField
    val WIRE_DISPLAY: NamespacedKey = key("wire_display")

    // endregion

    // region 手册页面

    /** 主页面 */
    @JvmField
    val PAGE_ENDFIELD_INDUSTRY: NamespacedKey = key("endfield_industry")

    /** 植物页 */
    @JvmField
    val PAGE_PLANTS: NamespacedKey = key("plants")

    /** 材料页 */
    @JvmField
    val PAGE_MATERIALS: NamespacedKey = key("materials")

    /** 零件页 */
    @JvmField
    val PAGE_COMPONENTS: NamespacedKey = key("components")

    /** 机器页 */
    @JvmField
    val PAGE_MACHINES: NamespacedKey = key("machines")

    /** 药品页 */
    @JvmField
    val PAGE_MEDICINES: NamespacedKey = key("medicines")

    /** 产品页 */
    @JvmField
    val PAGE_PRODUCTS: NamespacedKey = key("products")

    /** 电力系统页 */
    @JvmField
    val PAGE_POWER_SYSTEM: NamespacedKey = key("power_system")

    // endregion
}
