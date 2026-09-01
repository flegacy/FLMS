package io.github.flegacy.flms.mining

import io.github.flegacy.flms.FLMS
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockDamageAbortEvent
import org.bukkit.event.block.BlockDamageEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class MineListener(private val plugin: FLMS): Listener {

    private val manager = plugin.mineManager()

    private val haste = PotionEffect(
        PotionEffectType.HASTE,
        PotionEffect.INFINITE_DURATION,
        1,
        true,
        false,
        false
    )

    private val fatigue = PotionEffect(
        PotionEffectType.MINING_FATIGUE,
        PotionEffect.INFINITE_DURATION,
        2,
        true,
        false,
        false
    )

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        applyEffects(event.player)
    }

    // Apply effects for world switching and other occurances where the player would lose the effects.
    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        plugin.server.scheduler.runTaskLater(plugin, {task -> run {
            if (event.player.isOnline)
                applyEffects(event.player)
        }}, 1)
    }

    private fun applyEffects(player: Player) {
        player.removePotionEffect(PotionEffectType.HASTE)
        player.removePotionEffect(PotionEffectType.MINING_FATIGUE)
        player.addPotionEffect(haste)
        player.addPotionEffect(fatigue)
    }

    @EventHandler
    fun onBlockDamage(event: BlockDamageEvent) {
        val player = event.player
        if (player.gameMode != GameMode.SURVIVAL)
            return
        val block = plugin.registry().findBlock(event.block.type) ?: return
        val interval = if (event.block.type.hardness == 0f) 0u else 3u

        if (manager.hasTask(player)) {
            plugin.componentLogger.warn("Can't start mining task for '${player.name}, they are already mining.")
            return
        }
        manager.startTask(player, interval, event.block.location, block)
    }

    @EventHandler
    fun onDamageAbort(event: BlockDamageAbortEvent) {
        val player = event.player
        if (manager.hasTask(player))
            manager.stopTask(player)
    }
    
}
