package io.github.flegacy.flms.items

import io.github.flegacy.flms.util.msgFormat
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

class ItemStackBuilder(type: Material)  {

    private val item = ItemStack(type)
    private val tempMeta = item.itemMeta

    init {
        require(!type.isAir)
    }

    fun amount(amount: Int): ItemStackBuilder {
        item.amount = amount
        return this
    }

    fun name(name: String): ItemStackBuilder {
        tempMeta.displayName(msgFormat("<!i>$name"))
        return this
    }

    fun lore(vararg msgs: String): ItemStackBuilder {
        tempMeta.lore(msgs.map { msgFormat("<!i>$it") })
        return this
    }

    fun flag(vararg flags: ItemFlag): ItemStackBuilder {
        tempMeta.addItemFlags(*flags)
        return this
    }

    fun glow(glowing: Boolean): ItemStackBuilder {
        tempMeta.setEnchantmentGlintOverride(if (glowing) true else null)
        return this
    }

    fun unbreakable(unbreakable: Boolean): ItemStackBuilder {
        tempMeta.isUnbreakable = unbreakable
        return this
    }

    fun build(): ItemStack {
        item.itemMeta = tempMeta
        return item
    }


    


}
