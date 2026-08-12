package io.github.flegacy.flms.ui.element;

import org.bukkit.inventory.ItemStack;

public class DisplayElement implements InterfaceElement {

	private final ItemStack displayItem;

	public DisplayElement(ItemStack displayItem) {
		this.displayItem = displayItem;
	}

	@Override
	public ItemStack getDisplayItem() {
		return displayItem.clone();
	}
}
