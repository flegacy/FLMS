package io.github.flegacy.flms

import io.github.flegacy.flms.command.FLMSCommand
import io.github.flegacy.flms.config.WandListener
import io.github.flegacy.flms.data.ConfigurationValues
import io.github.flegacy.flms.data.DataHandler
import io.github.flegacy.flms.items.ItemLibrary
import io.github.flegacy.flms.mining.MineListener
import io.github.flegacy.flms.mining.MineManager
import io.github.flegacy.flms.mining.WorldProtectionListener
import io.github.flegacy.flms.registry.FLMSRegistry
import io.github.flegacy.flms.ui.InventoryListener
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.plugin.java.JavaPlugin

const val FLMS_PERMISSION = "flms.admin"
const val COMMAND_DESCRIPTION = "All-in-one command for FLMS."

// TODO GET RID OF ALL THE UNSIGNED NUMBERS PLEASE

class FLMS : JavaPlugin() {

    private var itemLib: ItemLibrary? = null
    private var configValues: ConfigurationValues? = null
    private var registry: FLMSRegistry? = null
    private var mineManager: MineManager? = null

    private var dataHandler: DataHandler? = null

    override fun onEnable() {

        if (dataFolder.exists()) dataFolder.createNewFile()

        saveDefaultConfig()
        config.options().copyDefaults(true)

        dataHandler = DataHandler(this)
        registry = FLMSRegistry(this, dataHandler!!)

        itemLib = ItemLibrary(this)
        configValues = ConfigurationValues(this)
        mineManager = MineManager(this)

        val manager = server.pluginManager
        manager.registerEvents(WorldProtectionListener(this), this)
        manager.registerEvents(MineListener(this), this)
        manager.registerEvents(InventoryListener(), this)
        manager.registerEvents(WandListener(this), this)

        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            event.registrar().register(FLMSCommand(this).buildCommandNode(), COMMAND_DESCRIPTION)
        }

        componentLogger.info("Successfully loaded. Hello world!")
    }

    override fun onDisable() {

        componentLogger.info("Successfully disabled. Goodbye!")
    }

    fun itemLib(): ItemLibrary {
        return itemLib!!
    }

    fun configValues(): ConfigurationValues {
        return configValues!!
    }

    fun registry(): FLMSRegistry {
        return registry!!
    }

    fun mineManager(): MineManager {
        return mineManager!!
    }
}
