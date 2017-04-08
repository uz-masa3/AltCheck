package net.masa3mc.altcheck;

import static org.bukkit.ChatColor.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.masa3mc.altcheck.command.AltCheckCommandExecutor;
import net.masa3mc.altcheck.data.DataMySQL;
import net.masa3mc.altcheck.data.DataType;

public final class AltCheck extends JavaPlugin {

	public static DataType dataType;
	public static AltCheck instance;
	public static final String ALTCHECK_PREFIX = translateAlternateColorCodes('&', "&c[&7AltCheck&c]&r ");
	public static int ConfigVersion;
	public static final int NowConfigVersion = 7;
	public static HashMap<String, String> CountryCache = new HashMap<String, String>();

	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		ConfigVersion = getConfig().getInt("configVersion");
		if (ConfigVersion != NowConfigVersion) {
			getLogger().warning("Outdated configuration file! Please delete old config.yml and restart server!");
		}
		SetType();
		Messages.load();
		Bukkit.getPluginManager().registerEvents(new Listeners(), this);
		getCommand("altcheck").setExecutor(new AltCheckCommandExecutor(this));

		getLogger().info("**************************");
		DataMySQL.setData("b6974f90-17a5-4563-828f-bc4b76072c31", "127.0.0.1");
		DataMySQL.setData("b6974f90-17a5-4563-828f-bc4b76072c32", "127.0.0.2");
		
		ResultSet rs = DataMySQL.getData();
		String t = "b6974f90-17a5-4563-828f-bc4b76072c32";
		try {
			while (rs.next()) {
				if (t.equals(rs.getString("UUID"))) {
					getLogger().info(rs.getString("UUID") + ": " + rs.getString("IP"));
				}
			}
			rs.close();
		} catch (SQLException e) {
			getLogger().info(e.getMessage());
		}
		getLogger().info("**************************");
	}

	public void onDisable() {
		DataMySQL.connection = null;
	}

	private void SetType() {
		if (getConfig().getString("Data.Type").equalsIgnoreCase("yaml")) {
			dataType = DataType.YAML;
			getLogger().info("Save altdata to a YAML.");
		} else if (getConfig().getString("Data.Type").equalsIgnoreCase("mysql")) {
			dataType = DataType.MYSQL;
			getLogger().info("Save altdata to a MySQL.");
		} else {
			dataType = DataType.YAML;
			getLogger().info("Save altdata to a YAML.");
		}
	}

}
