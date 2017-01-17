package net.masa3mc.altcheck.api;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AltCheckEvent extends Event {
	/*
	 *
	 * added AltCheck v1.5
	 *
	 */
	private static final HandlerList handlers = new HandlerList();
	CommandSender checkedby;
	String target;
	boolean found;
	List<String> alts;

	public AltCheckEvent(CommandSender CheckedBy, String Target, boolean found, List<String> alts) {
		this.checkedby = CheckedBy;
		this.target = Target;
		this.found = found;
		this.alts = alts;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public CommandSender getCheckedBy() {
		return checkedby;
	}

	public String getTarget() {
		return target;
	}

	public boolean isDataFound() {
		return found;
	}

	public List<String> getAlts() {
		return alts;
	}

}
