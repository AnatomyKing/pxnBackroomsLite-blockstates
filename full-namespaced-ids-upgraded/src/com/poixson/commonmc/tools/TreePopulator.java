package com.poixson.commonmc.tools;

import com.poixson.utils.FastNoiseLiteD;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;

public class TreePopulator extends BlockPopulator {
  protected final FastNoiseLiteD noise;
  
  protected final int chunkY;
  
  protected final Material trunk;
  
  protected final Material leaves;
  
  public TreePopulator(FastNoiseLiteD noise, int chunkY, Material trunk, Material leaves) {
    this.noise = noise;
    this.chunkY = chunkY;
    this.trunk = trunk;
    this.leaves = leaves;
  }
  
  public void populate(WorldInfo world, Random rnd, int chunkX, int chunkZ, LimitedRegion region) {
    for (int iz = 0; iz < 16; iz++) {
      for (int ix = 0; ix < 16; ix++) {
        int x = ix + chunkX * 16;
        int z = iz + chunkZ * 16;
        if (isTree(x, z))
          build(x, z, region); 
      } 
    } 
  }
  
  public boolean isTree(int x, int z) {
    double current = this.noise.getNoise(x, z);
    for (int xx = -1; xx < 2; xx++) {
      for (int zz = -1; zz < 2; zz++) {
        if ((xx != 0 || zz != 0) && 
          this.noise.getNoise((x + xx), (z + zz)) > current)
          return false; 
      } 
    } 
    return true;
  }
  
  public int getTreeSize(int x, int z) {
    return (int)Math.abs(Math.round(this.noise.getNoise(x * 555.0D, z * 555.0D) * 12.0D));
  }
  
  public void build(int x, int z, LimitedRegion region) {
    int size = getTreeSize(x, z);
    int size_half = size / 2 + 3;
    int size_tree = size + 6;
    int y = 0;
    for (int yy = 0; yy < 10; yy++) {
      if (Material.AIR.equals(region.getType(x, this.chunkY + yy, z))) {
        y = this.chunkY + yy;
        break;
      } 
    } 
    if (y == 0)
      return; 
    for (int iz = 0; iz < size_tree; iz++) {
      for (int i = 0; i < size_tree; i++) {
        for (int ix = 0; ix < size_tree; ix++) {
          double ax = 1.0D - Math.abs(ix - size_half) / size_half;
          double ay = 1.0D - Math.abs(i - size_half) / size_half;
          double az = 1.0D - Math.abs(iz - size_half) / size_half;
          double nx = (ix + x * 55);
          double nz = (iz + z * 55);
          double ny = (i * 211);
          double value = (ax + ay + az) / 2.5D - Math.pow(this.noise.getNoise(nx, ny, nz), 2.0D);
          if (value > 0.5D) {
            int xx = x + ix - size_half;
            int zz = z + iz - size_half;
            int j = y + i + 3;
            if (Material.AIR.equals(region.getType(xx, j, zz))) {
              region.setType(xx, j, zz, this.leaves);
              BlockData block = region.getBlockData(xx, j, zz);
              if (block instanceof Leaves) {
                Leaves leaves = (Leaves)block;
                leaves.setPersistent(true);
                region.setBlockData(xx, j, zz, block);
              } 
            } 
          } 
        } 
      } 
    } 
    for (int iy = 0; iy < size_tree; iy++)
      region.setType(x, y + iy, z, this.trunk); 
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\TreePopulator.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */