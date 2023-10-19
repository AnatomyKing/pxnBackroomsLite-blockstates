package com.poixson.commonmc.tools;

import com.poixson.utils.FastNoiseLiteD;
import com.poixson.utils.NumberUtils;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class PathTracer {
  protected final FastNoiseLiteD noise;
  
  protected final ConcurrentHashMap<Integer, Double> cache;
  
  protected final ThreadLocal<SoftReference<HashMap<Integer, Double>>> cacheLocal = new ThreadLocal<>();
  
  protected final int start_x;
  
  protected final int start_z;
  
  public PathTracer(FastNoiseLiteD noise) {
    this(noise, 0, 0, new ConcurrentHashMap<>());
  }
  
  public PathTracer(FastNoiseLiteD noise, int start_x, int start_z) {
    this(noise, start_x, start_z, new ConcurrentHashMap<>());
  }
  
  public PathTracer(FastNoiseLiteD noise, int start_x, int start_z, ConcurrentHashMap<Integer, Double> cache) {
    this.noise = noise;
    this.cache = cache;
    this.start_x = start_x;
    this.start_z = start_z;
  }
  
  public boolean isPath(int x, int z, int width) {
    if (z < this.start_z)
      return false; 
    int xx = getPathX(z);
    if (xx == Integer.MIN_VALUE)
      return false; 
    return (x >= xx - width && x <= xx + width);
  }
  
  public int getPathX(int z) {
    if (z < this.start_z)
      return Integer.MIN_VALUE; 
    HashMap<Integer, Double> local = getLocalCache();
    Double value = local.get(Integer.valueOf(z));
    if (value != null)
      return Math.round(value.intValue()); 
    value = this.cache.get(Integer.valueOf(z));
    if (value != null)
      return Math.round(value.intValue()); 
    if (z == this.start_z) {
      local.put(Integer.valueOf(this.start_z), Double.valueOf(this.start_x));
      this.cache.put(Integer.valueOf(this.start_z), Double.valueOf(this.start_x));
      return this.start_x;
    } 
    int from = this.start_z;
    double x = this.start_x;
    for (int i = z - 1; i >= this.start_z; i--) {
      value = local.get(Integer.valueOf(i));
      if (value != null) {
        x = value.doubleValue();
        from = i;
        break;
      } 
      value = this.cache.get(Integer.valueOf(i));
      if (value != null) {
        x = value.doubleValue();
        from = i;
        break;
      } 
    } 
    for (int j = from + 1; j <= z; j++) {
      if (j > this.start_z + 10) {
        double valueE = this.noise.getNoise(x + 1.0D, j);
        double valueW = this.noise.getNoise(x - 1.0D, j);
        x += (valueW - valueE) * 5.0D;
      } 
      int step = NumberUtils.MinMax((int)Math.floor(Math.pow(j, 0.5D)), 3, 1000);
      local.put(Integer.valueOf(j), Double.valueOf(x));
      if (j % step == 0)
        this.cache.put(Integer.valueOf(j), Double.valueOf(x)); 
    } 
    return (int)Math.round(x);
  }
  
  public HashMap<Integer, Double> getLocalCache() {
    SoftReference<HashMap<Integer, Double>> soft = this.cacheLocal.get();
    if (soft != null) {
      HashMap<Integer, Double> hashMap = soft.get();
      if (hashMap != null && 
        hashMap.size() < 1000)
        return hashMap; 
    } 
    HashMap<Integer, Double> map = new HashMap<>();
    this.cacheLocal.set(new SoftReference<>(map));
    return map;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\PathTracer.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */