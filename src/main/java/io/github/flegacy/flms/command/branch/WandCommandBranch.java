package io.github.flegacy.flms.command.branch;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.flegacy.flms.FLMS;
import io.github.flegacy.flms.ItemLibrary;
import io.github.flegacy.flms.utils.Sounds;
import io.github.flegacy.flms.utils.TextConstants;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;

public class WandCommandBranch implements CommandBranch {

	private static final String BRANCH_LITERAL = "wand";

	private final ItemLibrary itemLibrary;

	public WandCommandBranch(FLMS plugin) {
		itemLibrary = plugin.getItemLibrary();
	}

	@Override
	public LiteralArgumentBuilder<CommandSourceStack> getCommandTree() {
		return Commands.literal(BRANCH_LITERAL)
				.executes(ctk -> givePlayerWand(ctk.getSource()));
	}

	private int givePlayerWand(CommandSourceStack source) {
		if (!(source.getSender() instanceof Player player)) {
			source.getSender().sendMessage(TextConstants.errorMessage(TextConstants.ERROR_COMMAND_CONSOLE));
			return 0;
		}
		if (player.getInventory().firstEmpty() != -1) {
			player.getInventory().addItem(itemLibrary.getWand());
			player.sendMessage(TextConstants.prefixedMessage("Right click with the wand to use it!"));
			Sounds.playItemPickupSound(player);
			return Command.SINGLE_SUCCESS;
		}
		else {
			player.sendMessage(TextConstants.errorMessage(TextConstants.ERROR_INVENTORY_FULL));
			Sounds.playErrorSound(player);
			return 0;
		}
	}
}
