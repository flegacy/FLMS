package io.github.flegacy.flms.mining.listener;

import io.github.flegacy.flms.FLMS;
import io.github.flegacy.flms.mining.MineManager;
import io.github.flegacy.flms.registry.RegisteredBlock;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageAbortEvent;
import org.bukkit.event.block.BlockDamageEvent;

public class MineListener implements Listener {
	private static MineListener instance;

	private final FLMS plugin;

	private MineListener(FLMS plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onBlockDamage(BlockDamageEvent event) {
		RegisteredBlock matchedBlock = plugin.getRegistry().getBlock(event.getBlock().getType());
		if (matchedBlock == null)
			return;
		// TODO testing
		int tickInterval = 5;
        Player player = event.getPlayer();
        MineManager manager = plugin.getMineManager();
        if (!manager.hasTask(player))
            manager.startTask(player, tickInterval, event.getBlock().getLocation(), Material.AIR);
	}

	@EventHandler
	public void onBlockAbort(BlockDamageAbortEvent event) {
        Player player = event.getPlayer();
        MineManager manager = plugin.getMineManager();
        if (manager.hasTask(player))
			manager.cancelTask(player);
	}

	public static MineListener getInstance(FLMS plugin) {
		if (instance == null)
			instance = new MineListener(plugin);
		return instance;
	}
}
