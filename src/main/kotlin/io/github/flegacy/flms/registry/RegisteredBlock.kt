package io.github.flegacy.flms.registry

import io.github.flegacy.flms.util.resolveName
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class RegisteredBlock(val type: Material) {
    init {
        require(type.isBlock && !type.isAir)
    }

    var hardness: UShort = 0u
    var xp: UInt = 0u
    var postType: Material = Material.AIR
        set(value) {
            require(value.isBlock || value.isAir)
            field = value
        }
    var name: String = resolveName(type)
    val drops = mutableListOf<ItemStack>()





}
