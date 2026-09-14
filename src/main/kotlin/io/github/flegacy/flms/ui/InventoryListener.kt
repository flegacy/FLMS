package io.github.flegacy.flms.ui

import io.github.flegacy.flms.ui.element.ClickableElement
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent

class InventoryListener : Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.view.topInventory.holder !is FLMSInterface)
            return
        if ((event.clickedInventory ?: return) != event.view.topInventory)
            return
        event.isCancelled = true
        val openInterface = event.inventory.holder as FLMSInterface
        val correspondingElement = openInterface.getElement(event.rawSlot) ?: return
        if (correspondingElement is ClickableElement)
            correspondingElement.onClick(event)
    }

    @EventHandler
    fun onInventoryDrag(event: InventoryDragEvent) {
        if (event.view.topInventory.holder !is FLMSInterface)
            return
        event.isCancelled = true
    }
    
}
