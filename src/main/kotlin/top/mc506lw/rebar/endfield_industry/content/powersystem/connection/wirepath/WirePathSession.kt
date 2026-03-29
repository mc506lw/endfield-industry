package top.mc506lw.rebar.endfield_industry.content.powersystem.connection.wirepath

import io.github.pylonmc.rebar.i18n.RebarArgument
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.entity.Player
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerDevice
import java.util.UUID
import kotlin.math.abs

class WirePathSession(
    val player: Player,
    val startDevice: PowerDevice,
    private val maxDistance: Double
) {
    val playerId: UUID = player.uniqueId
    
    private var headPoint: PathPoint? = null
    private var tailPoint: PathPoint? = null
    private val spatialGrid: SpatialHashGrid = SpatialHashGrid(0.5)
    
    private var totalPathLength: Double = 0.0
    private var isActive: Boolean = true
    
    private val markers: MutableSet<PathMarker> = mutableSetOf()
    
    private var lastPlayerLocation: Location? = null

    val pathLength: Double
        get() = totalPathLength

    val isOverMaxDistance: Boolean
        get() = totalPathLength > maxDistance

    val remainingDistance: Double
        get() = (maxDistance - totalPathLength).coerceAtLeast(0.0)

    val isValid: Boolean
        get() = isActive && player.isOnline

    fun initialize() {
        val startLoc = startDevice.block.location.toCenterLocation()
        val startPoint = PathPoint.fromLocation(startLoc)
        
        headPoint = startPoint
        tailPoint = startPoint
        
        spatialGrid.addPoint(startPoint)
        
        val marker = PathMarker.createStandalone(startLoc)
        startPoint.marker = marker
        markers.add(marker)
        
        lastPlayerLocation = player.location
        
        updateActionBar()
    }

    fun updatePlayerPosition(): Boolean {
        if (!isValid) return false
        
        val currentLoc = player.location
        val lastLoc = lastPlayerLocation
        
        if (lastLoc != null && !hasSignificantMove(currentLoc, lastLoc)) {
            return !isOverMaxDistance
        }
        
        lastPlayerLocation = currentLoc.clone()
        
        val nearbyPoint = spatialGrid.findNearby(currentLoc.x, currentLoc.z, 0.5)
        
        if (nearbyPoint != null && nearbyPoint != tailPoint) {
            return handleBacktrack(nearbyPoint, currentLoc) && !isOverMaxDistance
        }
        
        return addNewPoint(currentLoc) && !isOverMaxDistance
    }

    private fun hasSignificantMove(current: Location, last: Location): Boolean {
        if (current.world != last.world) return true
        val dx = abs(current.x - last.x)
        val dy = abs(current.y - last.y)
        val dz = abs(current.z - last.z)
        return dx > 0.1 || dy > 0.1 || dz > 0.1
    }

    private fun addNewPoint(location: Location): Boolean {
        val currentTail = tailPoint ?: return false
        
        val distance = currentTail.distanceTo(location)
        if (distance < 0.5) {
            return true
        }
        
        val newPoint = PathPoint.fromLocation(location)
        
        val segmentLength = currentTail.distanceTo(newPoint)
        totalPathLength += segmentLength
        
        newPoint.prev = currentTail
        currentTail.next = newPoint
        tailPoint = newPoint
        
        spatialGrid.addPoint(newPoint)
        
        val lineMarker = PathMarker.createLine(currentTail, newPoint)
        if (lineMarker != null) {
            currentTail.marker?.remove()
            markers.remove(currentTail.marker)
            currentTail.marker = lineMarker
            markers.add(lineMarker)
        }
        
        val endMarker = PathMarker.createStandalone(location)
        newPoint.marker = endMarker
        markers.add(endMarker)
        
        updateActionBar()
        
        if (isOverMaxDistance) {
            return false
        }
        
        return true
    }

    private fun handleBacktrack(targetPoint: PathPoint, currentLoc: Location): Boolean {
        val currentTail = tailPoint ?: return false
        
        if (targetPoint == currentTail.prev) {
            return shortenPath()
        }
        
        if (isOnPath(targetPoint)) {
            return shortenPathTo(targetPoint)
        }
        
        return addNewPoint(currentLoc)
    }

    private fun isOnPath(point: PathPoint): Boolean {
        var current = headPoint
        while (current != null) {
            if (current == point) return true
            current = current.next
        }
        return false
    }

    private fun shortenPath(): Boolean {
        val currentTail = tailPoint ?: return false
        val newTail = currentTail.prev ?: return false
        
        val segmentLength = newTail.distanceTo(currentTail)
        totalPathLength -= segmentLength
        totalPathLength = totalPathLength.coerceAtLeast(0.0)
        
        spatialGrid.removePoint(currentTail)
        currentTail.marker?.remove()
        markers.remove(currentTail.marker)
        
        newTail.next = null
        tailPoint = newTail
        
        val endMarker = PathMarker.createStandalone(newTail.location)
        newTail.marker?.remove()
        markers.remove(newTail.marker)
        newTail.marker = endMarker
        markers.add(endMarker)
        
        updateActionBar()
        
        return true
    }

    private fun shortenPathTo(targetPoint: PathPoint): Boolean {
        var current = tailPoint
        var removedAny = false
        
        while (current != null && current != targetPoint) {
            val prev = current.prev
            
            if (prev != null) {
                val segmentLength = prev.distanceTo(current)
                totalPathLength -= segmentLength
                totalPathLength = totalPathLength.coerceAtLeast(0.0)
            }
            
            spatialGrid.removePoint(current)
            current.marker?.remove()
            markers.remove(current.marker)
            
            current = prev
            removedAny = true
        }
        
        if (current == targetPoint) {
            targetPoint.next = null
            tailPoint = targetPoint
            
            val endMarker = PathMarker.createStandalone(targetPoint.location)
            targetPoint.marker?.remove()
            markers.remove(targetPoint.marker)
            targetPoint.marker = endMarker
            markers.add(endMarker)
            
            updateActionBar()
        }
        
        return removedAny
    }

    fun getFullPath(): List<Location> {
        val path = mutableListOf<Location>()
        var current = headPoint
        while (current != null) {
            path.add(current.location)
            current = current.next
        }
        return path
    }

    fun getTailLocation(): Location? = tailPoint?.location

    fun cleanup() {
        isActive = false
        
        for (marker in markers) {
            marker.remove()
        }
        markers.clear()
        
        spatialGrid.clear()
        headPoint = null
        tailPoint = null
    }

    private fun updateActionBar() {
        if (!player.isOnline) return
        
        val remaining = remainingDistance.toInt()
        val total = totalPathLength.toInt()
        
        if (isOverMaxDistance) {
            player.sendActionBar(
                Component.translatable("endfield-industry.message.wirepath.over_distance")
                    .arguments(RebarArgument.of("distance", (totalPathLength - maxDistance).toInt()))
            )
        } else {
            player.sendActionBar(
                Component.translatable("endfield-industry.message.wirepath.status")
                    .arguments(
                        RebarArgument.of("current", total),
                        RebarArgument.of("max", maxDistance.toInt()),
                        RebarArgument.of("remaining", remaining)
                    )
            )
        }
    }

    override fun toString(): String {
        return "WirePathSession(player=${player.name}, pathLength=$totalPathLength, maxDistance=$maxDistance, points=${countPoints()})"
    }

    private fun countPoints(): Int {
        var count = 0
        var current = headPoint
        while (current != null) {
            count++
            current = current.next
        }
        return count
    }
}
