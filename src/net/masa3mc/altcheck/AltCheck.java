package net.masa3mc.altcheck;

import static org.bukkit.ChatColor.*;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.masa3mc.altcheck.command.AltCheckCommandExecutor;

public final class AltCheck extends JavaPlugin {

	public static AltCheck instance;
	public static final String ALTCHECK_PREFIX = translateAlternateColorCodes('&', "&c[&7AltCheck&c]&r ");
	public static int ConfigVersion;
	public static int NowConfigVersion = 6;
	public static HashMap<String, String> CountryCache = new HashMap<String, String>();

	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		ConfigVersion = getConfig().getInt("configVersion");
		if (ConfigVersion != 6) {
			getLogger().warning("Outdated configuration file! Please delete old config.yml and restart!");
		}
		Messages.load();
		Bukkit.getPluginManager().registerEvents(new Listeners(), this);
		getCommand("altcheck").setExecutor(new AltCheckCommandExecutor(this));
	}

}
