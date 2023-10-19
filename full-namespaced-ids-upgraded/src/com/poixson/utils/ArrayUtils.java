package com.poixson.utils;

import com.poixson.tools.Keeper;

public final class ArrayUtils {
  static {
    Keeper.add(new ArrayUtils());
  }
  
  public static double GetSafeOrZero(double[] array, int index) {
    if (index < 0)
      return 0.0D; 
    if (index >= array.length)
      return 0.0D; 
    return array[index];
  }
  
  public static double GetSafeOrNear(double[] array, int index) {
    if (index < 0)
      return array[0]; 
    if (index >= array.length)
      return GetSafeLooped(array, -1); 
    return array[index];
  }
  
  public static double GetSafeLooped(double[] array, int index) {
    int count = array.length;
    if (index < 0) {
      int i = index;
      while (i < 0)
        i += count; 
      return array[i % count];
    } 
    return array[index % count];
  }
  
  public static double GetSafeLoopExtend(double[] array, int index) {
    if (index < 0)
      return array[0] + GetSafeLooped(array, index - 1); 
    int count = array.length;
    if (index >= count)
      return GetSafeLooped(array, index - 1) + GetSafeLooped(array, index); 
    return array[index];
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixso\\utils\ArrayUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */