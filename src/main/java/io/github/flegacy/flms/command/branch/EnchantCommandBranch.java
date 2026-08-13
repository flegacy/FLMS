package io.github.flegacy.flms.command.branch;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.flegacy.flms.FLMS;
import io.github.flegacy.flms.items.FLMSEnchanter;
import io.github.flegacy.flms.utils.SoundPlayer;
import io.github.flegacy.flms.utils.TextConstants;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EnchantCommandBranch implements CommandBranch {

	private static final String BRANCH_LITERAL = "efficiency";
	private final FLMSEnchanter enchanter;

	public EnchantCommandBranch(FLMS plugin) {
		enchanter = plugin.getItemLibrary().getEnchanter();
	}

	@Override
	public LiteralArgumentBuilder<CommandSourceStack> getCommandTree() {
		return Commands.literal(BRANCH_LITERAL)
				.then(Commands.literal("get")
						.executes(ctx -> attemptGet(ctx.getSource())))
				.then(Commands.literal("remove")
						.executes(ctx -> attemptRemove(ctx.getSource())))
				.then(Commands.literal("set")
						.then(Commands.argument("level", IntegerArgumentType.integer())
								.then(Commands.argument("visibility", BoolArgumentType.bool())
										.executes(ctx -> attemptSet(
												ctx.getSource(),
												ctx.getArgument("level", int.class),
												ctx.getArgument("visibility", boolean.class)
										)))));
	}

	private int attemptGet(CommandSourceStack source) {
		if (!isEligible(source))
			return 0;
		final ItemStack heldItem = ((Player) source.getSender()).getInventory().getItemInMainHand();
		final int efficiencyLevel = enchanter.getEnchantLevel(heldItem);
		final Component msg;

		if (efficiencyLevel == 0)
			msg = TextConstants.prefixedMessage("Your held item isn't enchanted.");
		else
			msg = TextConstants.prefixedMessage(
					"Your held item has efficiency "
							+ TextConstants.FLMS_YELLOW + efficiencyLevel + TextConstants.FLMS_LIGHT_YELLOW + ".");
		source.getSender().sendMessage(msg);
		return Command.SINGLE_SUCCESS;
	}

	private int attemptRemove(CommandSourceStack source) {
		if (!isEligible(source))
			return 0;
		final Player player = (Player) source.getSender();
		final ItemStack heldItem = player.getInventory().getItemInMainHand();
		if (!enchanter.isEnchanted(heldItem)) {
			player.sendMessage(TextConstants.prefixedMessage("This item isn't enchanted."));
			SoundPlayer.playErrorSound(player);
			return 0;
		}

		enchanter.enchantEfficiency(heldItem, (short) 0, false);
		player.sendMessage(TextConstants.prefixedMessage("Removed FLMS efficiency from your held item."));
		return Command.SINGLE_SUCCESS;
	}

	private int attemptSet(CommandSourceStack source, int level, boolean visibility) {
		if (!isEligible(source))
			return 0;
		final Player player = (Player) source.getSender();
		final ItemStack heldItem = player.getInventory().getItemInMainHand();
        final short shortLevel;
        if (level > Short.MAX_VALUE)
            shortLevel = Short.MAX_VALUE;
        else
            shortLevel = (short) level;

		if (shortLevel < 0) {
			player.sendMessage(TextConstants.errorMessage("You can't enchant an item with negative efficiency."));
			SoundPlayer.playErrorSound(player);
			return 0;
		}

		final String visibilityInfo = (visibility)
				? "and it's showing!"
				: "but it's hidden...";

		enchanter.enchantEfficiency(heldItem, shortLevel, visibility);
		player.sendMessage(TextConstants.prefixedMessage(
				"You applied efficiency " + TextConstants.FLMS_YELLOW + shortLevel + TextConstants.FLMS_LIGHT_YELLOW
				+ ", " + visibilityInfo
		));
		SoundPlayer.playEnchantSound(player);
		return Command.SINGLE_SUCCESS;
	}



	private boolean isEligible(CommandSourceStack source) {
		if (!(source.getSender() instanceof Player player)) {
			source.getSender().sendMessage(TextConstants.errorMessage(TextConstants.ERROR_COMMAND_CONSOLE));
			return false;
		}

		// if player is holding nothing
		if (player.getInventory().getItemInMainHand().getType().isAir()) {
			player.sendMessage(TextConstants.errorMessage(TextConstants.ERROR_EMPTY_HAND));
			SoundPlayer.playErrorSound(player);
			return false;
		}
		return true;
	}
}
