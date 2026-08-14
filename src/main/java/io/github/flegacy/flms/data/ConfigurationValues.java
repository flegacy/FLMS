package io.github.flegacy.flms.data;

import io.github.flegacy.flms.FLMS;
import org.bukkit.configuration.file.FileConfiguration;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationValues {
	private static ConfigurationValues instance;

	private final FLMS plugin;
	private final Map<Key, Object> configurationOptions;

	public ConfigurationValues(FLMS plugin) {
		if (instance != null)
			throw new IllegalStateException("There is already a ConfigOptions instance active.");
		instance = this;
		this.plugin = plugin;
		configurationOptions = new HashMap<>();

		reload();
	}

	public boolean reload() {
		// TODO apply changes upon changing of 'enabled' key
		plugin.reloadConfig();
		FileConfiguration config = plugin.getConfig();
		boolean errors = false;

		// There shouldn't be deep keys in the FLMS config
		for (String key : config.getKeys(false)) {
			Key match = Key.fromString(key);
			if (match == null) {
				plugin.getLogger().warning("The config option '" + key + "' was found in the config, but is invalid.");
				continue;
			}
			var value = config.getObject(key, match.valueType);
			if (value == null || value.getClass() != match.valueType) {
				String classDisplay = (value == null) ? "null" : value.getClass().toString();
				plugin.getLogger().severe(
						"Can't load the configuration option '" + key + "' because it has an incorrect type of value. "
								+ "It should be a " + match.valueType + ", but found " + classDisplay);
				errors = true;
				continue;
			}
			configurationOptions.put(match, value);
		}

		if (errors)
			plugin.getLogger().severe("There were errors when reloading the FLMS config. Parts of the plugin won't work properly until they are fixed.");
		else
			plugin.getLogger().info("Config successfully loaded");
		return !errors;
	}

	public boolean getBoolean(Key key) {
		if (key.valueType != Boolean.class)
			throw new IllegalArgumentException(key + " does not return a boolean");
		return (boolean) configurationOptions.get(key);
	}

	@Nullable
	public String getString(Key key) {
		if (key.valueType != String.class)
			throw new IllegalArgumentException(key + " does not return a String");
		return (String) configurationOptions.get(key);
	}

	public enum Key {
		ENABLED("enabled", Boolean.class),
		ALLOW_VANILLA_HASTE_SOURCES("allow_vanilla_haste_sources", Boolean.class),
		ALLOW_VANILLA_FATIGUE_SOURCES("allow_vanilla_fatigue_sources", Boolean.class),
		ALLOW_VANILLA_EFFICIENCY_ENCHANTMENT("allow_vanilla_efficiency_enchantment", Boolean.class),
		ALLOW_VANILLA_GRINDSTONE_USAGE("allow_vanilla_grindstone_usage", Boolean.class),
		ALLOW_VANILLA_ANVIL_USAGE("allow_vanilla_anvil_usage", Boolean.class),
		USE_ROMAN_NUMERALS("use_roman_numerals", Boolean.class),
		BLOCK_BREAKING_PARTICLES("block_breaking_particles", Boolean.class),
		ENCHANT_FOR_MESSAGE("enchant_for_message", String.class),
		HASTE_EFFECT_RECEIVE_MESSAGE("haste_effect_recieve_message", String.class),
		FATIGUE_EFFECT_RECEIVE_MESSAGE("fatigue_effect_recieve_message", String.class),
		BLOCK_BREAK_DENIAL_MESSAGE("block_break_denial_message", String.class),
		BLOCK_BREAK_DENIAL_LOCATION("block_break_denial_location", String.class);

		public final String configKey;
		public final Class<?> valueType;

		Key(String configKey, Class<?> valueType) {
			this.configKey = configKey;
			this.valueType = valueType;
		}

		@Nullable
		public static Key fromString(String key) {
			for (Key enumKey : Key.values())
				if (enumKey.configKey.equalsIgnoreCase(key))
					return enumKey;
			return null;
		}
	}
}

