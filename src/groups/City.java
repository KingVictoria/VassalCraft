package groups;

import java.io.Serializable;
import java.util.ArrayList;

import org.bukkit.Location;

import location.Claim;
import players.VPlayer;

/**
 * A group of players with a social structure and property in a specific area
 * @author KingVictoria
 */
public class City extends Group implements Serializable {

	private static final long serialVersionUID = 2699643553534299461L;
	
	private ArrayList<Claim> claims = new ArrayList<Claim>();

	/**
	 * Creates a city at a given location
	 * @param creator The VPlayer reference to the creator of the city
	 * @param loc The location of the city
	 */
	public City(VPlayer creator, String name, Location loc) {
		super(creator, name);
		
		claims.add(new Claim(loc));
	}
	
	/**
	 * Gets the claims of this city
	 * @return ArrayList of Claim
	 */
	public ArrayList<Claim> getClaims(){
		return claims;
	}
	
	/**
	 * Adds a claim to the City
	 * @param loc the location where the claim should be added
	 * @return false if the the location is already claimed
	 */
	public boolean addClaim(Location loc){
		for(Claim claim: Groups.getClaims())
			if(claim.equals(loc.getChunk()))
				return false;
		
		claims.add(new Claim(loc));
		return true;
	}

}
