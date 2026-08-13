package io.github.flegacy.flms.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for managing Minecraft text components.
 */
public class TextConstants {

	public static final String FLMS_ORANGE = "<#ffa229>";
	public static final String FLMS_YELLOW = "<#ffcd61>";
	public static final String FLMS_LIGHT_YELLOW = "<#ffefcc>";
	public static final String FLMS_RED = "<#e0003c>";
	public static final String FLMS_LIGHT_RED = "<#ffd4d4>";
	public static final String FLMS_WHITE = "<#e4f0ef>";
	public static final String FLMS_GRAY = "<#898f8e>";

	public static final String ERROR_COMMAND_CONSOLE = "You must be in-game to use this command.";

	public static final String ERROR_INVENTORY_FULL = "Your inventory is too full to do this!";
	public static final String ERROR_EMPTY_HAND = "You need to hold an item to do this.";


	/**
	 * Private constructor
	 */
	private TextConstants() {}

	public static Component miniMessage(String msg) {
		return MiniMessage.miniMessage().deserialize(msg);
	}

	public static List<Component> messageList(String... msgs) {
		final List<Component> list = new ArrayList<>();
		for (String msg: msgs)
			list.add(miniMessage(msg));
		return list;
	}

	public static String formatEnum(Enum<?> enumToFormat) {
		final String[] words = enumToFormat.toString().split("_");
		final StringBuilder builder = new StringBuilder();
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
