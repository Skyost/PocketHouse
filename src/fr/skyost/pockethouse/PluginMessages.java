package fr.skyost.pockethouse;

import java.io.File;
import java.util.Arrays;

import fr.skyost.pockethouse.utils.Skyoconfig;

public class PluginMessages extends Skyoconfig {
	
	@ConfigOptions(name = "messages.no-permission")
	public String messageNoPermission = "You do not have the permission to do that !";
	@ConfigOptions(name = "messages.no-console")
	public String messageNoConsole = "You cannot run this command from the console !";
	@ConfigOptions(name = "messages.player-not-found")
	public String messagePlayerNotFound = "The player \"%s\" was not found on this server.";
	@ConfigOptions(name = "messages.player-no-house")
	public String messagePlayerNoHouse = "The player \"%s\" does not have a pocket house.";
	@ConfigOptions(name = "messages.wait")
	public String messageWait = "Please wait...";
	@ConfigOptions(name = "messages.cant-teleport.target")
	public String messageCantTeleportTarget = "Unable to teleport you. Please contact your administrator.";
	@ConfigOptions(name = "messages.cant-teleport.console")
	public String messageCantTeleportConsole = "Unable to teleport %s !";
	@ConfigOptions(name = "messages.house-delete.success")
	public String messageHouseDeleteSuccess = "House deleted with success !";
	@ConfigOptions(name = "messages.house-delete.error")
	public String messageHouseDeleteError = "Error (%s) while trying to delete this house. Maybe it does not exist ?";
	@ConfigOptions(name = "messages.teleport-back.json")
	public String messageTeleportBackJson = "[\"\","
			+	"{\"text\":\"[PocketHouse]\"},"
			+	"{\"text\":\" To go back to your location, \",\"color\":\"gold\"},"
			+	"{\"text\":\"click here\",\"bold\":true,\""
			+		"clickEvent\":{"
			+ 			"\"action\":\"run_command\","
			+			"\"value\":\"/pockethouse teleport-back\""
			+		"},\""
			+		"hoverEvent\":{"
			+ 			"\"action\":\"show_text\","
			+			"\"value\":{\"text\":\"\","
			+			"\"extra\":[{\"text\":\"Click to teleport !\","
			+			"\"color\":\"aqua\"}]}},\"color\":\"none\"},"
			+	"{\"text\":\" !\",\"color\":\"gold\",\"bold\":false}"
			+ "]";
	@ConfigOptions(name = "messages.teleport-back.success")
	public String messageTeleportedBackSuccess = "You are back !";
	@ConfigOptions(name = "messages.teleport-back.error")
	public String messageTeleportBackError = "Cannot teleport you back.";
	@ConfigOptions(name = "messages.teleport.error")
	public String messageTeleportError = "Cannot teleport you.";
	
	public PluginMessages(final File dataFolder) {
		super(new File(dataFolder, "messages.yml"), Arrays.asList("PocketHouse Messages File"));
	}
	
}