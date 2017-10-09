package groups;

import java.util.ArrayList;

/**
 * The manager for the Groups system
 * @author KingVictoria
 */
public class Groups {
	
	private static ArrayList<Group> groups;
	
	public static int getFreeId(){
    	int id = 0;
    	for(Group group: groups)
    		if(group.getId() == id){
    			id++;
    		}else{
    			return id;
    		}
    	
    	return id;
    }
	
    public static void setGroups(ArrayList<Group> list){
    	groups = list;
    }
    
    public static void addGroup(Group group){
    	groups.add(group);
    }

}
