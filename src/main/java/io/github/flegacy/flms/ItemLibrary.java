package io.github.flegacy.flms;

import io.github.flegacy.flms.utils.TextConstants;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ItemLibrary {
	private static final String FLMS_WAND_TAG = "flms_wand";

	private final ItemStack wand;
	private final NamespacedKey flmsItemKey;

	ItemLibrary(FLMS plugin) {

		flmsItemKey = new NamespacedKey(plugin, "flms_item");

		wand = new ItemStack(Material.GOLDEN_AXE);
		ItemMeta wandMeta = wand.getItemMeta();
		wandMeta.customName(TextConstants.miniMessage(TextConstants.FLMS_ORANGE + "<!i>FLMS Wand"));
		wandMeta.lore(TextConstants.messageList(TextConstants.FLMS_YELLOW + "<!i>Hold and right-click to use!"));
		wandMeta.setEnchantmentGlintOverride(true);
		AttributeModifier attackSpeedModifier = new AttributeModifier(flmsItemKey, 999, AttributeModifier.Operation.ADD_NUMBER);
		wandMeta.addAttributeModifier(Attribute.ATTACK_SPEED, attackSpeedModifier);
		wandMeta.setUnbreakable(true);
		wandMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_DESTROYS);
		wandMeta.getPersistentDataContainer().set(flmsItemKey, PersistentDataType.STRING, FLMS_WAND_TAG);
		wand.setItemMeta(wandMeta);
	}

	public ItemStack getWand() {
		return wand.clone();
	}

	public boolean isWand(@NotNull ItemStack item) {
		if (!item.hasItemMeta())
			return false;
		PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
		if (container.has(flmsItemKey))
			return false;
		return Objects.equals(container.get(flmsItemKey, PersistentDataType.STRING), FLMS_WAND_TAG);
	}

}
