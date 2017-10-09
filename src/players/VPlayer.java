package players;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import groups.City;
import groups.Groups;

public class VPlayer implements Serializable {
	
	private static final long serialVersionUID = 5676352331141799837L;
	
	private int mainCity;
	
	private UUID uuid;
	
	public VPlayer(Player player){
		uuid = player.getUniqueId();
		Players.addPlayer(this);
	}
	
	public City getMainCity(){
		if(Groups.getGroup(mainCity) == null || !(Groups.getGroup(mainCity) instanceof City))
			return null;
		
		return (City) Groups.getGroup(mainCity);
	}
	
	public OfflinePlayer getOfflinePlayer(){
		return Bukkit.getServer().getOfflinePlayer(uuid);
	}
	
	public Player getPlayer(){
		return Bukkit.getServer().getPlayer(uuid);
	}
	
	public UUID getUniqueId(){
		return uuid;
	}
	
	public void setMainCity(City city){
		mainCity = city.getId();
	}
	
	public boolean isOnline(){
		return getPlayer() != null;
	}

}
