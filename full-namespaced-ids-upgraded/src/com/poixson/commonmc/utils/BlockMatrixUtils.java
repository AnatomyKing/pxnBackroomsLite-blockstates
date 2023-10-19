package com.poixson.commonmc.utils;

public final class BlockMatrixUtils {
  public static int[] LocsToArray(String axis, int x, int y, int z) {
    int dims = axis.length();
    int[] locs = new int[dims];
    for (int i = 0; i < dims; i++) {
      char ax = axis.charAt(i);
      switch (ax) {
        case 'Y':
        case 'd':
        case 'u':
        case 'y':
          locs[i] = y;
          break;
        case 'Z':
        case 'n':
        case 's':
        case 'z':
          locs[i] = z;
          break;
        case 'X':
        case 'e':
        case 'w':
        case 'x':
          locs[i] = x;
          break;
        default:
          throw new RuntimeException("Unknown axis: " + Character.toString(ax));
      } 
    } 
    return locs;
  }
  
  public static int[] SizesToArray(String axis, int w, int h, int d) {
    int dims = axis.length();
    int[] sizes = new int[dims];
    for (int i = 0; i < dims; i++) {
      char ax = axis.charAt(i);
      switch (ax) {
        case 'Y':
        case 'd':
        case 'u':
        case 'y':
          sizes[i] = h;
          break;
        case 'Z':
        case 'n':
        case 's':
        case 'z':
          sizes[i] = d;
          break;
        case 'X':
        case 'e':
        case 'w':
        case 'x':
          sizes[i] = w;
          break;
        default:
          throw new RuntimeException("Unknown axis: " + Character.toString(ax));
      } 
    } 
    return sizes;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonm\\utils\BlockMatrixUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */