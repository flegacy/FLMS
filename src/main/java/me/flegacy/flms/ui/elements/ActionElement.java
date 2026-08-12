package me.flegacy.flms.ui.elements;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface ActionElement extends DisplayElement {
    void execute(Player player, InventoryClickEvent event);
}
