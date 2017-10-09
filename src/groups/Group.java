package groups;

import java.util.ArrayList;

import players.VPlayer;

public class Group {
	
	private int id;
	
	private ArrayList<VPlayer> members;
	
	public Group(){
		id = Groups.getFreeId();
		Groups.addGroup(this);
	}
	
	public int getId(){
		return id;
	}
	
	public ArrayList<VPlayer> getMembers(){
		return members;
	}
	
	public boolean hasMember(VPlayer player){
		for(VPlayer member: members)
			if(member.equals(player))
				return true;
		
		return false;
	}

}
