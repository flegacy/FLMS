package io.github.flegacy.flms.mining

import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent

class FLMSBlockBreakEvent(theBlock: Block, player: Player): BlockBreakEvent(theBlock, player)
