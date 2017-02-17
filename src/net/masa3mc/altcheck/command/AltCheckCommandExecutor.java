package net.masa3mc.altcheck.command;

import static org.bukkit.ChatColor.*;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.masa3mc.altcheck.AltCheck;
import net.masa3mc.altcheck.Messages;
import net.masa3mc.altcheck.Util;
import net.masa3mc.altcheck.api.AltCheckEvent;

public final class AltCheckCommandExecutor implements CommandExecutor {

	private AltCheck instance;

	@SuppressWarnings("unused") // Called from reflection
	private AltCheckCommandExecutor() {
		this.instance = null;
	}

	public AltCheckCommandExecutor(AltCheck instance) {
		this.instance = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("altcheck")) {
			Util u = new Util();
			if (args.length < 1) {
				help(sender);
				return true;
			}
			if (args[0].equalsIgnoreCase("check")) {
				if (sender.hasPermission("AltCheck.admin")) {
					if (args.length == 2) {
						u.checkLog(sender.getName(), args[1]);
						if (Bukkit.getPlayerExact(args[1]) == null) {
							String ip = args[1].replace('.', '_');
							if (u.check(ip) == null) {
								sender.sendMessage(AltCheck.ALTCHECK_PREFIX
										+ Messages.notfound);
								AltCheckEvent event = new AltCheckEvent(sender, args[1], false, null);
								Bukkit.getPluginManager().callEvent(event);
							} else {
								sender.sendMessage(Messages.checkHeader.replaceAll("%ip%", args[1]));
								new Thread(() -> {
									List<String> alts = u.check(ip);
									for (String list : alts) {
										OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(list));
										sender.sendMessage(translateAlternateColorCodes('&',
												"&c" + (player == null ? u.convertUUIDtoName(list.replace('-', '\0'))
														: player.getName()) + "&7(" + list + ")"));
									}
									AltCheckEvent event = new AltCheckEvent(sender, args[1], true, alts);
									Bukkit.getPluginManager().callEvent(event);
								}).start();

							}
						} else {
							Player p = Bukkit.getPlayer(args[1]);
							String getIP = u.getPlayerIP(p);
							String ip = getIP.replace('.', '_');
							if (u.check(ip) == null) {
								sender.sendMessage(AltCheck.ALTCHECK_PREFIX
										+ Messages.notfound);
								AltCheckEvent event = new AltCheckEvent(sender, p.getName(), false, null);
								Bukkit.getPluginManager().callEvent(event);
							} else {
								sender.sendMessage(Messages.checkHeader.replaceAll("%ip%", getIP));
								Thread t = new Thread() {
									public void run() {
										List<String> alts = u.check(ip);
										for (String list : alts) {
											String opn = Bukkit.getOfflinePlayer(UUID.fromString(list)).getName();
											if (opn == null) {
												sender.sendMessage(translateAlternateColorCodes('&',
														"&c" + u.convertUUIDtoName(list.replace("-", "")) + "&7(" + list
																+ ")"));
											} else {
												sender.sendMessage(translateAlternateColorCodes('&',
														"&c" + opn + "&7(" + list + ")"));
											}
										}
										AltCheckEvent event = new AltCheckEvent(sender, p.getName(), true, alts);
										Bukkit.getPluginManager().callEvent(event);
									}
								};
								t.start();

							}
						}
					} else {
						sender.sendMessage(AltCheck.ALTCHECK_PREFIX
								+ translateAlternateColorCodes('&', "&7/altcheck check [IP / Player(Online)]"));
					}
				} else {
					sender.sendMessage(AltCheck.ALTCHECK_PREFIX
							+ Messages.noPermission);
				}
			} else if (args[0].equalsIgnoreCase("reload")) {
				if (sender.hasPermission("AltCheck.admin")) {
					instance.reloadConfig();
					Messages.load();
					sender.sendMessage(
							AltCheck.ALTCHECK_PREFIX + Messages.reload);
				} else {
					sender.sendMessage(AltCheck.ALTCHECK_PREFIX
							+ Messages.noPermission);
				}
			} else {
				help(sender);
			}
			return true;
		}
		return false;
	}

	private void help(CommandSender sender) {
		String[] messages = { /* Messages */
				"&7==============================", // /* 1 */
				"&e AltCheck v" + Util.getVersion() + " by uz_masa3", // /* 2 */
				"&7 - /altcheck check [IP / Player(Online)]", // /* 3 */
				"&7 - /altcheck reload", // /* 4 */
		};
		for (int i = 0; i < messages.length; i++) {
			sender.sendMessage(translateAlternateColorCodes('&', messages[i]));
		}
	}

}
