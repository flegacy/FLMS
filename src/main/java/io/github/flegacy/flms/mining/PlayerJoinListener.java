package io.github.flegacy.flms.mining;

import io.github.flegacy.flms.FLMS;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerJoinListener implements Listener {
	private static PlayerJoinListener instance;

	private final PotionEffect haste = new PotionEffect(
			PotionEffectType.HASTE,
			PotionEffect.INFINITE_DURATION,
			1,
			true,
			false,
			false
	);

	private final PotionEffect fatigue = new PotionEffect(
			PotionEffectType.MINING_FATIGUE,
			PotionEffect.INFINITE_DURATION,
			2,
			true,
			false,
			false
	);

	private final NamespacedKey attributeKey;

	private PlayerJoinListener(FLMS plugin) {
		attributeKey = new NamespacedKey(plugin, "flms_attack_speed_offset");
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		applyBaseEffects(event.getPlayer());
	}

	private void applyBaseEffects(Player player) {
		player.removePotionEffect(PotionEffectType.HASTE);
		player.removePotionEffect(PotionEffectType.MINING_FATIGUE);
		player.addPotionEffect(haste);
		player.addPotionEffect(fatigue);
		// TODO fix attack speed
	}

	public static PlayerJoinListener getInstance(FLMS plugin) {
		if (instance == null)
			instance = new PlayerJoinListener(plugin);
		return instance;
	}
}
