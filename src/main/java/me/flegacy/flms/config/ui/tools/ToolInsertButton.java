package me.flegacy.flms.config.ui.tools;

import me.flegacy.flms.ui.elements.ActionElement;
import me.flegacy.flms.utils.Text;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ToolInsertButton implements ActionElement {

    private final ToolEditor editor;
    private final ItemStack display;
    private final ItemStack empty;

    public ToolInsertButton(ToolEditor editor) {
        this.editor = editor;
        ItemStack empty = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta meta = empty.getItemMeta();
        meta.setDisplayName(Text.format(""));
    }

    @Override
    public void execute(Player player, InventoryClickEvent event) {
        ItemStack cursor = event.getCursor();
        if (cursor == null)
            return;
        boolean editorFilled = editor.getItem() != null;
        if (editorfilled)
    }

    @Override
    public ItemStack getDisplayItem() {
        return null;
    }
}
