package io.github.flegacy.flms.config

import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.config.ui.element.StringInputDevice
import io.github.flegacy.flms.util.toPlainText
import io.papermc.paper.command.brigadier.argument.ArgumentTypes.player
import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class InputDeviceListener(private val plugin: FLMS): Listener {

    val deviceMap = ConcurrentHashMap<UUID, StringInputDevice>()

    @EventHandler
    fun onInventoryOpen(event: InventoryOpenEvent) {
        deviceMap.remove(event.player.uniqueId)
    }

    @EventHandler
    fun onAsyncChat(event: AsyncChatEvent) {
        if (deviceMap.containsKey(event.player.uniqueId))
            event.isCancelled = true
        object: BukkitRunnable() {
            override fun run() {
                if (!deviceMap.containsKey(event.player.uniqueId))
                    return
                val inputDevice = deviceMap[event.player.uniqueId] ?: return
                inputDevice.tryInput(event.player, toPlainText(event.originalMessage()))
            }
        }.runTask(plugin)
    }
}