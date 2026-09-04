package io.github.flegacy.flms.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.inventory.ItemStack

const val FLMS_ORANGE = "<#ffa229>"
const val FLMS_YELLOW = "<#ffcd61>"
const val FLMS_LIGHT_YELLOW = "<#ffefcc>"
const val FLMS_RED = "<#e0003c>"
const val FLMS_LIGHT_RED = "<#ffd4d4>"
const val FLMS_WHITE = "<#e4f0ef>"
const val FLMS_GRAY = "<#898f8e>"

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
        builder.append(it[0].uppercase())
        builder.append(it.substring(1).lowercase())
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
        return item.itemMeta.displayName().toString()
    return resolveName(item.type)
}

