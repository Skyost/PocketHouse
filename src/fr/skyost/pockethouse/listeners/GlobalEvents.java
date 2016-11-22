package fr.skyost.pockethouse.listeners;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;

import fr.skyost.pockethouse.PocketHouseAPI;

public class GlobalEvents implements Listener {
	
	private static final String METADATA_IN_WORLD = "pockethouse_world";
	
	@EventHandler(priority = EventPriority.LOWEST)
	public final void onPlayerDeath(final PlayerDeathEvent event) {
		final Player player = event.getEntity();
		if(!player.getWorld().equals(PocketHouseAPI.getWorld())) {
			return;
		}
		event.setKeepInventory(true);
		event.setKeepLevel(true);
		player.setMetadata(METADATA_IN_WORLD, new FixedMetadataValue(PocketHouseAPI.getPlugin(), true));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPlayerRespawn(final PlayerRespawnEvent event) {
		final Player player = event.getPlayer();
		if(!player.getWorld().equals(PocketHouseAPI.getWorld())) {
			return;
		}
		if(player.hasMetadata(METADATA_IN_WORLD)) {
			final Location location = PocketHouseAPI.getPlayerHouseSpawnLocation(player);
			final File previousLocation = new File(PocketHouseAPI.getLocationsDirectory(), player.getUniqueId().toString());
			if(location != null && (previousLocation.exists() && previousLocation.isFile())) {
				event.setRespawnLocation(location);
			}
			else if(!event.getRespawnLocation().getWorld().equals(PocketHouseAPI.getWorld())) {
				deleteLocationFileIfExists(player);
			}
			player.removeMetadata(METADATA_IN_WORLD, PocketHouseAPI.getPlugin());
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public final void onPlayerTeleport(final PlayerTeleportEvent event) {
		final World world = PocketHouseAPI.getWorld();
		if(event.getFrom().getWorld().equals(world) && !event.getTo().equals(world)) {
			deleteLocationFileIfExists(event.getPlayer());
		}
	}
	
	private final void deleteLocationFileIfExists(final Player player) {
		final File file = new File(PocketHouseAPI.getLocationsDirectory(), player.getUniqueId().toString());
		if(file.exists()) {
			file.delete();
		}
	}
	
}