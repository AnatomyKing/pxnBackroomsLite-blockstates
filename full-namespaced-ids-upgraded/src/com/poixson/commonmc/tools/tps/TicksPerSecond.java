package com.poixson.commonmc.tools.tps;

import com.poixson.commonmc.pxnCommonPlugin;
import com.poixson.utils.Utils;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TicksPerSecond extends BukkitRunnable {
  protected final pxnCommonPlugin plugin;
  
  protected final LinkedList<Double> history = new LinkedList<>();
  
  protected final AtomicReference<double[]> averages = (AtomicReference)new AtomicReference<>();
  
  protected final AtomicLong ticks = new AtomicLong(0L);
  
  protected long last = 0L;
  
  public TicksPerSecond(pxnCommonPlugin plugin) {
    this.plugin = plugin;
  }
  
  public boolean start() {
    try {
      runTaskTimer((Plugin)this.plugin, 100L, 1L);
      synchronized (this.history) {
        this.history.clear();
        this.ticks.set(0L);
        this.last = 0L;
      } 
      return true;
    } catch (IllegalStateException illegalStateException) {
      return false;
    } 
  }
  
  public boolean stop() {
    try {
      cancel();
      return true;
    } catch (IllegalStateException illegalStateException) {
      return false;
    } 
  }
  
  public void run() {
    long time = Utils.GetMS();
    this.ticks.incrementAndGet();
    long next = Math.floorDiv(this.last, 1000L) * 1000L + 999L;
    if (time > next) {
      long last = this.last;
      this.last = time;
      if (last > 0L) {
        long since = time - last;
        double tps = since / 50.0D;
        if (this.history.size() == 0)
          for (int i = 0; i < 299; i++)
            this.history.push(Double.valueOf(20.0D));  
        this.history.push(Double.valueOf(tps));
        while (this.history.size() > 300)
          this.history.removeLast(); 
        this.averages.set(null);
      } 
    } 
  }
  
  public double[] getTPS() {
    double[] result = this.averages.get();
    if (result != null)
      return result; 
    List<Double> list = Arrays.asList(this.history.<Double>toArray(new Double[0]));
    double total_10s = 0.0D;
    int count_10s = 0;
    double total_1m = 0.0D;
    int count_1m = 0;
    double total_5m = 0.0D;
    int count_5m = 0;
    if (list.size() > 0)
      for (int i = list.size() - 1; i >= 0; i--) {
        double value = ((Double)list.get(i)).doubleValue();
        total_5m += value;
        count_5m++;
        if (i < 10) {
          total_10s += value;
          count_10s++;
        } 
        if (i < 60) {
          total_1m += value;
          count_1m++;
        } 
      }  
    double[] arrayOfDouble1 = { total_10s / count_10s, total_1m / count_1m, total_5m / count_5m };
    this.averages.set(arrayOfDouble1);
    return arrayOfDouble1;
  }
  
  public long getTicks() {
    return this.ticks.get();
  }
  
  public static void DisplayTPS(UUID uuid) {
    DisplayTPS((uuid == null) ? null : Bukkit.getPlayer(uuid));
  }
  
  public static void DisplayTPS(UUID uuid, double[] tps) {
    DisplayTPS((uuid == null) ? null : Bukkit.getPlayer(uuid));
  }
  
  public static void DisplayTPS(Player player) {
    TicksPerSecond manager = pxnCommonPlugin.GetTicksManager();
    double[] tps = manager.getTPS();
    DisplayTPS(player, tps);
  }
  
  public static void DisplayTPS(Player player, double[] tps) {
    String msg = String.format("TPS: %.1f/%.1f/%.1f", new Object[] { Double.valueOf(tps[0]), 
          Double.valueOf(tps[1]), 
          Double.valueOf(tps[2]) });
    if (player == null) {
      Bukkit.getConsoleSender().sendMessage(msg);
    } else {
      player.sendMessage(msg);
    } 
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\tps\TicksPerSecond.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */