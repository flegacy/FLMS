package io.github.flegacy.flms.wand.ui;

import io.github.flegacy.flms.ItemLibrary;
import org.jetbrains.annotations.NotNull;

import io.github.flegacy.flms.FLMS;
import io.github.flegacy.flms.ui.FLMSInterface;
import net.kyori.adventure.text.Component;

public class WandMenu extends FLMSInterface {

	public WandMenu(FLMS plugin) {
		super(27, Component.text("FLMS Config Menu"), plugin);
		final ItemLibrary lib = plugin.getItemLibrary();
        
        setElement(10, new TransferButton(lib.getBlockConfigIcon(), null));
		setElement(12, new TransferButton(lib.getToolConfigIcon(), null));
		setElement(14, new TransferButton(lib.getEffectConfigIcon(), this));
		setElement(16, new TransferButton(lib.getRegionConfigIcon(), this));
	}
}
