package net.masa3mc.altcheck.api;

import java.util.List;

import net.masa3mc.altcheck.Util;

public class AltCheckAPI {

	/*
	 *
	 * added AltCheck v1.8
	 *
	 */

	private Util util = new Util();
	private String ip;

	public AltCheckAPI(String ip) {
		this.ip = ip.trim().replace(".", "_");
	}

	//v1.8 ~
	public List<String> getAccounts() {
		List<String> list = util.getDataYml().getStringList(ip);
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list;
	}

	/*
	//v1.9.5 ~
	public void setAccounts(List<String> data) throws IOException {
		YamlConfiguration yml  = util.getDataYml();
		yml.set(ip, data);
		yml.save(util.getDataFile());
	}
	*/
}
