package io.github.flegacy.flms.ui.element;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface ClickableElement extends InterfaceElement {
	void execute(InventoryClickEvent event);
}
