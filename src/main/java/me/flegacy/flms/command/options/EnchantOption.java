package me.flegacy.flms.command.options;

import me.flegacy.flms.FLMS;
import me.flegacy.flms.utils.ItemLibrary;
import me.flegacy.flms.utils.Text;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EnchantOption implements CommandOption {

	private static final String NAME = "efficiency";
	private final ItemLibrary library;

	public EnchantOption(FLMS plugin) {
		library = plugin.itemLibrary;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean execute(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player player) {
			ItemStack heldItem = player.getInventory().getItemInMainHand();

			if (heldItem.getType() == Material.AIR) {
				player.sendMessage(Text.error("You must be holding an item to use this command."));
				return true;
			}
			
			// Should be at least "efficiency set" or "efficiency remove"
			if (args.length < 2) return false;

			String action = args[1].toLowerCase();

            switch (action) {
                case "set" -> {
                    if (args.length < 4) return false;

                    int level;
                    try {
                        level = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(Text.error("You didn't enter the enchantment level properly; it must be a whole number. Usage: /flms efficiency set <level> <show/hide>"));
                        return true;
                    }

                    if (level < 0 || level > 255) {
                        sender.sendMessage(Text.error("Use an enchantment level between or equal to 0 and 255."));
                        return true;
                    }

                    if (level == 0 && library.getEnchantLevel(heldItem) == 0) {
                        player.sendMessage(Text.standard("This item already has no efficiency."));
                        return true;
                    }

                    String visibilityArg = args[3].toLowerCase();
                    boolean visibility;

                    if (visibilityArg.equals("show")) visibility = true;
                    else if (visibilityArg.equals("hide")) visibility = false;
                    else {
                        sender.sendMessage(Text.error("Do you want to show or hide the enchantment glint? Usage: /flms efficiency set " + level + " <show/hide>"));
                        return true;
                    }

                    library.enchantEfficiency(heldItem, level, visibility);
                    sender.sendMessage(Text.standard("Enchanted your item with efficiency " + level + "."));
                    return true;
                }
                case "remove" -> {

                    if (library.getEnchantLevel(heldItem) == 0) {
                        player.sendMessage(Text.standard("This item already has no efficiency."));
                        return true;
                    }

                    library.enchantEfficiency(heldItem, 0, true);
                    player.sendMessage(Text.standard("Removed efficiency from your held item."));
                    return true;
                }
                case "get" -> {
                    player.sendMessage(Text.standard("Your held item has efficiency " + library.getEnchantLevel(heldItem) + "."));
                    return true;
                }
                default -> {
                    return false;
                }
            }
		} else {
			sender.sendMessage(Text.error("This command can only be used in-game."));
			return true;
		}
	}

	@Override
	public List<String> getCompletionList(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 2) return List.of("set", "remove", "get");
		if (args[1].equalsIgnoreCase("set")) {
			if (args.length == 3) return List.of("<level>");
			if (args.length == 4) return List.of("show", "hide");
		}

		return List.of();
	}

}
