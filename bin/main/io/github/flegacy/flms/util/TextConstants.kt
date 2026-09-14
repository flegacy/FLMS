package io.github.flegacy.flms.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.inventory.ItemStack

const val FLMS_ORANGE = "<gold>"
const val FLMS_YELLOW = "<yellow>"
const val FLMS_LIGHT_YELLOW = "<white>"
const val FLMS_RED = "<red>"
const val FLMS_LIGHT_RED = "<white>"
const val FLMS_WHITE = "<white>"
const val FLMS_GRAY = "<grey>"
const val FLMS_GREEN = "<green>"
const val FLMS_LIGHT_GREEN = "<white>"

const val ERROR_COMMAND_CONSOLE = "You must be in-game to use this command."
const val ERROR_INVENTORY_FULL = "Your inventory is too full to do this!"
const val ERROR_EMPTY_HAND = "You need to hold an item to do this."

fun toPlainText(component: Component): String =
    PlainTextComponentSerializer.plainText().serialize(component)


fun msgFormat(msg: String): Component = MiniMessage.miniMessage().deserialize(msg)

fun msgList(vararg msg: String): List<Component> = msg.map { msgFormat(it) }

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

