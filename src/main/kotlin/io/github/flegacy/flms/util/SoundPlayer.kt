package io.github.flegacy.flms.util

import io.github.flegacy.flms.FLMS
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

fun soundError(player: Player) =
    player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, SoundCategory.UI, 1f, 0.5f)

fun soundPickup(player: Player) =
    player.playSound(player.location, Sound.ENTITY_ITEM_PICKUP, SoundCategory.UI, 1f, 1f)

fun soundClick(player: Player) =
    player.playSound(player.location, Sound.UI_BUTTON_CLICK, SoundCategory.UI, 1f, 1f)

fun soundEnchant(player: Player) =
    player.playSound(player.location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.UI, 1f, 1f)

fun soundBook(player: Player) =
    player.playSound(player.location, Sound.ITEM_BOOK_PAGE_TURN, SoundCategory.UI, 1f, 1f)

fun soundDelay(player: Player) =
    player.playSound(player.location, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.UI, 1f, 1f)

fun soundWandOpen(player: Player, plugin: FLMS) {
    object : BukkitRunnable() {
        var pitch = 0f
        override fun run() {
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.UI, 1f, pitch)
            pitch++
            if (pitch == 3f)
                cancel()
        }
    }.runTaskTimer(plugin, 0, 2)
}