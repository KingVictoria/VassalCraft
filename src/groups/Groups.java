package groups;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The manager for the Groups system
 * @author KingVictoria
 */
public class Groups implements Serializable {
	
	private static final long serialVersionUID = -7579063595516434513L;
	
	private static ArrayList<Group> groups;
	
	/**
	 * Gets a free id integer for creating a new group
	 * @return free id int
	 */
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
	
	/**
	 * Gets a Group by its id
	 * @param id the group's id
	 * @return the Group or null if that id does not correspond to a group
	 */
	public static Group getGroup(int id){
		for(Group group: groups)
			if(group.getId() == id)
				return group;
		
		return null;
	}
	
	/**
	 * Gets all Groups as an ArrayList
	 * @return ArrayList of Group
	 */
	public static ArrayList<Group> getGroups(){
		return groups;
	}
	
	/**
	 * Sets the groups to a new ArrayList (used for loading)
	 * @param list ArrayList of type Group
	 */
    public static void setGroups(ArrayList<Group> list){
    	groups = list;
    }
    
    /**
     * Adds a group
     * @param group Group to add
     */
    public static void addGroup(Group group){
    	groups.add(group);
    }
    
    /**
     * Removes a group
     * @param group Group to remove
     */
    public static void removeGroup(Group group){
    	groups.remove(group);
    }

}
