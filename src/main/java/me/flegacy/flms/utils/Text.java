package me.flegacy.flms.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

// UTILITY CLASS
public class Text {

    public static String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String standard(String msg) {
        return prefix("&e&lFLMS", msg);
    }

    public static String prefix(String prefix, String msg) {
        return format(prefix + " &8| &f" + msg);
    }

    public static String error(String msg) {
        return prefix("&c&lERROR", "&c" + msg);
    }

    public static List<String> formatList(String... msgs) {
        List<String> result = new ArrayList<>();
        for (String s : msgs) {
            result.add(format(s));
        }
        return result;
    }

    public static String enumToDisplayName(String enumString) {
        char[] enumChars = enumString.toCharArray();
        StringBuilder builder = new StringBuilder();

        boolean capitalizeNext = false;
        for (int i = 0; i < enumChars.length; i++) {
            String upperCase = String.valueOf(enumChars[i]).toUpperCase();
            if (i == 0) {
                builder.append(upperCase);
                continue;
            }
            if (enumChars[i] == '_') {
                builder.append(" ");
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    builder.append(upperCase);
                    capitalizeNext = false;
                } else builder.append(String.valueOf(enumChars[i]).toLowerCase());
            }
        }

        return builder.toString();
    }
}
