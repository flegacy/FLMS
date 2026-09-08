package io.github.flegacy.flms.command.branch

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.registry.RegisteredBlock
import io.github.flegacy.flms.util.resolveName
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.Bukkit
import org.bukkit.Material

private const val BRANCH_LITERAL = "test"

class TestCommandBranch(private val plugin: FLMS): CommandBranch {

    override fun buildCommandTree(): LiteralArgumentBuilder<CommandSourceStack> {
        
        return Commands.literal(BRANCH_LITERAL)
            .executes { ctx -> executeTest(ctx.source) }
    }

    private fun executeTest(source: CommandSourceStack): Int {

        val block = plugin.registry().findBlock(Material.DIAMOND_BLOCK) ?: return 0
        val p = source.sender
        p.sendMessage(block.name)
        p.sendMessage(block.type.toString())
        p.sendMessage(block.hardness.toString())
        p.sendMessage(block.postType.toString())
        p.sendMessage(block.drops.toString())
        p.sendMessage(block.xp.toString())


        return Command.SINGLE_SUCCESS
    }

}
