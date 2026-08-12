package me.flegacy.flms.config.ui.tools;

import com.comphenix.protocol.utility.MinecraftReflection;
import me.flegacy.flms.FLMS;
import me.flegacy.flms.mining.registry.RegisteredBlock;
import me.flegacy.flms.mining.registry.RegisteredTool;
import me.flegacy.flms.ui.FLMSInterface;
import me.flegacy.flms.utils.FLMSException;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class ToolEditor extends FLMSInterface {

    // If a tool is provided, it will be directly modified without adding a new tool.
    private RegisteredTool toolResult;
    private final boolean editing;
    private final Set<RegisteredBlock> breakableBlocks;
    private float breakingPower = 1;
    private boolean enabled = true;
    private boolean modifiable = false;
    private ItemStack item;

    /**
     * Creates a ToolEditor menu for a new tool
     *
     * @param plugin the FLMS instance
     */
    public ToolEditor(FLMS plugin) {
        super(plugin, "Creating Tool", 27);
        editing = false;
        breakableBlocks = new HashSet<>();
    }

    /**
     * Creates a ToolEditor menu for editing an existing tool.
     * The result will not be added to the registry if a base tool is passed.
     *
     * @param plugin   the FLMS instance
     * @param baseTool the tool to edit
     */
    public ToolEditor(FLMS plugin, @NotNull RegisteredTool baseTool) {
        super(plugin, "Editing Tool", 27);
        editing = true;
        toolResult = baseTool;
        breakingPower = toolResult.getBreakingPower();
        modifiable = toolResult.modifiable;
        enabled = toolResult.enabled;
        breakableBlocks = toolResult.getBreakableBlocks();

    }

    /**
     * Either finalize edits or create a new tool from the provided info.
     *
     * @return whether the operation was successful or not
     */
    public boolean finish() {
        if (item == null)
            return false;
        if (!editing)
            toolResult = new RegisteredTool(plugin, item);
        toolResult.setBreakingPower(breakingPower);
        toolResult.getBreakableBlocks().clear();
        toolResult.getBreakableBlocks().addAll(breakableBlocks);
        toolResult.modifiable = modifiable;
        toolResult.enabled = enabled;
        if (!editing)
            plugin.toolRegistry.register(toolResult);
        return true;
    }

    /**
     * Set the ItemStack this RegisteredTool is bound to.
     *
     * @param item the ItemStack
     * @return true if there were no duplicates found and the item was successfully set
     */
    public boolean setItem(@NotNull ItemStack item) {
        if (item.getType().isAir())
            throw new FLMSException("A registered tool can't be created with an AIR type.");
        for (RegisteredTool tool : plugin.toolRegistry.getToolsList())
            if (tool.isDuplicateItem(item))
                return false;
        this.item = MinecraftReflection.getBukkitItemStack(item);
        return true;
    }


    public ItemStack getItem() {
        return item;
    }

    public Set<RegisteredBlock> getBreakableBlocks() {
        return Set.copyOf(breakableBlocks);
    }

    public boolean addBreakableBlock(RegisteredBlock block) {
        return breakableBlocks.add(block);
    }

    public void toggleModifiable() {
        modifiable = !modifiable;
    }

    public boolean isModifiable() {
        return modifiable;
    }

    public void toggleEnabled() {
        enabled = !enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setBreakingPower(float value) {
        if (value <= 0)
            throw new FLMSException("Breaking power must be a float type > 0");
        breakingPower = value;
    }

    public float getBreakingPower() {
        return breakingPower;
    }

}
