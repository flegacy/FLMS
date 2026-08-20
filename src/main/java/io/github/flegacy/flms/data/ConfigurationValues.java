package io.github.flegacy.flms.data;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.jspecify.annotations.Nullable;

import io.github.flegacy.flms.FLMS;
import io.github.flegacy.flms.utils.TextConstants;

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
        for (Key key : Key.values()) {

            String lowerCaseString = key.toString().toLowerCase(Locale.ROOT);
            var matchedValue = config.getObject(lowerCaseString, key.valueType);

			if (!config.isSet(lowerCaseString)) {
				plugin.getComponentLogger().error(TextConstants.miniMessage(
						"Couldn't find value for '" + lowerCaseString + "' in the config. " +
								"Defaulting to value '" + matchedValue + "<reset>'."));
                errors = true;
                configurationOptions.put(key, key.defaultValue);
                continue;
			}

			if ((key.isBoolean() && !config.isBoolean(lowerCaseString))
                        || (key.isString() && !config.isString(lowerCaseString))) {
				plugin.getComponentLogger().error(TextConstants.miniMessage(
						"The value for '" + lowerCaseString + "' in your config isn't the correct " + key.valueType.getSimpleName()
						+ " type. Defaulting to value '" + key.defaultValue + "<reset>'."
				));
				errors = true;
				configurationOptions.put(key, key.defaultValue);
				continue;
			}

			configurationOptions.put(key, matchedValue);
        }

        if (errors)
			plugin.getComponentLogger().error(
					"There were errors when loading the FLMS config. Parts of the plugin won't work as you intend until they are fixed."
			);
        else
            plugin.getComponentLogger().info("Config successfully loaded.");
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
        ENABLED("enabled", Boolean.class, true),
        ALLOW_VANILLA_HASTE_SOURCES("allow_vanilla_haste_sources", Boolean.class, true),
        ALLOW_VANILLA_FATIGUE_SOURCES("allow_vanilla_fatigue_sources", Boolean.class, true),
        ALLOW_VANILLA_EFFICIENCY_ENCHANTMENT("allow_vanilla_efficiency_enchantment", Boolean.class, true),
        ALLOW_VANILLA_GRINDSTONE_USAGE("allow_vanilla_grindstone_usage", Boolean.class, true),
        ALLOW_VANILLA_ANVIL_USAGE("allow_vanilla_anvil_usage", Boolean.class, true),
        USE_ROMAN_NUMERALS("use_roman_numerals", Boolean.class, false),
        BLOCK_BREAKING_PARTICLES("block_breaking_particles", Boolean.class, true),
        BLOCK_BREAKING_UPDATES("block_breaking_updates", Boolean.class, false),
        BLOCK_XP_AUTO_PICKUP("block_xp_auto_pickup", Boolean.class, false),
        BLOCK_BREAK_FAILURE_SOUND("block_break_failure_sound", Boolean.class, true),
        ENCHANT_FOR_MESSAGE("enchant_for_message", String.class,
                "<gray><i>Your item was enchanted with Efficiency <efficiency>."),
        HASTE_EFFECT_RECEIVE_MESSAGE("haste_effect_recieve_message", String.class,
                "<gray><i>You feel gifted with Haste <haste>..."),
        FATIGUE_EFFECT_RECEIVE_MESSAGE("fatigue_effect_recieve_message", String.class,
                "<gray><i>You've fallen under the effects of Mining Fatigue <fatigue>..."),
        BLOCK_BREAK_DENIAL_MESSAGE("block_break_denial_message", String.class, "<red>You can't destroy this block."),
        BLOCK_BREAK_DENIAL_LOCATION("block_break_denial_location", String.class, "chat");

        public final String configKey;
        public final Class<?> valueType;
        public final Object defaultValue;

        Key(String configKey, Class<?> valueType, Object defaultValue) {
            this.configKey = configKey;
            this.valueType = valueType;
            this.defaultValue = defaultValue;
        }

        public boolean isBoolean() {
            return valueType == Boolean.class;
        }

        public boolean isString() {
            return valueType == String.class;
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
