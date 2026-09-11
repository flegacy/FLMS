package io.github.flegacy.flms.config.ui

import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.FLMS_PERMISSION
import io.github.flegacy.flms.config.ui.element.BlockRepresentation
import io.github.flegacy.flms.items.ItemLibrary
import io.github.flegacy.flms.ui.BookInterface
import io.github.flegacy.flms.ui.element.ClickableElement
import io.github.flegacy.flms.ui.element.FunctionalElement
import io.github.flegacy.flms.ui.element.InterfaceElement
import io.github.flegacy.flms.ui.element.TransferButton
import io.github.flegacy.flms.util.prefixed
import io.github.flegacy.flms.util.soundClick
import io.github.flegacy.flms.util.soundDelay
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

class BlockConfigMenu(private val origin: WandMenu, private val plugin: FLMS): BookInterface("Edit Custom Blocks") {

    init {
        refresh()
    }

    fun refresh() {
        
        for (player in plugin.server.onlinePlayers) {
            if (!player.hasPermission(FLMS_PERMISSION))
                continue
            if (player.openInventory.topInventory.holder is BlockConfigMenu) {
                player.closeInventory()
                soundDelay(player)
                player.sendMessage(prefixed("Config has changed, refreshing."))
            }
        }

        clear()
        setupBorder()

        for (block in plugin.registry().blocks()) {
            val configurator = BlockRepresentation(plugin, block, this)
            addElement(configurator)
        }
    }

    private fun setupBorder() {
        val back = TransferButton(ItemLibrary.BACK_BUTTON, origin)
        setElement(36, back)

        val create = FunctionalElement(ItemLibrary.CREATION_ICON) {
            soundClick(it)
            BlockConfigurator(plugin, this).open(it)
        }
        setGlobalBorderElement(46, create)
    }

    override fun removeElement(element: InterfaceElement) {
        super.removeElement(element)
        setupBorder()
    }



}
