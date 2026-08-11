package io.github.flegacy.flms.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

/**
 * Utility class for managing Minecraft text components.
 */
public class TextUtils {

	public static final String FLMS_ORANGE = "<#ffa229>";
	public static final String FLMS_YELLOW = "<#ffcd61>";
	public static final String FLMS_LIGHT_YELLOW = "<#ffefcc>";
	public static final String FLMS_RED = "<#e0003c>";
	public static final String FLMS_LIGHT_RED = "<#ffd4d4>";
	public static final String FLMS_WHITE = "<#e4f0ef>";
	public static final String FLMS_GRAY = "<#898f8e>";


	/**
	 * Private constructor
	 */
	private TextUtils() {}

	public static Component miniMessage(String msg) {
		return MiniMessage.miniMessage().deserialize(msg);
	}

	public static String formatEnum(Enum<?> enumToFormat) {
		String[] words = enumToFormat.toString().split("_");
		StringBuilder builder = new StringBuilder();
		for (String word: words) {
			builder.append(word.charAt(0));
			builder.append(word.substring(1));
			builder.append(" ");
		}
		return builder.substring(0, builder.length()-1);
	}

	public static Component prefixedMessage(String msg) {
		return miniMessage(FLMS_YELLOW + "<b>FLMS <reset><dark_gray>| " + FLMS_LIGHT_YELLOW + msg);
	}

	public static Component errorMessage(String msg) {
		return miniMessage(FLMS_RED + "<b>FLMS ERROR <reset><dark_gray>| " + FLMS_LIGHT_RED + msg);
	}

}
