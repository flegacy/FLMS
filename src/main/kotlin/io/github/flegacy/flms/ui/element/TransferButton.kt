package io.github.flegacy.flms.ui.element

import io.github.flegacy.flms.ui.FLMSInterface
import io.github.flegacy.flms.util.soundClick
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class TransferButton(private val display: ItemStack, private val goto: FLMSInterface): ClickableElement {

    override fun getDisplay(): ItemStack {
        return display.clone()
    }

    override fun onClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        soundClick(player)
        goto.open(player)
    }

}
