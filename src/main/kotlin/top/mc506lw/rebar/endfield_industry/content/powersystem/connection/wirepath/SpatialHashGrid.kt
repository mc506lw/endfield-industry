package top.mc506lw.rebar.endfield_industry.content.powersystem.connection.wirepath

import org.bukkit.Location
import org.bukkit.World
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.floor

class SpatialHashGrid(private val cellSize: Double = 0.5) {

    private val cells: MutableMap<String, MutableSet<PathPoint>> = ConcurrentHashMap()

    fun addPoint(point: PathPoint) {
        val key = getKey(point.x, point.z)
        cells.computeIfAbsent(key) { ConcurrentHashMap.newKeySet() }.add(point)
    }

    fun removePoint(point: PathPoint) {
        val key = getKey(point.x, point.z)
        cells[key]?.remove(point)
        if (cells[key]?.isEmpty() == true) {
            cells.remove(key)
        }
    }

    fun findNearby(x: Double, z: Double, radius: Double = cellSize): PathPoint? {
        val centerKey = getKey(x, z)
        val nearbyKeys = getNearbyKeys(centerKey)
        
        var closest: PathPoint? = null
        var closestDist = Double.MAX_VALUE
        
        for (key in nearbyKeys) {
            val points = cells[key] ?: continue
            for (point in points) {
                val dist = distance2D(x, z, point.x, point.z)
                if (dist <= radius && dist < closestDist) {
                    closest = point
                    closestDist = dist
                }
            }
        }
        
        return closest
    }

    fun findNearbyExcluding(x: Double, z: Double, excluding: PathPoint, radius: Double = cellSize): PathPoint? {
        val centerKey = getKey(x, z)
        val nearbyKeys = getNearbyKeys(centerKey)
        
        var closest: PathPoint? = null
        var closestDist = Double.MAX_VALUE
        
        for (key in nearbyKeys) {
            val points = cells[key] ?: continue
            for (point in points) {
                if (point == excluding) continue
                val dist = distance2D(x, z, point.x, point.z)
                if (dist <= radius && dist < closestDist) {
                    closest = point
                    closestDist = dist
                }
            }
        }
        
        return closest
    }

    fun getAllPoints(): Collection<PathPoint> {
        return cells.values.flatten()
    }

    fun clear() {
        cells.clear()
    }

    fun isEmpty(): Boolean = cells.isEmpty()

    private fun getKey(x: Double, z: Double): String {
        val cellX = floor(x / cellSize).toInt()
        val cellZ = floor(z / cellSize).toInt()
        return "$cellX:$cellZ"
    }

    private fun getNearbyKeys(centerKey: String): List<String> {
        val parts = centerKey.split(":")
        val cellX = parts[0].toInt()
        val cellZ = parts[1].toInt()
        
        return listOf(
            "$cellX:$cellZ",
            "${cellX - 1}:$cellZ",
            "${cellX + 1}:$cellZ",
            "$cellX:${cellZ - 1}",
            "$cellX:${cellZ + 1}",
            "${cellX - 1}:${cellZ - 1}",
            "${cellX - 1}:${cellZ + 1}",
            "${cellX + 1}:${cellZ - 1}",
            "${cellX + 1}:${cellZ + 1}"
        )
    }

    private fun distance2D(x1: Double, z1: Double, x2: Double, z2: Double): Double {
        val dx = x1 - x2
        val dz = z1 - z2
        return kotlin.math.sqrt(dx * dx + dz * dz)
    }
}
