package io.github.flegacy.flms.ui

import io.github.flegacy.flms.ui.element.ClickableElement
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class InventoryListener: Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val clickedItem = event.currentItem ?: return
        if (event.inventory.holder !is FLMSInterface)
            return
        event.isCancelled = true
        val openInterface = event.inventory.holder as FLMSInterface
        val correspondingElement = openInterface.getElement(event.rawSlot) ?: return
        if (correspondingElement is ClickableElement)
            correspondingElement.onClick(event)
    }
}
