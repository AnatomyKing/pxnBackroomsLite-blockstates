package com.poixson.backrooms.listeners;

import com.poixson.backrooms.BackroomsPlugin;
import com.poixson.commonmc.events.OutsideOfWorldEvent;
import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.commonmc.tools.plugin.xListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class Listener_000 extends xListener<BackroomsPlugin> {
  public Listener_000(BackroomsPlugin plugin) {
    super((BackroomsPlugin)plugin);
  }
  
  @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
  public void onOutsideOfWorld(OutsideOfWorldEvent event) {
    int level = ((BackroomsPlugin)this.plugin).getLevelFromWorld(event.getTo().getWorld());
    if (level == 0 && 
      event.getOutsideDistance() > 20) {
      Player player = event.getPlayer();
      switch (event.getOutsideWhere()) {
        case SKY:
          ((BackroomsPlugin)this.plugin).noclip(player, 771);
          return;
        case VOID:
          ((BackroomsPlugin)this.plugin).noclip(player, 309);
          return;
      } 
      throw new RuntimeException("Unknown OutsideOfWorld event type");
    } 
  }
}
