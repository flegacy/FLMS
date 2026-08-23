package io.github.flegacy.flms

import io.github.flegacy.flms.command.FLMSCommand
import io.github.flegacy.flms.items.ItemLibrary
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.plugin.java.JavaPlugin

const val FLMS_PERMISSION = "flms.admin"
const val COMMAND_DESCRIPTION = "All-in-one command for FLMS."

class FLMS: JavaPlugin() {

    var itemLibrary: ItemLibrary? = null

    override fun onEnable() {

        itemLibrary = ItemLibrary(this)

        if (dataFolder.exists())
            dataFolder.createNewFile()

        saveDefaultConfig()
        config.options().copyDefaults(true)

        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) {
            event -> event.registrar().register(FLMSCommand(this).buildCommandNode(), COMMAND_DESCRIPTION)
        }

        componentLogger.info("Successfully loaded. Hello world!")
    }

    override fun onDisable() {
        componentLogger.info("Successfully disabled. Goodbye!")
    }
}
