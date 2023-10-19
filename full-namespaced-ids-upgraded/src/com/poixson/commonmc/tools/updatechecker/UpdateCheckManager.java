package com.poixson.commonmc.tools.updatechecker;

import com.poixson.commonmc.pxnCommonPlugin;
import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.tools.abstractions.xStartStop;
import com.poixson.tools.xTime;
import com.poixson.utils.ThreadUtils;
import com.poixson.utils.Utils;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateCheckManager extends BukkitRunnable implements xStartStop {
  protected final pxnCommonPlugin plugin;
  
  protected final long delay = (new xTime("5s")).ticks(50L);
  
  protected final long loop = (new xTime("5m")).ticks(50L);
  
  protected final long period = (new xTime("12h")).ms();
  
  protected final AtomicLong lastCheck = new AtomicLong(0L);
  
  protected final ConcurrentHashMap<Integer, UpdateCheckerTask> checkers = new ConcurrentHashMap<>();
  
  protected final PlayerJoinListener listenerPlayerJoin;
  
  public UpdateCheckManager(pxnCommonPlugin plugin) {
    this.plugin = plugin;
    this.listenerPlayerJoin = new PlayerJoinListener(plugin, this);
  }
  
  public void start() {
    runTaskTimerAsynchronously((Plugin)this.plugin, this.delay, this.loop);
    this.listenerPlayerJoin.register();
  }
  
  public void startLater() {
    (new BukkitRunnable() {
        public void run() {
          UpdateCheckManager.this.start();
        }
      }).runTaskLater((Plugin)this.plugin, 10L);
  }
  
  public void stop() {
    try {
      cancel();
    } catch (IllegalStateException illegalStateException) {}
    this.listenerPlayerJoin.unregister();
  }
  
  public void run() {
    long now = Utils.GetMS();
    long last = this.lastCheck.get();
    if (now - last >= this.period) {
      this.lastCheck.set(now);
      xJavaPlugin.LOG.info(String.format("%sFetching latest versions for %d plugins..", new Object[] { "[pxnCommon] ", 
              
              Integer.valueOf(this.checkers.size()) }));
      boolean available = false;
      Iterator<UpdateCheckerTask> it = this.checkers.values().iterator();
      while (it.hasNext()) {
        UpdateCheckerTask dao = it.next();
        dao.run();
        if (dao.hasUpdate())
          available = true; 
        ThreadUtils.Sleep("1s");
      } 
      if (!available)
        xJavaPlugin.LOG.info("[pxnCommon] You have the latest versions"); 
    } 
  }
  
  public static UpdateCheckerTask Register(xJavaPlugin plugin) {
    int spigot_id = plugin.getSpigotPluginID();
    String version = plugin.getPluginVersion();
    return Register((JavaPlugin)plugin, spigot_id, version);
  }
  
  public static UpdateCheckerTask Register(JavaPlugin plugin, int spigot_id, String version) {
    if (spigot_id <= 0)
      return null; 
    UpdateCheckManager manager = (UpdateCheckManager)Bukkit.getServicesManager().load(UpdateCheckManager.class);
    if (manager == null)
      throw new RuntimeException("UpdateCheckManager is not available"); 
    return manager.addPlugin(plugin, spigot_id, version);
  }
  
  public UpdateCheckerTask addPlugin(JavaPlugin plugin, int spigot_id, String plugin_version) {
    if (spigot_id <= 0) {
      xJavaPlugin.LOG.warning(String.format("%sPlugin ID not set in: %s", new Object[] { "[pxnCommon] ", plugin
              
              .getName() }));
      return null;
    } 
    UpdateCheckerTask dao = new UpdateCheckerTask(plugin, spigot_id, plugin_version);
    this.checkers.put(Integer.valueOf(spigot_id), dao);
    return dao;
  }
  
  public static boolean Unregister(xJavaPlugin plugin) {
    int spigot_id = plugin.getSpigotPluginID();
    return Unregister(spigot_id);
  }
  
  public static boolean Unregister(int spigot_id) {
    if (spigot_id <= 0)
      return false; 
    UpdateCheckManager manager = (UpdateCheckManager)Bukkit.getServicesManager().load(UpdateCheckManager.class);
    if (manager == null)
      throw new RuntimeException("UpdateCheckManager is not available"); 
    return manager.removePlugin(spigot_id);
  }
  
  public boolean removePlugin(int spigot_id) {
    UpdateCheckerTask dao = this.checkers.remove(Integer.valueOf(spigot_id));
    return (dao != null);
  }
  
  public boolean hasUpdate() {
    Iterator<UpdateCheckerTask> it = this.checkers.values().iterator();
    while (it.hasNext()) {
      UpdateCheckerTask dao = it.next();
      if (dao.hasUpdate())
        return true; 
    } 
    return false;
  }
  
  public UpdateCheckerTask[] getUpdates() {
    HashSet<UpdateCheckerTask> updates = new HashSet<>();
    Iterator<UpdateCheckerTask> it = this.checkers.values().iterator();
    while (it.hasNext()) {
      UpdateCheckerTask dao = it.next();
      if (dao.hasUpdate())
        updates.add(dao); 
    } 
    return updates.<UpdateCheckerTask>toArray(new UpdateCheckerTask[0]);
  }
  
  public UpdateCheckerTask[] getUpdatesToPlayers() {
    HashSet<UpdateCheckerTask> updates = new HashSet<>();
    Iterator<UpdateCheckerTask> it = this.checkers.values().iterator();
    while (it.hasNext()) {
      UpdateCheckerTask dao = it.next();
      if (dao.hasUpdate() && dao.isToPlayers())
        updates.add(dao); 
    } 
    return updates.<UpdateCheckerTask>toArray(new UpdateCheckerTask[0]);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tool\\updatechecker\UpdateCheckManager.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */