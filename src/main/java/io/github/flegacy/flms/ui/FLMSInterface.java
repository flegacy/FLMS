package io.github.flegacy.flms.ui;

import io.github.flegacy.flms.ui.element.InterfaceElement;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;;

public abstract class FLMSInterface implements InventoryHolder {

    private final Inventory inventory;
    private final Map<Integer, InterfaceElement> elements;

    protected FLMSInterface(int size, String title) {
        this.inventory = Bukkit.createInventory(this, size, Component.text(title));

        elements = new HashMap<>();
    }

    protected void setElement(int slot, @Nullable InterfaceElement element) {
        if (slot < 0 || slot >= inventory.getSize())
            throw new IndexOutOfBoundsException("Slot " + slot + " is out of bounds for length " + inventory.getSize());

        if (element == null) {
            elements.remove(slot);
            inventory.setItem(slot, null);
        } else {
            elements.put(slot, element);
            inventory.setItem(slot, element.displayItem());
        }
    }

    @Nullable
    protected InterfaceElement getElement(int slot) {
        return elements.get(slot);
    }

    protected Set<Integer> getOccupiedSlots() {
        return elements.keySet();
    }

    @Override
    public @NonNull Inventory getInventory() {
        return inventory;
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }
}
