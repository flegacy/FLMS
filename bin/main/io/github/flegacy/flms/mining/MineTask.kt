package io.github.flegacy.flms.mining

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.wrappers.BlockPosition
import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.data.Key
import io.github.flegacy.flms.registry.RegisteredBlock
import io.github.flegacy.flms.util.msgFormat
import io.github.flegacy.flms.util.soundError
import net.kyori.adventure.chat.ChatType
import org.bukkit.Effect
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.ExperienceOrb
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class MineTask(
    private val plugin: FLMS,
    private val manager: MineManager,
    private val player: Player,
    private val interval: UInt,
    private val location: Location,
    private val block: RegisteredBlock
) {
    private val protocol = ProtocolLibrary.getProtocolManager()
    private val packet = protocol.createPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION)

    private var stage = 0
    private var active = true

    init {
        prepare()
    }

    private fun prepare() {
        if (interval == 0u)
            return
        // Player-unique entity id
        packet.integers.write(0, plugin.mineManager().getID(player))

        // Setting the packet to display at the given location
        val position = BlockPosition(location.blockX, location.blockY, location.blockZ)
        packet.blockPositionModifier.write(0, position)
        packet.integers.write(1, stage)
    }

    private fun finish() {
        packet.integers.write(1, 10)
        protocol.sendServerPacket(player, packet)

        val original = player.world.getBlockAt(location)
        val originalType = block.type

        val event = FLMSBlockBreakEvent(original, player)
        event.expToDrop = block.xp.toInt()
        event.isDropItems = true
        plugin.server.pluginManager.callEvent(event)
        // TODO notify user of the nature of block drops. they should be able to configure it as normally thansk to the custom event

        plugin.server.scheduler.runTaskLater(plugin, { task -> run {

            if (event.isCancelled) {
                if (plugin.cfgVals().boolean(Key.BLOCK_BREAK_FAILURE_SOUND))
                    soundError(player)
                val msg = plugin.cfgVals().string(Key.BLOCK_BREAK_DENIAL_MESSAGE)
                val msgLocation = plugin.cfgVals().string(Key.BLOCK_BREAK_DENIAL_LOCATION)

                if (msgLocation.equals("chat", true))
                    player.sendMessage(msgFormat(msg))
                else if (msgLocation.equals("actionbar", true))
                    player.sendActionBar(msgFormat(msg))

                if (block.type.hardness == 0f)
                    original.setType(originalType, false)
            } else
                fullBreak(original, originalType)

        } }, 1)

        plugin.mineManager().wipeTag(player)
    }

    private fun fullBreak(original: Block, originalType: Material) {
        player.world.playEffect(original.location, Effect.DESTROY_BLOCK, originalType.createBlockData())

        val update = plugin.cfgVals().boolean(Key.BLOCK_BREAKING_UPDATES)
        original.setType(block.postType, update)

        block.drops.forEach { drop -> original.world.dropItemNaturally(original.location, drop) }

        // TODO think hard about durability system and how certain tools are better at breaking blocks, which is specified from the tool config
        val held = player.inventory.itemInMainHand
        if (!held.type.isAir && !held.itemMeta.isUnbreakable)
            held.damage(1, player)

        original.world.spawn(original.location, ExperienceOrb::class.java) {
            val xp = if (block.xp > 1u) block.xp / 2u else block.xp
            val count = if (xp > 1u) 2 else 1
            it.experience = xp.toInt()
            it.count = count
        }
        
        // Go through all scenerios of this
        val next = plugin.registry().findBlock(block.postType) ?: return
        val newInterval = if (next.hardness == 0.toUShort()) 0u else 3u
        if (!plugin.mineManager().hasTask(player) && next.type.hardness != 0f)
            // TODO a NEW interval should be generated here
            plugin.mineManager().startTask(player, interval, location, next)
    }

    fun cycle() {
        if (!active)
            return
        if (interval == 0u || stage == 10) {
            finish()
            return
        }

        protocol.sendServerPacket(player, packet)
        packet.integers.write(1, stage++)

        plugin.server.scheduler.runTaskLater(plugin, { run -> cycle() }, interval.toLong())
    }

    fun abort() {
        active = false
        packet.integers.write(1, 10)
        protocol.sendServerPacket(player, packet)
        manager.wipeTag(player)
    }
}
