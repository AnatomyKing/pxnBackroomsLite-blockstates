package com.poixson.commonmc.tools.updatechecker;

import com.poixson.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

class null extends BukkitRunnable {
  public void run() {
    UpdateCheckerTask[] updates = PlayerJoinListener.this.manager.getUpdatesToPlayers();
    for (UpdateCheckerTask task : updates) {
      String msg = task.getUpdateMessage();
      if (Utils.notEmpty(msg))
        player.sendMessage(msg); 
    } 
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tool\\updatechecker\PlayerJoinListener$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */