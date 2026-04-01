package top.mc506lw.rebar.endfield_industry.content.powersystem

interface PowerConsumer {
    val powerConsumption: Int
    var isPowered: Boolean
    
    fun onPowerConnected() {
        isPowered = true
    }
    
    fun onPowerDisconnected() {
        isPowered = false
    }
    
    fun onGridDestroyed() {
        isPowered = false
    }
}
