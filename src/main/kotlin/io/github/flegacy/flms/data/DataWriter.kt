package io.github.flegacy.flms.data

import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.util.ERROR_SAVING_DATA
import io.github.flegacy.flms.util.WARNING_CHANGING_FILE
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

fun createDataFile(plugin: FLMS, file: File, configSection: String) {
    val version = plugin.pluginMeta.version

    try {
        if (!file.exists())
            file.createNewFile()
        file.setWritable(true)
        val yaml = YamlConfiguration.loadConfiguration(file)

        yaml.set("version", version)
        yaml.setComments("version", listOf(WARNING_CHANGING_FILE))
        yaml.createSection(configSection)
        yaml.save(file)
        file.setReadOnly()
        plugin.componentLogger.info("Created ${file.name} data file.")

    } catch (ex: IOException) {
        plugin.componentLogger.error(ERROR_SAVING_DATA)
        ex.printStackTrace()
        plugin.errorNotifyAdmins()
    }
}

fun flmsWriteData(plugin: FLMS, file: File, configSection: String, key: String, value: Any) {
    file.setWritable(true)
    val yaml = YamlConfiguration.loadConfiguration(file)
    val section = yaml.getConfigurationSection(configSection)
    if (section == null) {
        plugin.componentLogger.error("Couldn't find $configSection in ${file.name} Saved configs will not be written for this session!")
        return
    }

    section.set(key, value)
    yaml.save(file)
    file.setReadOnly()
}

fun flmsUnwriteData(plugin: FLMS, file: File, configSection: String, key: String) {
    file.setWritable(true)
    val yaml = YamlConfiguration.loadConfiguration(file)
    val section = yaml.getConfigurationSection(configSection)
    if (section == null) {
        plugin.componentLogger.error("Couldn't find $configSection in $file.name. Saved configs will not be written for this session!")
        return
    }

    section.set(key, null)
    yaml.save(file)
    file.setReadOnly()
}
