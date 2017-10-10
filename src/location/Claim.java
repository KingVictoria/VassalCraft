package location;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * A serializable claim thingo
 * @author KingVictoria
 */
public class Claim implements Serializable {
	
	private static final long serialVersionUID = -8106301645327673544L;
	
	private int x, z;
	private String world;
	
	/**
	 * Creates a claim at a location
	 * @param loc Location
	 */
	public Claim(Location loc){
		x = loc.getChunk().getX();
		z = loc.getChunk().getZ();
		world = loc.getWorld().getName();
	}
	
	/**
	 * Gets the chunk x
	 * @return int x
	 */
	public int getX(){
		return x;
	}
	
	/**
	 * Gets the chunk z
	 * @return int z
	 */
	public int getZ(){
		return z;
	}
	
	/**
	 * Gets the world object refrenced by this claim
	 * @return World
	 */
	public World getWorld(){
		return Bukkit.getWorld(world);
	}
	
	/**
	 * Gets the chunk referenced by this claim
	 * @return
	 */
	public Chunk getChunk(){
		return Bukkit.getWorld(world).getChunkAt(x, z);
	}
	
	/**
	 * Determins whether a given chunk is this claim
	 * @param chunk Chunk
	 * @return true if the chunk is this claim
	 */
	public boolean equals(Chunk chunk){
		return chunk.getX() == x && chunk.getZ() == z && chunk.getWorld().getName().equals(world);
	}
	
	/**
	 * Determines whether a given claim is this claim
	 * @param claim Claim
	 * @return true if the chunk is this claim
	 */
	public boolean equals(Claim claim){
		return claim.x == x && claim.z == z && claim.world.equals(world);
	}

}
