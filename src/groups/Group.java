package groups;

import java.util.ArrayList;
import java.util.UUID;

import players.Players;
import players.VPlayer;

public class Group {
	
	private int id;
	
	private ArrayList<UUID> members = new ArrayList<UUID>();
	
	public Group(VPlayer creator){
		id = Groups.getFreeId();
		members.add(creator.getUniqueId());
		Groups.addGroup(this);
	}
	
	public int getId(){
		return id;
	}
	
	public ArrayList<VPlayer> getMembers(){
		ArrayList<VPlayer> players = new ArrayList<VPlayer>();
		
		for(UUID uuid: members)
			for(VPlayer player: Players.getPlayers())
				if(uuid.equals(player.getUniqueId()))
					players.add(player);
		
		return players;
	}
	
	public boolean hasMember(VPlayer player){
		for(UUID uuid: members)
			if(uuid.equals(player.getUniqueId()))
				return true;
		
		return false;
	}

}
