package players;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import groups.City;
import groups.Groups;

/**
 * Serializable player reference with extra bits!
 * @author KingVictoria
 */
public class VPlayer implements Serializable {
	
	private static final long serialVersionUID = 5676352331141799837L;
	
	private int mainCity = -1;
	
	private UUID uuid;
	
	/**
	 * Creates an object VPlayer as a reference to a Player
	 * @param player Player object to reference
	 */
	public VPlayer(Player player){
		uuid = player.getUniqueId();
		Players.addPlayer(this);
	}
	
	/**
	 * Gets all cities to which this is a member
	 * @return ArrayList of City
	 */
	public ArrayList<City> getCities(){
		ArrayList<City> cities = new ArrayList<City>();
		for(City city: Groups.getCities())
			if(city.getMembers().contains(this))
				cities.add(city);
		
		return cities;
	}
	
	/**
	 * Gets the main city of the player
	 * @return the City or null if there is no main city
	 */
	public City getMainCity(){
		if(Groups.getGroup(mainCity) == null || !(Groups.getGroup(mainCity) instanceof City))
			return null;
		
		return (City) Groups.getGroup(mainCity);
	}
	
	/**
	 * Gets the OfflinePlayer object referenced by this object
	 * @return OfflinePlayer
	 */
	public OfflinePlayer getOfflinePlayer(){
		return Bukkit.getServer().getOfflinePlayer(uuid);
	}
	
	/**
	 * Gets the Player entity referenced by this object
	 * @return Player
	 */
	public Player getPlayer(){
		return Bukkit.getServer().getPlayer(uuid);
	}
	
	/**
	 * Gets the UUID referenced by this object
	 * @return UUID
	 */
	public UUID getUniqueId(){
		return uuid;
	}
	
	/**
	 * Sets the main city (will not set if the player is not a member of that city!)
	 * @param city The city to be set
	 */
	public void setMainCity(City city){
		if(!city.hasMember(this))
			return;
		
		mainCity = city.getId();
	}
	
	/**
	 * Checks whether the Playere referenced by this object is online
	 * @return true if online
	 */
	public boolean isOnline(){
		return getPlayer() != null;
	}

}
