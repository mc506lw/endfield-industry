package top.mc506lw.rebar.endfield_industry.content.powersystem.gui

import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.Gui
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

object PlayerGuiSessionManager {
    
    data class GuiSession(
        val playerId: UUID,
        var mode: PowerGridDisplayMode = PowerGridDisplayMode.POWER_DATA,
        var locationKey: String? = null,
        var page: Int = 0,
        var gui: Gui? = null,
        var guiBase: PowerGridGuiBase? = null,
        val createdAt: Long = System.currentTimeMillis()
    )
    
    private val sessions = ConcurrentHashMap<UUID, GuiSession>()
    
    fun createSession(player: Player, gui: Gui, guiBase: PowerGridGuiBase): GuiSession {
        val existing = sessions[player.uniqueId]
        if (existing != null) {
            existing.gui = gui
            existing.guiBase = guiBase
            return existing
        }
        val session = GuiSession(player.uniqueId, gui = gui, guiBase = guiBase)
        sessions[player.uniqueId] = session
        return session
    }
    
    fun getSession(playerId: UUID): GuiSession? = sessions[playerId]
    
    fun clearSession(playerId: UUID) {
        sessions.remove(playerId)
    }
    
    fun updateMode(playerId: UUID, mode: PowerGridDisplayMode) {
        sessions[playerId]?.mode = mode
    }
    
    fun updateLocationKey(playerId: UUID, locationKey: String?) {
        sessions[playerId]?.locationKey = locationKey
    }
    
    fun updatePage(playerId: UUID, page: Int) {
        sessions[playerId]?.page = page
    }
    
    fun getMode(playerId: UUID): PowerGridDisplayMode {
        return sessions[playerId]?.mode ?: PowerGridDisplayMode.POWER_DATA
    }
    
    fun getLocationKey(playerId: UUID): String? {
        return sessions[playerId]?.locationKey
    }
    
    fun getPage(playerId: UUID): Int {
        return sessions[playerId]?.page ?: 0
    }
    
    fun getGui(playerId: UUID): Gui? {
        return sessions[playerId]?.gui
    }
    
    fun getGuiBase(playerId: UUID): PowerGridGuiBase? {
        return sessions[playerId]?.guiBase
    }
    
    fun hasSession(playerId: UUID): Boolean {
        return sessions.containsKey(playerId)
    }
    
    fun getAllSessions(): Map<UUID, GuiSession> = sessions.toMap()
    
    fun clearAll() {
        sessions.clear()
    }
}
