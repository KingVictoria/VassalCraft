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
					player.sendMessage(ChatColor.YELLOW+"USAGE: new/n <name...>");
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
			if(claim(player)){
				player.sendMessage(ChatColor.YELLOW+"This location has been claimed for "+ChatColor.LIGHT_PURPLE+Groups.getCity(player.getLocation()).getName());
				return true;
			}else{
				player.sendMessage(ChatColor.YELLOW+"Unable to claim that location because already owned by "+ChatColor.LIGHT_PURPLE+Groups.getCity(player.getLocation()).getName());
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
		return city.addClaim(player.getLocation());
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
		player.sendMessage(ChatColor.LIGHT_PURPLE+"---===HELP===---");
		player.sendMessage(ChatColor.YELLOW+"help/? - brings up help");
		player.sendMessage(ChatColor.YELLOW+"new/n <name...> - makes a new city with <name>");
		player.sendMessage(ChatColor.YELLOW+"map/m - brings up map");
		player.sendMessage(ChatColor.YELLOW+"claim/c - claims chunk");
		
		return true;
	}

}
