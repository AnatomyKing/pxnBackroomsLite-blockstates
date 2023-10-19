package com.poixson.commonmc.tools.plugin;

import com.poixson.commonmc.events.PluginSaveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

class LocalPluginSaveListener extends xListener<xJavaPlugin> {
  public LocalPluginSaveListener(xJavaPlugin plugin) {
    super(plugin);
  }
  
  @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
  public void onPluginSave(PluginSaveEvent event) {
    xJavaPlugin.this.onSave();
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\plugin\xJavaPlugin$LocalPluginSaveListener.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */