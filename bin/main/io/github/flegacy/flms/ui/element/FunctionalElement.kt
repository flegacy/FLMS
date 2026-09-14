package io.github.flegacy.flms.ui.element

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class FunctionalElement(private val display: ItemStack, private val function: (Player, ItemStack) -> Unit) :
    ClickableElement {
    override fun getDisplay(): ItemStack {
        return display.clone()
    }

    override fun onClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        function(player, event.cursor)
    }
}
