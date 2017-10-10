package commands;

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
import groups.Groups;
import location.Claim;
import net.md_5.bungee.api.ChatColor;
import players.Players;
import players.VPlayer;

public class CommandVassalCraft implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player) || args.length == 0)
			return false;
		
		Player player = (Player) sender;
		
		// Help
		if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"))
			return help(player);
		
		// New City
		if(args[0].equalsIgnoreCase("new/n") || args[0].equalsIgnoreCase("n")){
			if(args.length > 1){
				String name = args[1];
				for(int i = 2; i < args.length; i++)
					name += " "+args[i];
				
				if(!newCity(player, name, player.getLocation())){
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
			if(claim(player) && args.length == 1){
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
				if(claim(player, city, player.getLocation())){
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
				
				if(setMain(player, name)){
					player.sendMessage(ChatColor.YELLOW+"Main city set to: "+ChatColor.LIGHT_PURPLE+Groups.getGroup(name).getName());
					return true;
				}else{
					player.sendMessage(ChatColor.YELLOW+"USAGE: setmain/sa <name...>");
					return true;
				}
			}
		
		// Remove Member
		if(args[0].equalsIgnoreCase("removemember") || args[0].equalsIgnoreCase("rm"))
			if(args.length > 1){
				// TODO implement remove member code
			}else{
				player.sendMessage(ChatColor.YELLOW+"USAGE: removemember/rm <name>");
				return true;
			}
		
		// Add Member
		if(args[0].equalsIgnoreCase("addmember") || args[0].equalsIgnoreCase("am"))
			if(args.length > 1){
				// TODO implement add member code
			}else{
				player.sendMessage(ChatColor.YELLOW+"USAGE: addmember/am <name>");
				return true;
			}
		
		return false;
	}
	
	/**
	 * Sets a players main city
	 * @param player Player
	 * @param name String name
	 * @return false if the city cannot be set
	 */
	private boolean setMain(Player player, String name) {
		VPlayer vplayer = Players.getPlayer(player.getUniqueId());
		
		for(City city: vplayer.getCities())
			if(city.getName().equalsIgnoreCase(name)){
				vplayer.setMainCity(city);
				return true;
			}
		
		return false;
	}

	/**
	 * Claims a chunk for a player's main city
	 * @param player
	 * @return false if unable to claim
	 */
	private boolean claim(Player player) {
		City city = Players.getPlayer(player.getUniqueId()).getMainCity();
		if(city == null)
			return false;
		
		return city.addClaim(player.getLocation());
	}
	
	/**
	 * Claims a chunk for a specified city
	 * @param player Player
	 * @param city City
	 * @param loc Location
	 * @return false if unable to claim
	 */
	private boolean claim(Player player, City city, Location loc){
		if(!city.hasMember(Players.getPlayer(player.getUniqueId())))
			return false;
			
		return city.addClaim(loc);
	}

	/**
	 * Gets a claims map
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
	 * @param player owner
	 * @param name City name
	 * @param location Location where the city shall be
	 * @return false if chunk already claimed
	 */
	private boolean newCity(Player player, String name, Location location) {
		for(Claim claim: Groups.getClaims())
			if(claim.equals(location.getChunk()))
				return false;
		
		new City(Players.getPlayer(player.getUniqueId()), name, location);
		return true;
	}


	/**
	 * Sends the help pages to the player
	 * @param player Player
	 * @return true
	 */
	private boolean help(Player player){
		player.sendMessage(ChatColor.LIGHT_PURPLE+"---===HELP-[pg 1]===---");
		player.sendMessage(ChatColor.YELLOW+"help/? <pg#> - brings up help");
		player.sendMessage(ChatColor.YELLOW+"new/n <name...> - makes a new city with <name...>");
		player.sendMessage(ChatColor.YELLOW+"map/m - brings up map");
		player.sendMessage(ChatColor.YELLOW+"claim/c - claims chunk");
		player.sendMessage(ChatColor.YELLOW+"setmain/sa <name...> - sets main city with <name...>");
		player.sendMessage(ChatColor.YELLOW+"removemember/rm <name> - removes a member");
		player.sendMessage(ChatColor.YELLOW+"addmember/am <name> - adds a member");
		
		return true;
	}

}
