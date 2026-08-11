package io.github.flegacy.flms.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.flegacy.flms.FLMS;
import io.github.flegacy.flms.command.branch.CommandBranch;
import io.github.flegacy.flms.command.branch.HelpCommandBranch;
import io.github.flegacy.flms.command.branch.TestCommandBranch;
import io.github.flegacy.flms.utils.Permissions;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class for the FLMS command.
 */
public class FLMSCommand {

	private static FLMSCommand instance;
	private static final String ROOT_LITERAL = "flms";

	private final List<CommandBranch> branches;

	/**
	 * Private constructor.
	 * @param plugin the FLMS plugin instance
	 */
	private FLMSCommand(FLMS plugin) {
		branches = new ArrayList<>();

		branches.add(new TestCommandBranch());
		branches.add(new HelpCommandBranch());
	}

	public LiteralCommandNode<CommandSourceStack> getCommandNode() {
		LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(ROOT_LITERAL);
		root.requires(sender -> sender.getSender().hasPermission(Permissions.COMMAND_PERMISSION));

		for (CommandBranch branch: branches)
			root.then(branch.getCommandTree());

		return root.build();
	}

	public static FLMSCommand getInstance(FLMS plugin) {
		if (instance == null)
			instance = new FLMSCommand(plugin);
		return instance;
	}
}
