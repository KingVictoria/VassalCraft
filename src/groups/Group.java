package groups;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;
import perms.Perms;
import perms.Rank;
import players.Players;
import players.VPlayer;

/**
 * A group of players
 * @author KingVictoria
 */
public class Group implements Serializable {
	
	private static final long serialVersionUID = 2190773968169393947L;

	private int id;
	
	private String name = "";
	private ArrayList<UUID> members = new ArrayList<UUID>();
	private ArrayList<Rank> ranks = new ArrayList<Rank>();
	
	/**
	 * Creates a group
	 * @param creator the creator of the group
	 */
	public Group(VPlayer creator, String name){
		id = Groups.getFreeId();
		members.add(creator.getUniqueId());
		Groups.addGroup(this);
		if(!setName(name)){
			int i = 0;
			while(true)
				if(setName("fail"+(i++)))
					break;
		}
		Rank owner = new Rank("owner", id);
		owner.addPerm(Perms.ALL);
		ranks.add(owner);
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
	 * Gets the name of the group
	 * @return String name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Gets the ranks in this group
	 * @return ArrayList of Rank
	 */
	public ArrayList<Rank> getRanks(){
		return ranks;
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
		if(!members.contains(player.getUniqueId()))
			return;
		
		if(player.getMainCity().getId() == id)
			player.setMainCity(null);
		
		for(Rank rank: ranks)
			if(rank.getRanked().contains(player))
				rank.removeRanked(player);
		
		if(members.size() == 1)
			Groups.removeGroup(this);
		
		members.remove(player.getUniqueId());
	}
	
	/**
	 * Sets the name of the group
	 * @param name String name
	 * @return false if the name already exists
	 */
	public boolean setName(String name){
		for(String group: Groups.getNames())
			if(group.equalsIgnoreCase(name))
				return false;
		
		this.name = name;
		return true;
	}
	
	/**
	 * Sends a message to the group
	 * @param message String
	 */
	public void sendMessage(String message){
		for(VPlayer player: getMembers())
			if(player.isOnline())
				player.getPlayer().sendMessage(ChatColor.AQUA+"["+name+"] "+ChatColor.YELLOW+message);
	}
	
	@Override
	public String toString(){
		return name;
	}

}
