package me.flegacy.flms.config.ui.effects;

import me.flegacy.flms.FLMS;
import me.flegacy.flms.mining.PlayerStats;
import me.flegacy.flms.ui.elements.ActionElement;
import me.flegacy.flms.utils.Text;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerEffectButton implements ActionElement {

    private final ItemStack display;
    private final Player player;
    private final FLMS plugin;

    public PlayerEffectButton(FLMS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.display = createDisplay();
    }


    @Override
    public void execute(Player player, InventoryClickEvent event) {

    }

    // TODO Since many players can quickly connect or disconnect during use of the menu, add checks if they player is still available
    // TODO Idea: maybe find a way to replace all normal instances of players gaining effects with the custom effects, just to make things easier

    @Override
    public ItemStack getDisplayItem() {
        return display;
    }

    private ItemStack createDisplay() {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(player);
        meta.setDisplayName(Text.format("&ePlayer '&f" + player.getName() + "&e'"));
        PlayerStats effects = plugin.playerEffects.get(player.getUniqueId());
        String hasteDisplay = (effects.getHaste() == 0) ? "&8None!" : "&e" + effects.getHaste();
        String fatigueDisplay = (effects.getFatigue() == 0) ? "&8None!" : "&e" + effects.getFatigue();
        meta.setLore(Text.formatList(
                "",
                "&7Current Effects: &e⤵",
                "  &7Haste: " + hasteDisplay,
                "  &7Mining Fatigue: " + fatigueDisplay,
                "",
                "&6&lCLICK TO EDIT"
        ));
        item.setItemMeta(meta);
        return item;
    }
}
