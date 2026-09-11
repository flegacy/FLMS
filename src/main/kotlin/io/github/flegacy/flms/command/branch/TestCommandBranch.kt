package io.github.flegacy.flms.command.branch

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.util.soundSuccess
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.entity.Player

private const val BRANCH_LITERAL = "test"

class TestCommandBranch(private val plugin: FLMS): CommandBranch {

    override fun buildCommandTree(): LiteralArgumentBuilder<CommandSourceStack> {
        
        return Commands.literal(BRANCH_LITERAL)
            .executes { ctx -> executeTest(ctx.source) }
    }

    private fun executeTest(source: CommandSourceStack): Int {
        soundSuccess(source.sender as? Player ?: return 0, plugin)
        return Command.SINGLE_SUCCESS
    }
}
