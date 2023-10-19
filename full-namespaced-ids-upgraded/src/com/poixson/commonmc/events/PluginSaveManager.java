package com.poixson.commonmc.events;

import com.poixson.commonmc.pxnCommonPlugin;
import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.commonmc.tools.plugin.xListener;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.plugin.PluginManager;

public class PluginSaveManager extends xListener<pxnCommonPlugin> {
  public PluginSaveManager(pxnCommonPlugin plugin) {
    super((xJavaPlugin)plugin);
  }
  
  @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
  public void onWorldSave(WorldSaveEvent event) {
    World world = event.getWorld();
    if ("world".equals(world.getName())) {
      PluginManager pm = Bukkit.getPluginManager();
      PluginSaveEvent eventSave = new PluginSaveEvent();
      pm.callEvent(eventSave);
    } 
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\events\PluginSaveManager.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */