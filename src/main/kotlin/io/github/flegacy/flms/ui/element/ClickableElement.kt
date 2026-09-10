package io.github.flegacy.flms.ui.element

import org.bukkit.event.inventory.InventoryClickEvent

interface ClickableElement: InterfaceElement {
    fun onClick(event: InventoryClickEvent)
}
