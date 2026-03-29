package top.mc506lw.rebar.endfield_industry.content.powersystem.devices

interface PowerProducer {

    fun getPowerProduction(): Int

    fun hasFuel(): Boolean = true

    fun getGrid(): PowerDevice?
}
