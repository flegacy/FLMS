package me.flegacy.flms.ui.elements;

import me.flegacy.flms.FLMS;
import me.flegacy.flms.ui.BookInventory;
import me.flegacy.flms.utils.FLMSException;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class BookFlipper implements ActionElement {

    private final BookInventory book;
    private final int currentPage;
    private final TransferButtonType type;
    private final FLMS plugin;

    public BookFlipper(FLMS plugin, BookInventory book, int currentPage, TransferButtonType type) {
        this.plugin = plugin;
        this.book = book;
        this.currentPage = currentPage;
        this.type = type;
        if (type != TransferButtonType.LEFT_ARROW && type != TransferButtonType.RIGHT_ARROW)
            throw new FLMSException("A book transfer button must have a left or right arrow type.");
    }

    @Override
    public void execute(Player player, InventoryClickEvent event) {
        int transferPage = (type == TransferButtonType.LEFT_ARROW) ? currentPage - 1 : currentPage + 1;
        plugin.soundLibrary.bookFlip(player);
        book.displayInterface(transferPage, player);
    }

    @Override
    public ItemStack getDisplayItem() {
        return type.getItem(plugin);
    }
}
