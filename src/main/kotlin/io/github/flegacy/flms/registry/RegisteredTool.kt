package io.github.flegacy.flms.registry

import io.github.flegacy.flms.FLMS
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.UUID


class RegisteredTool(private val plugin: FLMS, private val item: ItemStack) {
    
    // Multiple tools must not have the same exact itemstack
    
    init {
        require(!item.type.isAir)
    }
    
    var breakingPower = 1u
    var modifiable = false
    var onlyBreakPreferredBlocks = false
    private val preferredBlocks = mutableSetOf<Material>()

    val toolID = UUID.randomUUID()!!
    
    fun taggedTool(): ItemStack {
        val newItem = item.clone()        
        val meta = newItem.itemMeta
        meta.persistentDataContainer.set(plugin.itemLib().toolKey, PersistentDataType.STRING, toolID.toString())
        newItem.itemMeta = meta
        return newItem
    }
}