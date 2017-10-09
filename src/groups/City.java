package groups;

import java.io.Serializable;

import org.bukkit.Location;

import players.VPlayer;

/**
 * A group of players with a social structure and property in a specific area
 * @author KingVictoria
 */
public class City extends Group implements Serializable {

	private static final long serialVersionUID = 2699643553534299461L;

	/**
	 * Creates a city at a given location
	 * @param creator The VPlayer reference to the creator of the city
	 * @param loc The location of the city
	 */
	public City(VPlayer creator, Location loc) {
		super(creator);
	}

}
