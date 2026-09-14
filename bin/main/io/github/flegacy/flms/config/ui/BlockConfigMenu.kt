package io.github.flegacy.flms.config.ui

import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.FLMS_PERMISSION
import io.github.flegacy.flms.config.ui.element.BlockRepresentation
import io.github.flegacy.flms.items.ItemLibrary
import io.github.flegacy.flms.ui.BookInterface
import io.github.flegacy.flms.ui.element.FunctionalElement
import io.github.flegacy.flms.ui.element.InterfaceElement
import io.github.flegacy.flms.ui.element.TransferButton
import io.github.flegacy.flms.util.prefixed
import io.github.flegacy.flms.util.soundClick
import io.github.flegacy.flms.util.soundDelay

class BlockConfigMenu(private val origin: WandMenu, private val plugin: FLMS) : BookInterface("<bold>Edit Custom Blocks</bold>") {

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

        val create = FunctionalElement(ItemLibrary.CREATION_ICON) { player, cursor ->
            run {
                soundClick(player)
                BlockConfigurator(plugin, this).open(player)

            }
        }
        setGlobalBorderElement(46, create)
    }

    override fun removeElement(element: InterfaceElement) {
        super.removeElement(element)
        setupBorder()
    }


}
