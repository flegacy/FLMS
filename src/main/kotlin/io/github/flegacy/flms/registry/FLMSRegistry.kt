package io.github.flegacy.flms.registry

import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.data.flmsUnwriteData
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.UUID

const val SERIALIZED_HASTE_KEY = "haste"
const val SERIALIZED_FATIGUE_KEY = "fatigue"

class FLMSRegistry(private val plugin: FLMS) {

    private val blocks = mutableMapOf<Material, RegisteredBlock>()
    private val tools = mutableMapOf<UUID, RegisteredTool>()
    private val effects = mutableMapOf<UUID, EffectProfile>()

    init {
        for ((key, value) in plugin.blockData().read())
            blocks[key] = value
        for ((key, value) in plugin.effectData().read())
            effects[key] = value
    }

    fun register(block: RegisteredBlock) {
        blocks[block.type] = block
        plugin.blockData().write(block)
    }

    fun remove(block: RegisteredBlock) {
        blocks.remove(block.type)
        plugin.blockData().unwrite(block)
    }

    fun register(tool: RegisteredTool) {
        tools[tool.toolID] = tool
    }

    fun remove(tool: RegisteredTool) {
        tools.remove(tool.toolID)
    }

    fun ensureEffectProfile(player: Player) {
        if (!effects.containsKey(player.uniqueId))
            effects[player.uniqueId] = EffectProfile(plugin, 0, 0)
    }

    fun findEffectProfile(player: Player): EffectProfile {
        ensureEffectProfile(player)
        return effects[player.uniqueId]!!
    }

    fun findBlock(blockType: Material): RegisteredBlock? = blocks[blockType]

    fun blocks(): MutableCollection<RegisteredBlock> {
        return blocks.values
    }

    fun serializeEffectProfiles(): Map<String, Map<String, Int>> {
        val serialized = mutableMapOf<String, Map<String, Int>>()
        for ((key, value) in effects) {
            serialized[key.toString()] = value.serialize()
        }
        return serialized
    }

}
