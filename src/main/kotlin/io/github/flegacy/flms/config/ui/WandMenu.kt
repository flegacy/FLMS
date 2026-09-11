package io.github.flegacy.flms.config.ui

import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.items.ItemLibrary
import io.github.flegacy.flms.ui.FLMSInterface
import io.github.flegacy.flms.ui.element.FillerElement
import io.github.flegacy.flms.ui.element.TransferButton
import io.github.flegacy.flms.util.msgFormat
import net.kyori.adventure.text.Component

class WandMenu(plugin: FLMS): FLMSInterface(27, "FLMS Config Menu") {



    init {
        val filler = FillerElement()
        fill(filler)

        val blockConfig = BlockConfigMenu(this, plugin)
        val blockTransfer = TransferButton(ItemLibrary.BLOCK_CONFIG_ICON, blockConfig)
        setElement(11, blockTransfer)
    }


}
