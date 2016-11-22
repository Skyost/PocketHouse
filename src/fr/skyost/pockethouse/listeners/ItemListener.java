package fr.skyost.pockethouse.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import fr.skyost.pockethouse.PocketHouseAPI;

public class ItemListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	private final void onPrepareItemCraft(final PrepareItemCraftEvent event) {
		final Player player = (Player)event.getView().getPlayer();
		if(PocketHouseAPI.isItem(event.getInventory().getResult()) && !player.hasPermission("pockethouse.item.craft")) {
			PocketHouseAPI.log(ChatColor.RED, PocketHouseAPI.getPlugin().messages.messageNoPermission, player);
			event.getInventory().setResult(new ItemStack(Material.AIR));
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public final void onPlayerInteract(final PlayerInteractEvent event) {
		if(event.isCancelled()) {
			return;
		}
		
		final Action action = event.getAction();
		if(action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		
		final ItemStack item = event.getItem();
		if(!PocketHouseAPI.isItem(item)) {
			return;
		}
		
		try {
			final Player player = event.getPlayer();
			if(!player.hasPermission("pockethouse.item.use")) {
				PocketHouseAPI.log(ChatColor.RED, PocketHouseAPI.getPlugin().messages.messageNoPermission, player);
			}
			if(player.getWorld().equals(PocketHouseAPI.getWorld())) {
				PocketHouseAPI.teleportBack(player);
			}
			else {
				PocketHouseAPI.teleportToHouse(player);
			}
			event.setCancelled(true);
		}
		catch(final Exception ex) {
			ex.printStackTrace();
		}
	}
	
}