package io.github.flegacy.flms;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import io.github.flegacy.flms.command.FLMSCommand;
import io.github.flegacy.flms.data.ConfigurationValues;
import io.github.flegacy.flms.items.ItemLibrary;
import io.github.flegacy.flms.mining.PlayerJoinListener;
import io.github.flegacy.flms.registry.FLMSRegistry;
import io.github.flegacy.flms.ui.InterfaceListener;
import io.github.flegacy.flms.wand.WandListener;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

/**
 * FLMS main class
 */
public class FLMS extends JavaPlugin {

	private static final String COMMAND_DESCRIPTION = "All-in-one command for the FLMS plugin.";

	private ItemLibrary itemLibrary;
	private FLMSRegistry registry;
	private ProtocolManager protocolManager;
	private ConfigurationValues configurationValues;

	@Override
	public void onEnable() {

		if (!getDataFolder().exists()) {
			try {
				getDataFolder().createNewFile();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		saveResource("config.yml", false);
		saveDefaultConfig();

		configurationValues = new ConfigurationValues(this);
		itemLibrary = new ItemLibrary(this);
		protocolManager = ProtocolLibrary.getProtocolManager();
		registry = new FLMSRegistry();

		getLifecycleManager()
				.registerEventHandler(
						LifecycleEvents.COMMANDS,
						commands ->
								commands.registrar().register(
										FLMSCommand.getInstance(this).getCommandNode(), COMMAND_DESCRIPTION)
				);

		getServer().getPluginManager().registerEvents(PlayerJoinListener.getInstance(this), this);
		getServer().getPluginManager().registerEvents(InterfaceListener.getInstance(), this);
		getServer().getPluginManager().registerEvents(WandListener.getInstance(this), this);

		getLogger().info("Successfully loaded. Hello World!");
	}

	@Override
	public void onDisable() {
		getServer().getScheduler().cancelTasks(this);
		getLogger().info("Successfully disabled. Goodbye!");
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

}
