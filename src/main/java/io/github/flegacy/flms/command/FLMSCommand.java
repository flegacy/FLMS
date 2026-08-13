package io.github.flegacy.flms.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.flegacy.flms.FLMS;
import io.github.flegacy.flms.command.branch.*;
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

		// Add command branches here
		branches.add(new TestCommandBranch(plugin));
		branches.add(new HelpCommandBranch());
		branches.add(new WandCommandBranch(plugin));
		branches.add(new EnchantCommandBranch(plugin));
		branches.add(new ReloadCommandBranch(plugin));
	}

	public LiteralCommandNode<CommandSourceStack> getCommandNode() {
		final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(ROOT_LITERAL);
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
