package io.github.flegacy.flms;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SoundLibrary {

	private final FLMS plugin;

	SoundLibrary(FLMS plugin) {
		this.plugin = plugin;
	}

	public void playErrorSound(Player player) {
		player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, SoundCategory.UI, 1, 0.5f);
	}

	public void playItemPickupSound(Player player) {
		player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, SoundCategory.UI, 1, 1);
	}

    public void playButtonClickSound(Player player) {
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.UI, 1, 1);
    }

	public void playWandOpenSound(Player player) {
		new BukkitRunnable() {
			int pitch = 0;
			@Override
			public void run() {
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.UI, 1, pitch);
				pitch++;
				if (pitch == 3)
					cancel();
			}
		}.runTaskTimer(plugin, 0, 2);
	}
}
