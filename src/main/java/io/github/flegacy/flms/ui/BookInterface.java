package io.github.flegacy.flms.ui;

import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.google.common.util.concurrent.SettableFuture;

import io.github.flegacy.flms.FLMS;
import io.github.flegacy.flms.items.ItemLibrary;
import io.github.flegacy.flms.items.ItemStackBuilder;
import io.github.flegacy.flms.ui.FLMSInterface;
import io.github.flegacy.flms.ui.element.DisplayElement;
import io.github.flegacy.flms.ui.element.InterfaceElement;
import io.github.flegacy.flms.ui.element.TransferButton;
import net.kyori.adventure.text.Component;

public abstract class BookInterface extends FLMSInterface {

    private static final int ITEMS_PER_PAGE = 28;

    private static void applyFiller(ItemLibrary lib, FLMSInterface page) {

        assert ((page instanceof BookInterface) || (page instanceof PageInterface))
                : "This method should only be used on book-related interfaces.";

        DisplayElement filler = new DisplayElement(lib.getEmptyGlass());
        for (int i = 0; i < 9; i++) {
            page.setElement(i, filler);
            page.setElement(i + 45, filler);
        }

        /*
         * filler > filler
         * V
         * filler > filler
         * V
         * filler > filler
         */
        for (int i = 9; i < 45; i += 9) {
            page.setElement(i, filler);
            page.setElement(i + 8, filler);
        }
    }

    private final FLMS plugin;
    private final List<PageInterface> pages;
    private final List<InterfaceElement> totalElements;
    private final String title;

    protected BookInterface(FLMS plugin, String title) {
        super(54, title + " (Page 1)");
        this.plugin = plugin;
        this.pages = new ArrayList<>();
        this.totalElements = new ArrayList<>();
        this.title = title;

        applyFiller(plugin.getItemLibrary(), this);
    }

    protected void addElement(InterfaceElement element) {
        totalElements.add(element);
        if (totalElements.size() > ITEMS_PER_PAGE * (pages.size() + 1))
            append();
        FLMSInterface last = (pages.size() > 0)
                ? pages.getLast()
                : this;
        last.setElement(last.getInventory().firstEmpty(), element);
    }
    // TODO implement a system where mutliple players can't edit a block at the same
    // time, you dont want to to trick the system into creating objects that arent
    // supposed to exist

    protected void removeElement(InterfaceElement element) {
        boolean removed = totalElements.remove(element);
        if (!removed)
            return;
        List<InterfaceElement> copiedList = List.copyOf(totalElements);
        totalElements.clear();
        pages.clear();
        resetMain();
        for (InterfaceElement copied : copiedList)
            addElement(copied);

    }

    private void resetMain() {
        this.getInventory().clear();
        applyFiller(plugin.getItemLibrary(), this);
    }

    private void append() {
        PageInterface newPage = new PageInterface(this);
        FLMSInterface previousPage = (pages.size() == 0)
                ? this
                : pages.get(pages.size() - 1);
        TransferButton connector = new TransferButton(plugin.getItemLibrary().getLeftArrow(), previousPage);
        newPage.setElement(45, connector);

        TransferButton connectorPrevious = new TransferButton(plugin.getItemLibrary().getRightArrow(), newPage);
        previousPage.setElement(53, connectorPrevious);

        TransferButton jumpToFirst = new TransferButton(plugin.getItemLibrary().getJumpToFirstButton(), this);
        TransferButton jumpToLast = new TransferButton(plugin.getItemLibrary().getJumpToLastButton(), newPage);
        this.setElement(45, jumpToLast);
        newPage.setElement(53, jumpToFirst);

        pages.add(newPage);
    }

    private void trim() {
        boolean willOverflow = ITEMS_PER_PAGE * (pages.size()) < totalElements.size();
        assert (!willOverflow) : "Can't trim a page with items in it.";

        pages.removeLast();

        if (pages.size() == 0) {
            DisplayElement filler = new DisplayElement(plugin.getItemLibrary().getEmptyGlass());
            this.setElement(45, filler);
            this.setElement(53, filler);
        } else {
            PageInterface newLast = pages.getLast();
            TransferButton jumpToFirst = new TransferButton(plugin.getItemLibrary().getJumpToFirstButton(), this);
            TransferButton jumpToLast = new TransferButton(plugin.getItemLibrary().getJumpToLastButton(), newLast);
            newLast.setElement(53, jumpToFirst);
            this.setElement(45, jumpToLast);
        }

    }

    public void openSpecific(int page, Player player) {
        pages.get(page).open(player);
    }

    private class PageInterface extends FLMSInterface {

        public PageInterface(BookInterface origin) {
            super(54, origin.title + " (Page " + (origin.pages.size() + 2) + ")");
            applyFiller(origin.plugin.getItemLibrary(), this);
        }
    }
}
