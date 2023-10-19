package com.poixson.commonmc.tools;

import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class DelayedChestFiller extends BukkitRunnable {
  public static final long DEFAULT_DELAY = 20L;
  
  protected static CopyOnWriteArraySet<DelayedChestFiller> fillers = new CopyOnWriteArraySet<>();
  
  protected static final AtomicBoolean stopping = new AtomicBoolean(false);
  
  protected final JavaPlugin plugin;
  
  protected final Location loc;
  
  protected final String worldName;
  
  protected final int x;
  
  protected final int y;
  
  protected final int z;
  
  protected final AtomicBoolean done = new AtomicBoolean(false);
  
  public DelayedChestFiller(JavaPlugin plugin, String worldName, int x, int y, int z) {
    this.plugin = plugin;
    this.loc = null;
    this.worldName = worldName;
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public DelayedChestFiller(JavaPlugin plugin, Location loc) {
    this.plugin = plugin;
    this.loc = loc;
    this.worldName = null;
    this.x = 0;
    this.y = 0;
    this.z = 0;
  }
  
  public void start() {
    start(20L);
  }
  
  public void start(long delay) {
    if (stopping.get())
      return; 
    fillers.add(this);
    runTaskLater((Plugin)this.plugin, delay);
  }
  
  public static void stop() {
    stopping.set(true);
    xJavaPlugin.LOG.info("[pxnCommon] Finishing chest population..");
    int count = 0;
    while (!fillers.isEmpty()) {
      HashSet<DelayedChestFiller> remove = new HashSet<>();
      for (DelayedChestFiller filler : fillers) {
        remove.add(filler);
        try {
          filler.cancel();
        } catch (IllegalStateException illegalStateException) {}
        if (!filler.done.get()) {
          filler.run();
          count++;
        } 
      } 
      for (DelayedChestFiller filler : remove)
        fillers.remove(filler); 
    } 
    if (count > 0)
      xJavaPlugin.LOG.info("[pxnCommon] Finished populating chests: " + Integer.toString(count)); 
  }
  
  public Location getLocation() {
    return getBlock().getLocation();
  }
  
  public Block getBlock() {
    if (this.loc == null) {
      World world = Bukkit.getWorld(this.worldName);
      if (stopping.get() && 
        !world.getChunkAt(this.x, this.z).isLoaded())
        return null; 
      return world.getBlockAt(this.x, this.y, this.z);
    } 
    return this.loc.getBlock();
  }
  
  public void run() {
    if (!this.done.compareAndSet(false, true))
      return; 
    Block block = getBlock();
    if (block != null) {
      BlockState state = block.getState();
      if (state != null && state instanceof Container) {
        Container chest = (Container)state;
        fill(chest.getInventory());
      } 
    } 
  }
  
  public abstract void fill(Inventory paramInventory);
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\DelayedChestFiller.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */