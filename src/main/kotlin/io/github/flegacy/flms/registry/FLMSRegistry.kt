package io.github.flegacy.flms.registry

import io.github.flegacy.flms.FLMS
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.UUID

class FLMSRegistry(private val plugin: FLMS) {

    private val blocks = mutableMapOf<Material, RegisteredBlock>()
    private val tools = mutableMapOf<UUID, RegisteredTool>()
    private val effects = mutableMapOf<UUID, EffectProfile>()

    fun register(block: RegisteredBlock) {
        blocks[block.type] = block
    }

    fun remove(block: RegisteredBlock) {
        blocks.remove(block.type)
    }
    
    fun register(tool: RegisteredTool) {
        tools[tool.toolID] =  tool
    }

    fun remove(tool: RegisteredTool) {
        tools.remove(tool.toolID)
    }

    fun ensureEffectProfile(player: Player) {
        if (!effects.containsKey(player.uniqueId))
            effects[player.uniqueId] = EffectProfile(0u, 0u)
    }

    fun findEffectProfile(player: Player): EffectProfile {
        ensureEffectProfile(player)
        return effects[player.uniqueId]!!
    }

    fun findBlock(blockType: Material): RegisteredBlock? = blocks[blockType]
}
