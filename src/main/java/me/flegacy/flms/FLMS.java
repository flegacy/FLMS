package me.flegacy.flms;

import me.flegacy.flms.command.FLMSCommand;
import me.flegacy.flms.config.listeners.WandListener;
import me.flegacy.flms.mining.MiningCalculator;
import me.flegacy.flms.mining.PlayerStats;
import me.flegacy.flms.mining.listeners.MineListener;
import me.flegacy.flms.mining.listeners.PlayerJoinListener;
import me.flegacy.flms.mining.registry.BlockRegistry;
import me.flegacy.flms.mining.registry.ToolRegistry;
import me.flegacy.flms.ui.InventoryListener;
import me.flegacy.flms.utils.ItemLibrary;
import me.flegacy.flms.utils.SoundLibrary;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class FLMS extends JavaPlugin {

    public static final String FLMS_PERMISSION = "flms.admin";

    public final Map<UUID, PlayerStats> playerEffects = new HashMap<>();
    public final Random random = new Random();
    public final ItemLibrary itemLibrary = new ItemLibrary(this);
    public final SoundLibrary soundLibrary = new SoundLibrary(this);
    public final BlockRegistry blockRegistry = new BlockRegistry(this);
    public final ToolRegistry toolRegistry = new ToolRegistry(this);
    public final MiningCalculator calculator = new MiningCalculator(this);

    @Override
    public void onEnable() {
        checkDataFolder();

        getCommand("flms").setExecutor(FLMSCommand.getInstance(this));
        getCommand("flms").setTabCompleter(FLMSCommand.getInstance(this));
        getServer()
                .getPluginManager()
                .registerEvents(PlayerJoinListener.getInstance(this), this);
        getServer()
                .getPluginManager()
                .registerEvents(MineListener.getInstance(this), this);
        getServer()
                .getPluginManager()
                .registerEvents(InventoryListener.getInstance(this), this);
        getServer()
                .getPluginManager()
                .registerEvents(WandListener.getInstance(this), this);

        getLogger().info("Successfully loaded.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Successfully disabled.");
    }

    public List<String> getOnlinePlayerNames() {
        List<String> names = new ArrayList<>();
        for (Player p : getServer().getOnlinePlayers()) names.add(p.getName());
        return names;
    }

    public void checkDataFolder() {
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) dataFolder.mkdir();
        getLogger().info(
                "I couldn't find a data folder for this plugin, so I created one! You may ignore this if it's your first time running the FLMS plugin."
        );
    }
}
