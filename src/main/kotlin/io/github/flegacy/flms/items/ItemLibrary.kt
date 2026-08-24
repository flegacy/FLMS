package io.github.flegacy.flms.items

import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.util.FLMS_LIGHT_RED
import io.github.flegacy.flms.util.FLMS_LIGHT_YELLOW
import io.github.flegacy.flms.util.FLMS_ORANGE
import io.github.flegacy.flms.util.FLMS_RED
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
            .lore("${FLMS_YELLOW}Hold and right-click to use!)")
            .unbreakable(true)
            .glow(true)
            .flag(*ItemFlag.entries.toTypedArray())
            .build()
        wand.itemMeta.persistentDataContainer.set(itemKey, PersistentDataType.STRING, FLMS_WAND_TAG)
        return wand
    }

    fun emptyGlass(): ItemStack = ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE)
        .name("")
        .flag(*ItemFlag.entries.toTypedArray())
        .build()

    fun leftPointer(): ItemStack = ItemStackBuilder(Material.ARROW)
        .name("${FLMS_ORANGE}<- Previous Page")
        .lore("${FLMS_YELLOW}<b>CLICK TO VIEW")
        .build()

    fun rightPointer(): ItemStack = ItemStackBuilder(Material.ARROW)
        .name("${FLMS_ORANGE}Next Page ->")
        .lore("${FLMS_YELLOW}<b>CLICK TO VIEW")
        .build()

    fun backButton(): ItemStack = ItemStackBuilder(Material.BARRIER)
        .name("${FLMS_RED}Go Back")
        .lore("${FLMS_LIGHT_RED}<b>CLICK TO GO")
        .build()

    fun jumpFirst(): ItemStack = ItemStackBuilder(Material.SPECTRAL_ARROW)
        .name("${FLMS_ORANGE}<b><i>Jump to First -->")
        .lore("${FLMS_YELLOW}<b>CLICK TO VIEW")
        .build()

    fun jumpLast(): ItemStack = ItemStackBuilder(Material.SPECTRAL_ARROW)
        .name("${FLMS_ORANGE}<b><i><-- Jump to Last")
        .lore("${FLMS_YELLOW}<b>CLICK TO VIEW")
        .build()

    // TODO update descriptions
    fun blkCfgIcon(): ItemStack = ItemStackBuilder(Material.BEDROCK)
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

    fun tlCfgIcon(): ItemStack = ItemStackBuilder(Material.GOLDEN_PICKAXE)
        .name("${FLMS_ORANGE}Edit Custom Tools")
        .lore(
            "",
            "${FLMS_LIGHT_YELLOW}Create your own tools from any",
            "${FLMS_LIGHT_YELLOW}item in the game and define how",
            "${FLMS_LIGHT_YELLOW}strong they aree with breaking power",
            "${FLMS_LIGHT_YELLOW}and enchantments.",
            "",
            "${FLMS_YELLOW}<b>CLICK TO OPEN"
        )
        .build()

    fun rgnCfgIcon(): ItemStack = ItemStackBuilder(Material.GLOBE_BANNER_PATTERN)
        .name("${FLMS_ORANGE}Edit Regions")
        .lore(
            "",
            "${FLMS_LIGHT_YELLOW}If you only want certain parts of",
            "${FLMS_LIGHT_YELLOW}the world to be breakable by players,",
            "${FLMS_LIGHT_YELLOW}you can manage that here.",
            "",
            "${FLMS_YELLOW}<b>CLICK TO OPEN"
        )
        .build()

    fun eftCfgIcon(): ItemStack = ItemStackBuilder(Material.POTION)
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

    inner class Enchanter {

        fun effApply(item: ItemStack, level: Short, show: Boolean) {
            require(!item.type.isAir)
            require(level >= 0)

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
                meta.setEnchantmentGlintOverride(true)
            else
                meta.setEnchantmentGlintOverride(null)
        }

        fun hasEff(item: ItemStack): Boolean {
            if (item.type.isAir)
                return false
            return item.itemMeta.persistentDataContainer.has(itemKey)
        }

    }
}


