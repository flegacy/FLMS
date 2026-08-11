package io.github.flegacy.flms.command.branch;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.flegacy.flms.FLMS;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.awt.*;
import java.sql.Array;
import java.util.UUID;

/**
 * Branch for the test option in the FLMS command.
 */
public class TestCommandBranch implements CommandBranch {

	private static final String BRANCH_LITERAL = "test";
	private static final String DEV_UUID = "6a8a501c-ea79-4d9b-8a41-8a5cfb029d61";

	private final FLMS plugin;

	public TestCommandBranch(FLMS plugin) {
		this.plugin = plugin;
	}

	@Override
	public LiteralArgumentBuilder<CommandSourceStack> getCommandTree() {
		return Commands.literal(BRANCH_LITERAL)
				.requires(this::hasTestingUUID)
				.executes(ctx -> executeTest(ctx.getSource()));
	}

	private boolean hasTestingUUID(CommandSourceStack source) {
		if (!(source.getSender() instanceof Player player))
			return false;
		return (player.getUniqueId().equals(UUID.fromString(DEV_UUID)));
	}

	private int executeTest(CommandSourceStack source) {
		source.getSender().sendMessage("Hey! You just used the testing command.");
		return Command.SINGLE_SUCCESS;
	}



	private final PotionEffect haste = new PotionEffect(
			PotionEffectType.HASTE,
			PotionEffect.INFINITE_DURATION,
			1,
			true,
			false,
			false
	);

	private final PotionEffect fatigue = new PotionEffect(
			PotionEffectType.MINING_FATIGUE,
			PotionEffect.INFINITE_DURATION,
			2,
			true,
			false,
			false
	);
}
