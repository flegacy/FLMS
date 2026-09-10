package io.github.flegacy.flms.config.ui

import io.github.flegacy.flms.items.ItemStackBuilder
import io.github.flegacy.flms.registry.RegisteredBlock
import io.github.flegacy.flms.ui.element.ClickableElement
import io.github.flegacy.flms.util.FLMS_GRAY
import io.github.flegacy.flms.util.FLMS_LIGHT_YELLOW
import io.github.flegacy.flms.util.FLMS_ORANGE
import io.github.flegacy.flms.util.FLMS_YELLOW
import io.github.flegacy.flms.util.resolveName
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class BlockConfigurator(block: RegisteredBlock): ClickableElement {

    private val display: ItemStack

    init {
        val dropsLore = mutableListOf<String>()
        block.drops.forEach { dropsLore.add(" ${FLMS_GRAY}x${it.amount} ${FLMS_LIGHT_YELLOW}${resolveName(it)}") }

        val dropsLine = 
            if (dropsLore.isEmpty())
                "${FLMS_LIGHT_YELLOW}Drops: ${FLMS_GRAY}None!"
            else
                "${FLMS_LIGHT_YELLOW}Drops: ${FLMS_YELLOW}↴"

        display = ItemStackBuilder(block.type)
            .name("$FLMS_ORANGE${resolveName(block.type)}")
            .lore(
                "",
                "${FLMS_LIGHT_YELLOW}Name: ${FLMS_YELLOW}${block.name}",
                "${FLMS_LIGHT_YELLOW}Hardness: ${FLMS_YELLOW}${block.hardness}",
                "${FLMS_LIGHT_YELLOW}Dropped XP: ${FLMS_YELLOW}${block.xp}",
                "${FLMS_LIGHT_YELLOW}Post-Break Block: ${FLMS_YELLOW}${resolveName(block.postType)}",
                dropsLine,
                *dropsLore.toTypedArray(),
                "",
                "${FLMS_LIGHT_YELLOW}<bold>LEFT-CLICK TO EDIT",
                "${FLMS_YELLOW}<bold>MIDDLE-CLICK TO DELETE",
                "${FLMS_ORANGE}<bold>RIGHT-CLICK TO GET",
            )
            .build()
    }

    override fun getDisplay(): ItemStack {
        return display
    }

    override fun onClick(event: InventoryClickEvent) {
        // TODO this
    }

}
