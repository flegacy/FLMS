package me.flegacy.flms.mining;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import me.flegacy.flms.FLMS;
import me.flegacy.flms.mining.listeners.FLMSBlockBreakEvent;
import me.flegacy.flms.mining.listeners.MineListener;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MineTask {

	private final FLMS plugin;
	private final Player player;
	private final ProtocolManager manager;
	private final PacketContainer packet;
	private final Block block;

	private boolean running = true;
	private int stage = 0;
	public float tickDelay;
	private int stageAmplifier = 0;

	public MineTask(FLMS plugin, Player player, Integer playerID, Block block, float tickDelay) {
		this.plugin = plugin;
		this.player = player;
		this.block = block;
		this.tickDelay = tickDelay;

		if (tickDelay < 1)
			stageAmplifier += (int) (5-(tickDelay/0.2));

		manager = ProtocolLibrary.getProtocolManager();
		packet = manager.createPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
		packet.getIntegers().write(0, playerID);
		BlockPosition position = new BlockPosition(block.getX(), block.getY(), block.getZ());
		packet.getBlockPositionModifier().write(0, position);
		packet.getIntegers().write(1, stage);

	}

	public void cycle() {
		if (stage >= 10 || tickDelay <= 0) {
			block.setType(Material.AIR);
			FLMSBlockBreakEvent event = new FLMSBlockBreakEvent(block, player);
			plugin.getServer().getPluginManager().callEvent(event);
			// TODO add block drops integrating with plugin config
			// TODO add tool durability function
			// TODO add custom block event
			// TODO remove debug code
			// TODO PRIORITY figure out a way to let other plugins easily modify the block break event
			float seconds = (float) (System.currentTimeMillis()-MineListener.lastMineTime.get(player.getUniqueId()))/1000;
			BaseComponent msg = new TextComponent("BreakTime:" + tickDelay*10 + "ticks|RealTime:" + seconds*20 + "|TickDelay:" + tickDelay);
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, msg);
		} else {
			packet.getIntegers().write(1, stage+stageAmplifier);
			manager.sendServerPacket(player, packet);
			stage += 1+stageAmplifier;
			new BukkitRunnable() {
				@Override
				public void run() {
					if (running)
						cycle();
				}
			}.runTaskLater(plugin, (int) Math.max(tickDelay, 1));
		}
	}

	public void stop() {
		running = false;
		packet.getIntegers().write(1, -1);
		manager.sendServerPacket(player, packet);
	}
}
