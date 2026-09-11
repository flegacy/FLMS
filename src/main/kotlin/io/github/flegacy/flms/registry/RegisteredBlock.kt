package io.github.flegacy.flms.registry

import io.github.flegacy.flms.FLMS
import net.kyori.adventure.text.logger.slf4j.ComponentLogger
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

private const val HARDNESS_KEY = "hardness"
private const val XP_KEY = "xp"
private const val POST_TYPE_KEY = "post-type"
private const val NAME_KEY = "name"
private const val DROPS_KEY = "drops"

data class RegisteredBlock(val type: Material, val hardness: Float, val xp: Int, val postType: Material, val name: String, val drops: List<ItemStack>) {

    init {
        require(type.isBlock && !type.isAir)
        require(postType.isBlock || postType.isAir)
    }

    fun serialize(): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        // The block type is the key for this entry
        map[HARDNESS_KEY] = hardness
        map[XP_KEY] = xp
        map[POST_TYPE_KEY] = postType.toString()
        map[NAME_KEY] = name
        val dropsSerialized = drops.map { it.serialize() }
        map[DROPS_KEY] = dropsSerialized
        return map
    }

    companion object {
        fun deserialize(plugin: FLMS, blockType: Material, serialized: HashMap<String, Any>): RegisteredBlock? {
            try {
                val xp = (serialized[XP_KEY] as Int)
                val hardness = (serialized[HARDNESS_KEY] as Double).toFloat()
                val postType = Material.valueOf((serialized[POST_TYPE_KEY] as String))
                val name = serialized[NAME_KEY] as String
                val drops = (serialized[DROPS_KEY] as List<Map<String, Any>>).map { ItemStack.deserialize(it) }
                
                val attemptedBlock = RegisteredBlock(blockType, hardness, xp, postType, name, drops)
                return attemptedBlock

            } catch (ex: Exception) {
                plugin.componentLogger.error("There was an error when deserializing a block from the plugin's data folder. Please refrain from changing files other than config.yml.")
                ex.printStackTrace()
                return null
            }
        }
    }





}
