package com.poixson.commonmc.tools;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class DelayedLever extends BukkitRunnable {
  protected final JavaPlugin plugin;
  
  protected final Location loc;
  
  protected final boolean powered;
  
  protected final long delay;
  
  public DelayedLever(JavaPlugin plugin, Location loc, boolean powered, long delay) {
    this.plugin = plugin;
    this.loc = loc;
    this.powered = powered;
    this.delay = delay;
  }
  
  public void start() {
    runTaskLater((Plugin)this.plugin, this.delay);
  }
  
  public void run() {
    Block block = this.loc.getBlock();
    BlockData blockdata = block.getBlockData();
    if (blockdata instanceof Powerable) {
      Powerable power = (Powerable)blockdata;
      power.setPowered(this.powered);
      block.setBlockData((BlockData)power);
    } 
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\DelayedLever.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */