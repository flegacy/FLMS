package me.flegacy.flms.ui.elements;

import me.flegacy.flms.FLMS;
import org.bukkit.inventory.ItemStack;

public enum TransferButtonType {
    RIGHT_ARROW,
    LEFT_ARROW,
    EXIT_BUTTON;

    public ItemStack getItem(FLMS plugin) {
        return switch (this) {
            case LEFT_ARROW -> plugin.itemLibrary.createLeftArrowIcon();
            case RIGHT_ARROW -> plugin.itemLibrary.createRightArrowIcon();
            case EXIT_BUTTON -> plugin.itemLibrary.createExitIcon();
        };
    }
}
