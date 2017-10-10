package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import groups.Group;
import groups.Groups;
import players.Players;
import players.VPlayer;

/**
 * Handles files
 * @author KingVictoria
 */
public class FileHandler {
	
	File dir;
	
	/**
	 * Creates a FileHandler
	 */
	public FileHandler(){
		dir = VassalCraft.getInstance().getDataFolder();
		
		if(!dir.exists())
			if(!dir.mkdir())
				System.out.println("["+VassalCraft.getInstance().getName()+"] Could not create data directory");
	}
	
	/**
	 * Loads all the files
	 */
	public void load(){
		loadPlayers();
		loadGroups();
	}
	
	/**
	 * Saves all the files
	 */
	public void save(){
		savePlayers();
		saveGroups();
	}

	private void saveGroups() {
		if(Groups.getGroups().size() == 0)
			return;
		
		save(Groups.getGroups(), new File(dir, "groups.dat"));
		
		System.out.println("["+VassalCraft.getInstance().getName()+"] Saving groups:");
		for(Group group: Groups.getGroups())
			System.out.println(" - "+group.toString());	
	}

	private void savePlayers() {
		if(Players.getPlayers().size() == 0)
			return;
		
		save(Players.getPlayers(), new File(dir, "players.dat"));
		
		System.out.println("["+VassalCraft.getInstance().getName()+"] Saving players:");
		for(VPlayer player: Players.getPlayers())
			System.out.println(" - "+player.toString());	
	}

	@SuppressWarnings("unchecked")
	private void loadGroups() {
		Groups.setGroups((ArrayList<Group>) load(new File(dir, "groups.dat")));
		
		if(Groups.getGroups() == null)
			Groups.setGroups(new ArrayList<Group>());
		
		if(Groups.getGroups().size() == 0)
			return;
		System.out.println("["+VassalCraft.getInstance().getName()+"] Loaded groups:");
		for(Group group: Groups.getGroups())
			System.out.println(" - "+group.toString());	
	}

	@SuppressWarnings("unchecked")
	private void loadPlayers() {
		Players.setPlayers((ArrayList<VPlayer>) load(new File(dir, "players.dat")));
		
		if(Players.getPlayers() == null)
			Players.setPlayers(new ArrayList<VPlayer>());
		
		if(Players.getPlayers().size() == 0)
			return;
		System.out.println("["+VassalCraft.getInstance().getName()+"] Loaded players:");
		for(VPlayer player: Players.getPlayers())
			System.out.println(" - "+player.toString());	
	}

	private void save(Object obj, File file) {
		try {
			if(!file.exists())
				file.createNewFile();
			
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(obj);
			oos.flush();
			oos.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private Object load(File file) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			Object result = ois.readObject();
			ois.close();
			return result;
		} catch(Exception e) {
			return null;
		}
	}
	
}
