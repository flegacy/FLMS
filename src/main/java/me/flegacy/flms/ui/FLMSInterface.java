package me.flegacy.flms.ui;

import me.flegacy.flms.FLMS;
import me.flegacy.flms.ui.elements.DisplayElement;
import me.flegacy.flms.ui.elements.FillerElement;
import me.flegacy.flms.ui.elements.Identifier;
import me.flegacy.flms.utils.FLMSException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FLMSInterface {

    private final Map<UUID, Inventory> inventoryCache = new HashMap<>();
    private final String name;
    protected final FLMS plugin;
    private final int maxSize;
    private final Map<Integer, DisplayElement> elements = new HashMap<>();
    private final FillerElement filler;

    public FLMSInterface(FLMS plugin, String name, int size) {
        this.plugin = plugin;
        this.name = name;
        maxSize = size;

        if (size < 9 || size > 54)
            throw new FLMSException("An inventory was created with an invalid size. (Must be between 9 and 54)");
        if (size % 9 != 0)
            throw new FLMSException("An inventory was created with an invalid size. (Must be a multiple of 9)");

        elements.put(0, new Identifier(this));
        FillerElement filler = new FillerElement(plugin.itemLibrary.createEmptyGlass());
        this.filler = filler;
        for (int i = 1; i < size; i++)
            elements.put(i, filler);

    }

    public FLMS getFLMSInstance() {
        return plugin;
    }

    public void refresh() {
        inventoryCache.clear();
    }

    protected void setElement(int slot, DisplayElement element) {
        if (slot == 0)
            throw new FLMSException("An identifier slot was replaced in an FLMS inventory.");
        if (slot < 0 || slot > maxSize - 1)
            throw new FLMSException("An inventory element was placed in a slot out of bounds.");
        elements.put(slot, element);
        refresh();
    }

    protected void removeElement(int slot, boolean filler) {
        if (filler)
            setElement(slot, this.filler);
        else
            elements.remove(slot);
        refresh();
    }

    public DisplayElement getElement(int index) {
        return elements.get(index);
    }

    public void show(Player player) {
        if (inventoryCache.containsKey(player.getUniqueId()))
            player.openInventory(inventoryCache.get(player.getUniqueId()));
        else {
            Inventory inventory = Bukkit.createInventory(player, maxSize, name);

            for (Integer index : elements.keySet())
                inventory.setItem(index, elements.get(index).getDisplayItem());

            inventoryCache.put(player.getUniqueId(), inventory);
            player.openInventory(inventory);
        }
    }
}
