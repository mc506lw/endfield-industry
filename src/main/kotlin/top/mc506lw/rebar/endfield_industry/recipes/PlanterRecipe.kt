package top.mc506lw.rebar.endfield_industry.recipes

import io.github.pylonmc.rebar.config.Config
import io.github.pylonmc.rebar.config.ConfigSection
import io.github.pylonmc.rebar.config.Settings
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter
import io.github.pylonmc.rebar.guide.button.ItemButton
import io.github.pylonmc.rebar.item.ItemTypeWrapper
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import io.github.pylonmc.rebar.recipe.*
import io.github.pylonmc.rebar.util.gui.GuiItems
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import top.mc506lw.rebar.endfield_industry.EndfieldIndustryKeys
import xyz.xenondevs.invui.gui.Gui

class PlanterRecipe(
    private val recipeKey: NamespacedKey,
    val input: RecipeInput.Item,
    val output: ItemStack
) : RebarRecipe {

    companion object {
        @JvmStatic
        val RECIPE_TYPE = RecipeType<PlanterRecipe>(NamespacedKey("endfield-industry", "planter"))

        fun loadRecipes() {
            val config = Settings.get(EndfieldIndustryKeys.PLANTER)
            val recipesSection = config.getSection("recipes") ?: return
            
            for (recipeKey in recipesSection.keys) {
                val section = recipesSection.getSection(recipeKey) ?: continue
                val fullKey = NamespacedKey("endfield-industry", "planter_$recipeKey")
                
                val input = section.getOrThrow("input", ConfigAdapter.RECIPE_INPUT_ITEM)
                val outputSection = section.getSection("output")
                val output = if (outputSection != null) {
                    val itemKey = outputSection.getOrThrow("item", ConfigAdapter.NAMESPACED_KEY)
                    val amount = outputSection.get("amount", ConfigAdapter.INTEGER, 1)
                    val wrapper = ItemTypeWrapper(itemKey)
                    val itemStack = wrapper.createItemStack()
                    itemStack.amount = amount
                    itemStack
                } else {
                    section.getOrThrow("output", ConfigAdapter.ITEM_STACK)
                }
                
                RECIPE_TYPE.addRecipe(PlanterRecipe(fullKey, input, output))
            }
        }
    }

    override fun getKey(): NamespacedKey = recipeKey

    override val inputs: List<RecipeInput> = listOf(input)

    override val results: List<FluidOrItem> = listOf(FluidOrItem.of(output))

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
            .addIngredient('p', ItemButton.from(
                ItemStackBuilder.of(Material.SMOKER)
                    .name(net.kyori.adventure.text.Component.translatable("endfield-industry.gui.planter.title"))
                    .build()
            ))
            .addIngredient('i', ItemButton.from(input))
            .addIngredient('c', GuiItems.progressCyclingItem(20,
                ItemStackBuilder.of(Material.CLOCK)
                    .name(net.kyori.adventure.text.Component.translatable("endfield-industry.gui.processing"))
            ))
            .addIngredient('o', ItemButton.from(output))
            .build()
    }
}
