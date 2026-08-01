package top.mc506lw.rebar.endfield_industry

import io.github.pylonmc.rebar.item.ItemTypeWrapper
import io.github.pylonmc.rebar.recipe.RecipeType
import io.github.pylonmc.rebar.recipe.ingredient.FluidOrItem
import io.github.pylonmc.rebar.recipe.ingredient.ItemChoice
import io.github.pylonmc.rebar.recipe.vanilla.CraftingRecipeShape
import io.github.pylonmc.rebar.recipe.vanilla.ShapedRebarRecipe
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.recipe.CraftingBookCategory

/**
 * 机器的工作台合成配方（原版配方类型，由 Rebar 托管注册到 Bukkit）。
 */
object EndfieldIndustryRecipes {

    fun initialize() {
        shaped(EndfieldIndustryKeys.SEED_EXTRACTOR, EndfieldIndustryItems.SEED_EXTRACTOR,
            listOf("III", "IAI", "III"),
            mapOf('I' to Material.IRON_BLOCK, 'A' to Material.ANVIL))
        shaped(EndfieldIndustryKeys.PLANTER, EndfieldIndustryItems.PLANTER,
            listOf("III", "IDI", "III"),
            mapOf('I' to Material.IRON_BLOCK, 'D' to Material.DIRT))
        shaped(EndfieldIndustryKeys.RELAY_BASE, EndfieldIndustryItems.RELAY_BASE,
            listOf("III", "IRI", "III"),
            mapOf('I' to Material.IRON_BLOCK, 'R' to Material.REPEATER))
        shaped(EndfieldIndustryKeys.RELAY_DIFFUSER, EndfieldIndustryItems.RELAY_DIFFUSER,
            listOf("IRI"),
            mapOf('I' to Material.IRON_BARS, 'R' to Material.REPEATER))
        shaped(EndfieldIndustryKeys.POWER_STATION_BASE, EndfieldIndustryItems.POWER_STATION_BASE,
            listOf("III", "IRI", "III"),
            mapOf('I' to Material.IRON_BLOCK, 'R' to Material.REDSTONE))
        shaped(EndfieldIndustryKeys.POWER_STATION_EMITTER, EndfieldIndustryItems.POWER_STATION_EMITTER,
            listOf("IRI"),
            mapOf('I' to Material.IRON_BARS, 'R' to Material.REDSTONE))
        shaped(EndfieldIndustryKeys.PROTOCOL_CORE_CONTROLLER, EndfieldIndustryItems.PROTOCOL_CORE_CONTROLLER,
            listOf("III", "IDI", "III"),
            mapOf('I' to Material.IRON_BLOCK, 'D' to Material.DIAMOND_BLOCK))
    }

    private fun shaped(
        key: NamespacedKey,
        result: ItemStack,
        pattern: List<String>,
        ingredients: Map<Char, Material>,
    ) {
        RecipeType.VANILLA_SHAPED.addRecipe(
            ShapedRebarRecipe(
                CraftingRecipeShape.of(
                    ingredients.mapValuesTo(mutableMapOf<Char, ItemChoice?>()) { (_, material) ->
                        ItemChoice.fuzzy(ItemTypeWrapper(material))
                    },
                    pattern.toMutableList()
                ),
                FluidOrItem.Item(result),
                CraftingBookCategory.MISC,
                "",
                key
            )
        )
    }
}
