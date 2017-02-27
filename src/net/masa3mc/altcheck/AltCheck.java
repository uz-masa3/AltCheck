package net.masa3mc.altcheck;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.masa3mc.altcheck.api.AltCheckAPI;
import net.masa3mc.altcheck.command.AltCheckCommandExecutor;
import net.masa3mc.altcheck.command.AltCheckCommandTabCompleter;

public final class AltCheck extends JavaPlugin {

	public static AltCheck instance;
	public static final String ALTCHECK_PREFIX = ChatColor.translateAlternateColorCodes('&', "&c[&7AltCheck&c]&r ");
	public static HashMap<String, String> CountryCache = new HashMap<String, String>();

	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		if (getConfig().getInt("configVersion") < 8) {
			getLogger().warning("Outdated configVersion! Please delete old config.yml and restart!");
		}else if(getConfig().getInt("configVersion") > 8) {
			getLogger().warning("Incorrect configVersion! Please delete config.yml and restart!");
		}
		Messages.load();
		Bukkit.getPluginManager().registerEvents(new Listeners(this), this);
		getCommand("altcheck").setExecutor(new AltCheckCommandExecutor(this));
		getCommand("altcheck").setTabCompleter(new AltCheckCommandTabCompleter());
	}
	
	public static AltCheckAPI getData(String address) {
		return new AltCheckAPI(address);
	}

	public static AltCheckAPI getData(Player player) {
		return getData(player.getAddress().toString().split("/")[1].split(":")[0]);
	}

}
