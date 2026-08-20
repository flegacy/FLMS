package io.github.flegacy.flms.registry;

import io.github.flegacy.flms.utils.TextConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class RegisteredBlock {
	// Registered blocks will be unique based on the material. Multiple blocks can't be created with the same material.
	private final Material type;
    private final List<ItemStack> drops;
	private float hardness;
	private String name;
    private Material postBlockType;
    private int xp;

	// TODO drops, xp, post break form

	public RegisteredBlock(Material type) {
		if (!type.isBlock() || type.isAir())
			throw new IllegalStateException("Material must be a non-air block.");
		this.type = type;
        this.drops = new ArrayList<>();
		hardness = 0;
		name = TextConstants.formatEnum(type);
        postBlockType = Material.AIR;
        xp = 0;
	}

	public void setHardness(float level) {
		if (level < 0)
			throw new IllegalArgumentException("A block's hardness can't be negative.");
		hardness = level;
	}

    public void setPostBreakType(Material blockType) {
        if (!blockType.isBlock() && !blockType.isAir())
            throw new IllegalArgumentException("The post-break type has to be a block material.");
        postBlockType = blockType;
    }

    public Material getPostBreakMaterial() {
        return postBlockType;
    }

    public void setXP(int xp) {
        if (xp < 0)
            throw new IllegalArgumentException("Block xp must be a positive integer");
        this.xp = xp;
    }

    public int getXP() {
        return xp;
    }

	public void setName(String name) {
		this.name = name;
	}

    public void addDrop(ItemStack item) {
        drops.add(item);
    }

    public void removeDrop(ItemStack item) {
        drops.remove(item);
    }

    public ItemStack[] getDropList() {
        return drops.toArray(new ItemStack[0]);
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
