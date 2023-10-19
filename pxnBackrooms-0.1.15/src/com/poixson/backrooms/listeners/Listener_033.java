package com.poixson.backrooms.listeners;

import com.poixson.backrooms.BackroomsPlugin;
import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.commonmc.tools.plugin.xListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityExplodeEvent;

public class Listener_033 extends xListener<BackroomsPlugin> {
  public Listener_033(BackroomsPlugin plugin) {
    super((xJavaPlugin)plugin);
  }
  
  @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
  public void onEntityExplode(EntityExplodeEvent event) {
    int level = ((BackroomsPlugin)this.plugin).getLevelFromWorld(event.getEntity().getWorld());
    if (level == 33)
      event.setYield(0.0F); 
  }
}
