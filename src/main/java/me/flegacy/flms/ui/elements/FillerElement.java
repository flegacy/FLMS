package me.flegacy.flms.ui.elements;

import org.bukkit.inventory.ItemStack;

public class FillerElement implements DisplayElement {

    private final ItemStack display;

    public FillerElement(ItemStack display) {
        this.display = display;
    }

    @Override
    public ItemStack getDisplayItem() {
        return display;
    }
}
