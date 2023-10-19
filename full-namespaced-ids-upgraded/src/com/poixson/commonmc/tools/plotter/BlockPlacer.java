package com.poixson.commonmc.tools.plotter;

import com.poixson.exceptions.InvalidValueException;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.LimitedRegion;

public class BlockPlacer {
  public final World world;
  
  public final ChunkGenerator.ChunkData chunk;
  
  public final LimitedRegion region;
  
  public final BlockPlacer_WorldEdit worldedit;
  
  public BlockPlacer(World world) {
    this(world, null, null, null);
  }
  
  public BlockPlacer(ChunkGenerator.ChunkData chunk) {
    this(null, chunk, null, null);
  }
  
  public BlockPlacer(LimitedRegion region) {
    this(null, null, region, null);
  }
  
  public BlockPlacer(BlockPlacer_WorldEdit worldedit) {
    this(null, null, null, worldedit);
  }
  
  public BlockPlacer(World world, ChunkGenerator.ChunkData chunk, LimitedRegion region, BlockPlacer_WorldEdit worldedit) {
    this.world = world;
    this.chunk = chunk;
    this.region = region;
    this.worldedit = worldedit;
  }
  
  public BlockData getBlock(int x, int y, int z) {
    if (this.world != null)
      return this.world.getBlockData(x, y, z); 
    if (this.chunk != null)
      return this.chunk.getBlockData(x, y, z); 
    if (this.worldedit != null)
      return this.worldedit.getBlock(x, y, z); 
    if (this.region != null) {
      if (this.region.isInRegion(x, y, z))
        return this.region.getBlockData(x, y, z); 
      return null;
    } 
    throw new InvalidValueException("world/chunk/region");
  }
  
  public void setBlock(int x, int y, int z, BlockData type) {
    if (this.world != null) {
      this.world.setBlockData(x, y, z, type);
    } else if (this.chunk != null) {
      this.chunk.setBlock(x, y, z, type);
    } else if (this.worldedit != null) {
      this.worldedit.setBlock(x, y, z, type);
    } else if (this.region != null) {
      if (this.region.isInRegion(x, y, z))
        this.region.setBlockData(x, y, z, type); 
    } else {
      throw new InvalidValueException("world/chunk/region");
    } 
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\plotter\BlockPlacer.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */