package com.poixson.backrooms.listeners;

import com.poixson.backrooms.BackroomsPlugin;
import com.poixson.backrooms.worlds.Level_000;
import com.poixson.commonmc.tools.DelayedLever;
import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.commonmc.tools.plugin.xListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Listener_006 extends xListener<BackroomsPlugin> {
  protected final Level_000 level0;
  
  public Listener_006(BackroomsPlugin plugin, Level_000 level0) {
    super((BackroomsPlugin)plugin);
    this.level0 = level0;
  }
  
  @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
  public void onBlockRedstone(BlockRedstoneEvent event) {
    Block block = event.getBlock();
    World world = block.getWorld();
    int level = ((BackroomsPlugin)this.plugin).getLevelFromWorld(world);
    if (level == 0) {
      int y = block.getY();
      int diff_y = 10;
      int lvl = this.level0.getLevelFromY(y);
      switch (lvl) {
        case 0:
          if (y == 55 && Material.LEVER
            .equals(block.getType())) {
            Block blk = block.getRelative(BlockFace.UP, 10);
            if (Material.LEVER.equals(blk.getType())) {
              doLeverTP(6, block.getLocation(), 10);
              (new DelayedLever((JavaPlugin)this.plugin, block.getLocation(), false, 10L))
                .start();
            } 
          } 
          break;
        case 6:
          if (y == 65 && Material.LEVER
            .equals(block.getType())) {
            Block blk = block.getRelative(BlockFace.DOWN, 10);
            if (Material.LEVER.equals(blk.getType())) {
              doLeverTP(0, block.getLocation(), -10);
              (new DelayedLever((JavaPlugin)this.plugin, block.getLocation(), true, 10L))
                .start();
            } 
          } 
          break;
      } 
    } 
  }
  
  protected void doLeverTP(int to_level, Location leverLoc, int y) {
    World world = leverLoc.getWorld();
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (world.equals(player.getWorld())) {
        Location playerLoc = player.getLocation();
        if (playerLoc.distance(leverLoc) < 8.0D) {
          if (to_level == 6)
            player.setInvisible(true); 
          player.teleport(playerLoc.add(0.0D, y, 0.0D));
          if (to_level != 6)
            player.setInvisible(false); 
        } 
      } 
    } 
  }
}
