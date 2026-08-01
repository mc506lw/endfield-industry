package top.mc506lw.rebar.endfield_industry.recipes

/**
 * 配方类型注册入口。
 *
 * 各配方类型在伴生对象初始化时完成 [register]，此模块负责在插件启动时
 * 强制触发伴生对象初始化；配方内容由 Rebar 启动后从 `recipes/` 目录自动加载。
 */
object EndfieldIndustryRecipeTypes {

    @JvmStatic
    fun initialize() {
        PlanterRecipe.RECIPE_TYPE
        SeedExtractorRecipe.RECIPE_TYPE
    }
}
