package fr.skyost.pockethouse.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class PocketHouseCommand extends SubCommandsExecutor {

	@Override
	public final boolean onCommand(final CommandSender sender, final Command command, final String label, String[] args) {
		if(args.length == 0) {
			return false;
		}
		return super.onCommand(sender, command, label, args);
	}
	
}