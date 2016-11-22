package fr.skyost.pockethouse.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.skyost.pockethouse.PocketHouseAPI;
import fr.skyost.pockethouse.commands.SubCommandsExecutor.CommandInterface;
import fr.skyost.pockethouse.utils.Utils;

public class TeleportSubCommand implements CommandInterface {
	
	@Override
	public final String[] getNames() {
		return new String[]{"teleport", "tp"};
	}

	@Override
	public final boolean mustBePlayer() {
		return true;
	}

	@Override
	public final String getPermission() {
		return "pockethouse.command.teleport";
	}

	@Override
	public final int getMinArgsLength() {
		return 1;
	}

	@Override
	public final String getUsage() {
		return "<player>";
	}

	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
		final OfflinePlayer player = Utils.getPlayer(args[0]);
		if(!player.hasPlayedBefore()) {
			PocketHouseAPI.log(ChatColor.RED, String.format(PocketHouseAPI.getPlugin().messages.messagePlayerNotFound, args[0]), sender);
			return true;
		}
		final Location house = PocketHouseAPI.getPlayerHouseSpawnLocation(player);
		if(house == null) {
			PocketHouseAPI.log(ChatColor.RED, String.format(PocketHouseAPI.getPlugin().messages.messagePlayerNoHouse, args[0]), sender);
			return true;
		}
		try {
			PocketHouseAPI.teleportToHouse((Player)sender, player);
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + sender.getName() + " " + PocketHouseAPI.getPlugin().messages.messageTeleportBackJson);
		}
		catch(final Exception ex) {
			PocketHouseAPI.log(ChatColor.RED, PocketHouseAPI.getPlugin().messages.messageTeleportError, sender);
			ex.printStackTrace();
		}
		return true;
	}

}