package net.masa3mc.altcheck;

import static org.bukkit.ChatColor.*;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.masa3mc.altcheck.command.AltCheckCommandExecutor;

public final class AltCheck extends JavaPlugin {

  public static AltCheck instance;
  public static final String ALTCHECK_PREFIX = translateAlternateColorCodes('&', "&c[&7AltCheck&c]&r ");

  public void onEnable() {
    instance = this;
    saveDefaultConfig();
    if (getConfig().getInt("configVersion") != 3) {
      getLogger().warning("Outdated configuration file! Please delete old config.yml and restart!");
    }
    Bukkit.getPluginManager().registerEvents(new Listeners(), this);
    getCommand("altcheck").setExecutor(new AltCheckCommandExecutor(this));
  }

}
