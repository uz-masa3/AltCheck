package net.masa3mc.altcheck;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.masa3mc.altcheck.api.AltCheckAPI;
import net.masa3mc.altcheck.command.AltCheckCommandExecutor;

public final class AltCheck extends JavaPlugin {

	public static AltCheck instance;
	public static final String ALTCHECK_PREFIX = ChatColor.translateAlternateColorCodes('&', "&c[&7AltCheck&c]&r ");
	public static HashMap<String, String> CountryCache = new HashMap<String, String>();

	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		if (getConfig().getInt("configVersion") != 6) {
			getLogger().warning("Outdated configuration file! Please delete old config.yml and restart!");
		}
		Messages.load();
		Bukkit.getPluginManager().registerEvents(new Listeners(), this);
		getCommand("altcheck").setExecutor(new AltCheckCommandExecutor(this));
	}
	
	public static AltCheckAPI getData(String address) {
		return new AltCheckAPI(address);
	}

	public static AltCheckAPI getData(Player player) {
		return getData(new Util().getPlayerIP(player));
	}

}
