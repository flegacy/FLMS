package io.github.flegacy.flms.items;

import io.github.flegacy.flms.utils.TextConstants;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FLMSEnchanter {
	private static final String ENCHANT_PREFIX = "⛏ Efficiency ";
	private final ItemLibrary itemLibrary;

	FLMSEnchanter(ItemLibrary library) {
		itemLibrary = library;
	}

	public void enchantEfficiency(@NotNull ItemStack item, short level, boolean show) {
		if (item.getType().isAir())
			throw new IllegalArgumentException("Can't enchant air itemstacks.");

		if (level < 0)
			throw new IllegalArgumentException("Enchantment level can't be negative.");

		ItemMeta meta = item.getItemMeta();
		if (isEnchanted(item) && level == 0) {
			meta.getPersistentDataContainer().remove(itemLibrary.enchantKey);
		} else {
			meta
					.getPersistentDataContainer()
					.set(itemLibrary.enchantKey, PersistentDataType.SHORT, level);
		}

		updateLore(meta, level, show);
		updateGlint(meta, level, show);

		item.setItemMeta(meta);
	}

	public boolean isEnchanted(@NotNull ItemStack item) {
		if (!item.hasItemMeta())
			return false;
		return item.getItemMeta().getPersistentDataContainer().has(itemLibrary.enchantKey);
	}

	public int getEnchantLevel(@NotNull ItemStack item) {
		if (!isEnchanted(item)) return 0;
		return item
				.getItemMeta()
				.getPersistentDataContainer()
				.get(itemLibrary.enchantKey, PersistentDataType.SHORT);
	}

	private void updateLore(ItemMeta meta, int newLevel, boolean show) {
		List<Component> newLore = (meta.hasLore())
				? meta.lore()
				: new ArrayList<>();

		assert newLore != null;
		for (Component s : newLore) {
			if (s.toString().contains(ENCHANT_PREFIX)) {
				newLore.remove(s);
				break;
			}
		}
		// TODO add config option to display enchantments in roman numerals
		if (show && newLevel != 0)
			newLore.addFirst(TextConstants.miniMessage("<gray><!i>" + ENCHANT_PREFIX + newLevel));
		meta.lore(newLore);
	}

	private void updateGlint(ItemMeta meta, int newLevel, boolean show) {
		if (meta.hasEnchants()) return;

		if (newLevel == 0 || !show) {
			meta.setEnchantmentGlintOverride(null);
		} else {
			meta.setEnchantmentGlintOverride(true);
		}
	}
}
