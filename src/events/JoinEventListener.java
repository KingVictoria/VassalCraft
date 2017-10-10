package events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.md_5.bungee.api.ChatColor;
import players.Players;
import players.VPlayer;

public class JoinEventListener implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		
		if(Players.getPlayer(player.getUniqueId()) == null){
			new VPlayer(player);
			player.sendMessage(ChatColor.YELLOW+"Welcome to "+Bukkit.getServerName()+" "+player.getName()+"!");
		}else{
			player.sendMessage(ChatColor.YELLOW+"Welcome back to "+Bukkit.getServerName()+" "+player.getName()+"!");
		}
	}

}
