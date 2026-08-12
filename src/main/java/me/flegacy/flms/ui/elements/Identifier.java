package me.flegacy.flms.ui.elements;

import me.flegacy.flms.FLMS;
import me.flegacy.flms.ui.FLMSInterface;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Identifier implements DisplayElement {

	private static final Map<UUID, Identifier> IDENTIFIERS = new HashMap<>();

	private final ItemStack display;
	private final FLMSInterface ui;
	public final UUID uuid = UUID.randomUUID();

	public Identifier(FLMSInterface ui) {
		this.ui = ui;
		ItemStack display = ui.getFLMSInstance().itemLibrary.createEmptyGlass();
		ui.getFLMSInstance().itemLibrary.setFLMSTag(display, "identifier_" + uuid);
		this.display = display;
		IDENTIFIERS.put(uuid, this);
	}

	@Override
	public ItemStack getDisplayItem() {
		return display;
	}

	public FLMSInterface getInterface() {
		return ui;
	}

	public static Identifier findIdentifier(FLMS plugin, ItemStack item) {
		String foundTag = plugin.itemLibrary.getTag(item);
		if (foundTag == null)
			return null;
		if (!foundTag.contains("identifier_"))
			return null;
		UUID uuid = UUID.fromString(foundTag.replace("identifier_", ""));
		return IDENTIFIERS.get(uuid);
	}
}
