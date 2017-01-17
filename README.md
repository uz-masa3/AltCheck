# AltCheck<br>

This plugin can check alt account. But need target already logged to local datafile.

<b>Support Version (AltCheck 1.4.5 ~ )</b>
<ul>
<li>1.8</li>
<li>1.9</li>
<li>1.10</li>
<li>1.11</li>
</ul>

<b>Support Version (AltCheck ~ 1.4)</b>
<ul>
<li>1.7</li>
<li>1.8</li>
<li>1.9</li>
<li>1.10</li>
<li>1.11</li>
</ul>

<b>AltCheckEvent(AltCheck 1.5 ~)</b>
@EventHandler
public void checked(net.masa3mc.altcheck.api.AltCheckEvent event) {
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
