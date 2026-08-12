package me.flegacy.flms.mining.registry;

import com.comphenix.protocol.utility.MinecraftReflection;
import me.flegacy.flms.FLMS;
import me.flegacy.flms.utils.FLMSException;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RegisteredTool {

    public final UUID id = UUID.randomUUID();
    private final ItemStack tool;
    private final Set<RegisteredBlock> breakableBlocks = new HashSet<>();
    private float breakingPower = 1;
    public boolean enabled = true;
    public boolean modifiable = false;
    private final FLMS plugin;

    // TODO add feature to toggle the function of tools
    // TODO add optimized blocks
    // TODO add hand functionality

    public RegisteredTool(FLMS plugin, @NotNull ItemStack tool) {
        if (tool.getType().isAir())
            throw new FLMSException("A RegisteredTool cannot be created with an air ItemS");
        this.tool = tool;
        this.plugin = plugin;
    }

    public void setBreakingPower(float value) {
        if (value > 0) breakingPower = value;
        else throw new FLMSException(
                "The tool's breaking power must be a float type > 0"
        );
    }

    public float getBreakingPower() {
        return breakingPower;
    }

    public boolean addBreakableBlock(RegisteredBlock block) {
        return breakableBlocks.add(block);
    }

    public boolean removeBreakableBlock(RegisteredBlock block) {
        return breakableBlocks.remove(block);
    }

    public Set<RegisteredBlock> getBreakableBlocks() {
        return breakableBlocks;
    }

    public boolean isBreakableBlock(RegisteredBlock block) {
        return breakableBlocks.contains(block);
    }

    public boolean canBreakAny() {
        return breakableBlocks.isEmpty();
    }

    /**
     * Create a copy of this registered tool's original item
     *
     * @return A copy of the ItemStack
     */
    public ItemStack getExactCopy() {
        ItemStack copy = MinecraftReflection.getBukkitItemStack(tool);
        ItemMeta meta = copy.getItemMeta();
        meta
                .getPersistentDataContainer()
                .set(
                        plugin.toolRegistry.UUIDKey,
                        PersistentDataType.STRING,
                        id.toString()
                );
        copy.setItemMeta(meta);
        return copy;
    }

    public boolean isDuplicateItem(ItemStack comparison) {
        return tool.isSimilar(comparison);
    }

    public int getEnchantLevel() {
        return plugin.itemLibrary.getEnchantLevel(tool);
    }
}
