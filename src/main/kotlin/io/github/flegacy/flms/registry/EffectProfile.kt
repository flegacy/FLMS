package io.github.flegacy.flms.registry

import io.github.flegacy.flms.FLMS
import org.bukkit.entity.Player

class EffectProfile(private val plugin: FLMS, private var haste: Short, private var fatigue: Short) {

    fun setHaste(value: Short, player: Player) {
        require(value >= 0)
        haste = value
        plugin.effectData().write(player)
    }

    fun getHaste(): Short {
        return haste
    }

    fun setFatigue(value: Short, player: Player) {
        require(value >= 0)
        fatigue = value
        plugin.effectData().write(player)
    }

    fun getFatigue(): Short {
        return fatigue
    }

    fun serialize(): Map<String, Int> {
        val serialized = mutableMapOf<String, Int>()
        serialized[SERIALIZED_HASTE_KEY] = haste.toInt()
        serialized[SERIALIZED_FATIGUE_KEY] = fatigue.toInt()
        return serialized
    }

    companion object {

        fun deserialize(plugin: FLMS, serialized: Map<String, Int>, uuid: String): EffectProfile {
            var haste = serialized[SERIALIZED_HASTE_KEY]
            var fatigue = serialized[SERIALIZED_FATIGUE_KEY]

            if (haste == null) {
                plugin.componentLogger.error("Failed to find haste data for '${uuid}'")
                haste = 0
            }

            if (fatigue == null) {
                plugin.componentLogger.error("Failed to find fatigue data for '${uuid}'")
                fatigue = 0
            }

            if (haste > Short.MAX_VALUE)
                haste = Short.MAX_VALUE.toInt()
            if (fatigue > Short.MAX_VALUE)
                haste = Short.MAX_VALUE.toInt()

            return EffectProfile(plugin, haste.toShort(), fatigue.toShort())
        }
    }
}
