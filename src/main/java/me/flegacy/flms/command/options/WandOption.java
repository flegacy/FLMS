package me.flegacy.flms.command.options;

import me.flegacy.flms.FLMS;
import me.flegacy.flms.utils.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class WandOption implements CommandOption {

	private static final String NAME = "wand";
	private final ItemStack wand;
	
	public WandOption(FLMS plugin) {
		wand = plugin.itemLibrary.createWand();
	}

	@Override
	public boolean execute(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage(Text.error("This command can only be used in-game."));
			return true;
		}

		if (player.getInventory().firstEmpty() == -1) {
			sender.sendMessage(Text.error("Your inventory is too full for this."));
			return true;
		}

		player.getInventory().addItem(wand);
		player.sendMessage(Text.standard("The config wand was added to your inventory."));
		return true;

	}

	@Override
	public List<String> getCompletionList(CommandSender sender, Command command, String label, String[] args) {
		return List.of();
	}

	@Override
	public String getName() {
		return NAME;
	}
}
