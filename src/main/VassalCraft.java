package main;

import org.bukkit.plugin.java.JavaPlugin;

import commands.CommandVassalCraft;
import events.JoinEventListener;
import events.PlayerInteractListener;

public class VassalCraft extends JavaPlugin {
	
	private static VassalCraft instance;
	
	private FileHandler fileHandler;
	
	/**
	 * Gets an instance of the plugin
	 * @return VassalCraft plugin object
	 */
	public static VassalCraft getInstance(){
		return instance;
	}
	
	@Override
	public void onEnable(){
		instance = this;
		
		// FileHandler
		fileHandler = new FileHandler();
		fileHandler.load();
		
		// Event Listener
		getServer().getPluginManager().registerEvents(new JoinEventListener(), this);
		
		// Guis (events)
		getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
		
		
		// Commands
		getCommand("vassalcraft").setExecutor(new CommandVassalCraft());
		getCommand("vc").setExecutor(new CommandVassalCraft());
	}
	
	@Override
	public void onDisable(){
		fileHandler.save();
	}

}
