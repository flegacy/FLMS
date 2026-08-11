package io.github.flegacy.flms.command.branch;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;

/**
 * Interface for FLMS command branches that will be implemented by all branch classes.
 */
public interface CommandBranch {
	LiteralArgumentBuilder<CommandSourceStack> getCommandTree();
}
