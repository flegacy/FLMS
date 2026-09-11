package io.github.flegacy.flms.ui.element

import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.items.ItemLibrary
import org.bukkit.inventory.ItemStack

class FillerElement: InterfaceElement {

    override fun getDisplay(): ItemStack {
        return ItemLibrary.FILLER
    }

}

