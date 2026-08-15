package io.github.flegacy.flms.mining;

import io.github.flegacy.flms.FLMS;
import io.github.flegacy.flms.registry.RegisteredBlock;
import io.papermc.paper.event.block.BlockBreakBlockEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.Bukkit;

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
