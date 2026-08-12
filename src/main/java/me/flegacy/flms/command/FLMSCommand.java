package me.flegacy.flms.command;

import me.flegacy.flms.FLMS;
import me.flegacy.flms.command.options.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class FLMSCommand implements CommandExecutor, TabCompleter {

	private static FLMSCommand instance;

	public static FLMSCommand getInstance(FLMS plugin) {
		if (instance == null)
			instance = new FLMSCommand(plugin);
		return instance;
	}

	private final List<CommandOption> commandOptions = new ArrayList<>();
	private final List<String> optionSuggestions = new ArrayList<>();

	private FLMSCommand(FLMS plugin) {
		commandOptions.add(new InfoOption(plugin));
		commandOptions.add(new EnchantOption(plugin));
		commandOptions.add(new HelpOption());
		commandOptions.add(new WandOption(plugin));
		commandOptions.add(new EffectOption(plugin));
		commandOptions.add(new TestOption(plugin));
		
		for (CommandOption option : commandOptions) {
			optionSuggestions.add(option.getName());
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {
			String firstArg = args[0];
			for (CommandOption option : commandOptions)
				if (option.getName().equalsIgnoreCase(firstArg))
					return option.execute(sender, command, label, args);

		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 1) {
			String firstArg = args[0];
			for (CommandOption option : commandOptions)
				if (option.getName().equalsIgnoreCase(firstArg))
					return option.getCompletionList(sender, command, label, args);

		} else if (args.length == 1) {
			return optionSuggestions;
		}
		return List.of();
	}
}
