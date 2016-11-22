package fr.skyost.pockethouse.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Utils {
	
	/**
	 * Creates an item with a custom name.
	 * 
	 * @param name The name.
	 * @param material The item's material.
	 * 
	 * @return The item.
	 */
	
	public static final ItemStack createItem(final String name, final Material material) {
		final ItemStack item = new ItemStack(material);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	
	/**
	 * Checks if the specified item is valid.
	 * 
	 * @param item The item.
	 * 
	 * @return <b>true :</b> yes.
	 * <br /><b>false :</b> no.
	 */
	
	public static final boolean isValidItem(final ItemStack item) {
		if(item != null && item.hasItemMeta()) {
			final ItemMeta meta = item.getItemMeta();
			if(!meta.hasDisplayName()) {
				return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Inverse of addAll(...).
	 * 
	 * @param map The map.
	 * @param objects The objects.
	 * 
	 * @return A map which contains only the objects (as keys).
	 */
	
	public static final <V> Map<String, V> keepAll(final Map<String, V> map, final Collection<String> objects) {
		final Map<String, V> result = new HashMap<String, V>();
		for(final String object : objects) {
			final char[] chars = new char[object.length()];
			object.getChars(0, object.length() > 3 ? 3 : object.length(), chars, 0);
			for(final char c : chars) {
				if(c == ' ') {
					continue;
				}
				final String character = String.valueOf(c);
				if(map.containsKey(character)) {
					result.put(character, map.get(character));
				}
			}
		}
		return result;
	}
	
	/**
	 * Returns a player.
	 * 
	 * @param nameOrUUID The player's name or UUID.
	 * 
	 * @return The player.
	 */
	
	public static final OfflinePlayer getPlayer(final String nameOrUUID) {
		 UUID uuid = null;
		 try {
			 uuid = UUID.fromString(nameOrUUID);
		 }
		 catch(final IllegalArgumentException ex) {}
		 if(uuid == null) {
			 return Bukkit.getOfflinePlayer(nameOrUUID);
		 }
		 else {
			 return Bukkit.getOfflinePlayer(uuid);
		 }
	}
	
}