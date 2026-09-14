package io.github.flegacy.flms.config.ui.element

import io.github.flegacy.flms.config.ui.BlockDropsInputInterface
import io.github.flegacy.flms.ui.element.ClickableElement
import io.github.flegacy.flms.util.FLMS_GRAY
import io.github.flegacy.flms.util.FLMS_YELLOW
import io.github.flegacy.flms.util.msgFormat
import io.github.flegacy.flms.util.soundDestroy
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class DropRepresentation(private val item: ItemStack, private val origin: BlockDropsInputInterface) : ClickableElement {

    init {
        require(!item.type.isAir)
    }

    override fun getDisplay(): ItemStack {
        val display = item.clone()
        val meta = display.itemMeta!!

        if (!meta.hasLore())
            meta.lore(mutableListOf())


        meta.lore()!!.addFirst(msgFormat(""))
        meta.lore()!!.addFirst(msgFormat("<!i>${FLMS_GRAY}Block Drops"))
        meta.lore()!!.add(msgFormat(""))
        meta.lore()!!.add(msgFormat("<!i><bold>${FLMS_YELLOW}CLICK TO REMOVE"))
        display.itemMeta = meta
        return display
    }

    override fun onClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        soundDestroy(player)
        origin.drops.remove(item)
        origin.refresh()
    }
}
