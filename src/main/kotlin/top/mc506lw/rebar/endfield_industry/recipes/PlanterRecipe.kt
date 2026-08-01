package top.mc506lw.rebar.endfield_industry.recipes

import io.github.pylonmc.rebar.config.ConfigSection
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter
import io.github.pylonmc.rebar.guide.button.ItemButton
import io.github.pylonmc.rebar.item.ItemTypeWrapper
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import io.github.pylonmc.rebar.recipe.ConfigurableRecipeType
import io.github.pylonmc.rebar.recipe.RebarRecipe
import io.github.pylonmc.rebar.recipe.ingredient.FluidOrItem
import io.github.pylonmc.rebar.recipe.ingredient.FluidOrItemChoice
import io.github.pylonmc.rebar.recipe.ingredient.ItemChoice
import io.github.pylonmc.rebar.util.gui.GuiItems
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import top.mc506lw.rebar.endfield_industry.EndfieldIndustryKeys
import xyz.xenondevs.invui.gui.Gui

/**
 * 种植机配方：种子 → 作物。
 *
 * 配方配置由 Rebar 从插件资源的 `recipes/endfield-industry/planter.yml` 自动加载
 * （数据目录同名文件可覆盖，便于服主调整）。
 */
class PlanterRecipe(
    private val recipeKey: NamespacedKey,
    val input: ItemChoice,
    val output: ItemStack,
) : RebarRecipe {

    override val inputs: List<FluidOrItemChoice> = listOf(input)

    override val results: List<FluidOrItem> = listOf(FluidOrItem.Item(output))

    override fun getKey(): NamespacedKey = recipeKey

    override fun display(): Gui {
        return Gui.builder()
            .setStructure(
                "# # # # # # # # #",
                "# # # # # # # # #",
                "# p # # i c o # #",
                "# # # # # # # # #",
                "# # # # # # # # #"
            )
            .addIngredient('#', GuiItems.backgroundBlack())
            .addIngredient('p', ItemButton.of(
                ItemStackBuilder.of(Material.SMOKER)
                    .name(Component.translatable("endfield-industry.gui.planter.title"))
                    .build()
            ))
            .addIngredient('i', ItemButton.of(input))
            .addIngredient('c', GuiItems.progressCyclingItem(20,
                ItemStackBuilder.of(Material.CLOCK)
                    .name(Component.translatable("endfield-industry.gui.processing"))
            ))
            .addIngredient('o', ItemButton.of(FluidOrItem.Item(output)))
            .build()
    }

    companion object {
        @JvmField
        val RECIPE_TYPE: ConfigurableRecipeType<PlanterRecipe> =
            object : ConfigurableRecipeType<PlanterRecipe>(EndfieldIndustryKeys.PLANTER_RECIPE) {
                override fun loadRecipe(key: NamespacedKey, section: ConfigSection): PlanterRecipe {
                    val input = section.getOrThrow("input", ConfigAdapter.ITEM_CHOICE)
                    val outputSection = section.getSection("output")
                    val output = if (outputSection != null) {
                        val itemKey = outputSection.getOrThrow("item", ConfigAdapter.NAMESPACED_KEY)
                        val amount = outputSection.get("amount", ConfigAdapter.INTEGER, 1)
                        ItemTypeWrapper(itemKey).createItemStack(amount)
                    } else {
                        section.getOrThrow("output", ConfigAdapter.ITEM_STACK)
                    }
                    return PlanterRecipe(key, input, output)
                }
            }.apply { register() }
    }
}
