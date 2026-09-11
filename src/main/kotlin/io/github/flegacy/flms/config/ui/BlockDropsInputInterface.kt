package io.github.flegacy.flms.config.ui

import io.github.flegacy.flms.ui.BookInterface
import io.github.flegacy.flms.ui.FLMSInterface

class BlockDropsInputInterface(private val origin: FLMSInterface): FLMSInterface(54, "Editing Block Drops...") {

    init {
        BookInterface.setupBorder(this)
    }
}