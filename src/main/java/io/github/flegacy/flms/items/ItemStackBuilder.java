package io.github.flegacy.flms.items;

import java.util.ArrayList;
import java.util.List;

import io.github.flegacy.flms.utils.TextConstants;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;

public class ItemStackBuilder {

    private final ItemStack item;
    private final ItemMeta meta;
    
    public ItemStackBuilder(@NotNull Material type) {
        if (type.isAir())
            throw new IllegalArgumentException("You can't build an ItemStack of the air type.");
        item = new ItemStack(type);
        meta = item.getItemMeta();
    }

    public ItemStackBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemStackBuilder setDisplayName(String text) {
        meta.customName(TextConstants.miniMessage("<!i>" + text));
        return this;
    }

    public ItemStackBuilder setLore(String... msgs) {
        List<Component> lore = new ArrayList<>();
        for (String string: msgs)
            lore.add(TextConstants.miniMessage("<!i>" + string));
        meta.lore(lore);
        return this;
    }

    public ItemStackBuilder addItemFlags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return this;
    }

    public ItemStackBuilder setGlowing(boolean glowing) {
        if (glowing)
            meta.setEnchantmentGlintOverride(true);
        else
            meta.setEnchantmentGlintOverride(null);
        return this;
    }

    public ItemStackBuilder setUnbreakable(boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return this;
    }

    public ItemStackBuilder tag(NamespacedKey key, String tag) {
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, tag);
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }



}
