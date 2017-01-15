package net.masa3mc.altcheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;
import org.bukkit.entity.Player;

import com.google.gson.Gson;

public final class Util {

  private static AltCheck ins = AltCheck.instance;

  /**
   * Convert alternative color codes to ChatColor from source string.
   *
   * @deprecated Use {@link ChatColor#translateAlternateColorCodes(char, String)}
   * @param str string to convert alternative color codes
   * @return Converted String
   */
  @Deprecated
  public static String color(String str) {
    return ChatColor.translateAlternateColorCodes('&', str);
  }

  /**
   * Get version string of plugin.
   *
   * @return String expression of version
   */
  public static String getVersion() {
    return ins.getDescription().getVersion();
  }

  public List<String> check(String IP) {
    YamlConfiguration yml = getDataYml();
    if (yml.getString(IP) != null) {
      ArrayList<String> arraylist = new ArrayList<String>();
      for (String list : yml.getStringList(IP)) {
        arraylist.add(list);
      }
      return arraylist;
    } else {
      return null;
    }
  }

  public String getPlayerIP(Player player) {
    if (player == null) {
      return null;
    } else {
      String[] address = player.getAddress().toString().split("/");
      String[] ip = address[1].split(":");
      return ip[0];
    }
  }

  public void checkLog(String sender, String ip) {
    ins.getLogger().info(sender + " checked " + ip + "!");
  }

  public YamlConfiguration getDataYml() {
    FileConfiguration conf = ins.getConfig();
    String path = conf.getString("yml").replace("%altcheck%", ins.getDataFolder().getAbsolutePath());
    return YamlConfiguration.loadConfiguration(new File(path));
  }

  public File getDataFile() {
    FileConfiguration conf = ins.getConfig();
    String path = conf.getString("yml").replace("%altcheck%", ins.getDataFolder().getAbsolutePath());
    return new File(path);
  }

  public String convertUUIDtoName(String uuid) { // UUID to Name
    if (!uuid.equals("") && uuid.length() == 32) {
      try {
        URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
        HttpsURLConnection http = (HttpsURLConnection) url.openConnection();
        http.setRequestMethod("GET");
        http.setRequestProperty("User-Agent", "AltCheck");
        if (http.getResponseCode() == HttpsURLConnection.HTTP_OK) {
          BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream(), "UTF-8"));
          UUIDJson json = new Gson().fromJson(reader.readLine(), UUIDJson.class);
          reader.close();
          return json.name;
        } else {
          return null;
        }
      } catch (IOException ex) {
        return null;
      }
    } else {
      return null;
    }
  }

  public CountryJson getCountry(Player player) {
    if (player != null) {
      try {
        URL url = new URL("http://freegeoip.net/json/" + getPlayerIP(player));
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("GET");
        http.setRequestProperty("User-Agent", "AltCheck");
        if (http.getResponseCode() == HttpsURLConnection.HTTP_OK) {
          BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream(), "UTF-8"));
          CountryJson json = new Gson().fromJson(reader.readLine(), CountryJson.class);
          reader.close();
          return json;
        } else {
          return null;
        }
      } catch (IOException ex) {
        return null;
      }
    } else {
      return null;
    }
  }

}
