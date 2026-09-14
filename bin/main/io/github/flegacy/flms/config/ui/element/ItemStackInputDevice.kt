package io.github.flegacy.flms.config.ui.element

import io.github.flegacy.flms.ui.RefreshableInterface
import io.github.flegacy.flms.ui.element.ClickableElement
import io.github.flegacy.flms.util.*
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

class ItemStackInputDevice(
    private val origin: RefreshableInterface,
    private val inputTitle: String,
    private val emptyDisplay: ItemStack,
    private val condition: (ItemStack) -> Boolean
) : ClickableElement {


    var item: ItemStack? = null

    private fun itemIsEmpty(): Boolean {
        return (item == null || (item ?: return false).type.isAir)
    }

    override fun getDisplay(): ItemStack {
        if (itemIsEmpty()) {
            return emptyDisplay.clone()
        } else {
            val display = item!!.clone()
            display.amount = 1
            val meta = display.itemMeta
            val lore = meta.lore() ?: mutableListOf<Component>()
            lore.addFirst(msgFormat(""))
            lore.addFirst(msgFormat("<reset><!i>$FLMS_WHITE${resolveName(item)}"))
            lore.add(msgFormat("<reset><!i>${FLMS_YELLOW}<bold>RIGHT-CLICK TO REMOVE"))
            meta.lore(lore)
            meta.customName(msgFormat("<reset><!i>$FLMS_YELLOW$inputTitle"))
            meta.addItemFlags(*ItemFlag.entries.toTypedArray())
            display.itemMeta = meta
            return display
        }
    }

    override fun onClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        if (itemIsEmpty() && !event.cursor.type.isAir) {
            if (!condition(event.cursor)) {
                soundError(player)
                return
            }
            soundPickup(player)
            item = event.cursor.clone()
            origin.refresh()
        } else if (!itemIsEmpty() && (event.click == ClickType.RIGHT || event.click == ClickType.SHIFT_RIGHT)) {
            soundDestroy(player)
            item = null
            origin.refresh()
        }
    }

}