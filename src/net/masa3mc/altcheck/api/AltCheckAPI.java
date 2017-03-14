package net.masa3mc.altcheck.api;

import java.util.List;

import net.masa3mc.altcheck.Util;

public class AltCheckAPI {

	/*
	 *
	 * added AltCheck v1.8
	 *
	 */

	private final Util util = new Util();
	private String ip;
	private List<String> accounts;

	public AltCheckAPI(String ip) {
		this.ip = ip.trim().replace(".", "_");
		this.accounts = util.getDataYml().getStringList(ip);
	}

	//v1.8 ~
	public List<String> getAccounts() {
		return this.accounts;
	}
	
	public void setAccounts(List<String> accounts){
		this.accounts = accounts;
	}
	
	/*
	//v1.9.5 ~
	public void saveAccounts() throws IOException {
		YamlConfiguration yml  = util.getDataYml();
		yml.set(this.ip, this.accounts);
		yml.save(util.getDataFile());
	}
	*/
}
