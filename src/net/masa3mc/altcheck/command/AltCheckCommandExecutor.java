package net.masa3mc.altcheck.command;

import static org.bukkit.ChatColor.*;

import java.util.ArrayList;
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
			} else if (args[0].equalsIgnoreCase("kickmessage")) {
				if (sender.hasPermission("AltCheck.admin")) {
					if (args.length > 1) {
						StringBuilder message = new StringBuilder();
						for (int i = 1; i < args.length; i++) {
							message.append(args[i]).append(" ");
						}
						instance.getConfig().set("kickMessage", message.toString().trim());
						instance.saveConfig();
						instance.reloadConfig();
						sender.sendMessage(AltCheck.ALTCHECK_PREFIX
								+ Messages.save);
					} else {
						sender.sendMessage(translateAlternateColorCodes('&', "&7/altcheck kickmessage [Message]"));
					}
				} else {
					sender.sendMessage(AltCheck.ALTCHECK_PREFIX
							+ Messages.noPermission);
				}
			} else if (args[0].equalsIgnoreCase("maxalt")) {
				if (sender.hasPermission("AltCheck.admin")) {
					if (args.length == 2) {
						try {
							int max = Integer.parseInt(args[1]);
							if (max > 0 && max < 1000) {
								instance.getConfig().set("maxAlt", max);
								instance.saveConfig();
								instance.reloadConfig();
								sender.sendMessage(AltCheck.ALTCHECK_PREFIX
										+ Messages.save);
							} else {
								sender.sendMessage(translateAlternateColorCodes('&', "&7/altcheck maxalt [1 - 999]"));
							}
						} catch (NumberFormatException e) {
							sender.sendMessage(AltCheck.ALTCHECK_PREFIX
									+ Messages.enternumber);
						}
					} else {
						sender.sendMessage(translateAlternateColorCodes('&', "&7/altcheck maxalt [1 - 999]"));
					}
				} else {
					sender.sendMessage(AltCheck.ALTCHECK_PREFIX
							+ Messages.noPermission);
				}
			} else if (args[0].equalsIgnoreCase("ignore")) {
				if (sender.hasPermission("AltCheck.admin")) {
					if (args.length == 2) {
						if (args[1].length() == 36) {
							if (!instance.getConfig().getStringList("IgnoreUUID").contains(args[1])) {
								ArrayList<String> arraylist = new ArrayList<String>();
								arraylist.add(args[1]);
								for (String list : instance.getConfig().getStringList("IgnoreUUID")) {
									arraylist.add(list);
								}
								instance.getConfig().set("IgnoreUUID", arraylist);
								instance.saveConfig();
								instance.reloadConfig();
							}
							sender.sendMessage(AltCheck.ALTCHECK_PREFIX
									+ Messages.save);
						} else {
							sender.sendMessage(translateAlternateColorCodes('&', "&7/altcheck ignore [UUID]"));
						}
					} else {
						sender.sendMessage(translateAlternateColorCodes('&', "&7/altcheck ignore [UUID]"));
					}
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
				"&7 - /altcheck kickmessage [Message]", // /* 5 */
				"&7 - /altcheck maxalt [max]", // /* 6 */
				"&7 - /altcheck ignore [UUID]" // /* 7 */
		};
		for (int i = 0; i < messages.length; i++) {
			sender.sendMessage(translateAlternateColorCodes('&', messages[i]));
		}
	}

}
