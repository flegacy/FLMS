package me.flegacy.flms.command.options;

import me.flegacy.flms.FLMS;
import me.flegacy.flms.mining.registry.RegisteredBlock;
import me.flegacy.flms.mining.registry.RegisteredTool;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TestOption implements CommandOption {

    private static final String NAME = "test";
    private static final List<String> COMPLETIONS = List.of();

    private final FLMS plugin;

    public TestOption(FLMS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        RegisteredBlock testBlock = new RegisteredBlock(Material.GRASS_BLOCK);
        testBlock.setHardness(1.5f);
        RegisteredBlock testBlock2 = new RegisteredBlock(Material.BEDROCK);
        testBlock2.setHardness(100);
        testBlock2.addDrop(new ItemStack(Material.DIAMOND, 34));
        testBlock2.addDrop(new ItemStack(Material.MUSIC_DISC_RELIC));
        testBlock2.addDrop(plugin.itemLibrary.createWand());

        plugin.blockRegistry.add(testBlock);
        plugin.blockRegistry.add(testBlock2);

        RegisteredTool testTool1 = new RegisteredTool(plugin, new ItemStack(Material.WOODEN_PICKAXE));
        testTool1.setBreakingPower(3);
        testTool1.addBreakableBlock(testBlock);
        testTool1.addBreakableBlock(testBlock2);

        ItemStack item = new ItemStack(Material.POTATO);
        plugin.itemLibrary.enchantEfficiency(item, 2, true);
        RegisteredTool testTool2 = new RegisteredTool(plugin, item);
        testTool2.setBreakingPower(100);
        testTool2.addBreakableBlock(testBlock2);

        plugin.toolRegistry.register(testTool1);
        plugin.toolRegistry.register(testTool2);

        return true;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public List<String> getCompletionList(CommandSender sender, Command command, String label, String[] args) {
        return COMPLETIONS;
    }
}
