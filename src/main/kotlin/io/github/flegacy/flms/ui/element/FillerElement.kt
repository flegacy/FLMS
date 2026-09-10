package io.github.flegacy.flms.ui.element

import io.github.flegacy.flms.FLMS
import org.bukkit.inventory.ItemStack

class FillerElement(private val plugin: FLMS): InterfaceElement {

    override fun getDisplay(): ItemStack {
        return plugin.itemLib().emptyGlass()
    }

}

