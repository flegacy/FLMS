package io.github.flegacy.flms.ui.element;

import org.bukkit.inventory.ItemStack;

public record DisplayElement(ItemStack displayItem) implements InterfaceElement {

	@Override
	public ItemStack displayItem() {
		return displayItem.clone();
	}
}
