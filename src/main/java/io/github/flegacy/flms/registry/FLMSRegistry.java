package io.github.flegacy.flms.registry;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FLMSRegistry {
	private static FLMSRegistry instance;

	private final Map<Material, RegisteredBlock> blockMap;

	public FLMSRegistry() {
		if (instance != null)
			throw new IllegalStateException("There is already an active FLMSRegistry instance");
		instance = this;

		blockMap = new HashMap<>();
	}

	public void register(@NotNull RegisteredBlock block) {
		blockMap.put(block.getType(), block);
	}

	@Nullable
	public RegisteredBlock getBlock(Material type) {
		return blockMap.get(type);
	}

	public Collection<RegisteredBlock> getBlocks() {
		return blockMap.values();
	}


}
