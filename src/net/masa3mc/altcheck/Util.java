package net.masa3mc.altcheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;

import com.google.gson.Gson;

public final class Util {

	public YamlConfiguration getDataYml() {
		return YamlConfiguration.loadConfiguration(getDataFile());
	}

	public File getDataFile() {
		AltCheck instance = AltCheck.instance;
		String path = instance.getConfig().getString("yml").replace("%altcheck%",
				instance.getDataFolder().getAbsolutePath());
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

	public CountryJson getCountry(String ip) {
		try {
			URL url = new URL("http://freegeoip.net/json/" + ip);
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
	}

}
