package io.github.flegacy.flms.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.inventory.ItemStack

const val FLMS_ORANGE = "<#ffa229"
const val FLMS_YELLOW = "<#ffa229"
const val FLMS_LIGHT_YELLOW = "<#ffa229"
const val FLMS_RED = "<#ffa229"
const val FLMS_LIGHT_RED = "<#ffa229"
const val FLMS_WHITE = "<#ffa229"
const val FLMS_GRAY = "<#ffa229"

const val ERROR_COMMAND_CONSOLE = "You must be in-game to use this comand."
const val ERROR_INVENTORY_FULL = "Your inventory is too full to do this!"
const val ERROR_EMPTY_HAND = "You need to hold an item to do this."

fun msgFormat(msg: String): Component = MiniMessage.miniMessage().deserialize(msg)

fun msgList(vararg msgs: String): List<Component> = msgs.map { msgFormat(it) }

fun prefixed(msg: String): Component = msgFormat("$FLMS_YELLOW<b>FLMS</b> <dark_gray>| $FLMS_LIGHT_YELLOW$msg")

fun errPrefixed(msg: String): Component = msgFormat("$FLMS_RED<b>FLMS ERROR</b> <dark_gray>| $FLMS_LIGHT_RED$msg")

fun resolveName(enumToFormat: Enum<*>): String {
    val builder = StringBuilder()
    enumToFormat.toString().split("_").forEach {
        builder.append(it[0])
        builder.append(it.substring(1))
        builder.append(" ")
    }
    return builder.substring(0, builder.length - 1)
}

fun resolveName(item: ItemStack?): String {
    if (item == null)
        return "null"
    if (item.type.isAir)
        return "Air"
    if (item.hasItemMeta() && item.itemMeta.hasDisplayName())
        return MiniMessage.miniMessage().serialize(item.itemMeta.displayName())
    return resolveName(item.type)
}

