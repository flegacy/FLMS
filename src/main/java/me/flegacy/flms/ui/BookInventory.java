package me.flegacy.flms.ui;

import me.flegacy.flms.FLMS;
import me.flegacy.flms.ui.elements.BookFlipper;
import me.flegacy.flms.ui.elements.DisplayElement;
import me.flegacy.flms.ui.elements.TransferButton;
import me.flegacy.flms.ui.elements.TransferButtonType;
import me.flegacy.flms.utils.FLMSException;
import me.flegacy.flms.utils.Text;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class BookInventory {
    // 36 items per page

    protected final FLMS plugin;
    private final List<FLMSInterface> inventories = new ArrayList<>();
    private final List<DisplayElement> elements = new ArrayList<>();
    private final String title;
    private FLMSInterface origin;
    private TransferButton backButton;

    public BookInventory(FLMS plugin, String title) {
        this.plugin = plugin;
        this.title = title;

        extend();
    }

    public BookInventory(FLMS plugin, String title, FLMSInterface origin) {
        this.plugin = plugin;
        this.title = title;
        this.origin = origin;
        this.backButton = new TransferButton(origin, TransferButtonType.EXIT_BUTTON.getItem(plugin));

        extend();
    }

    protected void addElement(DisplayElement element) {
        elements.add(element);
        if (elements.size() > 36 * inventories.size())
            extend();
    }

    protected void removeElement(DisplayElement element) {
        elements.remove(element);
        if (inventories.size() == 1)
            return;
        if (elements.size() < 36 * (inventories.size() - 1) + 1)
            trim();
    }

    protected void clearElements() {
        elements.clear();
        inventories.subList(1, inventories.size()).clear();
    }

    protected abstract void refreshElements();

    private void extend() {
        FLMSInterface newInterface = new FLMSInterface(plugin, title + Text.format("&e (Page " + (inventories.size() + 1) + ")"), 54);
        if (!inventories.isEmpty()) {
            BookFlipper rightButton = new BookFlipper(plugin, this, inventories.size() - 1, TransferButtonType.RIGHT_ARROW);
            BookFlipper leftButton = new BookFlipper(plugin, this, inventories.size(), TransferButtonType.LEFT_ARROW);
            inventories.getLast().setElement(53, rightButton);
            newInterface.setElement(45, leftButton);
        }
        if (origin != null) {
            int slot = (inventories.isEmpty()) ? 45 : 46;
            newInterface.setElement(slot, backButton);
        }
        inventories.add(newInterface);
    }

    private void trim() {
        if (inventories.size() <= 1)
            throw new FLMSException("A book inventory can't be trimmed; length is less than 2");
        inventories.get(inventories.size() - 2).removeElement(53, true);
    }

    private void refreshInterface(int page) {
        if (page < 0 || page >= inventories.size())
            throw new FLMSException("An invalid page number was entered when refreshing a book inventory.");
        FLMSInterface ui = inventories.get(page);
        for (int i = (page) * 36; i < 36 + page * 36; i++) {
            int uiSlot = 9 + (i - page * 36);
            ui.removeElement(uiSlot, false);
            if (i < elements.size())
                ui.setElement(uiSlot, elements.get(i));
        }
    }

    public void displayInterface(int page, Player player) {
        refreshElements();
        refreshInterface(page);
        inventories.get(page).show(player);
    }

    public FLMSInterface getFirstPage() {
        refreshElements();
        refreshInterface(0);
        return inventories.getFirst();
    }


}
