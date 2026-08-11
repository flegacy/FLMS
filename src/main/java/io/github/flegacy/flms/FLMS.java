package io.github.flegacy.flms;

import io.github.flegacy.flms.command.FLMSCommand;
import io.github.flegacy.flms.mining.PlayerJoinListener;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * FLMS main class
 */
public class FLMS extends JavaPlugin {

	private static final String COMMAND_DESCRIPTION = "All-in-one command for the FLMS plugin.";

	private ItemLibrary itemLibrary;

	@Override
	public void onEnable() {

		getLifecycleManager()
				.registerEventHandler(
						LifecycleEvents.COMMANDS,
						commands ->
								commands.registrar().register(
										FLMSCommand.getInstance(this).getCommandNode(), COMMAND_DESCRIPTION)
				);

		getServer().getPluginManager().registerEvents(PlayerJoinListener.getInstance(this), this);

		itemLibrary = new ItemLibrary(this);

		getLogger().info("Successfully loaded. Hello World!");

	}

	@Override
	public void onDisable() {
		getLogger().info("Successfully disabled. Goodbye!");
	}

	public ItemLibrary getItemLibrary() {
		return itemLibrary;
	}
}
