package com.poixson.commonmc.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveNormalEvent extends PlayerMoveEvent {
  private static final HandlerList handlers = new HandlerList();
  
  private boolean cancel = false;
  
  public PlayerMoveNormalEvent(Player player, Location from, Location to) {
    super(player, from, to);
  }
  
  public HandlerList getHandlers() {
    return handlers;
  }
  
  public static HandlerList getHandlerList() {
    return handlers;
  }
  
  public boolean isCancelled() {
    return this.cancel;
  }
  
  public void setCancelled(boolean cancel) {
    this.cancel = cancel;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\events\PlayerMoveNormalEvent.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */