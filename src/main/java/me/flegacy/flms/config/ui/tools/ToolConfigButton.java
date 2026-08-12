package me.flegacy.flms.config.ui.tools;

import me.flegacy.flms.FLMS;
import me.flegacy.flms.mining.registry.RegisteredBlock;
import me.flegacy.flms.mining.registry.RegisteredTool;
import me.flegacy.flms.ui.elements.ActionElement;
import me.flegacy.flms.utils.Text;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ToolConfigButton implements ActionElement {

    private final ItemStack display;
    private final FLMS plugin;
    private final RegisteredTool toolToEdit;

    public ToolConfigButton(FLMS plugin, RegisteredTool toolToEdit) {
        this.plugin = plugin;
        this.toolToEdit = toolToEdit;
        this.display = createDisplay();
    }

    @Override
    public void execute(Player player, InventoryClickEvent event) {
    }

    @Override
    public ItemStack getDisplayItem() {
        return display;
    }

    private ItemStack createDisplay() {
        ItemStack item = toolToEdit.getExactCopy();
        ItemMeta meta = item.getItemMeta();
        List<String> newLore = (meta.hasLore())
                ? meta.getLore()
                : new ArrayList<>();
        newLore.add("");
        newLore.add(
                Text.format("&7Breaking Power: &e" + toolToEdit.getBreakingPower())
        );
        String enchantDisplay = (toolToEdit.getEnchantLevel() == 0)
                ? "&8None!"
                : String.valueOf(toolToEdit.getEnchantLevel());
        newLore.add(Text.format("&7Efficiency: &e" + enchantDisplay));
        if (!toolToEdit.canBreakAny()) {
            newLore.add(Text.format("&7Can break: &e⤵"));
            for (RegisteredBlock block : toolToEdit.getBreakableBlocks())
                newLore.add(Text.format("  &e- &f" + block.getDisplayName()));
        }
        newLore.add("");
        newLore.add(Text.format("&f&lLEFT-CLICK TO OBTAIN"));
        newLore.add(Text.format("&e&lMIDDLE-CLICK TO &c&lDELETE"));
        newLore.add(Text.format("&6&lRIGHT-CLICK TO EDIT"));
        meta.setLore(newLore);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }
}
