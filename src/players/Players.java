package players;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Players implements Serializable {
	
	private static final long serialVersionUID = 8689484529071371357L;
	
	private static ArrayList<VPlayer> players;
	
	public static VPlayer getPlayer(UUID uuid){
		for(VPlayer player: players)
			if(player.getUniqueId().equals(uuid))
				return player;
		
		return null;
	}

	public static ArrayList<VPlayer> getPlayers(){
		return players;
	}
	
	public static void addPlayer(VPlayer player){
		if(getPlayer(player.getUniqueId()) != null)
			return;
		
		players.add(player);
	}

}
