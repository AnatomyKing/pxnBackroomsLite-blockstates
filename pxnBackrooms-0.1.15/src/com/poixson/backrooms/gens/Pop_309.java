package com.poixson.backrooms.gens;

import com.poixson.backrooms.BackroomsPlugin;
import com.poixson.backrooms.BackroomsPop;
import com.poixson.backrooms.worlds.Level_000;
import com.poixson.commonmc.tools.plotter.BlockPlotter;
import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.commonmc.tools.scripts.CraftScript;
import com.poixson.commonmc.tools.scripts.loader.ScriptLoader;
import com.poixson.commonmc.tools.scripts.loader.ScriptLoader_File;
import java.util.LinkedList;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Fence;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.plugin.java.JavaPlugin;

public class Pop_309 implements BackroomsPop {
  public static final double FENCE_NOISE_STRENGTH = 2.0D;
  
  public static final double FENCE_RADIUS = 65.0D;
  
  public static final double FENCE_THICKNESS = 1.3D;
  
  protected final BackroomsPlugin plugin;
  
  protected final Gen_309 gen;
  
  protected final Pop_309_Trees treePop;
  
  public Pop_309(Level_000 level0) {
    this.plugin = level0.getPlugin();
    this.gen = level0.gen_309;
    this.treePop = new Pop_309_Trees(this.gen);
  }
  
  public void populate(int chunkX, int chunkZ, LimitedRegion region, LinkedList<BlockPlotter> plots) {
    this.treePop.populate(null, null, chunkX, chunkZ, region);
    if (chunkX == 0 && chunkZ == 0) {
      populate0x0(region);
    } else if (Math.abs(chunkX) < 8 && 
      Math.abs(chunkZ) < 8) {
      for (int iz = 0; iz < 16; iz++) {
        int zz = chunkZ * 16 + iz;
        for (int ix = 0; ix < 16; ix++) {
          int xx = chunkX * 16 + ix;
          double distance = this.gen.getCenterClearingDistance(xx, zz, 2.0D);
          if (distance >= 65.0D && distance <= 66.3D) {
            boolean found = false;
            int sy = this.gen.level_y;
            for (int i = 0; i < 10; i++) {
              Material type = region.getType(xx, sy + i, zz);
              if (Material.AIR.equals(type)) {
                found = true;
                sy += i;
                break;
              } 
            } 
            if (found) {
              int path_x = this.gen.getPathX(zz);
              if (zz <= 0 || xx >= path_x + 5 || xx <= path_x - 5) {
                for (int iy = 0; iy < 5; iy++) {
                  region.setType(xx, sy + iy, zz, Material.IRON_BARS);
                  Fence fence = (Fence)region.getBlockData(xx, sy + iy, zz);
                  synchronized (fence) {
                    fence.setFace(BlockFace.NORTH, true);
                    fence.setFace(BlockFace.SOUTH, true);
                    fence.setFace(BlockFace.EAST, true);
                    fence.setFace(BlockFace.WEST, true);
                    region.setBlockData(xx, sy + iy, zz, (BlockData)fence);
                  } 
                } 
                region.setType(xx, sy + 5, zz, Material.CUT_COPPER_SLAB);
              } 
            } 
          } 
        } 
      } 
    } 
  }
  
  public void populate0x0(LimitedRegion region) {
    int y = Integer.MIN_VALUE;
    for (int i = 0; i < 10; i++) {
      if (Material.AIR.equals(region.getType(0, this.gen.level_y + i, 31))) {
        y = this.gen.level_y + i;
        break;
      } 
    } 
    if (y == Integer.MIN_VALUE) {
      xJavaPlugin.LOG.warning("Failed to generate level 309 building; unknown y point.");
      return;
    } 
    ScriptLoader_File scriptLoader_File = new ScriptLoader_File((JavaPlugin)this.plugin, "scripts", "scripts", "backrooms-radiostation.js");
    CraftScript script = new CraftScript((ScriptLoader)scriptLoader_File, false);
    script.setVariable("region", region);
    script.setVariable("surface_y", Integer.valueOf(y));
    script.setVariable("enable_ceiling", Boolean.valueOf(true));
    script.setVariable("path_width", Integer.valueOf(this.gen.path_width.get()));
    script.setVariable("path_start_x", Integer.valueOf(14));
    script.setVariable("path_start_z", Integer.valueOf(32));
    script.run();
  }
}
