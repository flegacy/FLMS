package io.github.flegacy.flms.config.ui

import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.FLMS_PERMISSION
import io.github.flegacy.flms.ui.BookInterface
import io.github.flegacy.flms.ui.FLMSInterface
import io.github.flegacy.flms.ui.element.InterfaceElement
import io.github.flegacy.flms.ui.element.TransferButton
import io.github.flegacy.flms.util.prefixed
import io.github.flegacy.flms.util.soundDelay

class BlockConfigMenu(private val origin: WandMenu, private val plugin: FLMS): BookInterface(plugin, "Edit Custom Blocks") {

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
        setupBack()

        for (block in plugin.registry().blocks()) {
            val configurator = BlockConfigurator(block)
            addElement(configurator)
        }
    }

    private fun setupBack() {
        val back = TransferButton(plugin.itemLib().backButton(), origin)
        setElement(36, back)
    }



}
