package io.github.flegacy.flms.config.ui

import io.github.flegacy.flms.config.ui.element.DropRepresentation
import io.github.flegacy.flms.items.ItemLibrary
import io.github.flegacy.flms.ui.BookInterface
import io.github.flegacy.flms.ui.FLMSInterface
import io.github.flegacy.flms.ui.RefreshableInterface
import io.github.flegacy.flms.ui.element.FunctionalElement
import io.github.flegacy.flms.ui.element.TransferButton
import io.github.flegacy.flms.util.soundError
import io.github.flegacy.flms.util.soundPickup
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class BlockDropsInputInterface(origin: FLMSInterface) : FLMSInterface(54, "<bold>Editing Block Drops..."),
    RefreshableInterface {

    val drops = mutableListOf<ItemStack>()

    init {
        BookInterface.setupBorder(this)
        val backButton = TransferButton(ItemLibrary.BACK_BUTTON, origin)
        setElement(45, backButton)

        val inputButton = FunctionalElement(
            ItemLibrary.ADD_DROP_ICON,
            function = { player, cursor ->
                run {
                    if (cursor.type.isAir) {
                        soundError(player)
                        return@run
                    }

                    if (drops.size == 28) {
                        soundError(player)
                        return@run
                    }

                    soundPickup(player)
                    Bukkit.broadcastMessage(cursor.type.toString())
                    drops.add(cursor)
                    refresh()
                }
            }
        )

        setElement(49, inputButton)
    }

    override fun refresh() {
        for (index in 0..<28) {
            elements.remove(BookInterface.PAGE_ITEM_INDEXES[index])
            inventory.setItem(BookInterface.PAGE_ITEM_INDEXES[index], null)
            if (index in drops.indices) {
                Bukkit.broadcastMessage("looping index${index} creating drop${drops[index]} ")
                setElement(BookInterface.PAGE_ITEM_INDEXES[index], DropRepresentation(drops[index], this))
            }
        }

    }

    override fun openRefreshable(player: Player) {
        open(player)
    }


}
