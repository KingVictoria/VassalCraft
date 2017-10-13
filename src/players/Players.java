package players;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * The Manager for the Players
 * @author KingVictoria
 */
public class Players implements Serializable {
	
	private static final long serialVersionUID = 8689484529071371357L;
	
	private static ArrayList<VPlayer> players;
	
	/**
	 * Gets a player by their UUID
	 * @param uuid UUID
	 * @return the VPlayer or null if does not exist
	 */
	public static VPlayer getPlayer(UUID uuid){
		for(VPlayer player: players)
			if(player.getUniqueId().equals(uuid))
				return player;
		
		return null;
	}
	
	/**
	 * Gets a player by their String name (must be exactly the same!)
	 * @param name String name
	 * @return VPlayer corresponding to name or null if player does not exist
	 */
	public static VPlayer getPlayer(String name){
		for(VPlayer player: getPlayers())
			if(player.getOfflinePlayer().getName().equals(name))
				return player;
		
		return null;
	}

	/**
	 * Gets all the VPlayer players
	 * @return
	 */
	public static ArrayList<VPlayer> getPlayers(){
		return players;
	}
	
	/**
	 * Adds a new VPlayer
	 * @param player
	 */
	public static void addPlayer(VPlayer player){
		if(getPlayer(player.getUniqueId()) != null)
			return;
		
		players.add(player);
	}
	
	/**
	 * Sets the players (used for loading)
	 * @param VPlayers ArrayList of VPlayer type objects
	 */
	public static void setPlayers(ArrayList<VPlayer> VPlayers){
		players = VPlayers;
	}

}
