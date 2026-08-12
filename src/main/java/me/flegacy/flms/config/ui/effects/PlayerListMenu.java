package me.flegacy.flms.config.ui.effects;

import me.flegacy.flms.FLMS;
import me.flegacy.flms.ui.BookInventory;
import me.flegacy.flms.ui.FLMSInterface;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class PlayerListMenu extends BookInventory {

    public PlayerListMenu(FLMS plugin, FLMSInterface origin) {
        super(plugin, "All Online Players", origin);
    }

    @Override
    protected void refreshElements() {
        clearElements();
        String[] playerNamesInAlphabeticalOrder = plugin.getOnlinePlayerNames().toArray(String[]::new);
        Arrays.sort(playerNamesInAlphabeticalOrder);
        for (String playerName : playerNamesInAlphabeticalOrder) {
            Player player = Bukkit.getPlayerExact(playerName);
            if (player == null)
                continue;
            addElement(new PlayerEffectButton(plugin, player));
        }
    }

}
