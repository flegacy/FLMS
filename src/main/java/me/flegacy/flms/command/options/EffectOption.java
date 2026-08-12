package me.flegacy.flms.command.options;

import me.flegacy.flms.FLMS;
import me.flegacy.flms.mining.PlayerStats;
import me.flegacy.flms.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class EffectOption implements CommandOption {

    private static final String NAME = "effect";

    private final FLMS plugin;

    public EffectOption(FLMS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 4) return false;

        String option = args[1].toLowerCase();
        Player player = Bukkit.getPlayerExact(args[2]);
        String effect = args[3].toLowerCase();

        if (!option.equals("set") && !option.equals("remove") && !option.equals("get")) return false;
        if (player == null) {
            sender.sendMessage(Text.error("Couldn't find a player named &e" + args[2] + "&c. They have to be on the server!"));
            return true;
        }
        if (!effect.equals("haste") && !effect.equals("fatigue")) {
            sender.sendMessage(Text.error("Couldn't understand what effect you want to modify. Use /flms help for help."));
            return true;
        }

        PlayerStats stats = plugin.playerEffects.get(player.getUniqueId());

        if (option.equals("remove")) {
            if (effect.equals("haste")) stats.setHaste(0);
            else stats.setFatigue(0);
            sender.sendMessage(Text.standard("Removed " + effect + " effect from player " + player.getName() + ","));
            return true;
        } else if (option.equals("set")) {
            if (args.length < 5) return false;
            try {
                int level = Integer.parseInt(args[4]);
                if (level < 0 || level > 255) {
                    sender.sendMessage(Text.error("The effect level must be between or equal to 0 and 255."));
                    return true;
                }

                if (effect.equals("haste")) stats.setHaste(level);
                else stats.setFatigue(level);

                sender.sendMessage(Text.standard("Set player " + player.getName() + "'s " + effect + " effect level to " + level + "."));
                return true;

            } catch (NumberFormatException e) {
                return false;
            }
        // if option is "get"
        } else {
            int level = (effect.equals("haste")) ? stats.getHaste() : stats.getFatigue();
            sender.sendMessage(Text.standard("The player " + player.getName() + " has " + effect + " level " + level + "."));
            return true;
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public List<String> getCompletionList(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) return List.of("set", "remove", "get");
        if (args[1].equalsIgnoreCase("set")) {
            if (args.length == 3) return plugin.getOnlinePlayerNames();
            if (args.length == 4) return List.of("haste", "fatigue");
            if (args.length == 5) return List.of("<level>");
        } else if (args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("get")) {
            if (args.length == 3) return plugin.getOnlinePlayerNames();
            if (args.length == 4) return List.of("haste", "fatigue");
        }
        return List.of();
    }
}
