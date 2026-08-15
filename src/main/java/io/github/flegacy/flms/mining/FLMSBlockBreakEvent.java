package io.github.flegacy.flms.mining;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class FLMSBlockBreakEvent extends BlockBreakEvent {

    public FLMSBlockBreakEvent(Block theBlock, Player player) {
        super(theBlock, player);
    }

}
