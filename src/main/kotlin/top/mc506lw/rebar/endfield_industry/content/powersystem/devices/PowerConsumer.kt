package top.mc506lw.rebar.endfield_industry.content.powersystem.devices

interface PowerConsumer {

    fun getPowerConsumption(): Int

    fun isPowered(): Boolean {
        val device = getGrid()
        return device != null && device.getGrid() != null && !device.getGrid()!!.isOverloaded
    }

    fun getGrid(): PowerDevice?
}
