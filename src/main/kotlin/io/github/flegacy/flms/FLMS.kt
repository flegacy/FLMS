package io.github.flegacy.flms

import io.github.flegacy.flms.command.FLMSCommand
import io.github.flegacy.flms.data.ConfigurationValues
import io.github.flegacy.flms.items.ItemLibrary
import io.github.flegacy.flms.registry.FLMSRegistry
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.plugin.java.JavaPlugin
import sun.security.krb5.Config

const val FLMS_PERMISSION = "flms.admin"
const val COMMAND_DESCRIPTION = "All-in-one command for FLMS."

class FLMS: JavaPlugin() {

    private var itemLib: ItemLibrary? = null
    private var configValues: ConfigurationValues? = null
    private var registry: FLMSRegistry? = null

    override fun onEnable() {

        if (dataFolder.exists())
            dataFolder.createNewFile()

        saveDefaultConfig()
        config.options().copyDefaults(true)

        itemLib = ItemLibrary(this)
        configValues = ConfigurationValues(this)
        registry = FLMSRegistry(this)

        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) {
            event -> event.registrar().register(FLMSCommand(this).buildCommandNode(), COMMAND_DESCRIPTION)
        }

        componentLogger.info("Successfully loaded. Hello world!")
    }

    override fun onDisable() {
        componentLogger.info("Successfully disabled. Goodbye!")
    }

    fun itemLib(): ItemLibrary {
        return itemLib!!
    }

    fun cfgVals(): ConfigurationValues {
        return configValues!!
    }

    fun registry(): FLMSRegistry {
        return registry!!
    }
}
