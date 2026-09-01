package io.github.flegacy.flms.mining

import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.registry.RegisteredBlock
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.UUID
import kotlin.random.Random

class MineManager(private val plugin: FLMS) {

    private val tasks = mutableMapOf<UUID, MineTask>()
    private val ids = mutableMapOf<UUID, Int>()

    private fun ensureID(player: Player) {
        if (!ids.containsKey(player.uniqueId))
            ids[player.uniqueId] = Random.nextInt()
    }

    fun getID(player: Player): Int {
        ensureID(player)
        return ids[player.uniqueId]!!
    }

    fun startTask(player: Player, interval: UInt, location: Location, block: RegisteredBlock) {
        require(!hasTask(player))

        val task = MineTask(plugin, this, player, interval, location, block)
        tasks[player.uniqueId] = task
        task.cycle()
    }

    fun stopTask(player: Player) {
        if (tasks[player.uniqueId] == null)
            return
        tasks[player.uniqueId]!!.abort()
    }

    fun wipeTag(player: Player) {
        tasks.remove(player.uniqueId)
    }

    fun hasTask(player: Player): Boolean = tasks.containsKey(player.uniqueId)

}
