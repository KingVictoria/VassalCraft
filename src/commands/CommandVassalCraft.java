package commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import groups.City;
import groups.Group;
import groups.Groups;
import location.Claim;
import net.md_5.bungee.api.ChatColor;
import perms.Perms;
import players.Players;
import players.VPlayer;

public class CommandVassalCraft implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player) || args.length == 0)
			return false;
		
		Player player = (Player) sender;
		
		// Help
		if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")){
			if(args.length == 1)
				return help(player, 1);
			if(args.length == 2){
				try{
					int page = (new Integer(args[1])).intValue();
					return help(player, page);
				}catch(Exception e){
					return false;
				}
			}
		}
		
		// New City
		if(args[0].equalsIgnoreCase("new") || args[0].equalsIgnoreCase("n")){
			if(args.length > 1){
				String name = args[1];
				for(int i = 2; i < args.length; i++)
					name += " "+args[i];
				VPlayer vplayer = Players.getPlayer(player.getUniqueId());
				
				if(!newCity(vplayer, name, player.getLocation())){
					player.sendMessage(ChatColor.YELLOW+"Unable to make city because location already owned by "+ChatColor.LIGHT_PURPLE+Groups.getCity(player.getLocation()).getName());
				}else{
					player.sendMessage(ChatColor.YELLOW+"City of "+ChatColor.LIGHT_PURPLE+Groups.getCity(player.getLocation()).getName()+ChatColor.YELLOW+" has been founded here.");
				}
			}else{
				player.sendMessage(ChatColor.YELLOW+"USAGE: new/n <name...>");
			}
			
			return true;
		}
		
		// Map
		if(args[0].equalsIgnoreCase("map") || args[0].equalsIgnoreCase("m"))
			return map(player);
		
		// Claim
		if(args[0].equalsIgnoreCase("claim") || args[0].equalsIgnoreCase("c"))
			if(claim(Players.getPlayer(player.getUniqueId())) && args.length == 1){
				player.sendMessage(ChatColor.YELLOW+"This location has been claimed for "+ChatColor.LIGHT_PURPLE+Groups.getCity(player.getLocation()).getName());
				return true;
			}else if(args.length == 1){
				if(Players.getPlayer(player.getUniqueId()).getMainCity() == null){
					player.sendMessage(ChatColor.YELLOW+"You have no main city!");
					return true;
				}
				
				player.sendMessage(ChatColor.YELLOW+"Unable to claim that location because already owned by "+ChatColor.LIGHT_PURPLE+Groups.getCity(player.getLocation()).getName());
				return true;
			}else{
				String name = args[1];
				for(int i = 2; i < args.length; i++)
					name += " "+args[i];
				
				if(!(Groups.getGroup(name) instanceof City)){
					player.sendMessage(ChatColor.YELLOW+"That is not the name of a city!");
					return true;
				}
					
				City city = (City) Groups.getGroup(name);
				VPlayer vplayer = Players.getPlayer(player.getUniqueId());
				if(claim(vplayer, city, player.getLocation())){
					player.sendMessage(ChatColor.YELLOW+"This location has been claimed for "+ChatColor.LIGHT_PURPLE+Groups.getCity(player.getLocation()).getName());
					return true;
				}else{
					player.sendMessage(ChatColor.YELLOW+"Unable to claim that location because already owned by "+ChatColor.LIGHT_PURPLE+Groups.getCity(player.getLocation()).getName());
				}
			}
		
		// Set Main City
		if(args[0].equalsIgnoreCase("setmain") || args[0].equalsIgnoreCase("sa"))	
			if(args.length > 1){
				String name = args[1];
				for(int i = 2; i < args.length; i++)
					name += " "+args[i];
				VPlayer vplayer = Players.getPlayer(player.getUniqueId());
				
				if(setMain(vplayer, name)){
					player.sendMessage(ChatColor.YELLOW+"Main city set to: "+ChatColor.LIGHT_PURPLE+Groups.getGroup(name).getName());
					return true;
				}else{
					player.sendMessage(ChatColor.YELLOW+"USAGE: setmain/sa <city...>");
					return true;
				}
			}
		
		// Remove Member
		if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("r"))
			if(args.length == 2){
				City city = Players.getPlayer(player.getUniqueId()).getMainCity();
				VPlayer remover = Players.getPlayer(player.getUniqueId());
				VPlayer toRemove = Players.getPlayer(args[1]);
				if(removeMember(remover, toRemove, city)){
					player.sendMessage(ChatColor.YELLOW+"Successfully removed member "+ChatColor.LIGHT_PURPLE+toRemove.getOfflinePlayer().getName());
					return true;
				}else{
					player.sendMessage(ChatColor.YELLOW+"USAGE: remove/r <name>");
					return true;
				}
			}else{
				player.sendMessage(ChatColor.YELLOW+"USAGE: remove/r <name>");
				return true;
			}
		
		// List Invites
		if(args[0].equalsIgnoreCase("listinvites") || args[0].equalsIgnoreCase("li"))
			if(args.length == 1){
				ArrayList<String> groups = listInvites(Players.getPlayer(player.getUniqueId()));
				
				if(groups.size() == 0){
					player.sendMessage(ChatColor.YELLOW+"You have no invites!");
					return true;
				}
				
				for(String groupName: groups)
					player.sendMessage(ChatColor.YELLOW+groupName);
				
				return true;
			}else{
				player.sendMessage(ChatColor.YELLOW+"USAGE: li");
				return true;
			}
		
		// Invite (<name>) (<accept/a:decline/d>) <city...>
		if(args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("i"))
			if(args.length < 3){
				player.sendMessage(ChatColor.YELLOW+"USAGE: invite/i (<name>)(<accept/a:decline/d>) <group...>");
				return true;
			}else if(args[1].equalsIgnoreCase("accept") || args[1].equalsIgnoreCase("a")){
				// Accept
				String groupName = args[2];
				for(int i = 3; i < args.length; i++)
					groupName+=" "+args[i];
				Group group = Groups.getGroup(groupName);
				
				if(acceptInvite(Players.getPlayer(player.getUniqueId()), group)){
					player.sendMessage(ChatColor.YELLOW+"You have been added as a member to "+ChatColor.LIGHT_PURPLE+group.getName());
					group.sendMessage(ChatColor.YELLOW+player.getName()+ChatColor.YELLOW+" has accepted an invite to "+ChatColor.LIGHT_PURPLE+group.getName());
					return true;
				}else{
					player.sendMessage(ChatColor.YELLOW+"USAGE: invite/i <accept/a> <group...>");
					return true;
				}
			}else if(args[1].equalsIgnoreCase("decline") || args[1].equalsIgnoreCase("d")){
				// Decline
				String groupName = args[2];
				for(int i = 3; i < args.length; i++)
					groupName+=" "+args[i];
				Group group = Groups.getGroup(groupName);
				
				if(declineInvite(Players.getPlayer(player.getUniqueId()), group)){
					player.sendMessage(ChatColor.YELLOW+"You have declined an invite to "+ChatColor.LIGHT_PURPLE+group.getName());
					return true;
				}else{
					player.sendMessage(ChatColor.YELLOW+"USAGE: invite/i <decline/d> <group...>");
					return true;
				}
			}else{
				// Invite a Player
				VPlayer inviter = Players.getPlayer(player.getUniqueId());
				VPlayer toInvite = Players.getPlayer(args[1]);
				String groupName = args[2];
				for(int i = 3; i < args.length; i++)
					groupName+=" "+args[i];
				Group group = Groups.getGroup(groupName);
				
				if(invite(inviter, toInvite, group)){
					player.sendMessage(ChatColor.LIGHT_PURPLE+toInvite.getOfflinePlayer().getName()+ChatColor.YELLOW+" invited to "+ChatColor.LIGHT_PURPLE+group.getName());
					if(toInvite.isOnline()){
						toInvite.getPlayer().sendMessage(ChatColor.YELLOW+"You have been invited to "+ChatColor.LIGHT_PURPLE+group.getName());
						toInvite.getPlayer().sendMessage(ChatColor.YELLOW+"To accept use: "+ChatColor.LIGHT_PURPLE+"/vc i a "+group.getName());
					}
					return true;
				}else{
					player.sendMessage(ChatColor.YELLOW+"USAGE: invite/i <name> <group...>");
					return true;
				}
			}
		
		// List Members
		if(args[0].equalsIgnoreCase("listmembers") || args[0].equalsIgnoreCase("lm")){
			if(args.length == 1){
				player.sendMessage(ChatColor.YELLOW+"USAGE: listmembers <group...>");
				return true;
			}
			String groupName = args[1];
			for(int i = 2; i < args.length; i++)
				groupName+=" "+args[i];
			Group group = Groups.getGroup(groupName);
			
			if(group == null){
				player.sendMessage(ChatColor.YELLOW+"USAGE: listmembers <group...>");
				return true;
			}
			
			ArrayList<VPlayer> members = group.getMembers();
			
			player.sendMessage(ChatColor.YELLOW+"Members of "+ChatColor.LIGHT_PURPLE+group.getName());
			for(VPlayer member: members)
				player.sendMessage(ChatColor.YELLOW+" - "+member.getOfflinePlayer().getName());
			return true;
		}
		
		//TEST COMMANDS TODO
		/*
		 *  player.sendMessage(ChatColor.YELLOW+"listranks/lr <group...>"+ChatColor.WHITE+" - lists ranks of a group");
		 *	player.sendMessage(ChatColor.YELLOW+"getperms/gp <rank> <group...>"+ChatColor.WHITE+" - lists the perms of a rank");
		 *	player.sendMessage(ChatColor.YELLOW+"rank <add:remove> <rank> <name...>"+ChatColor.WHITE+" - adds a player to a specified rank");
		 *	player.sendMessage(ChatColor.YELLOW+"perm <add:remove> <rank> <perm...>"+ChatColor.WHITE+" - adds a perm to a rank");
		 */
		
		return false;
	}
	
	/**
	 * Declines an invite to a Group
	 * @param player VPlayer
	 * @param group Group
	 * @return false if unable to decline the invite (invite doesn't exist?)
	 */
	private boolean declineInvite(VPlayer player, Group group) {
		if(player == null || group == null)
			return false;
		
		if(!player.getInvites().contains(group))
			return false;
		
		if(group.hasMember(player)){
			player.removeInvite(group);
			return true;
		}
		
		player.removeInvite(group);
		return true;
	}

	/**
	 * Accepts an invite to a group
	 * @param player VPlayer
	 * @param group Group
	 * @return false if unable to accept the invite (invite doesn't exist?)
	 */
	private boolean acceptInvite(VPlayer player, Group group) {
		if(player == null || group == null)
			return false;
		
		if(!player.getInvites().contains(group))
			return false;
		
		if(group.hasMember(player)){
			player.removeInvite(group);
			return true;
		}
		
		group.addMember(player);
		player.removeInvite(group);
		return true;
	}

	/**
	 * Invites a player to a group
	 * @param inviter Inviter
	 * @param toInvite Invitee
	 * @param group Group
	 * @return false if unable todo
	 */
	private boolean invite(VPlayer inviter, VPlayer toInvite, Group group) {
		if(inviter == null || toInvite == null || group == null)
			return false;
		
		if(!group.hasMember(toInvite) || !group.hasMember(inviter))
			return false;
		
		if(!inviter.hasPerm(Perms.INVITE_MEMBER, group))
			return false;
			
		toInvite.addInvite(group);
		return true;
	}

	/**
	 * Gets all the names of the cities to which this player has invites
	 * @param vplayer VPlayer
	 * @return ArrayList of String
	 */
	private ArrayList<String> listInvites(VPlayer vplayer) {
		ArrayList<String> groups = new ArrayList<String>();
		
		if(vplayer.getInvites().size() == 0)
			return groups;
		
		for(Group group: vplayer.getInvites())
			groups.add(group.getName());
		
		return groups;
	}

	/**
	 * Removes a member from a group
	 * @param remover VPlayer issuing command
	 * @param toRemove VPlayer to remove
	 * @param group Group to remove from
	 * @return
	 */
	private boolean removeMember(VPlayer remover, VPlayer toRemove, Group group) {
		if(group == null || toRemove == null || remover == null)
			return false;
		
		if(!group.hasMember(remover) || !group.hasMember(toRemove))
			return false;
		
		if(!remover.hasPerm(Perms.REMOVE_MEMBER, group))
			return false;
		
		group.removeMember(toRemove);
		return true;
	}

	/**
	 * Sets a players main city
	 * @param player VPlayer
	 * @param name String name
	 * @return false if the city cannot be set
	 */
	private boolean setMain(VPlayer player, String name) {
		
		for(City city: player.getCities())
			if(city.getName().equalsIgnoreCase(name)){
				player.setMainCity(city);
				return true;
			}
		
		return false;
	}

	/**
	 * Claims a chunk for a player's main city
	 * @param player VPlayer
	 * @return false if unable to claim
	 */
	private boolean claim(VPlayer player) {
		City city = Players.getPlayer(player.getUniqueId()).getMainCity();
		if(city == null)
			return false;
		
		if(!player.hasPerm(Perms.CLAIM, city))
			return false;
		
		return city.addClaim(player.getPlayer().getLocation());
	}
	
	/**
	 * Claims a chunk for a specified city
	 * @param player VPlayer
	 * @param city City
	 * @param loc Location
	 * @return false if unable to claim
	 */
	private boolean claim(VPlayer player, City city, Location loc){
		if(!city.hasMember(player))
			return false;
		
		if(!player.hasPerm(Perms.CLAIM, city))
			return false;
			
		return city.addClaim(loc);
	}

	/**
	 * Gets a claims map (temporary implementation)
	 * @param player Player
	 * @return true
	 */
	@SuppressWarnings("deprecation")
	private boolean map(Player player) {
		MapView mapview = Bukkit.createMap(player.getWorld());
		for(MapRenderer r: mapview.getRenderers())
			mapview.removeRenderer(r);
		mapview.addRenderer(new LocalMap());
		
		ItemStack map = new ItemStack(Material.MAP, 1, mapview.getId());
		player.getInventory().addItem(map);
		return true;
	}

	/**
	 * Makes a new city
	 * @param player owner VPlayer
	 * @param name City name
	 * @param location Location where the city shall be
	 * @return false if chunk already claimed
	 */
	private boolean newCity(VPlayer player, String name, Location location) {
		for(Claim claim: Groups.getClaims())
			if(claim.equals(location.getChunk()))
				return false;
		
		new City(player, name, location);
		return true;
	}


	/**
	 * Sends the help pages to the player (temporary)
	 * @param player Player
	 * @param page int page number
	 * @return true
	 */
	private boolean help(Player player, int page){
		if(page == 2){
			player.sendMessage(ChatColor.LIGHT_PURPLE+"---===HELP-[pg 2]===---");
			player.sendMessage(ChatColor.YELLOW+"listmembers/lm <group...>"+ChatColor.WHITE+" - lists members of a group");
			player.sendMessage(ChatColor.YELLOW+"listranks/lr <group...>"+ChatColor.WHITE+" - lists ranks of a group");
			player.sendMessage(ChatColor.YELLOW+"getperms/gp <rank> <group...>"+ChatColor.WHITE+" - lists the perms of a rank");
			player.sendMessage(ChatColor.YELLOW+"rank <add:remove> <rank> <name...>"+ChatColor.WHITE+" - adds a player to a specified rank");
			player.sendMessage(ChatColor.YELLOW+"perm <add:remove> <rank> <perm...>"+ChatColor.WHITE+" - adds a perm to a rank");
			player.sendMessage(ChatColor.YELLOW+"cmd"+ChatColor.WHITE+" - desc");
			player.sendMessage(ChatColor.YELLOW+"cmd"+ChatColor.WHITE+" - desc");
			player.sendMessage(ChatColor.YELLOW+"cmd"+ChatColor.WHITE+" - desc");
			player.sendMessage(ChatColor.YELLOW+"cmd"+ChatColor.WHITE+" - desc");
			return true;
		}
		if(page == 3){
			player.sendMessage(ChatColor.LIGHT_PURPLE+"---===HELP-[pg 3]===---");
			player.sendMessage(ChatColor.YELLOW+"cmd"+ChatColor.WHITE+" - desc");
			player.sendMessage(ChatColor.YELLOW+"cmd"+ChatColor.WHITE+" - desc");
			player.sendMessage(ChatColor.YELLOW+"cmd"+ChatColor.WHITE+" - desc");
			player.sendMessage(ChatColor.YELLOW+"cmd"+ChatColor.WHITE+" - desc");
			player.sendMessage(ChatColor.YELLOW+"cmd"+ChatColor.WHITE+" - desc");
			player.sendMessage(ChatColor.YELLOW+"cmd"+ChatColor.WHITE+" - desc");
			player.sendMessage(ChatColor.YELLOW+"cmd"+ChatColor.WHITE+" - desc");
			player.sendMessage(ChatColor.YELLOW+"cmd"+ChatColor.WHITE+" - desc");
			player.sendMessage(ChatColor.YELLOW+"cmd"+ChatColor.WHITE+" - desc");
			return true;
		}
		
		player.sendMessage(ChatColor.LIGHT_PURPLE+"---===HELP-[pg 1]===---");
		player.sendMessage(ChatColor.YELLOW+"help/? <pg#>"+ChatColor.WHITE+" - brings up help");
		player.sendMessage(ChatColor.YELLOW+"new/n <name...>"+ChatColor.WHITE+" - makes a new city with <name...>");
		player.sendMessage(ChatColor.YELLOW+"map/m"+ChatColor.WHITE+" - brings up map");
		player.sendMessage(ChatColor.YELLOW+"claim/c"+ChatColor.WHITE+" - claims chunk");
		player.sendMessage(ChatColor.YELLOW+"setmain/sa <city...>"+ChatColor.WHITE+" - sets main city with <name...>");
		player.sendMessage(ChatColor.YELLOW+"remove/r <name> <group...>"+ChatColor.WHITE+" - removes a member");
		player.sendMessage(ChatColor.YELLOW+"listinvites/li"+ChatColor.WHITE+" - lists invites");
		player.sendMessage(ChatColor.YELLOW+"invite/i <name> <group...>"+ChatColor.WHITE+" - invites a member");
		player.sendMessage(ChatColor.YELLOW+"invite/i <accept/a:decline/d> <group...>"+ChatColor.WHITE+" - accepts/declines an invite");
		return true;
	}

}
