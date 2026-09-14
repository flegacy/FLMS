package io.github.flegacy.flms

import io.github.flegacy.flms.command.FLMSCommand
import io.github.flegacy.flms.config.InputDeviceListener
import io.github.flegacy.flms.config.WandListener
import io.github.flegacy.flms.data.ConfigurationValues
import io.github.flegacy.flms.data.handlers.BlockDataHandler
import io.github.flegacy.flms.data.handlers.EffectDataHandler
import io.github.flegacy.flms.items.ItemLibrary
import io.github.flegacy.flms.mining.MineListener
import io.github.flegacy.flms.mining.MineManager
import io.github.flegacy.flms.mining.WorldProtectionListener
import io.github.flegacy.flms.registry.FLMSRegistry
import io.github.flegacy.flms.ui.InventoryListener
import io.github.flegacy.flms.util.ERROR_SEVERE_CONSOLE
import io.github.flegacy.flms.util.errPrefixed
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

const val FLMS_PERMISSION = "flms.admin"
const val COMMAND_DESCRIPTION = "All-in-one command for FLMS."

// TODO implement a system that attempts to save lost data by creating a new "retry" file whenever the plugin fails to write 
// then read the retry file if it exists, deleting it and replacing the corrupted original file if the read is successful
// TODO test compatibility with WorldGuard and similar plugins that use BlockBreakEvent
// TODO ensure all config options are fulfilled
class FLMS : JavaPlugin() {

    private var itemLib: ItemLibrary? = null
    private var configValues: ConfigurationValues? = null
    private var registry: FLMSRegistry? = null
    private var mineManager: MineManager? = null
    private var inputDeviceListener: InputDeviceListener? = null

    private var saveFolder: File? = null
    private var blockDataHandler: BlockDataHandler? = null
    private var effectDataHandler: EffectDataHandler? = null

    override fun onEnable() {

        if (dataFolder.exists()) dataFolder.createNewFile()

        saveDefaultConfig()
        config.options().copyDefaults(true)

        saveFolder = File(dataFolder, "data (do not edit)${File.separator}")
        if (!saveFolder!!.exists())
            saveFolder!!.mkdirs()

        blockDataHandler = BlockDataHandler(this)
        effectDataHandler = EffectDataHandler(this)
        registry = FLMSRegistry(this)

        itemLib = ItemLibrary(this)
        configValues = ConfigurationValues(this)
        mineManager = MineManager(this)
        inputDeviceListener = InputDeviceListener(this)

        val manager = server.pluginManager
        manager.registerEvents(WorldProtectionListener(this), this)
        manager.registerEvents(MineListener(this), this)
        manager.registerEvents(InventoryListener(), this)
        manager.registerEvents(WandListener(this), this)
        manager.registerEvents(inputDeviceListener!!, this)

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

    fun inputDeviceListener(): InputDeviceListener {
        return inputDeviceListener!!
    }

    fun saveFolder(): File {
        return saveFolder!!
    }

    fun blockData(): BlockDataHandler {
        return blockDataHandler!!
    }
    
    fun effectData(): EffectDataHandler {
        return effectDataHandler!!
    }

    fun errorNotifyAdmins() {
        for (player in server.onlinePlayers)
            if (player.hasPermission(FLMS_PERMISSION))
                player.sendMessage(errPrefixed(ERROR_SEVERE_CONSOLE))
    }
}
