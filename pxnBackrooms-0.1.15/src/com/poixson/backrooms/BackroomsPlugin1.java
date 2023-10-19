package com.poixson.backrooms;

import java.util.Iterator;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

class BackroomsPlugin1 extends BukkitRunnable {
  public void run() {
    String seed = Long.toString(Bukkit.getWorld("world").getSeed());
    Iterator<Map.Entry<Integer, BackroomsLevel>> it = BackroomsPlugin.this.backlevels.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<Integer, BackroomsLevel> entry = it.next();
      int level = ((Integer)entry.getKey()).intValue();
      if (((BackroomsLevel)entry.getValue()).isWorldMain(level))
        BackroomsLevel.MakeWorld(level, seed); 
    } 
  }
}
