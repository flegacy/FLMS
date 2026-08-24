package io.github.flegacy.flms.command.branch

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder import io.github.flegacy.flms.FLMS import io.github.flegacy.flms.util.ERROR_COMMAND_CONSOLE import io.github.flegacy.flms.util.ERROR_EMPTY_HAND
import io.github.flegacy.flms.util.FLMS_LIGHT_YELLOW
import io.github.flegacy.flms.util.FLMS_YELLOW
import io.github.flegacy.flms.util.errPrefixed
import io.github.flegacy.flms.util.prefixed
import io.github.flegacy.flms.util.soundEnchant
import io.github.flegacy.flms.util.soundError
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

private const val BRANCH_LITERAL = "efficiency"

class EnchantCommandBranch(private val plugin: FLMS) : CommandBranch {

    override fun buildCommandTree(): LiteralArgumentBuilder<CommandSourceStack> {
        return Commands.literal(BRANCH_LITERAL)
            .then(
                Commands.literal("get")
                    .executes { ctx -> effGet(ctx.source) })
            .then(
                Commands.literal("remove")
                    .executes { ctx -> effRemove(ctx.source) })
            .then(
                Commands.literal("set")
                    .then(
                        Commands.argument("level", IntegerArgumentType.integer(1, 255))
                            .then(
                                Commands.argument("visible", BoolArgumentType.bool())
                                    .executes { ctx ->
                                        effSet(
                                            ctx.source,
                                            ctx.getArgument("level", Int::class.java),
                                            ctx.getArgument("visible", Boolean::class.java)
                                        )
                                    })
                    )
            )

    }

    private fun effGet(source: CommandSourceStack): Int {
        if (!checkEligible(source))
            return 0
        val player = source.sender as Player
        val held = player.inventory.itemInMainHand
        val level = plugin.itemLibrary.enchanter.level(held)

        val msg = 
            if (level == 0.toShort())
                prefixed("Your item isn't enchanted.")
            else
                prefixed("Your held item has efficiency $FLMS_YELLOW$level${FLMS_LIGHT_YELLOW}.")

        player.sendMessage(msg)
        return Command.SINGLE_SUCCESS
    }

    private fun effSet(source: CommandSourceStack, intLevel: Int, visible: Boolean): Int {
        if (!checkEligible(source))
            return 0
        
        val player = source.sender as Player
        val held = player.inventory.itemInMainHand
        val level = intLevel.toShort()
        // Brigadier should guarantee the input level being positive and between or equal to 1 and 255
        
        val visMsg = 
            if (visible)
                "and it's showing!"
            else
                "but it's hidden..."
        plugin.itemLibrary.enchanter.effApply(held, level, visible)
        player.sendMessage(prefixed("You applied efficiency $FLMS_YELLOW$level${FLMS_LIGHT_YELLOW}, $visMsg"))
        soundEnchant(player)
        return Command.SINGLE_SUCCESS
    }

    private fun effRemove(source: CommandSourceStack): Int {
        if (!checkEligible(source))
            return 0
        val player = source.sender as Player
        val held = player.inventory.itemInMainHand
        if (!plugin.itemLibrary.enchanter.hasEff(held)) {
            player.sendMessage(errPrefixed("This item isn't enchanted."))
            soundError(player)
            return 0
        }

        plugin.itemLibrary.enchanter.effApply(held, 0.toShort(), false)
        player.sendMessage(prefixed("Removed FLMS efficiency from your held item."))
        return Command.SINGLE_SUCCESS
    }

    private fun checkEligible(source: CommandSourceStack): Boolean {
        if (source.sender !is Player) {
            source.sender.sendMessage(errPrefixed(ERROR_COMMAND_CONSOLE))
            return false
        }

        val player = source.sender as Player
        if (player.inventory.itemInMainHand.type.isAir) {
            player.sendMessage(errPrefixed(ERROR_EMPTY_HAND))
            soundError(player)
            return false
        }

        return true
    }
}
