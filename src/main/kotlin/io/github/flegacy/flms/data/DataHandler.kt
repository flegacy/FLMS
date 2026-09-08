package io.github.flegacy.flms.data

import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.FLMS_PERMISSION
import io.github.flegacy.flms.registry.EffectProfile
import io.github.flegacy.flms.registry.RegisteredBlock
import io.github.flegacy.flms.util.errPrefixed
import org.bukkit.Material
import org.bukkit.configuration.MemorySection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

private const val EDIT_WARNING_MESSAGE =
    "Please don't change this file. In-game commands should be used to configure the plugin."
private const val BLOCKS_CONFIG_SECTION = "blocks"
private const val EFFECTS_CONFIG_SECTION = "profiles"

class DataHandler(private val plugin: FLMS) {

    private val objectDataFolder = File(plugin.dataFolder, "data (do not edit)${File.separator}")
    private val effectsFile = File(objectDataFolder, "effects.yml")
    private val blocksFile = File(objectDataFolder, "blocks.yml")

    private var blocksLoaded = false
    private var effectsLoaded = false

    init {
        val version = plugin.pluginMeta.version

        try {

            if (!objectDataFolder.exists())
                objectDataFolder.mkdirs()

            if (!effectsFile.exists()) {
                effectsFile.createNewFile()
                val effectsYaml = YamlConfiguration.loadConfiguration(effectsFile)
                effectsYaml.set("version", version)
                effectsYaml.setComments("version", listOf(EDIT_WARNING_MESSAGE))
                effectsYaml.createSection(EFFECTS_CONFIG_SECTION)
                effectsYaml.save(effectsFile)
                effectsFile.setReadOnly()
                plugin.componentLogger.info("Created effects.yml data file.")
            }

            if (!blocksFile.exists()) {
                blocksFile.createNewFile()
                val blocksYaml = YamlConfiguration.loadConfiguration(blocksFile)
                blocksYaml.set("version", version)
                blocksYaml.setComments("version", listOf(EDIT_WARNING_MESSAGE))
                blocksYaml.createSection(BLOCKS_CONFIG_SECTION)
                blocksYaml.save(blocksFile)
                blocksFile.setReadOnly()
                plugin.componentLogger.info("Created blocks.yml data file.")
            }

        } catch (ex: IOException) {
            plugin.componentLogger.error("-----------------------------------")
            plugin.componentLogger.error("There was an error when creating data files for FLMS. Any changes you make with the plugin will probably not be saved for this session!!!")
            ex.printStackTrace()

            for (player in plugin.server.onlinePlayers)
                if (player.hasPermission(FLMS_PERMISSION))
                    player.sendMessage(errPrefixed("There was a severe error in the FLMS plugin. Please check the console!"))
        }


    }

    fun readBlocks(): Map<Material, RegisteredBlock> {
        require(!blocksLoaded)
        val blocksYaml = YamlConfiguration.loadConfiguration(blocksFile)
        // TODO versioning when needed
        val blocksSection = blocksYaml.getConfigurationSection(BLOCKS_CONFIG_SECTION)

        if (blocksSection == null) {
            plugin.componentLogger.error("There is a formatting error in the blocks.yml data file. Failed to save data for this block.")
            throw IOException("Couldn't write block config data, 'blocks' section is missing.")
            return emptyMap()
        }

        val map = mutableMapOf<Material, RegisteredBlock>()

        for ((key, value) in blocksSection.getValues(false)) {
            try {

                val blockType = Material.valueOf(key)

                val memSection = value as MemorySection
                val attemptedBlock = memSection.getValues(false) as HashMap
                
                if (attemptedBlock.isEmpty()) {
                    plugin.componentLogger.error("Failed to recognize and load block '${blockType}'. Avoid changing any FLMS files other than config.yml.")
                    continue
                }

                val block = RegisteredBlock.deserialize(blockType, attemptedBlock)
                if (block == null) {
                    plugin.componentLogger.error("Failed to recognize and load block '${blockType}'. Avoid changing any FLMS files other than config.yml.")
                    continue
                }

                map[blockType] = block

            } catch (_: IllegalArgumentException) {
                plugin.componentLogger.error("Failed to detect block type of '${key}'. Avoid changing any FLMS files other than config.yml.")
                continue
            }
        }

        return map
    }

    fun loadEffects() {
        require(!effectsLoaded)
    }

    // TODO test further when GUI is created, to make sure that setting blocks works while multiple blocks are already written
    fun writeBlock(block: RegisteredBlock) {
        blocksFile.setWritable(true)
        val blocksYaml = YamlConfiguration.loadConfiguration(blocksFile)
        val blocksYamlSection = blocksYaml.getConfigurationSection(BLOCKS_CONFIG_SECTION)
        if (blocksYamlSection == null) {
            plugin.componentLogger.error("There is a formatting error in the blocks.yml data file. Failed to save data for this block.")
            throw IOException("Couldn't write block config data, 'blocks' section is missing.")
            return
        }
        blocksYamlSection.set(block.type.toString(), block.serialize())
        blocksYaml.save(blocksFile)
        blocksFile.setReadOnly()
    }


    fun writeEffectProfile(profile: EffectProfile) {

    }


}
