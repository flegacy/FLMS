package io.github.flegacy.flms.data

import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.util.msgFormat

class ConfigurationValues(private val plugin: FLMS) {

    private val opts = mutableMapOf<Key, Any>()

    init {
        reload()
    }

    fun reload(): Boolean {
        plugin.reloadConfig()
        val fileCfg = plugin.config
        var errors = false

        for (key in Key.entries) {
            val lowcase = key.toString().lowercase()
            val match = fileCfg.getObject(lowcase, key.type.javaObjectType)
            require(match != null)

            if (!fileCfg.isSet(lowcase)) {
                plugin.componentLogger.error(msgFormat(
                    "Couldn't find value for '${lowcase}' in the config. Defaulting to '${match}<reset>'."
                ))
                errors = true
                opts[key] = key.default
                continue
            }

            if ((key.isBoolean() && !fileCfg.isBoolean(lowcase))
            || (key.isString() && !fileCfg.isString(lowcase))) {
                plugin.componentLogger.error(msgFormat(
                    "The value for '${lowcase}' in your config isn't the correct type. Defaulting to '${key.default}<reset>'."
                ))
                errors = true
                opts[key] = key.default
                continue
            }

            opts[key] = match
        }

        if (errors)
            plugin.componentLogger.error(
                "There were errors when loading the FLMS config. Parts of the plugin won't work as you intend."
            )
        else
            plugin.componentLogger.info("Config successfully loaded.")
        return !errors
    }

    fun boolean(key: Key): Boolean {
        require(key.isBoolean())
        return opts[key] as Boolean
    }

    fun string(key: Key): String {
        require(key.isString())
        return opts[key] as String
    }

    


}
