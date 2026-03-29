package top.mc506lw.rebar.endfield_industry.content.powersystem.util

import org.bukkit.Location
import org.bukkit.block.Block

object DistanceCalculator {

    @JvmStatic
    fun calculateDistance(from: Location, to: Location): PathResult {
        if (from.world != to.world) {
            return PathResult(-1, emptyList())
        }

        return calculateStraightLineDistance(from, to)
    }

    private fun calculateStraightLineDistance(from: Location, to: Location): PathResult {
        val blocks = mutableListOf<Block>()
        val distance = Math.round(from.distance(to)).toInt()

        val dx = Integer.compare(to.blockX, from.blockX)
        val dy = Integer.compare(to.blockY, from.blockY)
        val dz = Integer.compare(to.blockZ, from.blockZ)

        var x = from.blockX
        var y = from.blockY
        var z = from.blockZ

        while (x != to.blockX || y != to.blockY || z != to.blockZ) {
            if (x != to.blockX) x += dx
            else if (y != to.blockY) y += dy
            else if (z != to.blockZ) z += dz

            blocks.add(from.world!!.getBlockAt(x, y, z))
        }

        return PathResult(distance, blocks)
    }

    data class PathResult(val distance: Int, val path: List<Block>)
}
