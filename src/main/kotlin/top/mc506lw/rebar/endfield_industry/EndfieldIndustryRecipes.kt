package top.mc506lw.rebar.endfield_industry

import io.github.pylonmc.rebar.recipe.RecipeType
import org.bukkit.Material
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory

object EndfieldIndustryRecipes {

    fun initialize() {
        registerSeedExtractorRecipe()
        registerPlanterRecipe()
        registerRelayBaseRecipe()
        registerRelayDiffuserRecipe()
        registerPowerStationBaseRecipe()
        registerPowerStationEmitterRecipe()
        registerProtocolCoreControllerRecipe()
    }

    private fun registerSeedExtractorRecipe() {
        val recipe = ShapedRecipe(EndfieldIndustryKeys.SEED_EXTRACTOR, EndfieldIndustryBlocks.SEED_EXTRACTOR_CONTROLLER.build())
            .shape("III", "IAI", "III")
            .setIngredient('I', Material.IRON_BLOCK)
            .setIngredient('A', Material.ANVIL)
        recipe.setCategory(CraftingBookCategory.MISC)
        RecipeType.VANILLA_SHAPED.addRecipe(recipe)
    }

    private fun registerPlanterRecipe() {
        val recipe = ShapedRecipe(EndfieldIndustryKeys.PLANTER, EndfieldIndustryBlocks.PLANTER_CONTROLLER.build())
            .shape("III", "IDI", "III")
            .setIngredient('I', Material.IRON_BLOCK)
            .setIngredient('D', Material.DIRT)
        recipe.setCategory(CraftingBookCategory.MISC)
        RecipeType.VANILLA_SHAPED.addRecipe(recipe)
    }

    private fun registerRelayBaseRecipe() {
        val recipe = ShapedRecipe(EndfieldIndustryKeys.RELAY_BASE, EndfieldIndustryBlocks.RELAY_BASE_BUILDER.build())
            .shape("III", "IRI", "III")
            .setIngredient('I', Material.IRON_BLOCK)
            .setIngredient('R', Material.REPEATER)
        recipe.setCategory(CraftingBookCategory.MISC)
        RecipeType.VANILLA_SHAPED.addRecipe(recipe)
    }

    private fun registerRelayDiffuserRecipe() {
        val recipe = ShapedRecipe(EndfieldIndustryKeys.RELAY_DIFFUSER, EndfieldIndustryBlocks.RELAY_DIFFUSER_BUILDER.build())
            .shape("IRI")
            .setIngredient('I', Material.IRON_BARS)
            .setIngredient('R', Material.REPEATER)
        recipe.setCategory(CraftingBookCategory.MISC)
        RecipeType.VANILLA_SHAPED.addRecipe(recipe)
    }

    private fun registerPowerStationBaseRecipe() {
        val recipe = ShapedRecipe(EndfieldIndustryKeys.POWER_STATION_BASE, EndfieldIndustryBlocks.POWER_STATION_BASE_BUILDER.build())
            .shape("III", "IRI", "III")
            .setIngredient('I', Material.IRON_BLOCK)
            .setIngredient('R', Material.REDSTONE)
        recipe.setCategory(CraftingBookCategory.MISC)
        RecipeType.VANILLA_SHAPED.addRecipe(recipe)
    }

    private fun registerPowerStationEmitterRecipe() {
        val recipe = ShapedRecipe(EndfieldIndustryKeys.POWER_STATION_EMITTER, EndfieldIndustryBlocks.POWER_STATION_EMITTER_BUILDER.build())
            .shape("IRI")
            .setIngredient('I', Material.IRON_BARS)
            .setIngredient('R', Material.REDSTONE)
        recipe.setCategory(CraftingBookCategory.MISC)
        RecipeType.VANILLA_SHAPED.addRecipe(recipe)
    }

    private fun registerProtocolCoreControllerRecipe() {
        val recipe = ShapedRecipe(EndfieldIndustryKeys.PROTOCOL_CORE_CONTROLLER, EndfieldIndustryBlocks.PROTOCOL_CORE_CONTROLLER_BUILDER.build())
            .shape("III", "IDI", "III")
            .setIngredient('I', Material.IRON_BLOCK)
            .setIngredient('D', Material.DIAMOND_BLOCK)
        recipe.setCategory(CraftingBookCategory.MISC)
        RecipeType.VANILLA_SHAPED.addRecipe(recipe)
    }
}
