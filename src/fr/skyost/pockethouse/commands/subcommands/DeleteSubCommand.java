package fr.skyost.pockethouse.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import fr.skyost.pockethouse.PocketHouseAPI;
import fr.skyost.pockethouse.commands.SubCommandsExecutor.CommandInterface;
import fr.skyost.pockethouse.utils.Utils;

public class DeleteSubCommand implements CommandInterface {
	
	@Override
	public final String[] getNames() {
		return new String[]{"delete", "remove", "rm"};
	}

	@Override
	public final boolean mustBePlayer() {
		return true;
	}

	@Override
	public final String getPermission() {
		return "pockethouse.command.delete";
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
		if(!PocketHouseAPI.hasHouse(player)) {
			PocketHouseAPI.log(ChatColor.RED, String.format(PocketHouseAPI.getPlugin().messages.messagePlayerNoHouse, args[0]), sender);
			return true;
		}
		PocketHouseAPI.log(ChatColor.GOLD, PocketHouseAPI.getPlugin().messages.messageWait, sender);
		new BukkitRunnable() {

			@Override
			public final void run() {
				try {
					PocketHouseAPI.deleteHouse(player);
					PocketHouseAPI.log(ChatColor.GREEN, PocketHouseAPI.getPlugin().messages.messageHouseDeleteSuccess, sender);
				}
				catch(final Exception ex) {
					PocketHouseAPI.log(ChatColor.RED, String.format(PocketHouseAPI.getPlugin().messages.messageHouseDeleteError, ex.getClass().getName()), sender);
					ex.printStackTrace();
				}
			}
			
		}.runTask(PocketHouseAPI.getPlugin());
		return true;
	}

}