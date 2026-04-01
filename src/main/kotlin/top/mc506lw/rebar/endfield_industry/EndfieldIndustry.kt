package top.mc506lw.rebar.endfield_industry

import io.github.pylonmc.rebar.addon.RebarAddon
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin
import top.mc506lw.rebar.endfield_industry.content.powersystem.PowerSystem
import top.mc506lw.rebar.endfield_industry.content.cloudstorage.CloudStorage
import top.mc506lw.rebar.endfield_industry.content.cloudstorage.CloudStorageGui
import top.mc506lw.rebar.endfield_industry.content.cloudstorage.CloudStorageCommand
import top.mc506lw.rebar.endfield_industry.recipes.EndfieldIndustryRecipeTypes

class EndfieldIndustry : JavaPlugin(), RebarAddon {

    companion object {
        @JvmStatic
        lateinit var instance: EndfieldIndustry
            private set

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
        EndfieldIndustryEntities.initialize()
        EndfieldIndustryFluids.initialize()
        EndfieldIndustryRecipes.initialize()
        EndfieldIndustryRecipeTypes.initialize()
        EndfieldIndustryPages.initialize()
        
        CloudStorage.initialize()
        CloudStorageGui.initialize()
        CloudStorageCommand.register()
    }
    
    override fun onDisable() {
        PowerSystem.shutdown()
        CloudStorage.shutdown()
    }

    override val javaPlugin: JavaPlugin
        get() = instance

    override val languages: Set<java.util.Locale>
        get() = setOf(java.util.Locale.ENGLISH, java.util.Locale.SIMPLIFIED_CHINESE)

    override val material: Material
        get() = Material.DEAD_BUSH
}
