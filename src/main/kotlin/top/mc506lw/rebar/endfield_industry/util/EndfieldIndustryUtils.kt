package top.mc506lw.rebar.endfield_industry.util

import org.bukkit.NamespacedKey
import top.mc506lw.rebar.endfield_industry.EndfieldIndustry

object EndfieldIndustryUtils {
    
    @JvmStatic
    fun key(key: String): NamespacedKey = EndfieldIndustry.key(key)
}
