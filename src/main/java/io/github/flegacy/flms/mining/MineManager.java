package io.github.flegacy.flms.mining;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import io.github.flegacy.flms.FLMS;
import io.github.flegacy.flms.data.ConfigurationValues;
import io.github.flegacy.flms.registry.RegisteredBlock;
import io.github.flegacy.flms.utils.SoundPlayer;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class MineManager {
    private static MineManager instance;
    private final FLMS plugin;
    private final Random random;

    private final Map<UUID, MineTask> taskMap;
    private final Map<UUID, Integer> playerIdMap;

    public MineManager(FLMS plugin) {

        if (instance != null)
            throw new IllegalStateException("There is already an active MineManager instance.");
        instance = this;

        this.plugin = plugin;
        taskMap = new HashMap<>();
        playerIdMap = new HashMap<>();
        random = new Random();
    }

    private void ensureMiningID(Player player) {
        if (!playerIdMap.containsKey(player.getUniqueId()))
            playerIdMap.put(player.getUniqueId(), random.nextInt());
    }

    public int getMiningID(Player player) {
        ensureMiningID(player);
        return playerIdMap.get(player.getUniqueId());
    }

    public void startTask(Player player, int tickInterval, Location location, RegisteredBlock registeredBlock) {
        if (hasTask(player))
            throw new IllegalStateException(
                    "Cant create a new mining task for '" + player + "', they are already mining.");
        MineTask task = new MineTask(player, tickInterval, location, registeredBlock);
        taskMap.put(player.getUniqueId(), task);
        task.cycle();
    }

    public boolean hasTask(Player player) {
        return taskMap.containsKey(player.getUniqueId());
    }

    public void cancelTask(Player player) {
        if (!taskMap.containsKey(player.getUniqueId()))
            return;
        MineTask task = taskMap.get(player.getUniqueId());

        task.abort();
    }

    private class MineTask {

        private final Player player;
        private final int tickInterval;
        private final Location location;
        private final ProtocolManager protocolManager;
        private final PacketContainer packet;
        private final RegisteredBlock block;

        private boolean active = true;
        // 0-9, 10 is empty
        private int stage = 0;

        public MineTask(Player player, int tickInterval, Location location, RegisteredBlock block) {

            if (tickInterval < 0)
                throw new IllegalArgumentException("Tick interval can't be negative.");

            this.player = player;
            this.tickInterval = tickInterval;
            this.location = location;
            this.block = block;
            protocolManager = plugin.getProtocolManager();
            packet = protocolManager.createPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);

            preparePacket();
        }

        private void preparePacket() {
            // Player-unique entity id
            packet.getIntegers().write(0, plugin.getMineManager().getMiningID(player));

            // Setting the packet to display at the given location
            BlockPosition position = new BlockPosition(location.getBlockX(), location.getBlockY(),
                    location.getBlockZ());
            packet.getBlockPositionModifier().write(0, position);

            packet.getIntegers().write(1, stage);
        }

        private void finish() {
            packet.getIntegers().write(1, 10);
            protocolManager.sendServerPacket(player, packet);

            Block original = player.getWorld().getBlockAt(location);
            FLMSBlockBreakEvent event = new FLMSBlockBreakEvent(original, player);
            event.setExpToDrop(block.getXP());
            event.setDropItems(true);
            // TODO notify user of the nature of block drops. They should be able to configure it as they would normally thanks to the custom event.
            plugin.getServer().getPluginManager().callEvent(event);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!event.isCancelled()) {
                        // TODO test event compatibility with other plugins
                        player.getWorld().playEffect(original.getLocation(), Effect.DESTROY_BLOCK, original.getBlockData());
                        boolean update = plugin.getConfigValues().getBoolean(ConfigurationValues.Key.BLOCK_BREAKING_UPDATES);
                        original.setType(block.getPostBreakMaterial(), update);

                        for (ItemStack item: block.getDropList())
                            original.getWorld().dropItemNaturally(original.getLocation(), item);

                        ItemStack heldItem = event.getPlayer().getInventory().getItemInMainHand();
                        if (!heldItem.getType().isAir() && !heldItem.getItemMeta().isUnbreakable())
                            heldItem.damage(1, player);

                        original.getWorld().spawn(original.getLocation(), ExperienceOrb.class, entity -> {
                            int xp = (block.getXP() > 1) ? block.getXP()/2 : block.getXP();
                            int count = (xp > 1) ? 2 : 1;
                            entity.setExperience(xp);
                            entity.setCount(count);
                        });
                    }
                    else {
                        boolean cancelledSound = plugin.getConfigValues().getBoolean(ConfigurationValues.Key.BLOCK_BREAK_FAILURE_SOUND);
                        if (cancelledSound)
                            SoundPlayer.playErrorSound(player);
                    }
                }
            }.runTaskLater(plugin, 1);

            taskMap.remove(player.getUniqueId());
        }

        public void cycle() {
            if (!active)
                return;
            if (tickInterval == 0 || stage == 10) {
                finish();
                return;
            }

            protocolManager.sendServerPacket(player, packet);
            packet.getIntegers().write(1, stage++);

            plugin.getServer().getScheduler().runTaskLater(plugin, this::cycle, tickInterval);
        }

        public void abort() {
            active = false;
            packet.getIntegers().write(1, 10);
            protocolManager.sendServerPacket(player, packet);
            taskMap.remove(player.getUniqueId());
        }
    }

}
