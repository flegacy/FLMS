package io.github.flegacy.flms.command

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.FLMS_PERMISSION
import io.github.flegacy.flms.command.branch.CommandBranch
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands

private const val ROOT_LITERAL = "flms"
// TODO see if permissions are working properly
private const val COMMAND_PERMISSION = "$FLMS_PERMISSION.command"

class FLMSCommand(private val plugin: FLMS) {

    private val branches = mutableListOf<CommandBranch>()

    fun buildCommandNode() : LiteralCommandNode<CommandSourceStack> {
        val root = Commands.literal(ROOT_LITERAL)
        
        for (branch in branches)
            root.then(branch.buildCommandTree())
        
        return root.build()
    }
}
