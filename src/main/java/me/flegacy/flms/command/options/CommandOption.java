package me.flegacy.flms.command.options;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface CommandOption {
	boolean execute(CommandSender sender, Command command, String label, String[] args);
	String getName();
	List<String> getCompletionList(CommandSender sender, Command command, String label, String[] args);

}
