package io.github.flegacy.flms.config.ui.element

import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.config.ui.BlockConfigMenu
import io.github.flegacy.flms.config.ui.BlockConfigurator
import io.github.flegacy.flms.items.ItemStackBuilder
import io.github.flegacy.flms.registry.RegisteredBlock
import io.github.flegacy.flms.ui.ConfirmationInterface
import io.github.flegacy.flms.ui.element.ClickableElement
import io.github.flegacy.flms.util.FLMS_GRAY
import io.github.flegacy.flms.util.FLMS_LIGHT_YELLOW
import io.github.flegacy.flms.util.FLMS_ORANGE
import io.github.flegacy.flms.util.FLMS_YELLOW
import io.github.flegacy.flms.util.errPrefixed
import io.github.flegacy.flms.util.resolveName
import io.github.flegacy.flms.util.soundClick
import io.github.flegacy.flms.util.soundDelay
import io.github.flegacy.flms.util.soundDestroy
import io.github.flegacy.flms.util.soundError
import io.github.flegacy.flms.util.soundPickup
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class BlockRepresentation(private val plugin: FLMS, private val block: RegisteredBlock, private val origin: BlockConfigMenu): ClickableElement {

    companion object {
        val LEFT_CLICKS = listOf(ClickType.SHIFT_LEFT, ClickType.LEFT)
    }

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
                "${FLMS_YELLOW}<bold>RIGHT-CLICK TO GET",
                "${FLMS_ORANGE}<bold>SHIFT-RIGHT-CLICK TO DELETE"
            )
            .build()
    }

    override fun getDisplay(): ItemStack {
        return display
    }

    override fun onClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return

        if (plugin.registry().findBlock(block.type) == null) {
            soundDelay(player)
            player.closeInventory()
            player.sendMessage(errPrefixed("That block doesn't seem to exist anymore. Refreshing..."))
            return
        }

        if (event.click in LEFT_CLICKS) {
            soundClick(player)
            BlockConfigurator(plugin, block, origin).open(player)

        } else if (event.click == ClickType.SHIFT_RIGHT) {
            val confirm = ConfirmationInterface({
                plugin.registry().remove(block)
                soundDestroy(player)
                origin.refresh()
                origin.open(player)
            }, origin)
            soundClick(player)
            confirm.open(player)
        } else if (event.click == ClickType.RIGHT) {
            if (player.inventory.firstEmpty() == -1) {
                soundError(player)
                return
            }
            soundPickup(player)
            player.inventory.addItem(ItemStack(block.type))
        }
    }

}
