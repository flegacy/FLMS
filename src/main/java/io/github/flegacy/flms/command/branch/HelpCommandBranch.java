package io.github.flegacy.flms.command.branch;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

import static io.github.flegacy.flms.utils.TextConstants.*;

public class HelpCommandBranch implements CommandBranch {

	private static final String BRANCH_LITERAL = "help";

	// TODO finish message once all commands are done.
	private static final Component MESSAGE = miniMessage(
			"<newline>" + FLMS_ORANGE + "<b>Plugin Information<reset>"
			+ "<newline>" + FLMS_LIGHT_YELLOW + " /flms " + FLMS_YELLOW + "info"
			+ "<newline>" + FLMS_ORANGE + "<b>Enchanting<reset>"
			+ "<newline>" + FLMS_LIGHT_YELLOW + " /flms " + FLMS_YELLOW + "enchant" + FLMS_LIGHT_YELLOW + "..."
			+ "<newline>" + FLMS_YELLOW + "    remove"
			+ "<newline>" + FLMS_YELLOW + "    set " + FLMS_LIGHT_YELLOW + "<" + FLMS_YELLOW + "level"  + FLMS_LIGHT_YELLOW
				+ "> <" + FLMS_YELLOW + "show" + FLMS_LIGHT_YELLOW + "/" + FLMS_YELLOW + "hide" + FLMS_LIGHT_YELLOW + ">"
			+ "<newline>" + FLMS_ORANGE + "<b>FLMS Wand<reset>"
			+ "<newline>" + FLMS_LIGHT_YELLOW + " /flms " + FLMS_YELLOW + "wand"
			+ "<newline>" + FLMS_ORANGE + "<b>Mining Effects<reset>"
			+ "<newline>" + FLMS_LIGHT_YELLOW + " /flms " + FLMS_YELLOW + "effect" + FLMS_LIGHT_YELLOW + "..."
	);


	@Override
	public LiteralArgumentBuilder<CommandSourceStack> getCommandTree() {
		return Commands.literal(BRANCH_LITERAL)
				.executes(ctx -> sendMessage(ctx.getSource()));
	}

	private int sendMessage(CommandSourceStack source) {
		CommandSender sender = source.getSender();
		sender.sendMessage(MESSAGE);
		return Command.SINGLE_SUCCESS;
	}
}
