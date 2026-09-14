package io.github.flegacy.flms.config.ui.element

import io.github.flegacy.flms.config.ui.BlockDropsInputInterface
import io.github.flegacy.flms.items.ItemStackBuilder
import io.github.flegacy.flms.ui.element.ClickableElement
import io.github.flegacy.flms.util.FLMS_GRAY
import io.github.flegacy.flms.util.FLMS_LIGHT_YELLOW
import io.github.flegacy.flms.util.FLMS_YELLOW
import io.github.flegacy.flms.util.msgFormat
import io.github.flegacy.flms.util.soundDestroy
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class DropRepresentation(private val item: ItemStack, private val origin: BlockDropsInputInterface) : ClickableElement {

    init {
        require(!item.type.isAir)
    }

    override fun getDisplay(): ItemStack {
        return ItemStackBuilder(item).lore(
            "${FLMS_GRAY}Block Drop",
            "",
            "${FLMS_LIGHT_YELLOW}<bold>CLICK TO REMOVE"
        )
        .amount(item.amount)
        .build()
    }

    override fun onClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        soundDestroy(player)
        origin.drops.remove(item)
        origin.refresh()
    }
}
