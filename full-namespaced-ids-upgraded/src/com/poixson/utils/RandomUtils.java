package com.poixson.utils;

import com.poixson.tools.Keeper;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public final class RandomUtils {
  static {
    Keeper.add(new RandomUtils());
  }
  
  protected static final AtomicInteger LastRND = new AtomicInteger(0);
  
  public static int GetRandom(int minNumber, int maxNumber) {
    return GetRandom(minNumber, maxNumber, LastRND.get());
  }
  
  public static int GetRandom(int minNumber, int maxNumber, int seed) {
    return GetRandom(minNumber, maxNumber, seed);
  }
  
  public static int GetRandom(int minNumber, int maxNumber, long seed) {
    if (minNumber > maxNumber)
      return minNumber; 
    if (minNumber == maxNumber)
      return minNumber; 
    if (seed == 0L)
      return GetRandom(minNumber, maxNumber, 1L); 
    Random rnd = new Random(Utils.GetMS() * seed);
    int num = rnd.nextInt(maxNumber - minNumber) + minNumber;
    LastRND.set(num * LastRND.get());
    return num;
  }
  
  public static int GetNewRandom(int minNumber, int maxNumber, int oldNumber) {
    if (minNumber > maxNumber)
      return minNumber; 
    if (minNumber == maxNumber)
      return minNumber; 
    if (maxNumber - minNumber == 1)
      return (oldNumber == minNumber) ? maxNumber : minNumber; 
    int seed = oldNumber;
    for (int i = 0; i < 100; i++) {
      int newNumber = GetRandom(minNumber, maxNumber, seed);
      if (newNumber != oldNumber) {
        LastRND.set(newNumber * LastRND.get());
        return newNumber;
      } 
      seed += i;
    } 
    throw new IllegalAccessError("Failed to generate a random number");
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixso\\utils\RandomUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */