package fr.skyost.pockethouse;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Biome;

import fr.skyost.pockethouse.utils.Skyoconfig;

public class PluginConfig extends Skyoconfig {
	
	@ConfigOptions(name = "enable-updater")
	public boolean enableUpdater = true;
	
	@ConfigOptions(name = "house.schematic")
	public String houseSchematic = "house.schematic";
	@ConfigOptions(name = "house.biome")
	public Biome houseBiome = Biome.PLAINS;
	@ConfigOptions(name = "house.spawn.x")
	public int houseSpawnX = 6;
	@ConfigOptions(name = "house.spawn.y")
	public int houseSpawnY = 21;
	@ConfigOptions(name = "house.spawn.z")
	public int houseSpawnZ = 10;
	
	@ConfigOptions(name = "world.name")
	public String worldName = "pockethouse_houses";
	@ConfigOptions(name = "world.origin.x")
	public int originX = 0;
	@ConfigOptions(name = "world.origin.y")
	public int originY = 64;
	@ConfigOptions(name = "world.origin.z")
	public int originZ = 0;
	@ConfigOptions(name = "world.gap.x")
	public int gapX = (Bukkit.getViewDistance() * 16) + 23 + 1; // (View distance (chunks) * 16 = blocks) + (house width) + 1
	@ConfigOptions(name = "world.gap.y")
	public int gapY = 0;
	@ConfigOptions(name = "world.gap.z")
	public int gapZ = (Bukkit.getViewDistance() * 16) + 26 + 1; // (View distance (chunks) * 16 = blocks) + (house length) + 1
	
	@ConfigOptions(name = "item.enable")
	public boolean itemEnable = true;
	@ConfigOptions(name = "item.name")
	public String itemName = ChatColor.GOLD + "My House";
	@ConfigOptions(name = "item.type")
	public Material itemType = Material.CHEST;
	@ConfigOptions(name = "item.craft")
	public List<String> itemCraft = Arrays.asList("B", "A");
	@ConfigOptions(name = "item.craft-map")
	public LinkedHashMap<String, String> itemMaterials = new LinkedHashMap<String, String>();
	
	public PluginConfig(final File dataFolder) {
		super(new File(dataFolder, "config.yml"), Arrays.asList("PocketHouse Configuration File"));
		
		itemMaterials.put("A", Material.CHEST.name());
		itemMaterials.put("B", Material.EYE_OF_ENDER.name());
	}
	
}