package com.poixson.commonmc.tools.plotter;

import com.poixson.commonmc.utils.LocationUtils;
import com.poixson.tools.dao.Iabc;
import com.poixson.tools.dao.Iabcd;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;

public class BlockPlotter implements Runnable {
  public final BlockPlacer placer;
  
  public final BlockMatrix matrix;
  
  public String axis;
  
  public int x;
  
  public int y;
  
  public int z;
  
  public int w;
  
  public int h;
  
  public int d;
  
  public BlockFace rotation = BlockFace.SOUTH;
  
  protected final Map<Character, BlockData> types = new HashMap<>();
  
  protected final Set<Iabc> autoface = new HashSet<>();
  
  public BlockPlotter(BlockPlacer placer, String axis, int[] locs, int[] sizes) {
    this.placer = placer;
    this.matrix = new BlockMatrix(axis, locs, sizes);
    this.axis = axis;
    this.x = this.matrix.getX();
    this.y = this.matrix.getY();
    this.z = this.matrix.getZ();
    this.w = this.matrix.getW();
    this.h = this.matrix.getH();
    this.d = this.matrix.getD();
  }
  
  public BlockPlotter(BlockPlacer placer, BlockMatrix matrix) {
    this.placer = placer;
    this.matrix = matrix;
    this.axis = matrix.getAxis();
    this.x = matrix.getX();
    this.y = matrix.getY();
    this.z = matrix.getZ();
    this.w = matrix.getW();
    this.h = matrix.getH();
    this.d = matrix.getD();
  }
  
  public BlockMatrix getMatrix() {
    return this.matrix;
  }
  
  public StringBuilder[][] getMatrix3D() {
    BlockMatrix matrix = getMatrix();
    LinkedList<StringBuilder[]> list = (LinkedList)new LinkedList<>();
    for (BlockMatrix mtx : matrix.array) {
      LinkedList<StringBuilder> list2 = new LinkedList<>();
      for (BlockMatrix mx : mtx.array)
        list2.add(mx.row); 
      list.add(list2.<StringBuilder>toArray(new StringBuilder[0]));
    } 
    return list.<StringBuilder[]>toArray(new StringBuilder[0][0]);
  }
  
  public StringBuilder[] getMatrix2D() {
    BlockMatrix matrix = getMatrix();
    LinkedList<StringBuilder> list = new LinkedList<>();
    for (BlockMatrix mtx : matrix.array)
      list.add(mtx.row); 
    return list.<StringBuilder>toArray(new StringBuilder[0]);
  }
  
  public StringBuilder getMatrix1D() {
    BlockMatrix matrix = getMatrix();
    return matrix.row;
  }
  
  public void run() {
    run(this.matrix, 0, 0, 0);
  }
  
  protected void run(BlockMatrix matrix, int x, int y, int z) {
    Iabc add = LocationUtils.AxToIxyz(matrix.ax);
    if (matrix.row == null) {
      int len = matrix.array.length;
      for (int i = 0; i < len; i++) {
        int xx = add.a * i + x;
        int yy = add.b * i + y;
        int zz = add.c * i + z;
        run(matrix.array[i], xx, yy, zz);
      } 
    } else {
      String row = matrix.row.toString();
      int len = row.length();
      for (int i = 0; i < len; i++) {
        char chr = row.charAt(i);
        if (chr != '\000' && chr != ' ') {
          int xx = add.a * i + x;
          int yy = add.b * i + y;
          int zz = add.c * i + z;
          BlockData type = this.types.get(Character.valueOf(chr));
          if (type == null)
            throw new RuntimeException("Unknown material: " + Character.toString(chr)); 
          setBlock(xx, yy, zz, type);
        } 
      } 
    } 
  }
  
  public BlockPlotter type(char chr, String type) {
    return type(chr, Bukkit.createBlockData(type));
  }
  
  public BlockPlotter type(char chr, Material type) {
    return type(chr, Bukkit.createBlockData(type));
  }
  
  public BlockPlotter type(char chr, BlockData block) {
    this.types.put(Character.valueOf(chr), block);
    return this;
  }
  
  public void setBlock(int x, int y, int z, BlockData type) {
    Iabcd loc = LocationUtils.Rotate(new Iabcd(x, z, this.w, this.d), this.rotation);
    int xx = this.x + loc.a;
    int zz = this.z + loc.b;
    int yy = this.y + y;
    this.placer.setBlock(xx, yy, zz, type);
  }
  
  public BlockData getBlock(int x, int y, int z) {
    Iabcd loc = LocationUtils.Rotate(new Iabcd(x, z, this.w, this.d), this.rotation);
    int xx = this.x + loc.a;
    int zz = this.z + loc.b;
    int yy = this.y + y;
    return this.placer.getBlock(xx, yy, zz);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\plotter\BlockPlotter.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */