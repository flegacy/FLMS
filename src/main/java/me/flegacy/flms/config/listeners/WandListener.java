package me.flegacy.flms.config.listeners;

import me.flegacy.flms.FLMS;
import me.flegacy.flms.config.ui.WandMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class WandListener implements Listener {
    private static WandListener instance;
    public static WandListener getInstance(FLMS plugin) {
        if (instance == null)
            instance = new WandListener(plugin);
        return instance;
    }

    private final FLMS plugin;
    private final WandMenu menu;

    private WandListener(FLMS plugin) {
        this.plugin = plugin;
        menu = new WandMenu(plugin);
    }

    @EventHandler
    public void firstStepMenu(PlayerInteractEvent event) {
        if (!event.getPlayer().hasPermission(FLMS.FLMS_PERMISSION))
            return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR)
            return;
        ItemStack heldItem = event.getPlayer().getInventory().getItemInMainHand();
        if (!plugin.itemLibrary.getTag(heldItem).equals("wand"))
            return;
        plugin.soundLibrary.wandOpen(event.getPlayer());
        menu.show(event.getPlayer());


    }

}
