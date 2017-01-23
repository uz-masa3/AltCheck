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
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

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
			if(cache.size() > 30) {
				cache.clear();
			}
			if (cache.containsKey(p.getName())) {
				country = cache.get(p.getName());
			} else {
				country = u.getCountry(p, ip1[1]).country_name;
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

	Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
	Team red = sb.getTeam("red"); //存在しない場合NullPointerException
	Team blue = sb.getTeam("blue"); //存在しない場合NullPointerException
	Objective obj = sb.getObjective("Sidebar"); //存在しない場合NullPointerException

	@EventHandler
	public void death(PlayerDeathEvent event) {
		if (event.getEntity().getKiller() instanceof Player) {
			Player player = event.getEntity();
			Player killer = player.getKiller();
			register();
			if (red.hasEntry(killer.getName())) {
				setScore(red, 1);
			} else if (blue.hasEntry(killer.getName())) {
				setScore(blue, 1);
			}
		}
	}

	private void setScore(Team team, int score) {
		if (team.equals(red)) {
			Score redteam = obj.getScore("red");
			redteam.setScore(score + redteam.getScore());
		} else if (team.equals(blue)) {
			Score blueteam = obj.getScore("blue");
			blueteam.setScore(score + blueteam.getScore());
		}

	}

	private void register() {
		for (Player players : Bukkit.getOnlinePlayers()) {
			players.setScoreboard(sb);
		}
		if (obj == null) {
			sb.registerNewObjective("Sidebar", "dummy");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			obj.setDisplayName("Hello world!");//16文字以下
		}
		if (red == null) {
			sb.registerNewTeam("red");
		}
		if (blue == null) {
			sb.registerNewTeam("blue");
		}
	}

}
