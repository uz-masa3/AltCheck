package net.masa3mc.altcheck.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class AltCheckCommandTabCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("altcheck")) {
			List<String> list = new ArrayList<String>();
			if (!sender.hasPermission("AltCheck.admin")) {
				return list;
			}
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("") || args[0].isEmpty()) {
					list.add("check");
					list.add("reload");
					list.add("list");
				} else if (args[0].startsWith("c")) {
					list.add("check");
				} else if (args[0].startsWith("r")) {
					list.add("reload");
				} else if (args[0].startsWith("l")) {
					list.add("list");
				}
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("check")) {
					Bukkit.getOnlinePlayers().forEach(players -> list.add(players.getName()));
				}
			}
			return list;
		}
		return null;
	}
}
