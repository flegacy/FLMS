package io.github.flegacy.flms.wand;

import io.github.flegacy.flms.FLMS;
import io.github.flegacy.flms.utils.Permissions;
import io.github.flegacy.flms.utils.SoundPlayer;
import io.github.flegacy.flms.wand.ui.WandMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class WandListener implements Listener {
	private static WandListener instance;
	private final FLMS plugin;
	private final WandMenu wandMenu;

	private WandListener(FLMS plugin) {
		this.plugin = plugin;
		this.wandMenu = new WandMenu(plugin);
	}

	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if (!player.hasPermission(Permissions.WAND_PERMISSION))
			return;
		if (!plugin.getItemLibrary().isWand(player.getInventory().getItemInMainHand()))
			return;

		if (event.getAction() == Action.RIGHT_CLICK_AIR) {
			SoundPlayer.playWandOpenSound(player, plugin);
			wandMenu.open(player);
		}

	}

	public static WandListener getInstance(FLMS plugin) {
		if (instance == null)
			instance = new WandListener(plugin);
		return instance;
	}

}
