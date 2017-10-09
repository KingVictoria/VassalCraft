package groups;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import players.Players;
import players.VPlayer;

/**
 * A group of players
 * @author KingVictoria
 */
public class Group implements Serializable {
	
	private static final long serialVersionUID = 2190773968169393947L;

	private int id;
	
	private ArrayList<UUID> members = new ArrayList<UUID>();
	
	/**
	 * Creates a group
	 * @param creator the creator of the group
	 */
	public Group(VPlayer creator){
		id = Groups.getFreeId();
		members.add(creator.getUniqueId());
		Groups.addGroup(this);
	}
	
	/**
	 * Gets the groups id
	 * @return int id
	 */
	public int getId(){
		return id;
	}
	
	/**
	 * Gets an ArrayList of VPlayer objects that are part of this group
	 * @return ArrayList of VPlayer objects
	 */
	public ArrayList<VPlayer> getMembers(){
		ArrayList<VPlayer> players = new ArrayList<VPlayer>();
		
		for(UUID uuid: members)
			for(VPlayer player: Players.getPlayers())
				if(uuid.equals(player.getUniqueId()))
					players.add(player);
		
		return players;
	}
	
	/**
	 * Determins whether this group has a player
	 * @param player VPlayer
	 * @return true if this group has that player
	 */
	public boolean hasMember(VPlayer player){
		for(UUID uuid: members)
			if(uuid.equals(player.getUniqueId()))
				return true;
		
		return false;
	}
	
	/**
	 * Adds a member to the city (does nothing if the player is already a member of the city)
	 * @param player VPlayer
	 */
	public void addMember(VPlayer player){
		if(hasMember(player))
			return;
		
		members.add(player.getUniqueId());
	}
	
	/**
	 * Removes a member from the city (does nothing if the player is not a member of the city)
	 * If this is the last member of the city then the city is removed
	 * @param player VPlayer
	 */
	public void removeMember(VPlayer player){
		for(UUID uuid: members)
			if(uuid.equals(player.getUniqueId())){
				if(members.size() == 1)
					Groups.removeGroup(this);
				members.remove(uuid);
			}
	}

}
