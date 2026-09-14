package io.github.flegacy.flms.data.handlers

import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.data.createDataFile
import io.github.flegacy.flms.data.flmsWriteData
import io.github.flegacy.flms.registry.EffectProfile
import io.github.flegacy.flms.registry.FLMSRegistry
import io.github.flegacy.flms.util.ERROR_SAVING_DATA
import io.github.flegacy.flms.util.WARNING_CHANGING_FILE
import org.bukkit.configuration.MemorySection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.io.IOException
import java.util.UUID

private const val CONFIG_SECTION = "profiles"
private const val FILE_NAME = "effects.yml"
private const val ERROR_MISSING_SECTION =
    "Couldn't find '${CONFIG_SECTION}' section in ${FILE_NAME}. Saved configs will not be processed for this session!"

class EffectDataHandler(private val plugin: FLMS) {

    private val file = File(plugin.saveFolder(), FILE_NAME)
    private var loaded = false

    init {
        if (!file.exists())
            createDataFile(plugin, file, CONFIG_SECTION)
    }

    fun write(player: Player) {
        flmsWriteData(plugin, file, CONFIG_SECTION, player.uniqueId.toString(), plugin.registry().findEffectProfile(player).serialize())
    }

    fun read(): Map<UUID, EffectProfile> {
        require(!loaded)
        
        val yaml = YamlConfiguration.loadConfiguration(file)
        val section = yaml.getConfigurationSection(CONFIG_SECTION)
        if (section == null) {
            plugin.componentLogger.error(ERROR_MISSING_SECTION)
            return emptyMap()
        }
        val map = mutableMapOf<UUID, EffectProfile>()

        // Should be Map<UUID, Map<String, Int>>
        for ((key, value) in section.getValues(false)) {
            try {
                val uuid = UUID.fromString(key)
                    ?: throw IllegalArgumentException()
                val memory = value as MemorySection
                val profile = memory.getValues(false) as? MutableMap<String, Int>

                if (profile == null) {
                    plugin.componentLogger.error("Failed to parse profile for '${key}'")
                    continue
                }
                map[uuid] = EffectProfile.deserialize(plugin, profile, key)

            } catch (_: IllegalArgumentException) {
                plugin.componentLogger.error("Failed to parse UUID '${key}'")
                continue
            }
        }
        return map
    }
    
}
