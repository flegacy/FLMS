package io.github.flegacy.flms.command.branch;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import io.github.flegacy.flms.FLMS;
import io.github.flegacy.flms.utils.TextConstants;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public class ReloadCommandBranch implements CommandBranch {
	private static final String BRANCH_LITERAL = "reload";

	private final FLMS plugin;

	public ReloadCommandBranch(FLMS plugin) {
		this.plugin = plugin;
	}

	@Override
	public LiteralArgumentBuilder<CommandSourceStack> getCommandTree() {
		return Commands.literal(BRANCH_LITERAL)
				.executes(ctx -> processCommand(ctx.getSource()));
	}

	private int processCommand(CommandSourceStack source) {
		boolean result = plugin.getConfigValues().reload();
		String msg = (result)
				? "Successfully reloaded the config with no errors"
				: "Reloaded the config with errors! Please check the console.";
		source.getSender().sendMessage(TextConstants.prefixedMessage(msg));
		return Command.SINGLE_SUCCESS;
	}

}
