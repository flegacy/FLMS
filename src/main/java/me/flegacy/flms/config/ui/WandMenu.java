package me.flegacy.flms.config.ui;

import me.flegacy.flms.FLMS;
import me.flegacy.flms.config.ui.blocks.BlockListMenu;
import me.flegacy.flms.config.ui.effects.PlayerListMenu;
import me.flegacy.flms.config.ui.tools.ToolListMenu;
import me.flegacy.flms.ui.FLMSInterface;
import me.flegacy.flms.ui.elements.FillerElement;
import me.flegacy.flms.ui.elements.TransferButton;

public class WandMenu extends FLMSInterface {

    public WandMenu(FLMS plugin) {
        super(plugin, "FLMS Configuration", 27);

        BlockListMenu blockList = new BlockListMenu(plugin, this);
        ToolListMenu toolList = new ToolListMenu(plugin, this);
        PlayerListMenu effectsList = new PlayerListMenu(plugin, this);

        TransferButton blocks = new TransferButton(blockList, plugin.itemLibrary.createBlockIcon());
        TransferButton tools = new TransferButton(toolList, plugin.itemLibrary.createToolIcon());
        TransferButton effects = new TransferButton(effectsList, plugin.itemLibrary.createEffectIcon());
        FillerElement regions = new FillerElement(plugin.itemLibrary.createRegionIcon());

        setElement(10, blocks);
        setElement(12, tools);
        setElement(14, effects);
        setElement(16, regions);
    }
}
