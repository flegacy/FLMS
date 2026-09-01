package io.github.flegacy.flms.registry

import io.github.flegacy.flms.util.resolveName
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class RegisteredBlock(val type: Material) {
    init {
        require(type.isBlock && !type.isAir)
    }

    var hardness: UShort = 0u
        // When a registered block is an instamine block like grass and crops, the hardness is locked to 0.
        set(value) {
            if (type.hardness == 0f)
                require(hardness == 0.toUShort())
            field = value
        }
    var xp: UInt = 0u
    var postType: Material = Material.AIR
        set(value) {
            require(value.isBlock || value.isAir)
            field = value
        }
    var name: String = resolveName(type)
    val drops = mutableListOf<ItemStack>()





}
