package top.mc506lw.rebar.endfield_industry

import io.github.pylonmc.rebar.content.guide.RebarGuide
import io.github.pylonmc.rebar.guide.pages.base.SimpleStaticGuidePage
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object EndfieldIndustryPages {

    val ENDFIELD_INDUSTRY = SimpleStaticGuidePage(EndfieldIndustryKeys.key("endfield_industry"))

    val PLANTS = SimpleStaticGuidePage(EndfieldIndustryKeys.key("plants"))
    val MATERIALS = SimpleStaticGuidePage(EndfieldIndustryKeys.key("materials"))
    val COMPONENTS = SimpleStaticGuidePage(EndfieldIndustryKeys.key("components"))
    val MACHINES = SimpleStaticGuidePage(EndfieldIndustryKeys.key("machines"))
    val MEDICINES = SimpleStaticGuidePage(EndfieldIndustryKeys.key("medicines"))
    val PRODUCTS = SimpleStaticGuidePage(EndfieldIndustryKeys.key("products"))

    val POWER_SYSTEM = SimpleStaticGuidePage(EndfieldIndustryKeys.key("power_system"))

    fun initialize() {
        RebarGuide.rootPage.addPage(ItemStack(Material.BLAST_FURNACE), ENDFIELD_INDUSTRY)

        ENDFIELD_INDUSTRY.addPage(ItemStack(Material.POPPY), PLANTS)
        ENDFIELD_INDUSTRY.addPage(ItemStack(Material.IRON_INGOT), MATERIALS)
        ENDFIELD_INDUSTRY.addPage(ItemStack(Material.QUARTZ), COMPONENTS)
        ENDFIELD_INDUSTRY.addPage(ItemStack(Material.FURNACE), MACHINES)
        ENDFIELD_INDUSTRY.addPage(ItemStack(Material.POTION), MEDICINES)
        ENDFIELD_INDUSTRY.addPage(ItemStack(Material.REDSTONE_BLOCK), PRODUCTS)
        ENDFIELD_INDUSTRY.addPage(ItemStack(Material.REDSTONE), POWER_SYSTEM)
    }
}
