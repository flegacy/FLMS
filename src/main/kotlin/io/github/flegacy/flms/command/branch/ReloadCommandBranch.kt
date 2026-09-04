package io.github.flegacy.flms.command.branch

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.util.errPrefixed
import io.github.flegacy.flms.util.prefixed
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands

private const val BRANCH_LITERAL = "reload"

class ReloadCommandBranch(private val plugin: FLMS): CommandBranch {
    override fun buildCommandTree(): LiteralArgumentBuilder<CommandSourceStack> {
        return Commands.literal(BRANCH_LITERAL)
            .executes { ctx -> execute(ctx.source) }
    }

    private fun execute(source: CommandSourceStack): Int {
        val result = plugin.configValues().reload()
        val msg = 
            if (result)
                prefixed("Sucessfully reloaded the config with no errors.")
            else
                errPrefixed("Reloaded the config with errors! Please check the console.")
        source.sender.sendMessage(msg)
        return Command.SINGLE_SUCCESS

    } 
}
