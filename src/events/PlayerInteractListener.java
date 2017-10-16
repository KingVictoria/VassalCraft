package events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import guis.IconMenu;
import guis.MainMenu;

public class PlayerInteractListener implements Listener {
	
	public static IconMenu menu;
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e){
		Player player = (Player) e.getPlayer();
		
		if(player.isSneaking() && e.getItem() == null && e.getClickedBlock() == null){
			MainMenu.create().open(player);
		}
	}

}
