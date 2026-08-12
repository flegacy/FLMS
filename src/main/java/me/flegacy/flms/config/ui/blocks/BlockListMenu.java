package me.flegacy.flms.config.ui.blocks;

import me.flegacy.flms.FLMS;
import me.flegacy.flms.mining.registry.RegisteredBlock;
import me.flegacy.flms.ui.BookInventory;
import me.flegacy.flms.ui.FLMSInterface;

public class BlockListMenu extends BookInventory {

    public BlockListMenu(FLMS plugin, FLMSInterface origin) {
        super(plugin, "All Custom Blocks", origin);
    }

    @Override
    protected void refreshElements() {
        clearElements();
        for (RegisteredBlock block : plugin.blockRegistry.getBlockList())
            addElement(new BlockConfigButton(plugin, block));
    }
}
