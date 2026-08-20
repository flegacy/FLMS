package io.github.flegacy.flms.command.branch;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.flegacy.flms.FLMS;
import io.github.flegacy.flms.utils.TextConstants;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.configuration.PluginMeta;
import net.kyori.adventure.text.Component;

import java.awt.print.Book;
import java.util.List;

import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class InfoCommandBranch implements CommandBranch {

	private static final String BRANCH_LITERAL = "info";

	private final FLMS plugin;
    // TODO improve info with actual help for the features of the plugin, and replace the help command with it

	public InfoCommandBranch(FLMS plugin) {
		this.plugin = plugin;
	}

	@Override
	public LiteralArgumentBuilder<CommandSourceStack> getCommandTree() {
		return Commands.literal(BRANCH_LITERAL)
				.executes(ctx -> sendInfo(ctx.getSource()));
	}

	private int sendInfo(CommandSourceStack source) {
		PluginMeta meta = plugin.getPluginMeta();
		List<Component> message = TextConstants.messageList(
				TextConstants.FLMS_ORANGE + "<b>FLEGACY'S MINING SPEED",
				TextConstants.FLMS_LIGHT_YELLOW + "- Description: " + TextConstants.FLMS_YELLOW + meta.getDescription(),
				TextConstants.FLMS_LIGHT_YELLOW + "- Version: " + TextConstants.FLMS_YELLOW + meta.getVersion(),
				TextConstants.FLMS_LIGHT_YELLOW + "- API-Version: " + TextConstants.FLMS_YELLOW + meta.getAPIVersion(),
				TextConstants.FLMS_LIGHT_YELLOW + "- Authors: " + TextConstants.FLMS_YELLOW + meta.getAuthors(),
				TextConstants.FLMS_LIGHT_YELLOW + "- Permission: '" + TextConstants.FLMS_YELLOW + FLMS.FLMS_PERMISSION + TextConstants.FLMS_LIGHT_YELLOW + "'"
				);
		for (Component component: message)
			source.getSender().sendMessage(component);
		return Command.SINGLE_SUCCESS;
	}
}
