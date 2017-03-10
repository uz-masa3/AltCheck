# AltCheck<br>

This plugin can check alt account. But need target already logged to local datafile.

<b>net.masa3mc.altcheck.api.AltCheckEvent(AltCheck 1.5 ~)</b>
<pre><code>
@EventHandler
public void checked(AltCheckEvent event) {
	CommandSender sender = event.getCheckedBy();
	boolean found = event.isDataFound();
	String target = event.getTarget();
	//List<String> alts = event.getAlts();
	List<String> accounts = event.getAccounts();
	getLogger().info("getCheckedBy(): "+sender);
	getLogger().info("isDataFound(): "+found);
	getLogger().info("getTarget(): "+target);
	getLogger().info("getAccouns(): "+accounts);
	if(accounts == null)return;
	for(String a : accounts) {
		getLogger().info("accounts: "+a);
	}
}
</code></pre>

<b>net.masa3mc.altcheck.api.AltCheckAPI(AltCheck 1.8 ~)</b>
<pre><code>
public void api(String address) {
	//AltCheckAPI api = new AltCheckAPI(address);
	AltCheckAPI api = AltCheck.getData(address or Player);
	List<String> accounts = api.getAccounts();
	getLogger().info("getAccounts(): " + accounts);
	if(accounts == null)return;
	for(String a : accounts) {
		getLogger().info("Account: " + a);
	}
}
</code></pre>

日本語config説明<br>
yml: アカウントデータを保存するファイルを指定。初期ではconfig.ymlと同じ所のdata.ymlに保存される<br>
maxAlt: 同一IPから設定値以上のログインがあると、そのIPからは入れなくする。初期では2アカウント以上で接続を試みると今後は入れなくする<br>
kickMessage: maxAltで弾かれた時のメッセージを変えれる<br>
IgnoreCheckAlt: ここに追加したUUIDはmaxAltで弾かれなくなる<br>
CountryFilter: 設定国以外からの接続を拒否するかしないか<br>
CountryKickMessage: 設定国以外から接続した時のkickメッセージを変えれる<br>
WhitelistedCountry: 初期では日本以外は入れなくなる<br>
