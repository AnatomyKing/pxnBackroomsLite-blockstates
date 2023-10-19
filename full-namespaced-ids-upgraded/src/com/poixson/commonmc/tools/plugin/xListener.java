package com.poixson.commonmc.tools.plugin;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public abstract class xListener<T extends xJavaPlugin> implements Listener {
  protected final T plugin;
  
  public xListener(T plugin) {
    this.plugin = plugin;
  }
  
  public void register() {
    Bukkit.getPluginManager()
      .registerEvents(this, (Plugin)this.plugin);
  }
  
  public void unregister() {
    HandlerList.unregisterAll(this);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\plugin\xListener.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */