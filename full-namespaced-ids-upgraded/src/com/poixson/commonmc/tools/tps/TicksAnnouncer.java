package com.poixson.commonmc.tools.tps;

import com.poixson.commonmc.pxnCommonPlugin;
import com.poixson.tools.abstractions.xStartStop;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TicksAnnouncer extends BukkitRunnable implements xStartStop {
  protected static final ConcurrentHashMap<UUID, TicksAnnouncer> instances = new ConcurrentHashMap<>();
  
  protected final pxnCommonPlugin plugin;
  
  protected final Player player;
  
  public static TicksAnnouncer Start(pxnCommonPlugin plugin, Player player) {
    UUID uuid = player.getUniqueId();
    TicksAnnouncer announcer = instances.get(uuid);
    if (announcer != null)
      return announcer; 
    announcer = new TicksAnnouncer(plugin, player);
    TicksAnnouncer existing = instances.putIfAbsent(uuid, announcer);
    if (existing == null) {
      announcer.start();
      return announcer;
    } 
    return existing;
  }
  
  public static boolean Stop(Player player) {
    TicksAnnouncer announcer = instances.remove(player.getUniqueId());
    if (announcer != null) {
      announcer.stop();
      return true;
    } 
    return false;
  }
  
  public static TicksAnnouncer Toggle(pxnCommonPlugin plugin, Player player) {
    if (Stop(player))
      return null; 
    return Start(plugin, player);
  }
  
  public TicksAnnouncer(pxnCommonPlugin plugin, Player player) {
    this.plugin = plugin;
    this.player = player;
  }
  
  public void start() {
    runTaskTimer((Plugin)this.plugin, 20L, 20L);
  }
  
  public void stop() {
    try {
      cancel();
    } catch (IllegalStateException illegalStateException) {}
  }
  
  public void run() {
    TicksPerSecond manager = pxnCommonPlugin.GetTicksManager();
    double[] tps = manager.getTPS();
    TicksPerSecond.DisplayTPS(this.player.getUniqueId(), tps);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\tps\TicksAnnouncer.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */