package io.github.flegacy.flms.registry;

import io.github.flegacy.flms.utils.TextConstants;
import org.bukkit.Material;

public class RegisteredBlock {
	// Registered blocks will be unique based on the material. Multiple blocks can't be created with the same material.
	private final Material type;
	private float hardness;
	private String name;

	// TODO drops, xp, post break form

	public RegisteredBlock(Material type) {
		this.type = type;
		hardness = 0;
		name = TextConstants.formatEnum(type);
	}

	public void setHardness(float level) {
		if (level < 0)
			throw new IllegalArgumentException("A block's hardness can't be negative");
		hardness = level;
	}

	public void setName(String name) {
		this.name = name;
	}


	public float getHardness() {
		return hardness;
	}

	public String getName() {
		return name;
	}

	public Material getType() {
		return type;
	}
}
