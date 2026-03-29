package top.mc506lw.rebar.endfield_industry.content.powersystem

import top.mc506lw.rebar.endfield_industry.EndfieldIndustry
import top.mc506lw.rebar.endfield_industry.content.powersystem.connection.ConnectionManager
import top.mc506lw.rebar.endfield_industry.content.powersystem.storage.ChunkLoadConnectionRestorer
import top.mc506lw.rebar.endfield_industry.content.powersystem.storage.PowerSystemStorage

object PowerSystem {

    private var instance: PowerSystem? = null

    lateinit var gridManager: PowerGridManager
        private set
    
    lateinit var config: PowerSystemConfig
        private set
    
    lateinit var connectionManager: ConnectionManager
        private set

    fun initialize() {
        if (instance == null) {
            instance = this
        }
        
        this.config = PowerSystemConfig()
        this.gridManager = PowerGridManager(config)
        this.connectionManager = ConnectionManager(config)
        
        PowerSystemStorage.initialize()
        
        ChunkLoadConnectionRestorer.register()
        
        PowerSystemMoveListener(EndfieldIndustry.instance)
        PowerSystemInteractListener(EndfieldIndustry.instance)
    }
    
    fun shutdown() {
        PowerSystemStorage.shutdown()
    }

    fun getInstance(): PowerSystem {
        return instance ?: throw IllegalStateException("PowerSystem not initialized")
    }
}
