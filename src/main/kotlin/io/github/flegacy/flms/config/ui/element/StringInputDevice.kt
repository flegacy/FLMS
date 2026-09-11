package io.github.flegacy.flms.config.ui.element

import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.items.ItemStackBuilder
import io.github.flegacy.flms.ui.RefreshableInterface
import io.github.flegacy.flms.ui.element.ClickableElement
import io.github.flegacy.flms.util.FLMS_LIGHT_YELLOW
import io.github.flegacy.flms.util.FLMS_ORANGE
import io.github.flegacy.flms.util.FLMS_YELLOW
import io.github.flegacy.flms.util.errPrefixed
import io.github.flegacy.flms.util.prefixed
import io.github.flegacy.flms.util.soundError
import io.github.flegacy.flms.util.soundRequest
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class StringInputDevice(
    private val plugin: FLMS,
    private val origin: RefreshableInterface,
    private val condition: (String) -> Boolean,
    private val inputName: String,
    private val displayType: Material = Material.PAPER,
    private vararg val lore: String = emptyArray()
) : ClickableElement {


    var string = "Nothing"

    override fun getDisplay(): ItemStack {
        return ItemStackBuilder(displayType)
            .name("$FLMS_ORANGE$inputName")
            .lore(
                "",
                "${FLMS_LIGHT_YELLOW}Current: $FLMS_YELLOW$string",
                "",
                *lore,
                "",
                "${FLMS_YELLOW}<bold>CLICK TO EDIT"
            )
            .build()
    }

    override fun onClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        soundRequest(player)
        player.closeInventory()
        plugin.inputDeviceListener().deviceMap[player.uniqueId] = this

        player.sendMessage("")
        player.sendMessage(prefixed("Type your input in the chat, or type 'cancel' to cancel:"))
        player.sendMessage("")
    }

    fun tryInput(player: Player, input: String) {
        if (input.equals("cancel", true)) {
            player.sendMessage(prefixed("Cancelled."))
            origin.openRefreshable(player)
        } else if (condition(input)) {
            string = input
            origin.refresh()
            origin.openRefreshable(player)
        } else {
            soundError(player)
            player.sendMessage(errPrefixed("Invalid input. Try again: "))
            return
        }
        plugin.inputDeviceListener().deviceMap.remove(player.uniqueId)
    }
}


