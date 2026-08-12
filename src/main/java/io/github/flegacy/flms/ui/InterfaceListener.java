package io.github.flegacy.flms.ui;

import io.github.flegacy.flms.FLMS;
import io.github.flegacy.flms.ui.element.ClickableElement;
import io.github.flegacy.flms.ui.element.InterfaceElement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InterfaceListener implements Listener {
	private static InterfaceListener instance;

	private InterfaceListener() {
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getClickedInventory() == null)
			return;
		if (!(event.getClickedInventory().getHolder(false) instanceof final FLMSInterface gui))
			return;
		event.setCancelled(true);
		final int clickedSlot = event.getRawSlot();
		final InterfaceElement clickedElement = gui.getElement(clickedSlot);
		if (clickedElement == null)
			return;
		if (!(clickedElement instanceof ClickableElement clickable))
			return;
		clickable.execute(event);
	}

	public static InterfaceListener getInstance() {
		if (instance == null)
			instance = new InterfaceListener();
		return instance;
	}
}
