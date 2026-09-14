package io.github.flegacy.flms.ui

import io.github.flegacy.flms.items.ItemLibrary
import io.github.flegacy.flms.ui.element.ClickableElement
import io.github.flegacy.flms.ui.element.FunctionalElement
import io.github.flegacy.flms.ui.element.InterfaceElement
import io.github.flegacy.flms.util.soundClick
import io.github.flegacy.flms.util.soundWuss
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ConfirmationInterface(confirm: (Player) -> Unit, origin: FLMSInterface? = null) :
    FLMSInterface(27, "<bold><italic>Are you sure?") {

    init {
        fill(object : InterfaceElement {
            override fun getDisplay(): ItemStack {
                return ItemLibrary.FILLER
            }
        })

        val cancelButton: ClickableElement =
            if (origin == null)
                FunctionalElement(ItemLibrary.CANCEL_ICON) { player, cursor ->
                    run {
                        soundClick(player)
                        player.closeInventory()

                    }
                }
            else
                FunctionalElement(ItemLibrary.CANCEL_ICON) { player, cursor ->
                    run {
                        soundWuss(player)
                        origin.open(player)

                    }
                }

        setElement(11, cancelButton)
        setElement(15, FunctionalElement(ItemLibrary.CONFIRM_ICON) { player, cursor -> confirm(player) })
    }


}
