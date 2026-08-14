package io.github.flegacy.flms.command.branch;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.flegacy.flms.FLMS;
import io.github.flegacy.flms.registry.RegisteredBlock;
import io.github.flegacy.flms.ui.FLMSInterface;
import io.github.flegacy.flms.ui.element.ClickableElement;
import io.github.flegacy.flms.ui.element.DisplayElement;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import jdk.jfr.Registered;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

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
		if (!(source.getSender() instanceof Player player))
			return 0;

		RegisteredBlock testBlock = new RegisteredBlock(Material.GRASS_BLOCK);
		plugin.getRegistry().register(testBlock);

		return Command.SINGLE_SUCCESS;
	}
}
