package com.poixson.commonmc.tools.updatechecker;

import com.poixson.commonmc.pxnCommonPlugin;
import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.commonmc.tools.plugin.xListener;
import com.poixson.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinListener extends xListener<pxnCommonPlugin> {
  protected final UpdateCheckManager manager;
  
  public PlayerJoinListener(pxnCommonPlugin plugin, UpdateCheckManager manager) {
    super((xJavaPlugin)plugin);
    this.manager = manager;
  }
  
  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerJoin(PlayerJoinEvent event) {
    if (this.manager.hasUpdate()) {
      final Player player = event.getPlayer();
      if (player.isOp() || player.hasPermission("pxncommon.updates"))
        (new BukkitRunnable() {
            public void run() {
              UpdateCheckerTask[] updates = PlayerJoinListener.this.manager.getUpdatesToPlayers();
              for (UpdateCheckerTask task : updates) {
                String msg = task.getUpdateMessage();
                if (Utils.notEmpty(msg))
                  player.sendMessage(msg); 
              } 
            }
          }).runTaskLater((Plugin)this.plugin, 10L); 
    } 
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tool\\updatechecker\PlayerJoinListener.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */