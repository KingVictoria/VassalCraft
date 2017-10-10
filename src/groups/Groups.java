package groups;

import java.io.Serializable;
import java.util.ArrayList;

import org.bukkit.Location;

import location.Claim;

/**
 * The manager for the Groups system
 * @author KingVictoria
 */
public class Groups implements Serializable {
	
	private static final long serialVersionUID = -7579063595516434513L;
	
	private static ArrayList<Group> groups;
	
	/**
	 * Gets all of the City claims
	 * @return ArrayList of Claim
	 */
	public static ArrayList<Claim> getClaims(){
		ArrayList<Claim> claims = new ArrayList<Claim>();
		
		for(Group group: groups)
			if(group instanceof City){
				City city = (City) group;
				for(Claim claim: city.getClaims())
					claims.add(claim);
			}
		
		return claims;
	}
	
	/**
	 * Gets a city by an id
	 * @param id int id
	 * @return the City or null if DNE
	 */
	public static City getCity(int id){
		for(City city: getCities())
			if(city.getId() == id)
				return city;
		
		return null;
	}
	
	/**
	 * Gets a city by a location
	 * @param loc Location
	 * @return the City or null if nothing there
	 */
	public static City getCity(Location loc){
		for(Claim claim: getClaims())
			if(claim.equals(loc.getChunk()))
				for(City city: getCities())
					if(city.getClaims().contains(claim))
						return city;
		
		return null;
	}
	
	/**
	 * GEts a city by a claim
	 * @param claim Claim
	 * @return the City or null if nothing there
	 */
	public static City getCity(Claim claim){
		for(City city: getCities())
			if(city.getClaims().contains(claim))
				return city;
		
		return null;
	}
	
	/**
	 * Gets the cities
	 * @return ArrayList of City
	 */
	public static ArrayList<City> getCities(){
		ArrayList<City> cities = new ArrayList<City>();
		
		for(Group group: groups)
			if(group instanceof City)
				cities.add((City) group);
		
		return cities;
	}
	
	/**
	 * Gets the names of all the groups
	 * @return ArrayList of String
	 */
	public static ArrayList<String> getNames(){
		ArrayList<String> names = new ArrayList<String>();
		
		for(Group group: groups)
			names.add(group.getName());
		
		return names;
	}
	
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
	 * Gets a Group by its name
	 * @param name the group's name
	 * @return the Group or null if that name does not correspond to a group
	 */
	public static Group getGroup(String name){
		for(Group group: groups)
			if(group.getName().equalsIgnoreCase(name))
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
