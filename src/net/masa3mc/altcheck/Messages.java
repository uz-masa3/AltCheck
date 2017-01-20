package net.masa3mc.altcheck;

import org.bukkit.configuration.file.FileConfiguration;

public class Messages {
	
	public static String notfound = c("&7Data not found.");
	public static String checkHeader = c("&7============&c%ip%&7============");
	public static String reload = c("&7Reload complete.");
	public static String noPermission = c("&cYou don't have permission.");
	public static String save = c("&aSuccessfully save settings.");
	public static String enternumber = c("&cPlease enter a number.");
	
	private static String c(String str) {
		return new Util().color(str);
	}
	
	public static void load() {
		if(AltCheck.ConfigVersion != AltCheck.NowConfigVersion) {
			return;
		}
		FileConfiguration conf = AltCheck.instance.getConfig();
		notfound = c(conf.getString("messages.notfound"));
		checkHeader = c(conf.getString("messages.checkHeader"));
		reload = c(conf.getString("messages.reload"));
		noPermission = c(conf.getString("messages.nopermission"));
		save = c(conf.getString("messages.save"));
		enternumber = c(conf.getString("messages.enternumber"));
	}
	
}
