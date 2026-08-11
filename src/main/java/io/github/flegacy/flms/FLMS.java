package io.github.flegacy.flms;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Welcome to the FLMS plugin.
 */
public class FLMS extends JavaPlugin {

	/**
	 * Default constructor.
	 */
	public FLMS() {}

	@Override
	public void onEnable() {
		getLogger().info("Successfully loaded. Hello World!");
	}

	@Override
	public void onDisable() {
		getLogger().info("Successfully disabled. Goodbye!");
	}
}
