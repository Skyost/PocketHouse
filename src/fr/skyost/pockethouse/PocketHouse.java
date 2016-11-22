package fr.skyost.pockethouse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.PluginCommand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Joiner;

import fr.skyost.pockethouse.commands.PocketHouseCommand;
import fr.skyost.pockethouse.commands.subcommands.DeleteSubCommand;
import fr.skyost.pockethouse.commands.subcommands.TeleportBackSubCommand;
import fr.skyost.pockethouse.commands.subcommands.TeleportSubCommand;
import fr.skyost.pockethouse.listeners.GlobalEvents;
import fr.skyost.pockethouse.listeners.ItemListener;
import fr.skyost.pockethouse.utils.EmptyGenerator;
import fr.skyost.pockethouse.utils.Skyupdater;
import fr.skyost.pockethouse.utils.Utils;

public class PocketHouse extends JavaPlugin {
	
	public PluginConfig config;
	public PluginMessages messages;
	
	protected ItemStack item;
	
	@Override
	public final void onEnable() {
		try {
			/* CONFIGURATION : */
			
			final File dataFolder = this.getDataFolder();
			
			config = new PluginConfig(dataFolder);
			config.load();
			if(config.enableUpdater) {
				new Skyupdater(this, 103428, this.getFile(), true, true);
			}
			
			messages = new PluginMessages(dataFolder);
			messages.load();
			
			/* EXTRACTING DEFAULT SCHEMATIC IF NEEDED : */
			
			final File schematic = new File(dataFolder, config.houseSchematic);
			if(!schematic.exists() || !schematic.isFile()) {
				PocketHouseAPI.log(ChatColor.GOLD, "Extracting the default schematic...");
				extract("house.schematic", dataFolder);
				PocketHouseAPI.log(ChatColor.GOLD, "Done !");
			}
			extract("IF YOU WANT TO EDIT THE SCHEMATIC.txt", dataFolder);
			
			/* CREATING RECIPES : */
			
			item = Utils.createItem(config.itemName, config.itemType);
			createRecipe(item, config.itemCraft, config.itemMaterials);
			
			/* CREATING WORLDS */
			
			World world = Bukkit.getWorld(config.worldName);
			if(world == null) {
				world = Bukkit.createWorld(WorldCreator.name(config.worldName).generator(new EmptyGenerator()));
				world.setGameRuleValue("doDaylightCycle", "false");
				world.setGameRuleValue("doWeatherCycle", "false");
				world.setGameRuleValue("doMobSpawning", "false");
				world.setGameRuleValue("showDeathMessages", "false");
			}
			world.setTime(0);
			
			/* REGISTERING EVENTS */
			
			final PluginManager manager = Bukkit.getPluginManager();
			manager.registerEvents(new GlobalEvents(), this);
			if(config.itemEnable) {
				manager.registerEvents(new ItemListener(), this);
			}
			
			/* REGISTERING COMMANDS */
			
			final PocketHouseCommand executor = new PocketHouseCommand();
			executor.registerSubCommand(new TeleportSubCommand());
			executor.registerSubCommand(new TeleportBackSubCommand());
			executor.registerSubCommand(new DeleteSubCommand());
			
			final PluginCommand command = this.getCommand("pockethouse");
			command.setUsage(ChatColor.RED + command.getUsage());
			command.setExecutor(executor);
			
			final PluginDescriptionFile description = this.getDescription();
			PocketHouseAPI.log(ChatColor.RESET, "Enabled " + ChatColor.GOLD + this.getName() + " v" + description.getVersion() + ChatColor.GRAY + " by " + Joiner.on(' ').join(description.getAuthors()) + ChatColor.RESET + " !");
		}
		catch(final Exception ex) {
			PocketHouseAPI.log(ChatColor.RED, "Unable to start the plugin !");
			ex.printStackTrace();
		}
	}
	
	/**
	 * Extracts a file to the specified directory.
	 * 
	 * @param destination The destination directory.
	 * 
	 * @throws IOException If an exception occurs when trying to extract the file.
	 */
	
	public final void extract(final String file, final File destination) throws IOException {
		final JarFile jar = new JarFile(this.getFile());
		final Enumeration<JarEntry> enumEntries = jar.entries();
		
		while(enumEntries.hasMoreElements()) {
			final JarEntry entry = enumEntries.nextElement();
			final String name = entry.getName();
			
			if(!name.endsWith(file)) {
				continue;
			}
			
			final File extract = new File(destination, file);
			extract.getParentFile().mkdirs();
			
			final InputStream is = jar.getInputStream(entry);
			final FileOutputStream fos = new FileOutputStream(extract);
			while(is.available() > 0) {
				fos.write(is.read());
			}
			fos.close();
			is.close();
		}
		jar.close();
	}
	
	/**
	 * Creates a recipe for an item.
	 * 
	 * @param result The item.
	 * @param shape The shape.
	 * @param ingredients The ingredients needed for the craft.
	 */
	
	private final void createRecipe(final ItemStack result, final List<String> shape, Map<String, String> ingredients) {
		final ShapedRecipe recipe = new ShapedRecipe(result);
		recipe.shape(shape.toArray(new String[shape.size()]));
		if(ingredients.equals(config.itemMaterials)) {
			ingredients = Utils.keepAll(ingredients, shape);
		}
		for(final Entry<String, String> entry : ingredients.entrySet()) {
			recipe.setIngredient(entry.getKey().charAt(0), Material.valueOf(entry.getValue()));
		}
		Bukkit.addRecipe(recipe);
	}
	
}