package net.masa3mc.altcheck;

import static org.bukkit.ChatColor.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listeners implements Listener {

	Util u = new Util();
	AltCheck ins = AltCheck.instance;

	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		Player p = event.getPlayer();
		YamlConfiguration yml = u.getDataYml();
		String[] ip1 = String.valueOf(event.getAddress()).split("/");
		String ip = ip1[1].replace('.', '_');
		String data = "" + p.getUniqueId();
		Configuration conf = ins.getConfig();
		if (conf.getBoolean("CountryFilter")) {
			String country = "";
			HashMap<String, String> cache = AltCheck.CountryCache;
			if (cache.size() > 30) {
				cache.clear();
			}
			if (cache.containsKey(p.getName())) {
				country = cache.get(p.getName());
			} else {
				country = u.getCountry(ip1[1]).country_name;
				cache.put(p.getName(), country);
			}
			if (!conf.getStringList("WhitelistedCountry").contains(country)) {
				p.kickPlayer(conf.getString("CountryKickMessage"));
				event.setKickMessage(translateAlternateColorCodes('&', conf.getString("CountryKickMessage")));
				event.setResult(Result.KICK_OTHER);
				for (Player players : Bukkit.getOnlinePlayers()) {
					if (players.hasPermission("AltCheck.admin")) {
						players.sendMessage(AltCheck.ALTCHECK_PREFIX
								+ translateAlternateColorCodes('&',
										"&7" + p.getName() + "(" + ip1[1]
												+ ") was kicked by this plugin. (CountryFilter)"));
					}
				}
			}
		}
		if (!yml.getStringList(ip).contains(data)) {
			ArrayList<String> arraylist = new ArrayList<String>();
			arraylist.add(data);
			for (String list : yml.getStringList(ip)) {
				arraylist.add(list);
			}
			yml.set(ip, arraylist);
			try {
				yml.save(u.getDataFile());
			} catch (IOException e) {
				ins.getLogger().warning("IOException: Couldn't write to DataFile.");
			}
		}
		if (!conf.getStringList("IgnoreUUID").contains("" + p.getUniqueId())) {
			if (yml.getStringList(ip).size() > conf.getInt("maxAlt")) {
				p.kickPlayer(conf.getString("kickMessage"));
				event.setKickMessage(translateAlternateColorCodes('&', conf.getString("kickMessage")));
				event.setResult(Result.KICK_OTHER);
				for (Player players : Bukkit.getOnlinePlayers()) {
					if (players.hasPermission("AltCheck.admin")) {
						players.sendMessage(AltCheck.ALTCHECK_PREFIX
								+ translateAlternateColorCodes('&',
										"&7" + p.getName() + "(" + ip1[1] + ") was kicked by this plugin. (Alt)"));
					}
				}
			}
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		YamlConfiguration yml = u.getDataYml();
		String ip_ = u.getPlayerIP(p);
		String ip = u.getPlayerIP(p).replace('.', '_');
		Thread t = new Thread() {
			public void run() {
				if (ip_.equals("127.0.0.1") || ip_.startsWith("192.168.") || ip_.startsWith("10.")
						|| ip_.startsWith("172.31.")) {
					for (Player players : Bukkit.getOnlinePlayers()) {
						if (players.hasPermission("AltCheck.admin")) {
							players.sendMessage(AltCheck.ALTCHECK_PREFIX
									+ translateAlternateColorCodes('&', "&7" + p.getName() + " (LocalNetwork) has "
											+ yml.getStringList(ip).size() + " accounts."));
						}
					}
				} else {
					CountryJson json = u.getCountry(p);
					if (json != null) {
						String country = json.country_name;
						for (Player players : Bukkit.getOnlinePlayers()) {
							if (players.hasPermission("AltCheck.admin")) {
								players.sendMessage(AltCheck.ALTCHECK_PREFIX + translateAlternateColorCodes('&',
										"&7" + p.getName() + " (" + country + ") has " + yml.getStringList(ip).size()
												+ " accounts."));
							}
						}
					} else {
						for (Player players : Bukkit.getOnlinePlayers()) {
							if (players.hasPermission("AltCheck.admin")) {
								players.sendMessage(AltCheck.ALTCHECK_PREFIX
										+ translateAlternateColorCodes('&', "&7" + p.getName() + " (Unknow) has "
												+ yml.getStringList(ip).size() + " accounts."));
							}

						}
					}
				}
			}
		};
		t.start();

	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		YamlConfiguration yml = u.getDataYml();
		String ip = u.getPlayerIP(p).replace('.', '_');
		String data = "" + p.getUniqueId();
		if (!yml.getStringList(ip).contains(data)) {
			ArrayList<String> arraylist = new ArrayList<String>();
			arraylist.add(data);
			for (String list : yml.getStringList(ip)) {
				arraylist.add(list);
			}
			yml.set(ip, arraylist);
			try {
				yml.save(u.getDataFile());
			} catch (IOException e) {
				ins.getLogger().warning("IOException: Couldn't write to DataFile.");
			}
		}
	}

}
