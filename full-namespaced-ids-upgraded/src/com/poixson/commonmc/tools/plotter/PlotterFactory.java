package com.poixson.commonmc.tools.plotter;

import com.poixson.commonmc.utils.BlockMatrixUtils;
import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.utils.Utils;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.LimitedRegion;

public class PlotterFactory {
  public BlockPlacer placer = null;
  
  public String axis = null;
  
  public BlockFace rotation = null;
  
  public int x = 0;
  
  public int y = 0;
  
  public int z = 0;
  
  public int w = Integer.MIN_VALUE;
  
  public int h = Integer.MIN_VALUE;
  
  public int d = Integer.MIN_VALUE;
  
  public BlockPlotter build() {
    if (this.placer == null)
      throw new RequiredArgumentException("placer"); 
    if (Utils.isEmpty(this.axis))
      throw new RequiredArgumentException("axis"); 
    if (this.axis.contains("x") || this.axis.contains("X") || this.axis
      .contains("e") || this.axis.contains("w")) {
      if (this.x == Integer.MIN_VALUE)
        throw new RequiredArgumentException("x"); 
      if (this.w == Integer.MIN_VALUE)
        throw new RequiredArgumentException("w"); 
    } 
    if (this.axis.contains("y") || this.axis.contains("Y") || this.axis
      .contains("u") || this.axis.contains("d")) {
      if (this.y == Integer.MIN_VALUE)
        throw new RequiredArgumentException("y"); 
      if (this.h == Integer.MIN_VALUE)
        throw new RequiredArgumentException("h"); 
    } 
    if (this.axis.contains("z") || this.axis.contains("Z") || this.axis
      .contains("n") || this.axis.contains("s")) {
      if (this.z == Integer.MIN_VALUE)
        throw new RequiredArgumentException("z"); 
      if (this.d == Integer.MIN_VALUE)
        throw new RequiredArgumentException("d"); 
    } 
    int[] locs = BlockMatrixUtils.LocsToArray(this.axis, this.x, this.y, this.z);
    int[] sizes = BlockMatrixUtils.SizesToArray(this.axis, this.w, this.h, this.d);
    BlockMatrix matrix = new BlockMatrix(this.axis, locs, sizes);
    BlockPlotter plot = new BlockPlotter(this.placer, matrix);
    if (this.rotation != null)
      plot.rotation = this.rotation; 
    return plot;
  }
  
  public PlotterFactory placer(BlockPlacer placer) {
    this.placer = placer;
    return this;
  }
  
  public PlotterFactory placer(World world) {
    this.placer = new BlockPlacer(world);
    return this;
  }
  
  public PlotterFactory placer(ChunkGenerator.ChunkData chunk) {
    this.placer = new BlockPlacer(chunk);
    return this;
  }
  
  public PlotterFactory placer(LimitedRegion region) {
    this.placer = new BlockPlacer(region);
    return this;
  }
  
  public PlotterFactory placer(BlockPlacer_WorldEdit worldedit) {
    this.placer = new BlockPlacer(worldedit);
    return this;
  }
  
  public PlotterFactory axis(String axis) {
    this.axis = axis;
    return this;
  }
  
  public PlotterFactory rotate(BlockFace rotation) {
    this.rotation = rotation;
    return this;
  }
  
  public PlotterFactory xyz(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
    return this;
  }
  
  public PlotterFactory xy(int x, int y) {
    this.x = x;
    this.y = y;
    return this;
  }
  
  public PlotterFactory xz(int x, int z) {
    this.x = x;
    this.z = z;
    return this;
  }
  
  public PlotterFactory x(int x) {
    this.x = x;
    return this;
  }
  
  public PlotterFactory y(int y) {
    this.y = y;
    return this;
  }
  
  public PlotterFactory z(int z) {
    this.z = z;
    return this;
  }
  
  public PlotterFactory whd(int w, int h, int d) {
    this.w = w;
    this.h = h;
    this.d = d;
    return this;
  }
  
  public PlotterFactory wh(int w, int h) {
    this.w = w;
    this.h = h;
    return this;
  }
  
  public PlotterFactory wd(int w, int d) {
    this.w = w;
    this.d = d;
    return this;
  }
  
  public PlotterFactory w(int w) {
    this.w = w;
    return this;
  }
  
  public PlotterFactory h(int h) {
    this.h = h;
    return this;
  }
  
  public PlotterFactory d(int d) {
    this.d = d;
    return this;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\plotter\PlotterFactory.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */