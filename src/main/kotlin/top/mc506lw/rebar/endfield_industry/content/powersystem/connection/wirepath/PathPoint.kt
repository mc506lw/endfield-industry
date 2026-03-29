package top.mc506lw.rebar.endfield_industry.content.powersystem.connection.wirepath

import org.bukkit.Location
import org.bukkit.World

class PathPoint(
    val x: Double,
    val y: Double,
    val z: Double,
    val world: World
) {
    var prev: PathPoint? = null
    var next: PathPoint? = null
    var marker: PathMarker? = null

    val location: Location
        get() = Location(world, x, y, z)

    fun distanceTo(other: PathPoint): Double {
        if (world != other.world) return Double.MAX_VALUE
        val dx = x - other.x
        val dy = y - other.y
        val dz = z - other.z
        return kotlin.math.sqrt(dx * dx + dy * dy + dz * dz)
    }

    fun distanceTo(loc: Location): Double {
        if (world != loc.world) return Double.MAX_VALUE
        val dx = x - loc.x
        val dy = y - loc.y
        val dz = z - loc.z
        return kotlin.math.sqrt(dx * dx + dy * dy + dz * dz)
    }

    fun distanceTo2D(loc: Location): Double {
        if (world != loc.world) return Double.MAX_VALUE
        val dx = x - loc.x
        val dz = z - loc.z
        return kotlin.math.sqrt(dx * dx + dz * dz)
    }

    fun isNear(other: PathPoint, threshold: Double = 0.5): Boolean {
        return distanceTo(other) <= threshold
    }

    fun isNear(loc: Location, threshold: Double = 0.5): Boolean {
        return distanceTo(loc) <= threshold
    }

    fun isNear2D(loc: Location, threshold: Double = 0.5): Boolean {
        return distanceTo2D(loc) <= threshold
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PathPoint) return false
        return x == other.x && y == other.y && z == other.z && world == other.world
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        result = 31 * result + world.hashCode()
        return result
    }

    override fun toString(): String {
        return "PathPoint(x=$x, y=$y, z=$z, world=${world.name})"
    }

    companion object {
        fun fromLocation(location: Location): PathPoint {
            return PathPoint(location.x, location.y, location.z, location.world!!)
        }
    }
}
