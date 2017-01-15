package net.masa3mc.altcheck.API;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AltCheckEvent extends Event {
	/*
	 * 
	 * I'll add this api, when next update.
	 * 
	 * */
	private static final HandlerList handlers = new HandlerList();
	CommandSender checker;
	
	public AltCheckEvent(CommandSender checker){
		this.checker = checker;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public CommandSender getChecker(){
		return checker;
	}
	
}
