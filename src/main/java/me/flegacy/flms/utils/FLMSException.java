package me.flegacy.flms.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FLMSException extends RuntimeException {

    public FLMSException(String msg) {
        super("FLMS Dev Issue: " + msg);
        for (Player p : Bukkit.getOnlinePlayers())
            if (p.isOp())
                p.sendMessage(Text.standard("&cThere was an internal error within the FLMS plugin. Please check the console!"));
    }
}
