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
