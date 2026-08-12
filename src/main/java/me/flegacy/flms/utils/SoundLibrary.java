package me.flegacy.flms.utils;

import me.flegacy.flms.FLMS;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SoundLibrary {

    private final FLMS plugin;

    public SoundLibrary(FLMS plugin) {
        this.plugin = plugin;
    }

    public void click(Player player) {
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.UI, 1, 1);
    }

    public void bookFlip(Player player) {
        player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, SoundCategory.UI, 1, 1);
    }

    public void success(Player player) {
        final float[] pitchArray = {0.71f, 0.89f, 1.06f, 1.59f};
        new BukkitRunnable() {
            int count = 0;
            public void run() {
                if (count == 4) {
                    cancel();
                    return;
                }
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, SoundCategory.UI, 1, pitchArray[count]);
                count++;
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    public void wandOpen(Player player) {
        final float[] pitchArray = {1.189207f, 0.890899f, 0.707107f, 1.059463f};
        new BukkitRunnable() {
            int count = 0;
            public void run() {
                if (count == 4) {
                    cancel();
                    return;
                }
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, SoundCategory.UI, 1, pitchArray[count]);
                count++;
            }
        }.runTaskTimer(plugin, 0, 3);
    }

    public void inventoryOpen(Player player) {
        player.playSound(player.getLocation(), Sound.ITEM_BUNDLE_DROP_CONTENTS, SoundCategory.UI, 1, 1);
    }


}
