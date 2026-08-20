package io.github.flegacy.flms.mining;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import io.github.flegacy.flms.FLMS;

public class WorldProtectionListener implements Listener {
	private static WorldProtectionListener instance;

	private final FLMS plugin;

	private WorldProtectionListener(FLMS plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		// TODO enable compatibility with other listeners later instead of just stopping the event altogether.
		// TODO integrate crops
        if (event.getPlayer().getGameMode() != GameMode.SURVIVAL)
            return;
        if (!(event instanceof FLMSBlockBreakEvent flmsEvent)) {
            event.setCancelled(true);
            return;
        }
	}

	public static WorldProtectionListener getInstance(FLMS plugin) {
		if (instance == null)
			instance = new WorldProtectionListener(plugin);
		return instance;
	}
}
