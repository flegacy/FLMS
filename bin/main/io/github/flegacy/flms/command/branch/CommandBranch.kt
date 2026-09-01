package io.github.flegacy.flms.command.branch

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack

interface CommandBranch {
    fun buildCommandTree() : LiteralArgumentBuilder<CommandSourceStack>
}
