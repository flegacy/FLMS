package io.github.flegacy.flms.command.branch

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.registry.RegisteredBlock
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

private const val BRANCH_LITERAL = "test"

class TestCommandBranch(private val plugin: FLMS): CommandBranch {

    override fun buildCommandTree(): LiteralArgumentBuilder<CommandSourceStack> {
        
        return Commands.literal(BRANCH_LITERAL)
            .executes { ctx -> executeTest(ctx.source) }
    }

    private fun executeTest(source: CommandSourceStack): Int {

        return Command.SINGLE_SUCCESS
    }

}
