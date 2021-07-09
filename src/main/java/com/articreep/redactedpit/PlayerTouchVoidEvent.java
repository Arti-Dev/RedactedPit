package com.articreep.redactedpit;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerTouchVoidEvent extends Event implements Cancellable {
	
	private static final HandlerList HANDLERS = new HandlerList();
	private final Player player;
	private boolean isCancelled;
	
	public PlayerTouchVoidEvent(Player player) {
		this.player = player;
	}

	@Override
	public HandlerList getHandlers() {
		// TODO Auto-generated method stub
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
        return HANDLERS;
    }
	
	public Player getPlayer() {
		return this.player;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.isCancelled = cancel;
		
	}
}
