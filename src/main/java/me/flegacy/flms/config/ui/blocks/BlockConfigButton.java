package me.flegacy.flms.config.ui.blocks;

import me.flegacy.flms.FLMS;
import me.flegacy.flms.mining.registry.RegisteredBlock;
import me.flegacy.flms.ui.elements.ActionElement;
import me.flegacy.flms.utils.Text;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BlockConfigButton implements ActionElement {

    private final ItemStack display;
    private final FLMS plugin;
    private final RegisteredBlock blockToEdit;

    public BlockConfigButton(FLMS plugin, RegisteredBlock blockToEdit) {
        this.plugin = plugin;
        this.blockToEdit = blockToEdit;
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
        ItemStack display = new ItemStack(blockToEdit.type);
        ItemMeta meta = display.getItemMeta();
        meta.setDisplayName(blockToEdit.getDisplayName());
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Text.format("&7Hardness: &e" + blockToEdit.getHardness()));
        List<ItemStack> drops = blockToEdit.getDropsList();
        if (drops.isEmpty())
            lore.add(Text.format("&7Drops: &8None!"));
        else {
            lore.add(Text.format("&7Drops: &e⤵"));
            for (ItemStack drop : drops)
                if (drop.getItemMeta().hasDisplayName())
                    lore.add(Text.format("  &ex" + drop.getAmount() + " &f" + drop.getItemMeta().getDisplayName()));
                else
                    lore.add(Text.format("  &ex" + drop.getAmount() + " &f" + Text.enumToDisplayName(drop.getType().toString())));
        }
        lore.add("");
        // TODO obtain? maybe do something cool with this
        lore.add(Text.format("&f&lLEFT-CLICK TO OBTAIN"));
        lore.add(Text.format("&e&lMIDDLE-CLICK TO &c&lDELETE"));
        lore.add(Text.format("&6&lRIGHT-CLICK TO EDIT"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        display.setItemMeta(meta);
        return display;
    }
}
