package com.poixson.backrooms.tasks;

import com.poixson.backrooms.BackroomsPlugin;
import com.poixson.tools.abstractions.xStartStop;
import com.poixson.tools.xTime;
import com.poixson.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskHourly extends BukkitRunnable implements xStartStop {
  protected final BackroomsPlugin plugin;
  
  protected final long updateTicks = (new xTime("1m")).ticks(50L);
  
  protected final long updatePeriod = xTime.ParseToLong("1h");
  
  protected final long updateGrace = xTime.ParseToLong("3m");
  
  protected final long maxGrace = xTime.ParseToLong("10m");
  
  protected long lastUpdated = 0L;
  
  protected long lastUsed = 0L;
  
  public TaskHourly(BackroomsPlugin plugin) {
    this.plugin = plugin;
  }
  
  public void start() {
    runTaskTimer((Plugin)this.plugin, this.updateTicks * 2L, this.updateTicks);
    long time = Utils.GetMS();
    this.lastUpdated = time;
    this.lastUsed = time;
  }
  
  public void stop() {
    try {
      cancel();
    } catch (IllegalStateException illegalStateException) {}
  }
  
  public void run() {
    if (Bukkit.getOnlinePlayers().size() > 0) {
      long time = Utils.GetMS();
      long sinceUpdated = time - this.lastUpdated;
      if (sinceUpdated >= this.updatePeriod) {
        long sinceReset = time - this.lastUsed;
        if (sinceReset <= this.updateGrace && 
          sinceUpdated <= this.updatePeriod + this.maxGrace)
          return; 
        this.lastUpdated = time;
        update();
      } 
    } 
  }
  
  protected void update() {
    this.plugin.getTeleportManager()
      .flush();
    this.plugin.getQuoteAnnouncer()
      .announce();
  }
  
  public void markUsed() {
    markUsed(Utils.GetMS());
  }
  
  public void markUsed(long time) {
    this.lastUsed = time;
  }
}
