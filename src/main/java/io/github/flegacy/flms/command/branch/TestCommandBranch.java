package io.github.flegacy.flms.command.branch;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.flegacy.flms.FLMS;
import io.github.flegacy.flms.ui.FLMSInterface;
import io.github.flegacy.flms.ui.element.ClickableElement;
import io.github.flegacy.flms.ui.element.DisplayElement;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
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
		if (!(source.getSender() instanceof Player player))
			return 0;

		TestInventory testInv = new TestInventory(plugin);
		testInv.open(player);

		return Command.SINGLE_SUCCESS;
	}

	public class TestInventory extends FLMSInterface {

		protected TestInventory(FLMS plugin) {
			super(27, Component.text("Test Interface"), plugin);
			ClickableElement clickable = new ClickableElement() {
				@Override
				public void execute(InventoryClickEvent event) {
					event.getWhoClicked().sendMessage("Hey there!");
				}

				@Override
				public ItemStack getDisplayItem() {
					return new ItemStack(Material.DIAMOND_SWORD);
				}
			};
			setElement(13, clickable);
			setElement(14, new DisplayElement(new ItemStack(Material.STONE_PICKAXE)));
		}
	}


}
