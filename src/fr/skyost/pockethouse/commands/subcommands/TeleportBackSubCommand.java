package fr.skyost.pockethouse.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.skyost.pockethouse.PocketHouseAPI;
import fr.skyost.pockethouse.commands.SubCommandsExecutor.CommandInterface;

public class TeleportBackSubCommand implements CommandInterface {
	
	@Override
	public final String[] getNames() {
		return new String[]{"teleport-back", "tp-back", "tpb"};
	}

	@Override
	public final boolean mustBePlayer() {
		return true;
	}

	@Override
	public final String getPermission() {
		return "pockethouse.command.teleport-back";
	}

	@Override
	public final int getMinArgsLength() {
		return 0;
	}

	@Override
	public final String getUsage() {
		return null;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
		try {
			PocketHouseAPI.teleportBack((Player)sender);
			PocketHouseAPI.log(ChatColor.GREEN, PocketHouseAPI.getPlugin().messages.messageTeleportedBackSuccess, sender);
		}
		catch(final Exception ex) {
			PocketHouseAPI.log(ChatColor.RED, PocketHouseAPI.getPlugin().messages.messageTeleportBackError, sender);
			ex.printStackTrace();
		}
		return true;
	}

}