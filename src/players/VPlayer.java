package players;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import groups.City;
import groups.Group;
import groups.Groups;

/**
 * Serializable player reference with extra bits!
 * @author KingVictoria
 */
public class VPlayer implements Serializable {
	
	private static final long serialVersionUID = 5676352331141799837L;
	
	private int mainCity = -1;
	
	private UUID uuid;
	private ArrayList<Integer> invites = new ArrayList<Integer>();
	
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
	 * Gets all groups to which this is a member
	 * @return ArrayList of Group
	 */
	public ArrayList<Group> getGroups(){
		ArrayList<Group> groups = new ArrayList<Group>();
		
		for(Group group: Groups.getGroups())
			if(group.getMembers().contains(this))
				groups.add(group);
		
		return groups;
	}
	
	/**
	 * Gets all groups to which this has an invite
	 * @return ArrayList of Group
	 */
	public ArrayList<Group> getInvites(){
		ArrayList<Group> groupInvites = new ArrayList<Group>();
		
		if(invites.size() == 0)
			return groupInvites;
		
		for(Group group: getGroups())
			if(invites.contains(new Integer(group.getId())))
				groupInvites.add(group);
		
		return groupInvites;
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
	 * Sets the main city (will not set if the player is not a member of that city!) (if parameter is null will set to no city)
	 * @param city The city to be set
	 */
	public void setMainCity(City city){
		if(city == null){
			mainCity = -1;
			return;
		}
		
		if(!city.hasMember(this))
			return;
		
		mainCity = city.getId();
	}
	
	/**
	 * Adds an invite from a Group to this player
	 * @param group Group
	 */
	public void addInvite(Group group){
		if(invites.contains(new Integer(group.getId())))
			return;
		invites.add(new Integer(group.getId()));
	}
	
	/**
	 * Removes an invite to a Group from this player
	 * @param group Group
	 */
	public void removeInvite(Group group){
		if(invites.contains(new Integer(group.getId())))
			invites.remove(new Integer(group.getId()));
	}
	
	/**
	 * Checks whether the Playere referenced by this object is online
	 * @return true if online
	 */
	public boolean isOnline(){
		return getPlayer() != null;
	}

}
