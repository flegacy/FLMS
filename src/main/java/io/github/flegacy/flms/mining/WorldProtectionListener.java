package io.github.flegacy.flms.mining;

import io.github.flegacy.flms.FLMS;
import io.github.flegacy.flms.data.ConfigurationValues;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class WorldProtectionListener implements Listener {
	private static WorldProtectionListener instance;

	private final ConfigurationValues config;

	private WorldProtectionListener(FLMS plugin) {
		config = plugin.getConfigValues();
	}

	@EventHandler
	public void onVanillaBlockBreak(BlockBreakEvent event) {
		// TODO enable compatibility with other listeners later instead of just stopping the event altogether.
		Material blockType = event.getBlock().getType();

	}

	private static WorldProtectionListener getInstance(FLMS plugin) {
		if (instance == null)
			instance = new WorldProtectionListener(plugin);
		return instance;
	}
}
