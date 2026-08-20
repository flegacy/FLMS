package io.github.flegacy.flms.ui.element;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import io.github.flegacy.flms.ui.FLMSInterface;
import io.github.flegacy.flms.utils.SoundPlayer;

public class TransferButton implements ClickableElement {

    private final ItemStack display;
    private final FLMSInterface next;

    public TransferButton(ItemStack display, FLMSInterface next) {
        this.display = display;
        this.next = next;
    }

	@Override
	public ItemStack displayItem() {
        return display.clone();
	}

	@Override
	public void execute(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player))
            return;

        SoundPlayer.playButtonClickSound(player);
        next.open(player);
	}

}
