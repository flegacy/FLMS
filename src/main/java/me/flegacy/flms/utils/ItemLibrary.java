package me.flegacy.flms.utils;

import me.flegacy.flms.FLMS;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ItemLibrary {

    private final NamespacedKey flmsKey;
    private static final String ENCHANT_PREFIX = "⛏ Efficiency ";
    private final NamespacedKey enchantKey;

    public ItemLibrary(FLMS plugin) {
        flmsKey = new NamespacedKey(plugin, "flms_item");
        enchantKey = new NamespacedKey(plugin, "flms_efficiency");
    }

    public void enchantEfficiency(@NotNull ItemStack item, int level, boolean show) {
        if (item.getItemMeta() == null) throw new FLMSException(
                "The item inserted can't be enchanted; it has no item meta."
        );
        if (level > 255 || level < 0) throw new FLMSException(
                "Invalid enchantment level; must be an integer between 0 and 255."
        );

        ItemMeta meta = item.getItemMeta();
        if (isEnchanted(item) && level == 0) {
            meta.getPersistentDataContainer().remove(enchantKey);
        } else {
            meta
                    .getPersistentDataContainer()
                    .set(enchantKey, PersistentDataType.INTEGER, level);
        }

        updateLore(meta, level, show);
        updateGlint(meta, level, show);

        item.setItemMeta(meta);
    }

    public boolean isEnchanted(@NotNull ItemStack item) {
        if (!item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(enchantKey);
    }

    public int getEnchantLevel(@NotNull ItemStack item) {
        if (!isEnchanted(item)) return 0;
        return item
                .getItemMeta()
                .getPersistentDataContainer()
                .get(enchantKey, PersistentDataType.INTEGER);
    }

    private void updateLore(ItemMeta meta, int newLevel, boolean show) {
        List<String> newLore = (meta.hasLore())
                ? meta.getLore()
                : new ArrayList<>();

        for (String s : newLore) {
            if (ChatColor.stripColor(s).contains(ENCHANT_PREFIX)) {
                newLore.remove(s);
                break;
            }
        }
        // TODO add config option to display enchantments in roman numerals
        if (show && newLevel != 0) newLore.addFirst(
                Text.format("&7" + ENCHANT_PREFIX + newLevel)
        );
        meta.setLore(newLore);
    }

    private void updateGlint(ItemMeta meta, int newLevel, boolean show) {
        if (meta.hasEnchants()) return;

        if (newLevel == 0 || !show) {
            meta.setEnchantmentGlintOverride(null);
        } else {
            meta.setEnchantmentGlintOverride(true);
        }
    }

    public String getTag(@NotNull ItemStack item) {
        if (!item.hasItemMeta()) return null;
        if (
                !item.getItemMeta().getPersistentDataContainer().has(flmsKey)
        ) return null;
        return item
                .getItemMeta()
                .getPersistentDataContainer()
                .get(flmsKey, PersistentDataType.STRING);
    }

    public void setFLMSTag(@NotNull ItemStack item, String value) {
        if (item.getType().isAir())
            throw new FLMSException("Can't create an FLMS tag for an air ItemStack");
        ItemMeta meta = item.getItemMeta();
        meta
                .getPersistentDataContainer()
                .set(flmsKey, PersistentDataType.STRING, value);
        item.setItemMeta(meta);
    }

    public ItemStack createWand() {
        ItemStack item = new ItemStack(Material.GOLDEN_AXE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Text.format("&eFLMS Wand"));
        meta.setLore(Text.formatList("", "&6&lHOLD & CLICK TO USE"));
        meta.addItemFlags(
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_ADDITIONAL_TOOLTIP
        );
        meta
                .getPersistentDataContainer()
                .set(flmsKey, PersistentDataType.STRING, "wand");
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createEmptyGlass() {
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Text.format("&7"));
        meta.addItemFlags(ItemFlag.values());
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createLeftArrowIcon() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Text.format("&e<- Previous Menu"));
        meta.setLore(Text.formatList("&6&lCLICK TO OPEN"));
        meta.addItemFlags(
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_ADDITIONAL_TOOLTIP
        );
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createRightArrowIcon() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Text.format("&eNext Menu ->"));
        meta.setLore(Text.formatList("&6&lCLICK TO OPEN"));
        meta.addItemFlags(
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_ADDITIONAL_TOOLTIP
        );
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createExitIcon() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Text.format("&cExit Menu"));
        meta.setLore(Text.formatList("&6&lCLICK"));
        meta.addItemFlags(
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_ADDITIONAL_TOOLTIP
        );
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createBlockIcon() {
        ItemStack item = new ItemStack(Material.BEDROCK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Text.format("&eEdit Custom Blocks"));
        meta.addItemFlags(
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_ADDITIONAL_TOOLTIP
        );
        meta.setLore(
                Text.formatList(
                        "",
                        "&7Manipulate all types of blocks by",
                        "&7setting their hardness values, changing",
                        "&7what tools are the best at breaking them,",
                        "&7and controlling what items they drop.",
                        "",
                        "&6&lCLICK TO OPEN"
                )
        );
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createToolIcon() {
        ItemStack item = new ItemStack(Material.GOLDEN_PICKAXE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Text.format("&eEdit Custom Tools"));
        meta.addItemFlags(
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_ADDITIONAL_TOOLTIP
        );
        meta.setLore(
                Text.formatList(
                        "",
                        "&7Create your own tools from any",
                        "&7item in the game and define how",
                        "&7strong they are with breaking power",
                        "&7and enchantments.",
                        "",
                        "&6&lCLICK TO OPEN"
                )
        );
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createEffectIcon() {
        ItemStack item = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        meta.setColor(Color.LIME);
        meta.setDisplayName(Text.format("&eEdit Player Effects"));
        meta.setLore(
                Text.formatList(
                        "",
                        "&7Vanilla haste and mining fatigue",
                        "&7wont work with this plugin, so you",
                        "&7can instead modify those values here,",
                        "&7or with the &f/flms effect &7command.",
                        "",
                        "&6&lCLICK TO OPEN"
                )
        );
        meta.addItemFlags(
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_ADDITIONAL_TOOLTIP
        );
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createRegionIcon() {
        ItemStack item = new ItemStack(Material.GLOBE_BANNER_PATTERN);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Text.format("&eEdit Regions"));
        meta.setLore(
                Text.formatList(
                        "",
                        "&7If you only want certain parts of",
                        "&7the world to be breakable by players,",
                        "&7you can manage that here.",
                        "",
                        "&6&lCLICK TO OPEN"
                )
        );
        meta.addItemFlags(
                ItemFlag.HIDE_BANNER_PATTERNS,
                ItemFlag.HIDE_PROVIDES_BANNER_PATTERNS,
                ItemFlag.HIDE_ADDITIONAL_TOOLTIP,
                ItemFlag.HIDE_ATTRIBUTES
        );
        item.setItemMeta(meta);
        return item;
    }
}
