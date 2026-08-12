package me.flegacy.flms.config.ui.tools;

import me.flegacy.flms.FLMS;
import me.flegacy.flms.mining.registry.RegisteredTool;
import me.flegacy.flms.ui.BookInventory;
import me.flegacy.flms.ui.FLMSInterface;

public class ToolListMenu extends BookInventory {

    public ToolListMenu(FLMS plugin, FLMSInterface origin) {
        super(plugin, "All Custom Tools", origin);
    }

    @Override
    protected void refreshElements() {
        clearElements();
        for (RegisteredTool tool : plugin.toolRegistry.getToolsList())
            addElement(new ToolConfigButton(plugin, tool));
    }
}
