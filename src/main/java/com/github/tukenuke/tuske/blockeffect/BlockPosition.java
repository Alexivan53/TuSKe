package com.github.tukenuke.tuske.blockeffect;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class BlockPosition {
	public Location loc;
	public double x;
	public double y;
	public double z;
	public int id;
	public byte data;
	public BlockPosition(Location loc, int id, byte data){
		this.loc = loc;
		this.x = loc.getX();
		this.y = loc.getY();
		this.z = loc.getZ();
		this.id = id;
		this.data = data;
	}
	public void set(){
		Block b = loc.getBlock();
		// Best effort: map legacy id+data to a Material if possible; fallback to AIR
		Material material = Material.matchMaterial(String.valueOf(id));
		if (material == null)
			material = Material.AIR;
		b.setType(material, false);
	}
}
