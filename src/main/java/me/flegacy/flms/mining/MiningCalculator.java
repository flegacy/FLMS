package me.flegacy.flms.mining;

import me.flegacy.flms.FLMS;
import me.flegacy.flms.mining.registry.RegisteredBlock;
import me.flegacy.flms.mining.registry.RegisteredTool;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MiningCalculator {

    private final FLMS plugin;

    public MiningCalculator(FLMS plugin) {
        this.plugin = plugin;
    }

    private boolean isSubmerged(Player player) {
        Material feetBlockType = player.getEyeLocation().getBlock().getType();
        return (feetBlockType == Material.WATER || feetBlockType == Material.LAVA);
    }

    // TODO maybe a better method would be using armor change events, if that exists (in order for it to work on all armor pieces)
    private boolean hasAquaAffinity(Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        if (helmet == null) return false;
        return helmet.getEnchantmentLevel(Enchantment.AQUA_AFFINITY) > 0;
    }

    private boolean isOnGroud(Player player) {
        // Cast to living entity for server-side calculation, not client side
        return ((LivingEntity) player).isOnGround();
    }

    // TODO debug
    private void db(String msg) {
        Bukkit.broadcastMessage(msg);
    }

    // TODO ensure that regions and harvestable checks are done before this calculation
    // TODO calculation is STILL inaccurate
    public int getTicks(Player player, RegisteredTool tool, RegisteredBlock block, int efficiency) {
        float hardness = block.getHardness();
        float timeTicks = 20*hardness;

        // TODO what? is this supposed to be proper tool or world guard?
        if (tool.isBreakableBlock(block))
            timeTicks *= 1.5f;
        else
            timeTicks *= 5;

        // TODO add proper tool function; like pickaxes to stone and axes to wood
        boolean properTool = true;
        if (properTool) {
            timeTicks -= (float) (20*(1+Math.pow(efficiency, 2)));
        }

        PlayerStats stats = plugin.playerEffects.get(player.getUniqueId());
        float hastePercentage = (20*stats.getHaste());
        float fatiguePercentage = (float) (100-30*Math.pow(0.3, stats.getFatigue()-1));
        timeTicks /= 1+(hastePercentage/100);
        timeTicks *= 1+(fatiguePercentage/100);

        int penalty = 1;
        if (isSubmerged(player) && !hasAquaAffinity(player))
            penalty *= 5;
        if (!isOnGroud(player))
            penalty *= 5;
        timeTicks *= penalty;

        return Math.round(timeTicks);
    }
}
