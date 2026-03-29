package top.mc506lw.rebar.endfield_industry.content.powersystem.connection.wirepath

import io.github.pylonmc.rebar.entity.display.ItemDisplayBuilder
import io.github.pylonmc.rebar.entity.display.transform.TransformBuilder
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ItemDisplay
import org.bukkit.util.Transformation
import org.joml.Vector3f

class PathMarker private constructor(
    private val entity: ItemDisplay,
    private var fromPoint: PathPoint?,
    private var toPoint: PathPoint?
) {

    companion object {
        private const val BASE_SCALE = 0.05f
        private const val HEIGHT_SCALE = 0.1f
        private const val PATH_LENGTH = 0.5f
        private const val VIEW_RANGE = 64.0f

        fun createStandalone(location: Location): PathMarker {
            val display = ItemDisplayBuilder()
                .material(Material.YELLOW_WOOL)
                .transformation(
                    TransformBuilder()
                        .scale(BASE_SCALE, BASE_SCALE * HEIGHT_SCALE, BASE_SCALE)
                        .buildForItemDisplay()
                )
                .viewRange(VIEW_RANGE)
                .build(location)
            
            display.isPersistent = false
            display.setGravity(false)
            display.isSilent = true
            
            return PathMarker(display, null, null)
        }

        fun createLine(from: PathPoint, to: PathPoint): PathMarker? {
            if (from.world != to.world) return null
            
            val world = from.world
            val midX = (from.x + to.x) / 2.0
            val midY = (from.y + to.y) / 2.0
            val midZ = (from.z + to.z) / 2.0
            
            val midPoint = Location(world, midX, midY, midZ)
            
            val dx = to.x - from.x
            val dy = to.y - from.y
            val dz = to.z - from.z
            val length = kotlin.math.sqrt(dx * dx + dy * dy + dz * dz)
            
            val scaleX = BASE_SCALE
            val scaleY = BASE_SCALE * HEIGHT_SCALE
            val scaleZ = PATH_LENGTH
            
            val display = ItemDisplayBuilder()
                .material(Material.YELLOW_WOOL)
                .transformation(
                    TransformBuilder()
                        .lookAlong(
                            Vector3f(-dx.toFloat(), -dy.toFloat(), -dz.toFloat()),
                            Vector3f(dx.toFloat(), dy.toFloat(), dz.toFloat())
                        )
                        .scale(scaleX, scaleY, scaleZ)
                        .buildForItemDisplay()
                )
                .viewRange(VIEW_RANGE)
                .build(midPoint)
            
            display.isPersistent = false
            display.setGravity(false)
            display.isSilent = true
            
            return PathMarker(display, from, to)
        }
    }

    fun updateLine(from: PathPoint, to: PathPoint) {
        if (entity.world != from.world || entity.world != to.world) return
        
        val dx = to.x - from.x
        val dy = to.y - from.y
        val dz = to.z - from.z
        val length = kotlin.math.sqrt(dx * dx + dy * dy + dz * dz)
        
        val midX = (from.x + to.x) / 2.0
        val midY = (from.y + to.y) / 2.0
        val midZ = (from.z + to.z) / 2.0
        
        val scaleX = BASE_SCALE
        val scaleY = BASE_SCALE * HEIGHT_SCALE
        val scaleZ = PATH_LENGTH
        
        entity.teleport(Location(entity.world, midX, midY, midZ))
        
        entity.setTransformationMatrix(
            TransformBuilder()
                .lookAlong(
                    Vector3f(-dx.toFloat(), -dy.toFloat(), -dz.toFloat()),
                    Vector3f(dx.toFloat(), dy.toFloat(), dz.toFloat())
                )
                .scale(scaleX, scaleY, scaleZ)
                .buildForItemDisplay()
        )
        
        fromPoint = from
        toPoint = to
    }

    fun remove() {
        if (entity.isValid) {
            entity.remove()
        }
    }

    fun isValid(): Boolean = entity.isValid

    fun getEntity(): ItemDisplay = entity
}
