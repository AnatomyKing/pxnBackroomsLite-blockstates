package com.poixson.commonmc.tools.plotter;

import java.util.Arrays;

public class BlockMatrix {
  public final BlockMatrix[] array;
  
  public final StringBuilder row;
  
  public final char ax;
  
  public final int loc;
  
  public final int size;
  
  public BlockMatrix(String axis, int[] locs, int[] sizes) {
    if (axis.length() != locs.length)
      throw new RuntimeException("axis length doesn't match locs length"); 
    if (axis.length() != sizes.length)
      throw new RuntimeException("axis length doesn't match sizes length"); 
    this.loc = locs[0];
    this.size = sizes[0];
    if (sizes.length == 1) {
      this.array = null;
      this.row = new StringBuilder();
      this.ax = axis.charAt(0);
    } else {
      this.row = null;
      int[] locsNew = Arrays.copyOfRange(locs, 1, locs.length);
      int[] sizesNew = Arrays.copyOfRange(sizes, 1, sizes.length);
      this.array = new BlockMatrix[this.size];
      this.array[0] = new BlockMatrix(axis.substring(1), locsNew, sizesNew);
      for (int i = 1; i < this.size; i++)
        this.array[i] = this.array[0].clone(); 
      this.ax = axis.charAt(0);
    } 
  }
  
  public BlockMatrix(BlockMatrix[] array, char ax, int loc, int size) {
    this.array = array;
    this.row = null;
    this.ax = ax;
    this.loc = loc;
    this.size = array.length;
  }
  
  public BlockMatrix(StringBuilder row, char ax, int loc, int size) {
    this.array = null;
    this.row = row;
    this.ax = ax;
    this.loc = loc;
    this.size = size;
  }
  
  public BlockMatrix clone() {
    if (this.row == null) {
      BlockMatrix[] array = new BlockMatrix[this.size];
      for (int i = 0; i < this.array.length; i++)
        array[i] = this.array[i].clone(); 
      BlockMatrix blockMatrix = new BlockMatrix(array, this.ax, this.loc, this.size);
      return blockMatrix;
    } 
    BlockMatrix matrix = new BlockMatrix(new StringBuilder(), this.ax, this.loc, this.size);
    matrix.row.append(this.row.toString());
    return matrix;
  }
  
  public int getDimensions() {
    BlockMatrix matrix = this;
    for (int i = 0; i < 3; i++) {
      if (matrix.row != null)
        return i + 1; 
      matrix = matrix.array[0];
    } 
    return 0;
  }
  
  public String getAxis() {
    int dims = getDimensions();
    StringBuilder axis = new StringBuilder();
    BlockMatrix matrix = this;
    for (int i = 0; i < dims; i++) {
      axis.append(matrix.ax);
      if (matrix.row != null)
        break; 
      matrix = matrix.array[0];
    } 
    return axis.toString();
  }
  
  public int[] getLocsArray() {
    int dims = getDimensions();
    int[] locs = new int[dims];
    BlockMatrix matrix = this;
    for (int i = 0; i < dims; i++) {
      locs[i] = matrix.loc;
      if (matrix.row != null)
        break; 
      matrix = matrix.array[0];
    } 
    return locs;
  }
  
  public int[] getSizesArray() {
    int dims = getDimensions();
    int[] sizes = new int[dims];
    BlockMatrix matrix = this;
    for (int i = 0; i < dims; i++) {
      sizes[i] = matrix.size;
      if (matrix.row != null)
        break; 
      matrix = matrix.array[0];
    } 
    return sizes;
  }
  
  public int getX() {
    int dims = getDimensions();
    BlockMatrix matrix = this;
    for (int i = 0; i < dims; i++) {
      switch (matrix.ax) {
        case 'X':
        case 'e':
        case 'w':
        case 'x':
          return matrix.loc;
      } 
      if (matrix.row != null)
        break; 
      matrix = matrix.array[0];
    } 
    return Integer.MIN_VALUE;
  }
  
  public int getY() {
    int dims = getDimensions();
    BlockMatrix matrix = this;
    for (int i = 0; i < dims; i++) {
      switch (matrix.ax) {
        case 'Y':
        case 'd':
        case 'u':
        case 'y':
          return matrix.loc;
      } 
      if (matrix.row != null)
        break; 
      matrix = matrix.array[0];
    } 
    return Integer.MIN_VALUE;
  }
  
  public int getZ() {
    int dims = getDimensions();
    BlockMatrix matrix = this;
    for (int i = 0; i < dims; i++) {
      switch (matrix.ax) {
        case 'Z':
        case 'n':
        case 's':
        case 'z':
          return matrix.loc;
      } 
      if (matrix.row != null)
        break; 
      matrix = matrix.array[0];
    } 
    return Integer.MIN_VALUE;
  }
  
  public int getW() {
    int dims = getDimensions();
    BlockMatrix matrix = this;
    for (int i = 0; i < dims; i++) {
      switch (matrix.ax) {
        case 'X':
        case 'e':
        case 'w':
        case 'x':
          return matrix.size;
      } 
      if (matrix.row != null)
        break; 
      matrix = matrix.array[0];
    } 
    return Integer.MIN_VALUE;
  }
  
  public int getH() {
    int dims = getDimensions();
    BlockMatrix matrix = this;
    for (int i = 0; i < dims; i++) {
      switch (matrix.ax) {
        case 'Y':
        case 'd':
        case 'u':
        case 'y':
          return matrix.size;
      } 
      if (matrix.row != null)
        break; 
      matrix = matrix.array[0];
    } 
    return Integer.MIN_VALUE;
  }
  
  public int getD() {
    int dims = getDimensions();
    BlockMatrix matrix = this;
    for (int i = 0; i < dims; i++) {
      switch (matrix.ax) {
        case 'Z':
        case 'n':
        case 's':
        case 'z':
          return matrix.size;
      } 
      if (matrix.row != null)
        break; 
      matrix = matrix.array[0];
    } 
    return Integer.MIN_VALUE;
  }
  
  public char get(int... pos) {
    if (this.row == null)
      return this.array[pos[0]].get(Arrays.copyOfRange(pos, 1, pos.length)); 
    return this.row.charAt(pos[0]);
  }
  
  public String getRow(int... pos) {
    if (this.row == null)
      return this.array[pos[0]].getRow(Arrays.copyOfRange(pos, 1, pos.length)); 
    return this.row.toString();
  }
  
  public void set(char chr, int... pos) {
    if (this.row == null) {
      this.array[pos[0]].set(chr, Arrays.copyOfRange(pos, 1, pos.length));
    } else if (pos[0] >= this.row.length()) {
      while (pos[0] > this.row.length())
        this.row.append(' '); 
      this.row.append(chr);
    } else {
      this.row.setCharAt(pos[0], chr);
    } 
  }
  
  public void append(char chr, int... pos) {
    if (this.row == null) {
      this.array[pos[0]].append(chr, Arrays.copyOfRange(pos, 1, pos.length));
    } else {
      this.row.append(chr);
    } 
  }
  
  public void append(String str, int... pos) {
    if (this.row == null) {
      this.array[pos[0]].append(str, Arrays.copyOfRange(pos, 1, pos.length));
    } else {
      this.row.append(str);
    } 
  }
  
  public void fill() {
    fill(' ');
  }
  
  public void fill(char chr) {
    if (this.row == null) {
      for (BlockMatrix matrix : this.array)
        matrix.fill(chr); 
    } else {
      while (this.size > this.row.length())
        this.row.append(chr); 
    } 
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\plotter\BlockMatrix.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */