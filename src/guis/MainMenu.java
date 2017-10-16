package guis;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import main.VassalCraft;
import net.md_5.bungee.api.ChatColor;

/**
 * The main menu
 * @author KingVictoria
 */
public class MainMenu {

	private static Plugin plugin = VassalCraft.getInstance();
	
	public static IconMenu create(){
		IconMenu menu = new IconMenu("Main Menu", 9, new IconMenu.OptionClickEventHandler() {
            @Override
            public void onOptionClick(IconMenu.OptionClickEvent event) {
                if(event.getName().equals("Subreddit"))
                	event.getPlayer().sendMessage(ChatColor.YELLOW+"https://www.reddit.com/r/VassalCraft/");
                
                if(event.getName().equals("Help"))
                	ReferenceBook.give((Player) event.getPlayer());
                
                event.setWillDestroy(true);
            }
        }, plugin);
		
		menu.setOption(0, new ItemStack(Material.BANNER), "Groups", "View and Manage Your Groups");
		menu.setOption(2, new ItemStack(Material.MAP), "Map", "View Claims Map");
		ItemStack nugget = new ItemStack(Material.GOLD_NUGGET);
		ItemMeta nuggetMeta = nugget.getItemMeta();
		nuggetMeta.addEnchant(Enchantment.DURABILITY, 1, false);
		nuggetMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		nugget.setItemMeta(nuggetMeta);
		menu.setOption(4, nugget, "News", "See What's New!");
		menu.setOption(6, new ItemStack(Material.WORKBENCH), "Subreddit", "Subreddit");
		menu.setOption(8, new ItemStack(Material.BOOK_AND_QUILL), "Help", "View Reference Book");
		
		return menu;
	}
	
}
