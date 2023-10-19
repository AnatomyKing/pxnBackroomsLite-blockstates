package com.poixson.commonmc.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PluginSaveEvent extends Event {
  private static final HandlerList handlers = new HandlerList();
  
  public HandlerList getHandlers() {
    return handlers;
  }
  
  public static HandlerList getHandlerList() {
    return handlers;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\events\PluginSaveEvent.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */