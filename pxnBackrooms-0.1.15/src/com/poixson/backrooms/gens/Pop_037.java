package com.poixson.backrooms.gens;

import com.poixson.backrooms.BackroomsPop;
import com.poixson.backrooms.worlds.Level_000;
import com.poixson.commonmc.tools.LineTracer;
import com.poixson.commonmc.tools.plotter.BlockPlotter;
import com.poixson.tools.dao.Iab;
import java.util.LinkedList;
import org.bukkit.Material;
import org.bukkit.generator.LimitedRegion;

public class Pop_037 implements BackroomsPop {
  public static final int SUBFLOOR = 3;
  
  public static final double THRESH_TUNNEL = 0.95D;
  
  protected final Gen_037 gen;
  
  protected final Iab[] starting_points;
  
  public Pop_037(Level_000 level0) {
    this.gen = level0.gen_037;
    LinkedList<Iab> list = new LinkedList<>();
    int i = 0;
    while (true) {
      i += 21;
      int x = i % 16;
      int y = Math.floorDiv(i, 16);
      if (y >= 16)
        break; 
      list.addLast(new Iab(x, y));
    } 
    this.starting_points = list.<Iab>toArray(new Iab[0]);
  }
  
  public class TunnelTracer extends LineTracer {
    public final LinkedList<TunnelTracer> otherTracers;
    
    public final LimitedRegion region;
    
    public final int y;
    
    public int ends = 0;
    
    public TunnelTracer(LimitedRegion region, LinkedList<TunnelTracer> otherTracers, int x, int z) {
      super(x, z, false);
      this.region = region;
      this.otherTracers = otherTracers;
      this.y = Pop_037.this.gen.level_y + 9;
      Material type = region.getType(x, this.y, z);
      if (Material.AIR.equals(type))
        this.ok = false; 
    }
    
    public void check(Iab from) {
      checkone(from.a, this.y, from.b - 1);
      checkone(from.a, this.y, from.b + 1);
      checkone(from.a + 1, this.y, from.b);
      checkone(from.a - 1, this.y, from.b);
    }
    
    protected void checkone(int x, int y, int z) {
      if (!this.ok)
        return; 
      Iab loc = new Iab(x, z);
      if (this.checked.add(loc) && 
        isValidPoint(x, z)) {
        if (contains(loc))
          return; 
        for (TunnelTracer tracer : this.otherTracers) {
          if (tracer.contains(loc)) {
            this.ok = false;
            return;
          } 
        } 
        if (!this.region.isInRegion(x, y, z)) {
          this.ok = false;
          return;
        } 
        Material type = this.region.getType(x, y, z);
        if (Material.AIR.equals(type)) {
          this.ends++;
          return;
        } 
        if (add(loc))
          this.queued.add(loc); 
      } 
    }
    
    public boolean isValidPoint(int x, int y) {
      double value = Pop_037.this.gen.noiseTunnels.getNoiseRot(x, y, 0.25D);
      return (value > 0.95D);
    }
  }
  
  public void populate(int chunkX, int chunkZ, LimitedRegion region, LinkedList<BlockPlotter> plots) {
    Material block_wall_a = Material.matchMaterial(this.gen.block_wall_a.get());
    Material block_wall_b = Material.matchMaterial(this.gen.block_wall_b.get());
    if (block_wall_a == null)
      throw new RuntimeException("Invalid block type for level 37 WallA"); 
    if (block_wall_b == null)
      throw new RuntimeException("Invalid block type for level 37 WallB"); 
    LinkedList<TunnelTracer> tunnelTracers = new LinkedList<>();
    label96: for (Iab loc : this.starting_points) {
      for (TunnelTracer trace : tunnelTracers) {
        if (trace.contains(loc))
          continue label96; 
      } 
      int x = chunkX * 16 + loc.a;
      int y = chunkZ * 16 + loc.b;
      TunnelTracer tracer = new TunnelTracer(region, tunnelTracers, x, y);
      tracer.run();
      if (tracer.ok && tracer.ends == 2) {
        tunnelTracers.addLast(tracer);
        break;
      } 
    } 
    for (TunnelTracer tracer : tunnelTracers) {
      int x_low = Integer.MAX_VALUE;
      int z_low = Integer.MAX_VALUE;
      int x_high = Integer.MIN_VALUE;
      int z_high = Integer.MIN_VALUE;
      for (Iab loc : tracer.points) {
        if (x_low > loc.a)
          x_low = loc.a; 
        if (z_low > loc.b)
          z_low = loc.b; 
        if (x_high < loc.a)
          x_high = loc.a; 
        if (z_high < loc.b)
          z_high = loc.b; 
      } 
      if (x_low == Integer.MAX_VALUE || z_low == Integer.MAX_VALUE || x_high == Integer.MIN_VALUE || z_high == Integer.MIN_VALUE)
        continue; 
      x_low -= 3;
      x_high += 4;
      z_low -= 3;
      z_high += 4;
      int w = Math.abs(x_high - x_low);
      int d = Math.abs(z_high - z_low);
      int yy = this.gen.level_y + 7;
      for (int iz = 0; iz < d; iz++) {
        int zz = z_low + iz;
        for (int ix = 0; ix < w; ix++) {
          int xx = x_low + ix;
          int distance = Integer.MAX_VALUE;
          for (Iab loc : tracer.points) {
            int dist = ShortestDistance(xx, zz, loc.a, loc.b);
            if (distance > dist)
              distance = dist; 
            if (distance == 0)
              break; 
          } 
          if (region.isInRegion(xx, 0, zz))
            if (distance < 2) {
              if (block_wall_b.equals(region.getType(xx, yy, zz)))
                region.setType(xx, yy, zz, block_wall_a); 
              for (int iy = 1; iy < 5; iy++) {
                if (block_wall_b.equals(region.getType(xx, yy + iy, zz)))
                  region.setType(xx, yy + iy, zz, Material.AIR); 
              } 
              if (block_wall_b.equals(region.getType(xx, yy + 5, zz)))
                region.setType(xx, yy + 5, zz, block_wall_a); 
            } else if (distance == 2) {
              if (block_wall_b.equals(region.getType(xx, yy, zz)))
                region.setType(xx, yy, zz, block_wall_a); 
              for (int iy = 1; iy < 4; iy++) {
                if (block_wall_b.equals(region.getType(xx, yy + iy, zz)))
                  region.setType(xx, yy + iy, zz, Material.AIR); 
              } 
              if (block_wall_b.equals(region.getType(xx, yy + 4, zz)))
                region.setType(xx, yy + 4, zz, block_wall_a); 
            } else if (distance == 3) {
              for (int iy = 0; iy < 4; iy++) {
                if (block_wall_b.equals(region.getType(xx, yy + iy, zz)))
                  region.setType(xx, yy + iy, zz, block_wall_a); 
              } 
            }  
        } 
      } 
    } 
  }
  
  public static int ShortestDistance(int x1, int z1, int x2, int z2) {
    return Math.max(
        Math.min(Math.abs(x1 - x2), Math.abs(x2 - x1)), 
        Math.min(Math.abs(z1 - z2), Math.abs(z2 - z1)));
  }
}
