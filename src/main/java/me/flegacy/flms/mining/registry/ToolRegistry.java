package me.flegacy.flms.mining.registry;

import me.flegacy.flms.FLMS;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ToolRegistry {
    private final Map<UUID, RegisteredTool> tools = new HashMap<>();
    private final FLMS plugin;
    private final File toolsFile;
    protected final NamespacedKey UUIDKey;

    public ToolRegistry(FLMS plugin) {
        this.plugin = plugin;
        toolsFile = new File(plugin.getDataFolder() + "toolsave.json");
        UUIDKey = new NamespacedKey(plugin, "flms_tool");
    }

    public void register(RegisteredTool tool) {
        tools.put(tool.id, tool);
    }

    public boolean remove(RegisteredTool tool) {
        return tools.remove(tool.id, tool);
    }

    public RegisteredTool getTool(ItemStack match) {
        if (match.getType().isAir())
            return null;
        if (!match.getItemMeta().getPersistentDataContainer().has(UUIDKey))
            return null;
        UUID foundID = UUID.fromString(match.getItemMeta().getPersistentDataContainer().get(UUIDKey, PersistentDataType.STRING));
        RegisteredTool tool = tools.get(foundID);

        if (!tool.modifiable && !tool.getExactCopy().isSimilar(match))
            return null;
        return tool;
    }

    public Collection<RegisteredTool> getToolsList() {
        return tools.values();
    }

    public void checkSaveFile() {
        if (!toolsFile.exists())
            try {
                toolsFile.createNewFile();
                plugin.getLogger().info("I couldn't find a tool save file for this plugin, so I created one! You may ignore this if it's your first time running the FLMS plugin.");
            } catch (IOException e) {
                plugin.getLogger().severe("I tried to create a save file for tool data, but there was an error. FLMS configurations will not be saved for this session.");
            }
    }


}
