package io.github.flegacy.flms.ui;

import io.github.flegacy.flms.FLMS;
import io.github.flegacy.flms.ui.element.DisplayElement;
import io.github.flegacy.flms.ui.element.InterfaceElement;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class FLMSInterface implements InventoryHolder {

	private final Inventory inventory;
	private final Map<Integer, InterfaceElement> elements;
    private final FLMS plugin;

	protected FLMSInterface(int size, @NotNull Component title, FLMS plugin) {
        this.plugin = plugin;
		this.inventory = Bukkit.createInventory(this, size, title);

		elements = new HashMap<>();

		final DisplayElement filler = new DisplayElement(plugin.getItemLibrary().getEmptyGlass());
		for (int i = 0; i < size; i++) {
			setElement(i, filler);

		}
	}

	protected void setElement(int slot, InterfaceElement element) {
		if (slot < 0 || slot >= inventory.getSize())
			throw new IndexOutOfBoundsException("Slot " + slot + " is out of bounds for length " + inventory.getSize());
		elements.put(slot, element);
		inventory.setItem(slot, element.getDisplayItem());
	}

	@Nullable
	protected InterfaceElement getElement(int slot) {
		return elements.get(slot);
	}

	@Override
	public @NonNull Inventory getInventory() {
		return inventory;
	}

	public void open(Player player) {
		player.openInventory(inventory);
	}

    public FLMS getFLMSInstance() {
        return plugin;
    }



}
