package top.mc506lw.rebar.endfield_industry

import io.github.pylonmc.rebar.addon.RebarAddon
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin
import top.mc506lw.rebar.endfield_industry.content.cloudstorage.CloudStorage
import top.mc506lw.rebar.endfield_industry.content.cloudstorage.CloudStorageCommand
import top.mc506lw.rebar.endfield_industry.content.cloudstorage.CloudStorageGui
import top.mc506lw.rebar.endfield_industry.content.powersystem.PowerSystem
import top.mc506lw.rebar.endfield_industry.recipes.EndfieldIndustryRecipeTypes
import java.util.Locale

/**
 * Endfield Industry 插件主类。
 *
 * 同时作为 Rebar 插件附加（addon）注册，所有内容（物品、方块、配方、电力系统等）
 * 都以 [RebarAddon] 的形式由 Rebar 框架统一管理。
 */
class EndfieldIndustry : JavaPlugin(), RebarAddon {

    companion object {
        @JvmStatic
        lateinit var instance: EndfieldIndustry
            private set

        /**
         * 生成插件的命名空间键，内容 id 形如 `endfield-industry:<key>`。
         */
        @JvmStatic
        fun key(key: String): NamespacedKey = NamespacedKey(instance, key)
    }

    override fun onEnable() {
        instance = this

        registerWithRebar()
        saveDefaultConfig()

        PowerSystem.initialize()
        EndfieldIndustryItems.initialize()
        EndfieldIndustryBlocks.initialize()
        EndfieldIndustryRecipes.initialize()
        EndfieldIndustryRecipeTypes.initialize()
        EndfieldIndustryPages.initialize()
        CloudStorage.initialize()
        CloudStorageGui.initialize()
        CloudStorageCommand.register()
    }

    override fun onDisable() = Unit

    override val javaPlugin: JavaPlugin
        get() = this

    override val material: Material
        get() = Material.DEAD_BUSH

    override val defaultLanguage: Locale
        get() = Locale.forLanguageTag(config.getString("default-language", "en"))
}
