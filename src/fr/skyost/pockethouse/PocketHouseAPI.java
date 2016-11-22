package fr.skyost.pockethouse;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import fr.skyost.pockethouse.utils.Schematic;
import fr.skyost.pockethouse.utils.Utils;

public class PocketHouseAPI {
	
	/**
	 * The plugin instance.
	 */
	
	private static final PocketHouse plugin = (PocketHouse)Bukkit.getPluginManager().getPlugin("PocketHouse");
	
	/**
	 * The current schematic.
	 */
	
	private static Schematic schematic;
	
	/**
	 * This metadata is used to mark grounds that have been updated.
	 */
	
	private static final String METADATA_UPDATED = "updated";
	
	/**
	 * The directory where players' files are saved.
	 */
	
	public static final String DIRECTORY_PLAYERS = "players";
	
	/**
	 * The directory where players' locations are saved.
	 */
	
	public static final String DIRECTORY_LOCATIONS = "locations";
	
	/**
	 * The last house that have been created is saved to this file.
	 */
	
	public static final String FILE_LAST = "last";
	
	/**
	 * Used to serialize the world of a location.
	 */
	
	public static final String JSON_LOCATION_WORLD = "world";
	
	/**
	 * Used to serialize the X of a location.
	 */
	
	public static final String JSON_LOCATION_X = "x";
	
	/**
	 * Used to serialize the Y of a location.
	 */
	
	public static final String JSON_LOCATION_Y = "y";
	
	/**
	 * Used to serialize the Z of a location.
	 */
	
	public static final String JSON_LOCATION_Z = "z";
	
	/**
	 * Gets the PocketHouse instance.
	 * 
	 * @return The PocketHouse instance.
	 */
	
	public static final PocketHouse getPlugin() {
		return plugin;
	}
	
	/**
	 * Gets the item (or null if not enabled).
	 * 
	 * @return The PocketHouse item.
	 */
	
	public static final ItemStack getItem() {
		return plugin.item;
	}
	
	/**
	 * Checks if the specified item is the PocketHouse item.
	 * 
	 * @param item The item.
	 * 
	 * @return <b>true</b> or <b>false</b>.
	 */
	
	public static final boolean isItem(final ItemStack item) {
		return Utils.isValidItem(item) && item.getType() == plugin.config.itemType && item.getItemMeta().getDisplayName().equals(plugin.config.itemName);
	}
	
	/**
	 * Gets the world where houses are saved.
	 * 
	 * @return The world where house are saved.
	 */
	
	public static final World getWorld() {
		return Bukkit.getWorld(plugin.config.worldName);
	}
	
	/**
	 * Gets the house Schematic.
	 * 
	 * @return The Schematic.
	 * 
	 * @throws IOException If an exception occurs while trying to read the Schematic.
	 */
	
	public static final Schematic getSchematic() throws IOException {
		if(schematic == null) {
			schematic = Schematic.loadSchematic(new File(plugin.getDataFolder(), plugin.config.houseSchematic));
		}
		return schematic;
	}
	
	/**
	 * Creates an house for the specified player.
	 * 
	 * @param player The player.
	 * 
	 * @throws IOException If an exception occurs while trying to read the Schematic.
	 * @throws ParseException If an exception occurs while trying to read the "last" file.
	 */
	
	public static final void createHouse(final OfflinePlayer player) throws IOException, ParseException {
		createHouse(player.getUniqueId());
	}
	
	/**
	 * Creates an house for the specified UUID.
	 * 
	 * @param uuid The UUID.
	 * 
	 * @throws IOException If an exception occurs while trying to read the Schematic.
	 * @throws ParseException If an exception occurs while trying to read the "last" file.
	 */
	
	public static final void createHouse(final UUID uuid) throws IOException, ParseException {
		if(hasHouse(uuid)) {
			return;
		}
		
		Location location = new Location(getWorld(), plugin.config.originX, plugin.config.originY, plugin.config.originZ);
		final File last = new File(getPlayersDirectory(), FILE_LAST);
		if(!last.exists() || !last.isFile()) {
			Files.write(last.toPath(), toJSON(location).getBytes(StandardCharsets.UTF_8));
		}
		else {
			location = fromJSON(new String(Files.readAllBytes(last.toPath()), StandardCharsets.UTF_8));
			location.add(plugin.config.gapX, plugin.config.gapY, plugin.config.gapZ);
		}
		getSchematic().paste(location, plugin.config.houseBiome);
		
		Files.write(new File(getPlayersDirectory(), uuid.toString()).toPath(), toJSON(location).getBytes(StandardCharsets.UTF_8));
	}
	
	/**
	 * Checks if the specified player has an house.
	 * 
	 * @param player The player.
	 * 
	 * @return <b>true</b> or <b>false</b>.
	 */
	
	public static final boolean hasHouse(final OfflinePlayer player) {
		return hasHouse(player.getUniqueId());
	}
	
	/**
	 * Checks if the specified UUID has an house.
	 * 
	 * @param player The UUID.
	 * 
	 * @return <b>true</b> or <b>false</b>.
	 */

	public static final boolean hasHouse(final UUID uuid) {
		final File playerFile = new File(getPlayersDirectory(), uuid.toString());
		return playerFile.exists() && playerFile.isFile();
	}
	
	/**
	 * Deletes the house of the specified player.
	 * 
	 * @param player The player.
	 * 
	 * @throws IOException If an exception occurs while deleting the file in the locations folder.
	 */
	
	public static final void deleteHouse(final OfflinePlayer player) throws IOException {
		deleteHouse(player.getUniqueId());
	}
	
	/**
	 * Deletes the house of the specified UUID.
	 * 
	 * @param uuid The UUID.
	 * 
	 * @throws IOException If an exception occurs while deleting the file in the locations folder.
	 */
	
	public static final void deleteHouse(final UUID uuid) throws IOException {
		if(!hasHouse(uuid)) {
			return;
		}
		final Location location = getPlayerHouseLocation(uuid);
		final Schematic schematic = getSchematic();
		
		for(int x = 0; x < schematic.getWidth(); ++x) {
			for(int y = 0; y < schematic.getHeight(); ++y) {
				for(int z = 0; z < schematic.getLength(); ++z) {
					final Block block = (location.clone().add(x, y, z)).getBlock();
					block.setType(Material.AIR);
					block.getState().update();
				}
			}
		}
		
		new File(getPlayersDirectory(), uuid.toString()).delete();
	}
	
	/**
	 * Teleports a player to his house.
	 * 
	 * @param player The player.
	 * 
	 * @throws IOException If an exception occurs while creating his house, writing his location or reading the schematic.
	 * @throws ParseException If an exception occurs while reading his house's location.
	 */
	
	public static final void teleportToHouse(final Player player) throws IOException, ParseException {
		teleportToHouse(player, player);
	}
	
	/**
	 * Teleports a player to a player's house.
	 * 
	 * @param teleport The player to teleport.
	 * @param player The target player.
	 * 
	 * @throws IOException If an exception occurs while creating his house, writing his location or reading the schematic.
	 * @throws ParseException If an exception occurs while reading his house's location.
	 */
	
	public static final void teleportToHouse(final Player teleport, final OfflinePlayer player) throws IOException, ParseException {
		teleportToHouse(teleport, player.getUniqueId());
	}
	
	/**
	 * Teleports a player to an UUID's house.
	 * 
	 * @param teleport The player to teleport.
	 * @param uuid The target UUID.
	 * 
	 * @throws IOException If an exception occurs while creating his house, writing his location or reading the schematic.
	 * @throws ParseException If an exception occurs while reading his house's location.
	 */
	
	public static final void teleportToHouse(final Player teleport, final UUID uuid) throws IOException, ParseException {
		final Location spawn = getPlayerHouseSpawnLocation(uuid);
		if(spawn == null) {
			if(hasHouse(uuid)) {
				log(ChatColor.RED, plugin.messages.messageCantTeleportTarget, teleport);
				log(ChatColor.RED, String.format(plugin.messages.messageCantTeleportTarget, teleport.getName()));
				return;
			}
			createHouse(uuid);
			teleportToHouse(teleport);
		}
		else {
			final World world = getWorld();
			if(!teleport.getWorld().equals(world)) {
				Files.write(new File(getLocationsDirectory(), uuid.toString()).toPath(), toJSON(teleport.getLocation()).getBytes(StandardCharsets.UTF_8));
			}
			teleport.teleport(spawn);
			
			if(!spawn.getBlock().hasMetadata(METADATA_UPDATED)) {
				final Location location = getPlayerHouseLocation(uuid);
				final Schematic schematic = getSchematic();
				for(int x = 0; x < schematic.getWidth(); ++x) {
					for(int y = 0; y < schematic.getHeight(); ++y) {
						for(int z = 0; z < schematic.getLength(); ++z) {
							final Block block = world.getBlockAt((location.clone()).add(x, y, z));
							block.getState().update();
						}
					}
				}
				spawn.getBlock().setMetadata(METADATA_UPDATED, new FixedMetadataValue(plugin, true));
			}
		}
	}
	
	/**
	 * Teleports back a player to his location.
	 * 
	 * @param player The player.
	 * 
	 * @throws ParseException If an exception occurs while reading his last location (JSON).
	 * @throws IOException If an exception occurs while reading his last location (file).
	 */
	
	public static final void teleportBack(final Player player) throws ParseException, IOException {
		if(!player.getWorld().equals(getWorld())) {
			return;
		}
		Location location = null;
		final File previousLocation = new File(getLocationsDirectory(), player.getUniqueId().toString());
		if(previousLocation.exists() && previousLocation.isFile()) {
			location = fromJSON(new String(Files.readAllBytes(previousLocation.toPath()), StandardCharsets.UTF_8));
		}
		player.teleport(location == null ? Bukkit.getWorlds().get(0).getSpawnLocation() : location);
	}
	
	/**
	 * Gets the absolute location of a player's house.
	 * 
	 * @param player The player.
	 * 
	 * @return The absolute location of the player's house.
	 */
	
	public static final Location getPlayerHouseLocation(final OfflinePlayer player) {
		return getPlayerHouseLocation(player.getUniqueId());
	}
	
	/**
	 * Gets the absolute location of an UUID's house.
	 * 
	 * @param uuid The UUID.
	 * 
	 * @return The absolute location of the UUID's house.
	 */
	
	public static final Location getPlayerHouseLocation(final UUID uuid) {
		if(!hasHouse(uuid)) {
			return null;
		}
		final File playerFile = new File(getPlayersDirectory(), uuid.toString());
		try {
			return fromJSON(new String(Files.readAllBytes(playerFile.toPath()), StandardCharsets.UTF_8));
		}
		catch(final Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Gets the spawn location (in a house) of the specified player.
	 * 
	 * @param player The player.
	 * 
	 * @return The spawn location.
	 */
	
	public static final Location getPlayerHouseSpawnLocation(final OfflinePlayer player) {
		return getPlayerHouseSpawnLocation(player.getUniqueId());
	}
	
	/**
	 * Gets the spawn location (in a house) of the specified UUID.
	 * 
	 * @param uuid The UUID.
	 * 
	 * @return The spawn location.
	 */
	
	public static final Location getPlayerHouseSpawnLocation(final UUID uuid) {
		final Location location = getPlayerHouseLocation(uuid);
		if(location == null) {
			return null;
		}
		return location.add(plugin.config.houseSpawnX, plugin.config.houseSpawnY, plugin.config.houseSpawnZ);
	}
	
	/**
	 * Gets the players directory (and creates it if it does not exist).
	 * 
	 * @return The players directory.
	 */
	
	public static final File getPlayersDirectory() {
		final File playersDirectory = new File(plugin.getDataFolder(), DIRECTORY_PLAYERS);
		if(!playersDirectory.exists() || !playersDirectory.isDirectory()) {
			playersDirectory.mkdir();
		}
		return playersDirectory;
	}
	
	/**
	 * Gets the locations directory (and creates it if it does not exist).
	 * 
	 * @return The locations directory.
	 */
	
	public static final File getLocationsDirectory() {
		final File locationsDirectory = new File(getPlayersDirectory(), DIRECTORY_LOCATIONS);
		if(!locationsDirectory.exists() || !locationsDirectory.isDirectory()) {
			locationsDirectory.mkdir();
		}
		return locationsDirectory;
	}
	
	/**
	 * Logs a message to the console.
	 * 
	 * @param color The color (after the [plugin-name]).
	 * @param message The message.
	 */
	
	public static final void log(final ChatColor color, final String message) {
		log(color, message, Bukkit.getConsoleSender());
	}
	
	/**
	 * Logs a message to a CommandSender.
	 * 
	 * @param color The color (after the [plugin-name]).
	 * @param message The message.
	 * @param sender The sender.
	 */
	
	public static final void log(final ChatColor color, final String message, final CommandSender sender) {
		sender.sendMessage("[" + plugin.getName() + "] " + color + message);
	}
	
	/**
	 * Creates a location from a JSON string.
	 * 
	 * @param json The string.
	 * 
	 * @return The location.
	 * 
	 * @throws ParseException If an exception occurs while reading the JSON string.
	 */
	
	private static final Location fromJSON(final String json) throws ParseException {
		final JSONObject location = (JSONObject)JSONValue.parseWithException(json);
		
		final String uuid = (String)location.get(JSON_LOCATION_WORLD);
		World world = Bukkit.getWorld(UUID.fromString(uuid));
		if(world == null) {
			world = Bukkit.getWorlds().get(0);
		}
		
		return new Location(world, (long)location.get(JSON_LOCATION_X), (long)location.get(JSON_LOCATION_Y), (long)location.get(JSON_LOCATION_Z));
	}
	
	/**
	 * Creates a JSON string from a location.
	 * 
	 * @param location The location.
	 * 
	 * @return The JSON string.
	 */
	
	private static final String toJSON(final Location location) {
		final JSONObject object = new JSONObject();
		object.put(JSON_LOCATION_WORLD, location.getWorld().getUID().toString());
		object.put(JSON_LOCATION_X, location.getBlockX());
		object.put(JSON_LOCATION_Y, location.getBlockY());
		object.put(JSON_LOCATION_Z, location.getBlockZ());
		return object.toJSONString();
	}
	
}