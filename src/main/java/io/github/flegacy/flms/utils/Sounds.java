package io.github.flegacy.flms.utils;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class Sounds {

	private Sounds() {}

	public static void playErrorSound(Player player) {
		player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, SoundCategory.UI, 1, 0.5f);
	}

	public static void playItemPickupSound(Player player) {
		player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, SoundCategory.UI, 1, 1);
	}
}
