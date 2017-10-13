package perms;

import java.util.ArrayList;
import java.util.UUID;

import groups.Groups;
import players.Players;
import players.VPlayer;

public class Rank {

	private ArrayList<String> perms;
	private ArrayList<UUID> ranked;
	private String name;
	private int groupId;
	
	/**
	 * Creates a new rank
	 * @param name String name of rank
	 * @param cityId Id of the group
	 */
	public Rank(String name, int groupId){
		perms = new ArrayList<String>();
		ranked = new ArrayList<UUID>();
		this.groupId = groupId;
		this.name = name;
	}
	
	/**
	 * Gets the id of the Group to which this Rank belongs
	 * @return
	 */
	public int getGroupId(){
		return groupId;
	}
	
	/**
	 * Gets the name of the Rank
	 * @return String name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Sets the name of the Rank
	 * @param name String name
	 * @return false if name already taken by another rank in the group
	 */
	public boolean setName(String name){
		for(Rank rank: Groups.getGroup(groupId).getRanks())
			if(rank.getName().equalsIgnoreCase(name))
				return false;
		
		this.name = name;
		return true;
	}
	
	/**
	 * Adds a permission
	 * @param perm String perm
	 */
	public void addPerm(String perm){
		if(!perms.contains(perm))
			perms.add(perm);
	}
	
	/**
	 * Tells whether rank has permission (or true always if has ALL permission)
	 * @param perm String perm
	 * @return true if has permission
	 */
	public boolean hasPerm(String perm){
		return perms.contains(perm) || perms.contains(Perms.ALL);
	}
	
	/**
	 * Removes a permission
	 * @param perm String perm
	 */
	public void removePerm(String perm){
		if(perms.contains(perm))
			perms.remove(perm);
	}
	
	/**
	 * Adds a player to the Rank
	 * @param player VPlayer
	 */
	public void addRanked(VPlayer player){
		if(Groups.getGroup(groupId).getMembers().contains(player) && !ranked.contains(player.getUniqueId()))
			ranked.add(player.getUniqueId());
	}
	
	/**
	 * Gets all VPlayer in this Rank
	 * @return ArrayList of VPlayer
	 */
	public ArrayList<VPlayer> getRanked(){
		ArrayList<VPlayer> rankedMembers = new ArrayList<VPlayer>();
		
		for(UUID uuid: ranked)
			if(Groups.getGroup(groupId).getMembers().contains(Players.getPlayer(uuid)))
				rankedMembers.add(Players.getPlayer(uuid));
		
		return rankedMembers;
	}
	
	/**
	 * Removes a player from the Rank
	 * @param player VPlayer
	 */
	public void removeRanked(VPlayer player){
		if(Groups.getGroup(groupId).getMembers().contains(player) && ranked.contains(player.getUniqueId()))
			ranked.remove(player.getUniqueId());
	}
	
}
