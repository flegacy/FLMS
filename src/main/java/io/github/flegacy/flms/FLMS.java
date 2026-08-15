package io.github.flegacy.flms;

import java.io.IOException;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import io.github.flegacy.flms.command.FLMSCommand;
import io.github.flegacy.flms.data.ConfigurationValues;
import io.github.flegacy.flms.items.ItemLibrary;
import io.github.flegacy.flms.mining.MineListener;
import io.github.flegacy.flms.mining.MineManager;
import io.github.flegacy.flms.mining.PlayerJoinListener;
import io.github.flegacy.flms.mining.WorldProtectionListener;
import io.github.flegacy.flms.registry.FLMSRegistry;
import io.github.flegacy.flms.ui.InterfaceListener;
import io.github.flegacy.flms.wand.WandListener;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;

/**
 * FLMS main class
 */
public class FLMS extends JavaPlugin {

    private static final String COMMAND_DESCRIPTION = "All-in-one command for the FLMS plugin.";
    public static final String FLMS_PERMISSION = "flms.admin";

    private ItemLibrary itemLibrary;
    private FLMSRegistry registry;
    private ProtocolManager protocolManager;
    private ConfigurationValues configurationValues;
    private MineManager mineManager;

    @Override
    public void onEnable() {

        if (!getDataFolder().exists()) {
            try {
                getDataFolder().createNewFile();
            } catch (IOException e) {
				getComponentLogger().error(
						"I was unable to create the plugin data folder, and this I can't save any configurations for this session. This plugin will shut down as a result.");
				getServer().getPluginManager().disablePlugin(this);
            }
        }
        // TODO check if config is working

        saveDefaultConfig();
        getConfig().options().copyDefaults(true);

        configurationValues = new ConfigurationValues(this);
        itemLibrary = new ItemLibrary(this);
        protocolManager = ProtocolLibrary.getProtocolManager();
        registry = new FLMSRegistry();
        mineManager = new MineManager(this);

        getLifecycleManager()
                .registerEventHandler(
                        LifecycleEvents.COMMANDS,
                        commands -> commands.registrar().register(
                                FLMSCommand.getInstance(this).getCommandNode(), COMMAND_DESCRIPTION));

        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(WorldProtectionListener.getInstance(this), this);
        manager.registerEvents(PlayerJoinListener.getInstance(this), this);
        manager.registerEvents(InterfaceListener.getInstance(), this);
        manager.registerEvents(WandListener.getInstance(this), this);
        manager.registerEvents(MineListener.getInstance(this), this);

        getComponentLogger().info("Successfully loaded. Hello World!");
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
		getComponentLogger().info("Successfully disabled.");
    }

    public ItemLibrary getItemLibrary() {
        return itemLibrary;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public ConfigurationValues getConfigValues() {
        return configurationValues;
    }

    public FLMSRegistry getRegistry() {
        return registry;
    }

    public MineManager getMineManager() {
        return mineManager;
    }

}
