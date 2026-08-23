package io.github.flegacy.flms

import org.bukkit.plugin.java.JavaPlugin

class FLMS : JavaPlugin() {

    override fun onEnable() {

        componentLogger.info("Successfully loaded. Hello world!")
    }

    override fun onDisable() {
        componentLogger.info("Successfully disabled. Goodbye!")
    }
}
