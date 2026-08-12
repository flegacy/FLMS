package me.flegacy.flms.command.options;

import me.flegacy.flms.FLMS;
import me.flegacy.flms.utils.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class InfoOption implements CommandOption {

    private static final String NAME = "info";
    private static final List<String> COMPLETIONS = List.of();

    private final FLMS plugin;

    public InfoOption(FLMS plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public List<String> getCompletionList(CommandSender sender, Command command, String label, String[] args) {
        return COMPLETIONS;
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        List<String> response = Text.formatList(
                "&6&lFLEGACY'S MINING SPEED",
                "&f- Description: &e" + plugin.getDescription().getDescription(),
                "&f- Version: &e" + plugin.getDescription().getVersion(),
                "&f- API-Version: &e" + plugin.getDescription().getAPIVersion(),
                "&f- Authors: &e" + plugin.getDescription().getAuthors(),
                "&f -Permission: &e'" + FLMS.FLMS_PERMISSION + "'"
        );
        for (String s : response) {
            sender.sendMessage(s);
        }

        return true;
    }

}
