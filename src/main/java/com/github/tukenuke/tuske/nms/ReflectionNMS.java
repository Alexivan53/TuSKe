package com.github.tukenuke.tuske.nms;

import java.lang.reflect.Constructor;
import java.util.logging.Level;

import com.github.tukenuke.tuske.util.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

// Removed GameProfile for 1.21 compatibility; not used in modern path

import com.github.tukenuke.tuske.TuSKe;

public class ReflectionNMS implements NMS {

	private String version = ReflectionUtils.packageVersion;
	public ReflectionNMS(){
	}

	@Override
	public Player getToPlayer(OfflinePlayer p) {
		if (!p.isOnline()) {
			return null;
		}
		return p.getPlayer();
	}

	@Override
	public void makeDrop(Player p, ItemStack i) {
		if (p == null || i == null) {
			return;
		}
		org.bukkit.Location loc = p.getLocation();
		p.getWorld().dropItemNaturally(loc, i.clone()).setPickupDelay(10);
	}

	@Override
	public void setFastBlock(World world, int x, int y, int z, int blockId, byte data) {		
	}

	@Override
	public void updateChunk(Chunk c) {
	}

}
