package me.flegacy.flms.command.options;

import me.flegacy.flms.utils.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class HelpOption implements CommandOption {

    private static final String NAME = "help";

    public HelpOption() {}

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {

        List<String> msgs = Text.formatList(
                "",
                "&6&lInformation",
                "&f /flms &einfo",
                "&6&lEnchanting",
                "&f /flms &eefficiency&f...",
                "&f  &eremove",
                "&f  &eset &f<&elevel&f> <&eshow&f/&ehide&f>",
                "&6&lFLMS Wand",
				"&f /flms &ewand&f"
        );
        for (String s : msgs) {
            sender.sendMessage(s);
        }
        return true;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public List<String> getCompletionList(CommandSender sender, Command command, String label, String[] args) {
        return List.of();
    }
}
