package me.flegacy.flms.mining.registry;

import me.flegacy.flms.FLMS;
import org.bukkit.Material;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BlockRegistry {

    private final Map<Material, RegisteredBlock> blocks = new HashMap<>();
    private final FLMS plugin;
    private final File blocksFile;

    public BlockRegistry(FLMS plugin) {
        this.plugin = plugin;
        blocksFile = new File(plugin.getDataFolder(), "blocksave.json");
        checkSaveFile();

    }

    public boolean add(RegisteredBlock block) {
        if (!blocks.containsKey(block.type))
            blocks.put(block.type, block);
        else
            return false;
        return true;
    }

    public void remove(RegisteredBlock block) {
        blocks.remove(block.type);
    }

    public RegisteredBlock getBlock(Material type) {
        return blocks.get(type);
    }

    public Collection<RegisteredBlock> getBlockList() {
        return blocks.values();
    }

    private void checkSaveFile() {
        if (!blocksFile.exists())
            try {
                blocksFile.createNewFile();
                plugin.getLogger().info("I couldn't find a block save file for this plugin, so I created one! You may ignore this if it's your first time running the FLMS plugin.");
            } catch (IOException e) {
                plugin.getLogger().severe("I tried to create a save file for block data, but there was an error. FLMS configurations will not be saved for this session.");
                e.printStackTrace();
            }
    }

    public void writeToFile() {
        File dataFolder = plugin.getDataFolder();
        plugin.checkDataFolder();
        checkSaveFile();

        // TOOD change this to just one instance in main class
        ObjectMapper mapper = new ObjectMapper();

    }
}
