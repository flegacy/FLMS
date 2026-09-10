package io.github.flegacy.flms.config

import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.FLMS_PERMISSION
import io.github.flegacy.flms.config.ui.WandMenu
import io.github.flegacy.flms.util.soundWandOpen
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class WandListener(private val plugin: FLMS): Listener {

    private val wandMenu = WandMenu(plugin)

    private fun hasWand(player: Player): Boolean {
        return plugin.itemLib().isWand(player.inventory.itemInMainHand)
    }

    @EventHandler
    fun onRightClickAir(event: PlayerInteractEvent) {
        val canOpen = event.action == Action.RIGHT_CLICK_AIR && event.player.hasPermission(FLMS_PERMISSION) && hasWand(event.player)
        if (!canOpen)
            return
        soundWandOpen(event.player, plugin)
        wandMenu.open(event.player)
    }
}
