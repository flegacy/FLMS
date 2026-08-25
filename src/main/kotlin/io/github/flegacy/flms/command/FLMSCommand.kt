package io.github.flegacy.flms.command

import com.mojang.brigadier.tree.LiteralCommandNode
import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.FLMS_PERMISSION
import io.github.flegacy.flms.command.branch.CommandBranch
import io.github.flegacy.flms.command.branch.EnchantCommandBranch
import io.github.flegacy.flms.command.branch.ReloadCommandBranch
import io.github.flegacy.flms.command.branch.WandCommandBranch
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.command.defaults.ReloadCommand

private const val ROOT_LITERAL = "flms"
// TODO see if permissions are working properly
private const val COMMAND_PERMISSION = "$FLMS_PERMISSION.command"

class FLMSCommand(plugin: FLMS) {

    private val branches = listOf<CommandBranch>(
        WandCommandBranch(plugin),
        EnchantCommandBranch(plugin),
        ReloadCommandBranch(plugin)
    )

    fun buildCommandNode() : LiteralCommandNode<CommandSourceStack> {
        val root = Commands.literal(ROOT_LITERAL).requires { source -> source.sender.hasPermission(COMMAND_PERMISSION) }

        for (branch in branches)
            root.then(branch.buildCommandTree())
        
        return root.build()
    }
}
