package io.github.flegacy.flms.items;

import io.github.flegacy.flms.FLMS;
import io.github.flegacy.flms.utils.TextConstants;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ItemLibrary {

	private static ItemLibrary instance;
	private final FLMSEnchanter enchanter;

    private static final String FLMS_WAND_TAG = "flms_wand";

    private final ItemStack wand;
    private final ItemStack emptyGlass;
    private final ItemStack leftArrow;
    private final ItemStack rightArrow;
    private final ItemStack backButton;
    private final ItemStack toolConfigIcon;
    private final ItemStack blockConfigIcon;
    private final ItemStack regionConfigIcon;
	private final ItemStack effectConfigIcon;

    protected final NamespacedKey flmsItemKey;
	protected final NamespacedKey enchantKey;

    public ItemLibrary(FLMS plugin) {
		if (instance != null)
			throw new IllegalStateException("There can only be one ItemLibrary instance.");
		instance = this;
		enchanter = new FLMSEnchanter(this);

        flmsItemKey = new NamespacedKey(plugin, "flms_item");
		enchantKey = new NamespacedKey(plugin, "flms_efficiency");

        wand = new ItemStack(Material.GOLDEN_AXE);
        final ItemMeta wandMeta = wand.getItemMeta();
        wandMeta.customName(TextConstants.miniMessage(TextConstants.FLMS_ORANGE + "<!i>FLMS Wand"));
        wandMeta.lore(TextConstants.messageList(TextConstants.FLMS_YELLOW + "<!i>Hold and right-click to use!"));
        wandMeta.setEnchantmentGlintOverride(true);
        final AttributeModifier attackSpeedModifier = new AttributeModifier(flmsItemKey, 999,
                AttributeModifier.Operation.ADD_NUMBER);
        wandMeta.addAttributeModifier(Attribute.ATTACK_SPEED, attackSpeedModifier);
        wandMeta.setUnbreakable(true);
        wandMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_DESTROYS);
        wandMeta.getPersistentDataContainer().set(flmsItemKey, PersistentDataType.STRING, FLMS_WAND_TAG);
        wand.setItemMeta(wandMeta);

        emptyGlass = new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE)
                .setDisplayName("")
                .addItemFlags(ItemFlag.values())
                .build();

        leftArrow = new ItemStackBuilder(Material.ARROW)
                .setDisplayName(TextConstants.FLMS_ORANGE + "<- Previous Page")
                .setLore(TextConstants.FLMS_YELLOW + "<b>CLICK TO VIEW")
                .build();

        rightArrow = new ItemStackBuilder(Material.ARROW)
                .setDisplayName(TextConstants.FLMS_ORANGE + "Next Page ->")
                .setLore(TextConstants.FLMS_YELLOW + "<b>CLICK TO VIEW")
                .build();

        backButton = new ItemStackBuilder(Material.BARRIER)
                .setDisplayName(TextConstants.FLMS_RED + "<- Go Back")
                .setLore(TextConstants.FLMS_YELLOW + "<b>CLICK TO VIEW")
                .build();

        blockConfigIcon = new ItemStackBuilder(Material.BEDROCK)
                .setDisplayName(TextConstants.FLMS_ORANGE + "Edit Custom Blocks")
                .setLore(
                        "",
                        TextConstants.FLMS_LIGHT_YELLOW + "Manipulate all types of blocks by",
                        TextConstants.FLMS_LIGHT_YELLOW + "setting their hardness values, changing",
                        TextConstants.FLMS_LIGHT_YELLOW + "what tools are the best at breaking them,",
                        TextConstants.FLMS_LIGHT_YELLOW + "and controlling what items they drop.",
                        "",
                        TextConstants.FLMS_YELLOW + "<b>CLICK TO OPEN")
                .build();
        
        toolConfigIcon = new ItemStackBuilder(Material.GOLDEN_PICKAXE)
                .setDisplayName(TextConstants.FLMS_ORANGE + "Edit Custom Tools")
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .setLore(
                        "",
                        TextConstants.FLMS_LIGHT_YELLOW + "Create your own tools from any",
                        TextConstants.FLMS_LIGHT_YELLOW + "item in the game and define how",
                        TextConstants.FLMS_LIGHT_YELLOW + "strong they are with breaking power",
                        TextConstants.FLMS_LIGHT_YELLOW + "and enchantments.",
                        "",
                        TextConstants.FLMS_YELLOW + "<b>CLICK TO OPEN")
                .build();

        regionConfigIcon = new ItemStackBuilder(Material.GLOBE_BANNER_PATTERN)
                .setDisplayName(TextConstants.FLMS_ORANGE + "Edit Regions")
                .setLore(
                        "",
                        TextConstants.FLMS_LIGHT_YELLOW + "If you only want certain parts of",
                        TextConstants.FLMS_LIGHT_YELLOW + "the world to be breakable by players,",
                        TextConstants.FLMS_LIGHT_YELLOW + "you can manage that here.",
                        "",
                        TextConstants.FLMS_YELLOW + "<b>CLICK TO OPEN")
		        .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .build();

	    effectConfigIcon = new ItemStackBuilder(Material.POTION)
			    .setDisplayName(TextConstants.FLMS_ORANGE + "Edit Player Effects")
			    .addItemFlags(ItemFlag.values())
			    .setLore(
					    "",
					    TextConstants.FLMS_LIGHT_YELLOW + "Vanilla haste and mining fatigue",
					    TextConstants.FLMS_LIGHT_YELLOW + "won't work with this plugin, so you",
					    TextConstants.FLMS_LIGHT_YELLOW + "can modify those here instead or with",
					    TextConstants.FLMS_LIGHT_YELLOW + "the /flms effect command",
					    "",
					    TextConstants.FLMS_YELLOW + "<b>CLICK TO OPEN")
			    .build();
	    PotionMeta potionMeta = (PotionMeta) effectConfigIcon.getItemMeta();

    }

    public ItemStack getWand() {
        return wand.clone();
    }

    public boolean isWand(@NotNull ItemStack item) {
        if (!item.hasItemMeta())
            return false;
        final PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        if (!container.has(flmsItemKey))
            return false;
        return Objects.equals(container.get(flmsItemKey, PersistentDataType.STRING), FLMS_WAND_TAG);
    }

    public ItemStack getEmptyGlass() {
        return emptyGlass.clone();
    }

    public ItemStack getLeftArrow() {
        return leftArrow.clone();
    }

    public ItemStack getRightArrow() {
        return rightArrow.clone();
    }

    public ItemStack getBackButton() {
        return backButton.clone();
    }

	public ItemStack getToolConfigIcon() {
		return toolConfigIcon.clone();
	}

	public ItemStack getBlockConfigIcon() {
		return blockConfigIcon.clone();
	}

	public ItemStack getRegionConfigIcon() {
		return regionConfigIcon.clone();
	}

	public ItemStack getEffectConfigIcon() {
		return effectConfigIcon.clone();
	}

	public FLMSEnchanter getEnchanter() {
		return enchanter;
	}
}
