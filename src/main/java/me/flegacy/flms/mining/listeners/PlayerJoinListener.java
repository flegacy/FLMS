package me.flegacy.flms.mining.listeners;

import me.flegacy.flms.FLMS;
import me.flegacy.flms.mining.PlayerStats;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerJoinListener implements Listener {

    // TODO add other instances where you lose potion effects, like death, sea guardians, and milk

    private static PlayerJoinListener instance;
    public static PlayerJoinListener getInstance(FLMS plugin) {
        if (instance == null) instance = new PlayerJoinListener(plugin);
        return instance;
    }

    private final FLMS plugin;
    private final PotionEffect haste = new PotionEffect(PotionEffectType.HASTE, Integer.MAX_VALUE, 1, true, false, false);
    private final PotionEffect fatigue = new PotionEffect(PotionEffectType.MINING_FATIGUE, Integer.MAX_VALUE, 2, true, false, false);

    private PlayerJoinListener(FLMS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!plugin.playerEffects.containsKey(player.getUniqueId())) {
            plugin.playerEffects.put(player.getUniqueId(), new PlayerStats());
            plugin.getLogger().info("Created new player stats for " + player.getName() + ".");
        }

        player.removePotionEffect(PotionEffectType.HASTE);
        player.removePotionEffect(PotionEffectType.MINING_FATIGUE);
        player.addPotionEffect(haste);
        player.addPotionEffect(fatigue);
    }
}
