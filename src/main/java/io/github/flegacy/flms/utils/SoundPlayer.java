package io.github.flegacy.flms.utils;

import io.github.flegacy.flms.FLMS;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SoundPlayer {

	private SoundPlayer() {}

	public static void playErrorSound(Player player) {
		player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, SoundCategory.UI, 1, 0.5f);
	}

	public static void playItemPickupSound(Player player) {
		player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, SoundCategory.UI, 1, 1);
	}

    public static void playButtonClickSound(Player player) {
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.UI, 1, 1);
    }

	public static void playEnchantSound(Player player) {
		player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.UI, 1, 1);
	}

	public static void playWandOpenSound(Player player, FLMS plugin) {
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
