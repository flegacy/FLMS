package me.flegacy.flms.ui.elements;

import me.flegacy.flms.ui.BookInventory;
import me.flegacy.flms.ui.FLMSInterface;
import me.flegacy.flms.utils.FLMSException;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class TransferButton implements ActionElement {

    private final ItemStack display;
    private FLMSInterface whereToGo;
    private BookInventory bookToGo;

    public TransferButton(FLMSInterface whereToGo, ItemStack display) {
        this.display = display;
        this.whereToGo = whereToGo;
    }

    public TransferButton(BookInventory bookToGo, ItemStack display) {
        this.display = display;
        this.bookToGo = bookToGo;
    }


    @Override
    public void execute(Player player, InventoryClickEvent event) {
        FLMSInterface destination;
        if (whereToGo != null)
            destination = whereToGo;
        else if (bookToGo != null)
            destination = bookToGo.getFirstPage();
        else
            throw new FLMSException("A TransferButton was created with a null destination!");
        destination.getFLMSInstance().soundLibrary.inventoryOpen(player);
        destination.show(player);
    }

    @Override
    public ItemStack getDisplayItem() {
        return display;
    }
}
