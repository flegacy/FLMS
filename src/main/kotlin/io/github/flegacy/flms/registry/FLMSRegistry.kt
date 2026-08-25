package io.github.flegacy.flms.registry

import io.github.flegacy.flms.FLMS
import org.bukkit.Material

class FLMSRegistry(private val plugin: FLMS) {

    private val blocks = mutableMapOf<Material, RegisteredBlock>()

    fun register(block: RegisteredBlock) {
        blocks[block.type] = block
    }

    fun remove(block: RegisteredBlock) {
        blocks.remove(block.type)
    }

    fun findBlock(blockType: Material): RegisteredBlock? = blocks[blockType]
}
