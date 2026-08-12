package me.flegacy.flms.mining.listeners;

import me.flegacy.flms.FLMS;
import me.flegacy.flms.mining.MineTask;
import me.flegacy.flms.mining.registry.RegisteredBlock;
import me.flegacy.flms.mining.registry.RegisteredTool;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageAbortEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MineListener implements Listener {

    private static MineListener instance;
    public static MineListener getInstance(FLMS plugin) {
        if (instance == null) instance = new MineListener(plugin);
        return instance;
    }
    private MineListener(FLMS plugin) {
        this.plugin = plugin;
    }

    private final FLMS plugin;
	private final Map<UUID, Integer> playerIDs = new HashMap<>();
    private final Map<UUID, MineTask> playerTasks = new HashMap<>();

    // TODO testing
    public static final Map<UUID, Long> lastMineTime = new HashMap<>();

	private void createPlayerID(Player player) {
		plugin.getLogger().info("Creating new mining ID for player '" + player.getName() + "'.");
		playerIDs.put(player.getUniqueId(), plugin.random.nextInt());
	}

    @EventHandler
    public void onMineStart(BlockDamageEvent event) {
        if (event.getInstaBreak()) return;
        // TODO integrate vanilla instamined blocks with the registered block system

        Player player = event.getPlayer();
        //TODO testing
        lastMineTime.put(player.getUniqueId(), System.currentTimeMillis());
        if (player.getGameMode() != GameMode.SURVIVAL) return;

        if (playerTasks.containsKey(player.getUniqueId())) {
            plugin.getLogger().info("Couldn't start new breaking task; the player is already mining?!");
            return;
        }

		Integer playerID = playerIDs.get(player.getUniqueId());
		if (playerID == null) {
			createPlayerID(player);
			playerID = playerIDs.get(player.getUniqueId());
		}

        // TODO add regions
        RegisteredBlock block = plugin.blockRegistry.getBlock(event.getBlock().getType());
        if (block == null)
            return;
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        RegisteredTool tool = plugin.toolRegistry.getTool(heldItem);
        if (tool == null)
            return;
        int efficiency = plugin.itemLibrary.getEnchantLevel(heldItem);

        int ticks = plugin.calculator.getTicks(player, tool, block, efficiency);
        MineTask task = new MineTask(plugin, player, playerID, event.getBlock(), (float) ticks /10);
        playerTasks.put(player.getUniqueId(), task);
		task.cycle();
    }

    @EventHandler
    public void onMineEnd(BlockDamageAbortEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        if (playerTasks.containsKey(playerUUID)) {
            MineTask task = playerTasks.get(playerUUID);
            task.stop();
            playerTasks.remove(playerUUID);
        }
    }
}
