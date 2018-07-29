package net.masa3mc.altcheck;

import static org.bukkit.ChatColor.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class Listeners implements Listener {

	private final Util u = new Util();
	private final AltCheck ins;

	public Listeners() {
		this.ins = null;
	}

	public Listeners(AltCheck instance) {
		this.ins = instance;
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		Player p = event.getPlayer();
		String[] ip = event.getAddress().toString().split("/");
		String ip_ = ip[1].replace('.', '_');
		Configuration conf = ins.getConfig();
		new Thread() {
			public void run() {
				YamlConfiguration yml = u.getDataYml();
				if (!yml.getStringList(ip_).contains("" + p.getUniqueId())) {
					List<String> arraylist = yml.getStringList(ip_);
					arraylist.add("" + p.getUniqueId());
					yml.set(ip_, arraylist);
					try {
						yml.save(u.getDataFile());
					} catch (IOException e) {
						ins.getLogger().warning("IOException: Couldn't write to DataFile.");
					}
				}
			}
		};
		if (conf.getBoolean("CountryFilter")
				&& !conf.getStringList("IgnoreCountryFilter").contains("" + p.getUniqueId())) {
			if (ip[1].equals("127.0.0.1") || ip[1].startsWith("192.168.") || ip[1].startsWith("10.")
					|| ip[1].startsWith("172.31.")) {
				AltCheck.CountryCache.put(p.getName(), "LocalNetwork");
			} else {
				String country = "";
				HashMap<String, String> cache = AltCheck.CountryCache;
				if (cache.size() > 30) {
					cache.clear();
				}
				if (cache.containsKey(p.getName())) {
					country = cache.get(p.getName());
				} else {
					country = u.getCountry(ip[1]).country_name;
					cache.put(p.getName(), country);
				}
				if (!conf.getStringList("WhitelistedCountry").contains(country)) {
					p.kickPlayer(conf.getString("CountryKickMessage"));
					event.setKickMessage(translateAlternateColorCodes('&', conf.getString("CountryKickMessage")));
					event.setResult(Result.KICK_OTHER);
					Bukkit.getOnlinePlayers().forEach(players -> {
						if (players.hasPermission("AltCheck.admin")) {
							players.sendMessage(AltCheck.ALTCHECK_PREFIX
									+ translateAlternateColorCodes('&',
											"&7" + p.getName() + "(" + ip[1]
													+ ") was kicked by AltCheck-CountryFilter."));
						}
					});
				}
			}
		}
		if (!conf.getStringList("IgnoreCheckAlt").contains("" + p.getUniqueId())) {
			YamlConfiguration yml = u.getDataYml();
			if (yml.getStringList(ip_).size() > conf.getInt("maxAlt")) {
				p.kickPlayer(conf.getString("kickMessage"));
				event.setKickMessage(translateAlternateColorCodes('&', conf.getString("kickMessage")));
				event.setResult(Result.KICK_OTHER);
				Bukkit.getOnlinePlayers().forEach(players -> {
					if (players.hasPermission("AltCheck.admin")) {
						players.sendMessage(AltCheck.ALTCHECK_PREFIX
								+ translateAlternateColorCodes('&',
										"&7" + p.getName() + "(" + ip[1] + ") was kicked by AltCheck- Alt."));
					}
				});
			}
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		/*
		Player p = event.getPlayer();
		YamlConfiguration yml = u.getDataYml();
		String ip_ = p.getAddress().toString().split("/")[1].split(":")[0];
		String ip = ip_.replace('.', '_');
		
		if (ip_.equals("127.0.0.1") || ip_.startsWith("192.168.") || ip_.startsWith("10.")
				|| ip_.startsWith("172.31.")) {
			Bukkit.getOnlinePlayers().forEach(players -> {
				if (players.hasPermission("AltCheck.admin")) {
					players.sendMessage(AltCheck.ALTCHECK_PREFIX
							+ translateAlternateColorCodes('&', "&7" + p.getName() + " (LocalNetwork) has "
									+ yml.getStringList(ip).size() + " accounts."));
				}
			});
		} else {
			String country = AltCheck.CountryCache.get(p.getName());
			if (country == null || country.isEmpty()) {
				new Thread(() -> {
					CountryJson json = u.getCountry(ip_);
					if (json == null) {
						Bukkit.getOnlinePlayers().forEach(players -> {
							if (players.hasPermission("AltCheck.admin")) {
								players.sendMessage(AltCheck.ALTCHECK_PREFIX
										+ translateAlternateColorCodes('&', "&7" + p.getName() + " (Unknow) has "
												+ yml.getStringList(ip).size() + " accounts."));
							}
						});
					} else {
						Bukkit.getOnlinePlayers().forEach(players -> {
							if (players.hasPermission("AltCheck.admin")) {
								players.sendMessage(AltCheck.ALTCHECK_PREFIX
										+ translateAlternateColorCodes('&',
												"&7" + p.getName() + " (" + json.country_name + ") has "
														+ yml.getStringList(ip).size() + " accounts."));
														
							}
						});
						AltCheck.CountryCache.put(p.getName(), json.country_name);
					}
				}).start();
			} else {
				Bukkit.getOnlinePlayers().forEach(players -> {
					if (players.hasPermission("AltCheck.admin")) {
						players.sendMessage(AltCheck.ALTCHECK_PREFIX + translateAlternateColorCodes('&',
								"&7" + p.getName() + " (" + country + ") has " + yml.getStringList(ip).size()
										+ " accounts."));
					}
				});
			}
		}
		*/
	}

}
