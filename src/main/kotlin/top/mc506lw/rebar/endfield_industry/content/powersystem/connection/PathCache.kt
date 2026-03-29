package top.mc506lw.rebar.endfield_industry.content.powersystem.connection

import org.bukkit.Location
import org.bukkit.block.Block

class PathCache {

    private val cache: MutableMap<String, CachedPath> = mutableMapOf()

    fun getCachedPath(from: Location, to: Location): CachedPath? {
        val key = generateKey(from, to)
        return cache[key]
    }

    fun cachePath(from: Location, to: Location, path: List<Block>, distance: Int) {
        val key = generateKey(from, to)
        cache[key] = CachedPath(path, distance)
    }

    fun clearCache() {
        cache.clear()
    }

    fun invalidatePath(from: Location, to: Location) {
        val key = generateKey(from, to)
        cache.remove(key)
    }

    private fun generateKey(from: Location, to: Location): String {
        return "${from.blockX},${from.blockY},${from.blockZ}-${to.blockX},${to.blockY},${to.blockZ}"
    }

    data class CachedPath(val path: List<Block>, val distance: Int)
}
