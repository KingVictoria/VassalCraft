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
 * Makes a book from a file with my special book format!
 * @author KingVictoria
 */
public class Book {
	
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
	static {
		
	}
	
	/**
	 * Gets the contents of a book in a managable format
	 * @param path String path to the file
	 */
	private static void get(String path){
		pageStrings = new ArrayList<String>();
		String pageString = "";
		Scanner scan = null;
		
		try{
			InputStream is = VassalCraft.getInstance().getClass().getResourceAsStream(path);
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
		boolean newline = false;
		while(scan.hasNext()){
			String word = scan.next();
			if(word.equalsIgnoreCase("/p/")){
				pageStrings.add(pageString);
				pageString = "";
				page++;
			}else if(word.equalsIgnoreCase("/h/")){
				section(page, scan.next().replaceAll("_", " "));
			}else if(word.equalsIgnoreCase("\\n")){
				pageString += "\n";
				newline = true;
			}else if(pageString.length() == 0){
				pageString = word;
			}else{
				if(newline){
					pageString += word;
					newline = false;
				}else{
					pageString += " "+word;
				}
				if(!scan.hasNext())
					pageStrings.add(pageString);
			}
		}
	}
	
	/**
	 * Gives a given player a book from a file
	 * @param player Player
	 * @param title Title of the book
	 * @param author Author of the book
	 * @param path Path to the book file
	 */
	@SuppressWarnings("unchecked")
	public static void give(Player player, String title, String author, String path){
		
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
    	get(path);
    	
    	// Set the Title and Author of this book
    			bookMeta.setTitle(title);
    	    	bookMeta.setAuthor(author);
    	
    	// Creates Table of Contents
    	tableOfContents();
    	
    	// Pages
    	for(int i = 0; i < pageStrings.size(); i++)
    		if(contents.containsKey(i+2)){
    			page(contents.get(i+2), pageStrings.get(i));
    		}else{
    			page(pageStrings.get(i));
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
