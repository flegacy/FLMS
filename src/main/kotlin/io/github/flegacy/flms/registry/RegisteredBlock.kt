package io.github.flegacy.flms.registry

import io.github.flegacy.flms.util.resolveName
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import sun.tools.serialver.resources.serialver_ja

private const val HARDNESS_KEY = "hardness"
private const val XP_KEY = "xp"
private const val POSTTYPE_KEY = "posttype"
private const val NAME_KEY = "name"
private const val DROPS_KEY = "drops"

data class RegisteredBlock(val type: Material, val hardness: UShort, val xp: UInt, val postType: Material, val name: String, val drops: List<ItemStack>) {

    init {
        require(type.isBlock && !type.isAir)
        require(postType.isBlock || postType.isAir)
    }

    fun serialize(): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        // The block type is the key for this entry
        map[HARDNESS_KEY] = hardness.toShort()
        map[XP_KEY] = xp.toInt()
        map[POSTTYPE_KEY] = postType.toString()
        map[NAME_KEY] = name
        val dropsSerialized = drops.map { it.serialize() }
        map[DROPS_KEY] = dropsSerialized
        return map
    }

    companion object {
        fun deserialize(blockType: Material, serialized: HashMap<String, Any>): RegisteredBlock? {
            try {
                val xp = (serialized[XP_KEY] as Int).toUInt()
                val hardness = (serialized[HARDNESS_KEY] as Int).toUShort()
                val postType = Material.valueOf((serialized[POSTTYPE_KEY] as String))
                val name = serialized[NAME_KEY] as String
                val drops = (serialized[DROPS_KEY] as List<Map<String, Any>>).map { ItemStack.deserialize(it) }
                
                val attemptedBlock = RegisteredBlock(blockType, hardness, xp, postType, name, drops)
                return attemptedBlock

            } catch (ex: Exception) {
                Bukkit.getLogger().severe("There was an error when deserializing a block from the plugin's data folder. Please refrain from changing files other than config.yml.")
                ex.printStackTrace()
                return null
            }
        }
    }





}
