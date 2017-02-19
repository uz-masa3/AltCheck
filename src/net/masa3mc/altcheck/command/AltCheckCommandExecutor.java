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
import net.masa3mc.altcheck.api.AltCheckAPI;
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
			if (args.length < 1) {
				if (sender.hasPermission("AltCheck.admin")) {
					help(sender);
				} else {
					sender.sendMessage(AltCheck.ALTCHECK_PREFIX
							+ Messages.noPermission);
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("check")) {
				if (sender.hasPermission("AltCheck.admin")) {
					if (args.length == 2) {
						Util u = new Util();
						u.checkLog(instance, sender.getName(), args[1]);
						if (Bukkit.getPlayerExact(args[1]) == null) {
							AltCheckAPI api = new AltCheckAPI(args[1]);
							if (api.getAccounts() == null || api.getAccounts().isEmpty()) {
								sender.sendMessage(AltCheck.ALTCHECK_PREFIX
										+ Messages.notfound);
								Bukkit.getPluginManager().callEvent(new AltCheckEvent(sender, args[1], false, null));
							} else {
								sender.sendMessage(Messages.checkHeader.replaceAll("%ip%", args[1]));
								new Thread(() -> {
									List<String> accounts = api.getAccounts();
									accounts.forEach(list -> {
										OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(list));
										sender.sendMessage(translateAlternateColorCodes('&',
												"&c" + (player == null ? u.convertUUIDtoName(list.replace('-', '\0'))
														: player.getName()) + "&7(" + list + ")"));
									});
									Bukkit.getPluginManager()
											.callEvent(new AltCheckEvent(sender, args[1], true, accounts));
								}).start();
							}
						} else {
							Player p = Bukkit.getPlayer(args[1]);
							String getIP = u.getPlayerIP(p);
							AltCheckAPI api = new AltCheckAPI(getIP);
							if (api.getAccounts() == null || api.getAccounts().isEmpty()) {
								sender.sendMessage(AltCheck.ALTCHECK_PREFIX
										+ Messages.notfound);
								AltCheckEvent event = new AltCheckEvent(sender, p.getName(), false, null);
								Bukkit.getPluginManager().callEvent(event);
							} else {
								sender.sendMessage(Messages.checkHeader.replaceAll("%ip%", getIP));
								new Thread(() -> {
									List<String> accounts = api.getAccounts();
									accounts.forEach(list -> {
										OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(list));
										sender.sendMessage(translateAlternateColorCodes('&',
												"&c" + (player == null ? u.convertUUIDtoName(list.replace('-', '\0'))
														: player.getName()) + "&7(" + list + ")"));
									});
									Bukkit.getPluginManager()
											.callEvent(new AltCheckEvent(sender, p.getName(), true, accounts));
								}).start();
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
					if (args.length == 1) {
						instance.reloadConfig();
						Messages.load();
						sender.sendMessage(
								AltCheck.ALTCHECK_PREFIX + Messages.reload);
					} else {
						sender.sendMessage(AltCheck.ALTCHECK_PREFIX
								+ translateAlternateColorCodes('&', "&7/altcheck reload"));
					}
				} else {
					sender.sendMessage(AltCheck.ALTCHECK_PREFIX
							+ Messages.noPermission);
				}
			} else if (args[0].equalsIgnoreCase("list")) {
				if (sender.hasPermission("AltCheck.admin")) {
					if (args.length == 1) {
						Util u = new Util();
						new Thread(() -> {
							Bukkit.getOnlinePlayers().forEach(players -> {
								String country = AltCheck.CountryCache.get(players.getName());
								String ip = u.getPlayerIP(players);
								if (ip.equals("127.0.0.1") || ip.startsWith("192.168.") || ip.startsWith("10.")
										|| ip.startsWith("172.31.")) {
									country = "LocalNetwork";
								} else {
									if (country == null) {
										country = u.getCountry(players).country_name;
									}
									if (country.equals("") || country.isEmpty()) {
										country = "Unknow";
									}
								}
								sender.sendMessage(AltCheck.ALTCHECK_PREFIX + translateAlternateColorCodes('&',
										instance.getConfig().getString("messages.list", "&c%user%&7(%country% / %ip%)")
												.replaceAll("%user%", players.getName())
												.replaceAll("%country%", country).replaceAll("%ip%", ip)));

							});
						}).start();
					} else {
						sender.sendMessage(AltCheck.ALTCHECK_PREFIX
								+ translateAlternateColorCodes('&', "&7/altcheck list"));
					}
				} else {
					sender.sendMessage(AltCheck.ALTCHECK_PREFIX
							+ Messages.noPermission);
				}
			} else {
				help(sender);
			}
		}
		return true;
	}

	private void help(CommandSender sender) {
		String[] messages = {
				"&7==============================",
				"&e AltCheck v" + instance.getDescription().getVersion() + " by uz_masa3",
				"&7 - /altcheck check [IP / Player(Online)]",
				"&7 - /altcheck list",
				"&7 - /altcheck reload",
		};
		for (int i = 0; i < messages.length; i++) {
			sender.sendMessage(translateAlternateColorCodes('&', messages[i]));
		}
	}

}
