package top.mc506lw.rebar.endfield_industry.content.powersystem.connection

import io.github.pylonmc.rebar.entity.display.ItemDisplayBuilder
import io.github.pylonmc.rebar.entity.display.transform.LineBuilder
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ItemDisplay
import org.joml.Vector3d

class WireDisplay private constructor(
    private val entity: ItemDisplay
) {

    companion object {
        private const val WIRE_THICKNESS = 0.08f

        fun create(start: Location, end: Location): WireDisplay? {
            if (start.world != end.world) return null
            
            val world = start.world ?: return null
            val midX = (start.x + end.x) / 2.0
            val midY = (start.y + end.y) / 2.0
            val midZ = (start.z + end.z) / 2.0
            
            val midPoint = Location(world, midX, midY, midZ)
            
            val halfDx = (end.x - start.x) / 2.0
            val halfDy = (end.y - start.y) / 2.0
            val halfDz = (end.z - start.z) / 2.0
            
            val display = ItemDisplayBuilder()
                .material(Material.YELLOW_STAINED_GLASS)
                .transformation(
                    LineBuilder()
                        .from(Vector3d(-halfDx, -halfDy, -halfDz))
                        .to(Vector3d(halfDx, halfDy, halfDz))
                        .thickness(WIRE_THICKNESS)
                        .build()
                )
                .build(midPoint)
            
            display.isPersistent = false
            display.setGravity(false)
            display.isSilent = true
            
            return WireDisplay(display)
        }
    }

    fun remove() {
        if (entity.isValid) {
            entity.remove()
        }
    }
    
    fun isValid(): Boolean = entity.isValid
}
