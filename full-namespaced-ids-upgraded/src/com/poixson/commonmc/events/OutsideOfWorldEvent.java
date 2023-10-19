package com.poixson.commonmc.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;

public class OutsideOfWorldEvent extends PlayerMoveEvent {
  private static final HandlerList handlers = new HandlerList();
  
  private boolean cancel = false;
  
  protected final Outside outside;
  
  protected final int distance;
  
  public enum Outside {
    SKY, VOID;
  }
  
  public OutsideOfWorldEvent(Player player, Location from, Location to, Outside outside, int distance) {
    super(player, from, to);
    this.outside = outside;
    this.distance = distance;
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
  
  public Outside getOutsideWhere() {
    return this.outside;
  }
  
  public int getOutsideDistance() {
    return this.distance;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\events\OutsideOfWorldEvent.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */