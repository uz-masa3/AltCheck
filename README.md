# AltCheck<br>

This plugin can check alt account. But need target already logged to local datafile.

<b>Support Version (AltCheck ~ 1.4)</b>
<ul>
<li>1.7</li>
<li>1.8</li>
<li>1.9</li>
<li>1.10</li>
<li>1.11</li>
</ul>

<b>Support Version (AltCheck 1.4.5 ~ )</b>
<ul>
<li>1.8</li>
<li>1.9</li>
<li>1.10</li>
<li>1.11</li>
</ul>

<b>net.masa3mc.altcheck.api.AltCheckEvent(AltCheck 1.5 ~)</b>
<pre><code>
@EventHandler
public void checked(AltCheckEvent event) {
	CommandSender sender = event.getCheckedBy();
	boolean found = event.isDataFound();
	String target = event.getTarget();
	List<String> alts = event.getAlts();
	getLogger().info("getCheckedBy(): "+sender);
	getLogger().info("isDataFound(): "+found);
	getLogger().info("getTarget(): "+target);
	getLogger().info("getAlts(): "+alts);
	if(alts == null)return;
	for(String a : alts) {
		getLogger().info("alts: "+a);
	}
}
</code></pre>

<b>net.masa3mc.altcheck.api.AltCheckAPI(AltCheck 1.8 ~)</b>
<pre><code>
public void api(String address) {
	AltCheckAPI api = new AltCheckAPI(address);
	List<String> accounts = api.getAccounts();
	getLogger().info("getAccounts(): " + accounts);
	if(alts == null)return;
	for(String a : accounts) {
		getLogger().info("Account: " + a);
	}
}
</code></pre>

