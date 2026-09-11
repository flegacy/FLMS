package io.github.flegacy.flms.items

import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.util.FLMS_GRAY
import io.github.flegacy.flms.util.FLMS_GREEN
import io.github.flegacy.flms.util.FLMS_LIGHT_GREEN
import io.github.flegacy.flms.util.FLMS_LIGHT_RED
import io.github.flegacy.flms.util.FLMS_LIGHT_YELLOW
import io.github.flegacy.flms.util.FLMS_ORANGE
import io.github.flegacy.flms.util.FLMS_RED
import io.github.flegacy.flms.util.FLMS_WHITE
import io.github.flegacy.flms.util.FLMS_YELLOW
import io.github.flegacy.flms.util.msgFormat
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType

private const val FLMS_WAND_TAG = "flms_wand"
private const val EFFICIENCY_PREFIX = "⛏ Efficiency "

class ItemLibrary(plugin: FLMS) {

    private val itemKey = NamespacedKey(plugin, "flms_item")
    private val effKey = NamespacedKey(plugin, "flms_efficiency")
    val toolKey = NamespacedKey(plugin, "flms_tool")

    val enchanter = Enchanter()

    fun isWand(item: ItemStack): Boolean {
        if (item.type.isAir)
            return false
        if (!item.itemMeta.persistentDataContainer.has(itemKey))
            return false
        return item.itemMeta.persistentDataContainer.get(itemKey, PersistentDataType.STRING)
            .equals(FLMS_WAND_TAG)
    }

    fun wand(): ItemStack {
        val wand = ItemStackBuilder(Material.GOLDEN_AXE)
            .name("${FLMS_ORANGE}FLMS Wand")
            .lore("${FLMS_YELLOW}Hold and right-click to use!")
            .unbreakable(true)
            .glow(true)
            .flag(*ItemFlag.entries.toTypedArray())
            .build()
        val meta = wand.itemMeta
        meta.persistentDataContainer.set(itemKey, PersistentDataType.STRING, FLMS_WAND_TAG)
        wand.itemMeta = meta
        return wand
    }

    companion object {
        val FILLER = ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE)
            .name("")
            .build()
            get() {
                return field.clone()
            }

        val LEFT_POINTER = ItemStackBuilder(Material.ARROW)
            .name("${FLMS_ORANGE}<- Previous Page")
            .lore("${FLMS_YELLOW}<b>CLICK TO VIEW")
            .build()
            get() {
                return field.clone()
            }

        val RIGHT_POINTER = ItemStackBuilder(Material.ARROW)
            .name("${FLMS_ORANGE}Next Page ->")
            .lore("${FLMS_YELLOW}<b>CLICK TO VIEW")
            .build()
            get() {
                return field.clone()
            }

        val BACK_BUTTON = ItemStackBuilder(Material.BARRIER)
            .name("${FLMS_RED}Go Back")
            .lore("${FLMS_LIGHT_RED}<b>CLICK TO GO")
            .build()
            get() {
                return field.clone()
            }

        val JUMP_FIRST = ItemStackBuilder(Material.SPECTRAL_ARROW)
            .name("${FLMS_ORANGE}<b><i>Jump to First -->")
            .lore("${FLMS_YELLOW}<b>CLICK TO VIEW")
            .build()
            get() {
                return field.clone()
            }

        val JUMP_LAST = ItemStackBuilder(Material.SPECTRAL_ARROW)
            .name("${FLMS_ORANGE}<b><i><-- Jump to Last")
            .lore("${FLMS_YELLOW}<b>CLICK TO VIEW")
            .build()
            get() {
                return field.clone()
            }

        // TODO update wand icon descriptions
        val BLOCK_CONFIG_ICON = ItemStackBuilder(Material.BEDROCK)
            .name("${FLMS_ORANGE}Edit Custom Blocks")
            .lore(
                "",
                "${FLMS_LIGHT_YELLOW}Manipulate all types of blocks by",
                "${FLMS_LIGHT_YELLOW}setting their hardness values, changing",
                "${FLMS_LIGHT_YELLOW}what tools are the best at breaking them,",
                "${FLMS_LIGHT_YELLOW}and controlling what items they drop.",
                "",
                "${FLMS_YELLOW}<b>CLICK TO OPEN"
            )
            .build()
            get() {
                return field.clone()
            }

        val TOOL_CONFIG_ICON = ItemStackBuilder(Material.GOLDEN_PICKAXE)
            .name("${FLMS_ORANGE}Edit Custom Tools")
            .lore(
                "",
                "${FLMS_LIGHT_YELLOW}Create your own tools from any",
                "${FLMS_LIGHT_YELLOW}item in the game and define how",
                "${FLMS_LIGHT_YELLOW}strong they are with breaking power",
                "${FLMS_LIGHT_YELLOW}and enchantments.",
                "",
                "${FLMS_YELLOW}<b>CLICK TO OPEN"
            )
            .build()
            get() {
                return field.clone()
            }

        val EFFECT_CONFIG_ICON = ItemStackBuilder(Material.POTION)
            .name("${FLMS_ORANGE}Edit Player Effects")
            .lore(
                "",
                "${FLMS_LIGHT_YELLOW}Vanilla haste and mining fatigue",
                "${FLMS_LIGHT_YELLOW}won't work with this plugin, so you",
                "${FLMS_LIGHT_YELLOW}can modify those here instead or with",
                "${FLMS_LIGHT_YELLOW}the ${FLMS_YELLOW}/flms effect ${FLMS_LIGHT_YELLOW}command.",
                "",
                "${FLMS_YELLOW}<b>CLICK TO OPEN"
            )
            .build()
            get() {
                return field.clone()
            }

        val CREATION_ICON = ItemStackBuilder(Material.CRAFTING_TABLE)
            .name("${FLMS_GREEN}Create New...")
            .lore("${FLMS_LIGHT_GREEN}<bold>CLICK TO CREATE")
            .build()
            get() {
                return field.clone()
            }

        val FINISH_ICON = ItemStackBuilder(Material.WRITABLE_BOOK)
            .name("${FLMS_GREEN}Finish")
            .lore("${FLMS_LIGHT_GREEN}<bold>CLICK TO CREATE")
            .build()
            get() {
                return field.clone()
            }

        val POST_BREAK_EMPTY_ICON = ItemStackBuilder(Material.WHITE_STAINED_GLASS_PANE)
            .name("${FLMS_YELLOW}Post-Break Type")
            .lore(
                "${FLMS_WHITE}Drag an item here to set",
                "${FLMS_WHITE}the post-type for this block.",
                "",
                "${FLMS_GRAY}The block's post-break type is",
                "${FLMS_GRAY}the type of block it will turn into",
                "${FLMS_GRAY}after it's broken."
            )
            .build()
            get() {
                return field.clone()
            }

        val BLOCK_TYPE_EMPTY_ICON = ItemStackBuilder(Material.WHITE_STAINED_GLASS_PANE)
            .name("${FLMS_YELLOW}Block Type ${FLMS_RED}*REQUIRED*")
            .lore(
                "${FLMS_WHITE}Drag an item here to set",
                "${FLMS_WHITE}the type for this block.",
                "",
                "${FLMS_GRAY}Every custom block must have",
                "${FLMS_GRAY}a unique type. If another block",
                "${FLMS_GRAY}is using your block, you won't be",
                "${FLMS_GRAY}able to put it in here."
            )
            .build()
            get() {
                return field.clone()
            }

        val NOT_READY_ICON = ItemStackBuilder(Material.BOOK)
            .name("Not ready to finish")
            .lore(
                "",
                "${FLMS_GRAY}Make sure all the block",
                "${FLMS_GRAY}values are set correctly."
            )
            .build()
            get() {
                return field.clone()
            }

        val CANCEL_ICON = ItemStackBuilder(Material.RED_WOOL)
            .name("${FLMS_RED}Cancel")
            .build()
            get() {
                return field.clone()
            }

        val CONFIRM_ICON = ItemStackBuilder(Material.LIME_WOOL)
            .name("${FLMS_GREEN}Confirm")
            .build()
            get() {
                return field.clone()
            }

    }

    inner class Enchanter {

        fun effApply(item: ItemStack, level: Short, show: Boolean) {
            require(!item.type.isAir)

            val meta = item.itemMeta
            if (hasEff(item) && level == 0.toShort())
                meta.persistentDataContainer.remove(effKey)
            else
                meta.persistentDataContainer.set(effKey, PersistentDataType.SHORT, level)

            updateLore(meta, level, show)
            updateGlint(meta, level, show)

            item.itemMeta = meta
        }

        fun updateLore(meta: ItemMeta, level: Short, show: Boolean) {
            val cleanLore =
                (if (meta.hasLore())
                    meta.lore()
                else
                    mutableListOf<Component>())!!

            for (line in cleanLore)
                if (line.toString().contains(EFFICIENCY_PREFIX)) {
                    cleanLore.remove(line)
                    break
                }

            if (show && level != 0.toShort())
                cleanLore.addFirst(msgFormat("<gray><!i>$EFFICIENCY_PREFIX$level"))

            meta.lore(cleanLore)
        }


        @Suppress("UsePropertyAccessSyntax") // doesn't work with enchantment glint override
        fun updateGlint(meta: ItemMeta, level: Short, show: Boolean) {
            if (meta.hasEnchants())
                return
            if (level == 0.toShort() || !show)
                meta.setEnchantmentGlintOverride(null)
            else
                meta.setEnchantmentGlintOverride(true)
        }

        fun hasEff(item: ItemStack): Boolean {
            return !item.type.isAir && item.itemMeta.persistentDataContainer.has(effKey)
        }

        fun level(item: ItemStack): Short {
            if (!hasEff(item))
                return 0
            return item.itemMeta.persistentDataContainer.get(effKey, PersistentDataType.SHORT)!!
        }

    }
}


