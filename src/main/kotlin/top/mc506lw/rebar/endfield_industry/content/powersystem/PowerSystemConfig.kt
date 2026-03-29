package top.mc506lw.rebar.endfield_industry.content.powersystem

import io.github.pylonmc.rebar.config.Settings
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter
import top.mc506lw.rebar.endfield_industry.EndfieldIndustryKeys

class PowerSystemConfig {
    val relayConnectionDistance: Int
    val powerStationConnectionDistance: Int
    val powerStationSupplyRange: Int

    constructor() {
        val powerSystemSettings = Settings.get(EndfieldIndustryKeys.POWER_STATION_EMITTER)
        relayConnectionDistance = powerSystemSettings.getOrThrow("relay-connection-distance", ConfigAdapter.INTEGER)
        powerStationConnectionDistance = powerSystemSettings.getOrThrow("power-station-connection-distance", ConfigAdapter.INTEGER)
        powerStationSupplyRange = powerSystemSettings.getOrThrow("power-station-supply-range", ConfigAdapter.INTEGER)
    }
}
