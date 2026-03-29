package top.mc506lw.rebar.endfield_industry.recipes

object EndfieldIndustryRecipeTypes {

    @JvmStatic
    fun initialize() {
        PlanterRecipe.RECIPE_TYPE.register()
        SeedExtractorRecipe.RECIPE_TYPE.register()
        
        PlanterRecipe.loadRecipes()
        SeedExtractorRecipe.loadRecipes()
    }
}
