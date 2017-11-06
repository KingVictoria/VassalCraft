package events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import guis.IconMenu;
import guis.MainMenu;

public class PlayerInteractListener implements Listener {
	
	public static IconMenu menu;
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e){
		Player player = (Player) e.getPlayer();
		
		// Open Menu
		if(player.isSneaking() && e.getItem() == null && e.getClickedBlock() == null){
			MainMenu.create().open(player);
		}
		
		// Look for Sticks and Stones
		if(e.getClickedBlock().getType().equals(Material.GRASS) && e.getItem() == null){
			if(Math.random() > 0.85){
				player.getInventory().addItem(new ItemStack(Material.STICK));
				player.sendMessage(ChatColor.YELLOW + "You have found a stick!");
			}
		}
	}

}
