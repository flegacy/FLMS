package io.github.flegacy.flms;

import io.github.flegacy.flms.command.FLMSCommand;
import io.github.flegacy.flms.items.ItemLibrary;
import io.github.flegacy.flms.mining.PlayerJoinListener;
import io.github.flegacy.flms.ui.InterfaceListener;
import io.github.flegacy.flms.utils.SoundPlayer;
import io.github.flegacy.flms.wand.WandListener;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * FLMS main class
 */
public class FLMS extends JavaPlugin {

	private static final String COMMAND_DESCRIPTION = "All-in-one command for the FLMS plugin.";

	private ItemLibrary itemLibrary;
	private SoundPlayer soundPlayer;

	@Override
	public void onEnable() {
		itemLibrary = new ItemLibrary(this);


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
		getLogger().info("Successfully disabled. Goodbye!");
	}

	public ItemLibrary getItemLibrary() {
		return itemLibrary;
	}

}
