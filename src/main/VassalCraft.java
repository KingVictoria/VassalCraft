package main;

import org.bukkit.plugin.java.JavaPlugin;

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
		
		fileHandler = new FileHandler();
		fileHandler.load();
		
		
	}
	
	@Override
	public void onDisable(){
		fileHandler.save();
	}

}
