package io.github.flegacy.flms.wand.ui;

import io.github.flegacy.flms.utils.SoundPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import io.github.flegacy.flms.ui.FLMSInterface;
import io.github.flegacy.flms.ui.element.ClickableElement;

public class TransferButton implements ClickableElement {
    private final FLMSInterface next;
    private final ItemStack display;

    public TransferButton(ItemStack display, FLMSInterface next) {
        this.next = next;
        this.display = display;
    }

    @Override
    public void execute(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player))
            return;
        SoundPlayer.playButtonClickSound(player);
        next.open(player);
    }

    @Override
    public ItemStack getDisplayItem() {
        return display;
    }
}

