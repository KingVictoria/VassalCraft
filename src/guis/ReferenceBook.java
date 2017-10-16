package guis;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaBook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import main.VassalCraft;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;

/**
 * Manual for VassalCraft
 * @author KingVictoria
 */
public class ReferenceBook {
	
	private static final String TITLE = ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"VassalCraft Manual";
	private static final String AUTHOR = ChatColor.DARK_PURPLE+"KingVictoria";
	
	private static ItemStack book;
	private static BookMeta bookMeta;
	private static List<IChatBaseComponent> pages;
	private static HashMap<Integer, String> contents = new HashMap<Integer, String>();;
	
	private static TextComponent top = new TextComponent(ChatColor.LIGHT_PURPLE+"[T]");
	static {
		top.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "1"));
		top.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Go to the Table of Contents").create()));
	}
	
	private static ArrayList<String> pageStrings;
	static { // There is something so f***ed about this
		pageStrings = new ArrayList<String>();
		String pageString = "";
		Scanner scan = null;
		
		try{
			InputStream is = VassalCraft.getInstance().getClass().getResourceAsStream("/assets/Manual.txt");
			String toScan = "";
			while(is.available() > 0){
				toScan += (char) is.read();
			}
			is.close();
			scan = new Scanner(toScan);
		} catch(IOException e){
			e.printStackTrace();
		}
		int page = 2;
		while(scan.hasNext()){
			String word = scan.next();
			if(word.equalsIgnoreCase("/p/")){
				pageStrings.add(pageString);
				pageString = "";
				page++;
			}else if(word.equalsIgnoreCase("/h/")){
				section(page, scan.next().replaceAll("_", " "));
			}else if(pageString.length() == 0){
				pageString = word;
			}else{
				pageString += " "+word;
			}
		}
	}
	
	/**
	 * Gives a given player a ReferenceBook
	 * @param player Player
	 */
	@SuppressWarnings("unchecked")
	public static void give(Player player){
		
		// Make New Book
		book = new ItemStack(Material.WRITTEN_BOOK);
		bookMeta = (BookMeta) book.getItemMeta();
    	
    	// Get the pages
    	try {
    	    pages = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(bookMeta);
    	} catch (ReflectiveOperationException ex) {
    	    ex.printStackTrace();
    	    return;
    	}
    	
    	// Set the Title and Author of this book
    			bookMeta.setTitle(TITLE);
    	    	bookMeta.setAuthor(AUTHOR);
    	
    	// Creates Table of Contents
    	tableOfContents();
    	
    	// Pages
    	for(int i = 2; i < pageStrings.size()+2; i++)
    		if(contents.containsKey(i)){
    			page(contents.get(i), pageStrings.get(i-2));
    		}else{
    			page(pageStrings.get(i-2));
    		}
    	
    	// Update ItemStack with this new meta
    	book.setItemMeta(bookMeta);
    	
    	// Give the book to the player
    	player.getInventory().addItem(book);
	}
	
	/**
	 * Adds a section
	 * @param pageNumber int number of page
	 * @param name String name of section
	 */
	private static void section(int pageNumber, String name){
		contents.put(pageNumber, name);
	}
	
	/**
	 * Makes a page
	 * @param body Body of the page
	 */
	private static void page(String body){
		TextComponent page = new TextComponent(body);
		pages.add(ChatSerializer.a(ComponentSerializer.toString(page)));
	}
	
	/**
	 * Makes a page with a header
	 * @param header Title of the page
	 * @param body Body of the page
	 */
	private static void page(String header, String body){
		TextComponent page = new TextComponent(ChatColor.DARK_AQUA+""+ChatColor.BOLD+header+" ");
		
		page.addExtra(top);
		page.addExtra("\n\n");
		page.addExtra(body);
		pages.add(ChatSerializer.a(ComponentSerializer.toString(page)));
	}
	
	/**
	 * Creates a table of contents from a HashMap of Integers as keys and Strings as values
	 * @param contents HashMap of Integer key and String value
	 */
	private static void tableOfContents(){
		TextComponent page = new TextComponent();
		
		for(Integer key: contents.keySet()){
			TextComponent line = new TextComponent();
			line.addExtra(new TextComponent(ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+""+key.intValue()));
			line.addExtra(new TextComponent(ChatColor.DARK_AQUA+" "+contents.get(key)+"\n"));
			line.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, key.toString()));
			line.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Go to "+contents.get(key)).create()));
			page.addExtra(line);
		}
		
		pages.add(ChatSerializer.a(ComponentSerializer.toString(page)));
	}
	
}
