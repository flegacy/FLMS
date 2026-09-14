package io.github.flegacy.flms.data.handlers

import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.data.createDataFile
import io.github.flegacy.flms.data.flmsUnwriteData
import io.github.flegacy.flms.data.flmsWriteData
import io.github.flegacy.flms.registry.RegisteredBlock
import io.github.flegacy.flms.util.ERROR_SAVING_DATA
import io.github.flegacy.flms.util.WARNING_CHANGING_FILE
import jdk.jfr.Registered
import org.bukkit.Material
import org.bukkit.configuration.MemorySection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

private const val CONFIG_SECTION = "blocks"
private const val FILE_NAME = "blocks.yml"
private const val ERROR_MISSING_SECTION =
    "Couldn't find '${CONFIG_SECTION}' section in ${FILE_NAME}. Saved configs will not be processed for this session!"


class BlockDataHandler(private val plugin: FLMS) {

    private val file = File(plugin.saveFolder(), FILE_NAME)
    private var loaded = false

    init {
        if (!file.exists())
            createDataFile(plugin, file, CONFIG_SECTION)
    }

    fun write(block: RegisteredBlock) {
        flmsWriteData(plugin, file, CONFIG_SECTION, block.type.toString(), block.serialize())
    }

    fun unwrite(block: RegisteredBlock) {
        flmsUnwriteData(plugin, file, CONFIG_SECTION, block.type.toString())
    }

    fun read(): Map<Material, RegisteredBlock> {
        require(!loaded)
        val yaml = YamlConfiguration.loadConfiguration(file)
        // TODO versioning when needed

        val blockSection = yaml.getConfigurationSection(CONFIG_SECTION)
        if (blockSection == null) {
            plugin.componentLogger.error(ERROR_MISSING_SECTION)
            return emptyMap()
        }

        val blockMap = mutableMapOf<Material, RegisteredBlock>()

        for ((key, value) in blockSection.getValues(false)) {
            try {

                val blockType = Material.valueOf(key)

                val memSection = value as MemorySection

                val attemptedBlock = memSection.getValues(false) as HashMap
                val block = RegisteredBlock.deserialize(plugin, blockType, attemptedBlock)

                if (attemptedBlock.isEmpty() || block == null) {
                    plugin.componentLogger.error("Failed to read block type '${key}'")
                    continue
                }

                blockMap[blockType] = block

            } catch (_: IllegalArgumentException) {
                plugin.componentLogger.error("Failed to read block type '${key}'")
                continue
            }
        }
        loaded = true
        return blockMap
    }
}
