package io.github.flegacy.flms.command.branch

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.util.ERROR_COMMAND_CONSOLE
import io.github.flegacy.flms.util.ERROR_INVENTORY_FULL
import io.github.flegacy.flms.util.errPrefixed
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.entity.Player

private const val BRANCH_LITERAL = "wand"

class WandCommandBranch(private val plugin: FLMS): CommandBranch {

    override fun buildCommandTree(): LiteralArgumentBuilder<CommandSourceStack> {
        return Commands.literal()
    }

    private fun execute(source: CommandSourceStack): Int {
        if (source.sender !is Player) {
            source.sender.sendMessage(errPrefixed(ERROR_COMMAND_CONSOLE))
            return 0
        }

        val player = source.sender as Player

        if (player.inventory.firstEmpty() == -1) {
            player.sendMessage(errPrefixed(ERROR_INVENTORY_FULL))
            return 0
        }


        player.inventory.addItem(plugin.itemLibrary.wand())
        player.sendMessage(errPrefixed("The FLMS wand was added to your inventory."))
    }

}
