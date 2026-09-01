package io.github.flegacy.flms.mining

import io.github.flegacy.flms.FLMS
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class WorldProtectionListener(private val plugin: FLMS): Listener {

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        // TODO enable compatiiblity with other listeners later isntead of just stopping the event entirely
        // TODO integrate crops
        if (event.player.gameMode != GameMode.SURVIVAL)
            return
        val registeredCrop = plugin.registry().findBlock(event.block.type) != null && event.block.type.hardness == 0f
        if (registeredCrop)
            return
        if (event !is FLMSBlockBreakEvent) {
            event.isCancelled = true
            return
        }

    }

}
