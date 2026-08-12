package me.flegacy.flms.ui;

import me.flegacy.flms.FLMS;
import me.flegacy.flms.ui.elements.ActionElement;
import me.flegacy.flms.ui.elements.DisplayElement;
import me.flegacy.flms.ui.elements.Identifier;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {

    private static InventoryListener instance;
    private final FLMS plugin;

    private InventoryListener(FLMS plugin) {
        this.plugin = plugin;
    }

    public static InventoryListener getInstance(FLMS plugin) {
        if (instance == null)
            instance = new InventoryListener(plugin);
        return instance;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player))
            return;
        if (event.getClickedInventory() != event.getView().getTopInventory())
            return;
        ItemStack firstItem = event.getView().getTopInventory().getItem(0);
        if (firstItem == null || firstItem.getType() == Material.AIR)
            return;
        Identifier identifier = Identifier.findIdentifier(plugin, firstItem);
        if (identifier == null)
            return;
        FLMSInterface ui = identifier.getInterface();
        // This event is clicked in an FLMS inventory beyond this point

        event.setCancelled(true);
        int slotClicked = event.getRawSlot();
        DisplayElement element = ui.getElement(slotClicked);
        if (element == null)
            return;
        if (!(element instanceof ActionElement clickElement))
            return;
        clickElement.execute(player, event);
    }
}
