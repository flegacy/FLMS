package io.github.flegacy.flms.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.inventory.ItemStack

const val FLMS_ORANGE = "<#edae10>"
const val FLMS_YELLOW = "<#fccd55>"
const val FLMS_LIGHT_YELLOW = "<#fcf4e0>"
const val FLMS_RED = "<#f94113>"
const val FLMS_LIGHT_RED = "<#fcded6>"
const val FLMS_WHITE = "<white>"
const val FLMS_GRAY = "<grey>"
const val FLMS_GREEN = "<#a1fc05>"
const val FLMS_LIGHT_GREEN = "<#e9fcc7>"

const val ERROR_COMMAND_CONSOLE = "You must be in-game to use this command."
const val ERROR_INVENTORY_FULL = "Your inventory is too full to do this!"
const val ERROR_EMPTY_HAND = "You need to hold an item to do this."
const val ERROR_SEVERE_CONSOLE = "There was a severe error in the FLMS plugin. Please check the console!"
const val ERROR_SAVING_DATA = "There was an error when saving data in the FLMS plugin. Some data may be lost from this session!"
const val WARNING_CHANGING_FILE = "Please don't change this file. In-game commands should be used to configure the plugin."

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

