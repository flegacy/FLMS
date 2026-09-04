package io.github.flegacy.flms.data

import kotlin.reflect.KClass

enum class Key(private val string: String, val type: KClass<*>, val default: Any) {
    ENABLED("enabled", Boolean::class, true),
    ALLOW_VANILLA_HASTE_SOURCES("allow_vanilla_haste_sources", Boolean::class, true),
    ALLOW_VANILLA_FATIGUE_SOURCES("allow_vanilla_fatigue_sources", Boolean::class, true),
    ALLOW_VANILLA_EFFICIENCY_ENCHANTMENT("allow_vanilla_efficiency_enchantment", Boolean::class, true),
    ALLOW_VANILLA_GRINDSTONE_USAGE("allow_vanilla_grindstone_usage", Boolean::class, true),
    ALLOW_VANILLA_ANVIL_USAGE("allow_vanilla_anvil_usage", Boolean::class, true),
    USE_ROMAN_NUMERALS("use_roman_numerals", Boolean::class, false),
    BLOCK_BREAKING_PARTICLES("block_breaking_particles", Boolean::class, true),
    BLOCK_BREAKING_UPDATES("block_breaking_updates", Boolean::class, false),
    BLOCK_XP_AUTO_PICKUP("block_xp_auto_pickup", Boolean::class, false),
    BLOCK_BREAK_FAILURE_SOUND("block_break_failure_sound", Boolean::class, true),
    ENCHANT_FOR_MESSAGE(
        "enchant_for_message", String::class,
        "<gray><i>Your item was enchanted with Efficiency <efficiency>."
    ),
    HASTE_EFFECT_RECEIVE_MESSAGE(
        "haste_effect_recieve_message", String::class,
        "<gray><i>You feel gifted with Haste <level>..."
    ),
    FATIGUE_EFFECT_RECEIVE_MESSAGE(
        "fatigue_effect_recieve_message", String::class,
        "<gray><i>You've fallen under the effects of Mining Fatigue <level>..."
    ),
    FATIGUE_EFFECT_REMOVE_MESSAGE(
        "fatigue_effect_remove_message", String::class,
        "<gray><i>You've been cleansed of mining fatigue..."
    ),
    HASTE_EFFECT_REMOVE_MESSAGE(
        "haste_effect_remove_message", String::class,
        "<gray><i>You've been cleansed of haste..."
    ),
    BLOCK_BREAK_DENIAL_MESSAGE("block_break_denial_message", String::class, "<red>You can't destroy this block."),
    BLOCK_BREAK_DENIAL_LOCATION("block_break_denial_location", String::class, "chat");

    fun isBoolean(): Boolean {
        return type == Boolean::class
    }

    fun isString(): Boolean {
        return type == String::class
    }


}
