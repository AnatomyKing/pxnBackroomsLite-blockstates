package com.poixson.commonmc.tools.locationstore;

import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.commonmc.utils.BukkitUtils;
import com.poixson.tools.dao.Iab;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class LocationStoreManager extends BukkitRunnable {
  protected final String type;
  
  protected final File path;
  
  protected final AtomicBoolean loaded = new AtomicBoolean(false);
  
  protected final ConcurrentHashMap<Iab, LocationStore> regions = new ConcurrentHashMap<>();
  
  public LocationStoreManager(String worldStr, String type) {
    this.type = type;
    this.path = new File(BukkitUtils.GetServerPath(), worldStr + "/locs");
  }
  
  public void load() {
    if (this.loaded.compareAndSet(false, true) && 
      !this.path.isDirectory()) {
      if (!this.path.mkdir())
        throw new RuntimeException("Failed to create directory: " + this.path.toString()); 
      xJavaPlugin.LOG.info(String.format("%sCreated directory: %s", new Object[] { "[pxn] ", this.path.toString() }));
    } 
  }
  
  public LocationStoreManager start(JavaPlugin plugin) {
    runTaskTimerAsynchronously((Plugin)plugin, 20L, 20L);
    return this;
  }
  
  public void run() {
    Iterator<Map.Entry<Iab, LocationStore>> it = this.regions.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<Iab, LocationStore> entry = it.next();
      if (((LocationStore)entry.getValue()).should_unload())
        this.regions.remove(entry.getKey()); 
    } 
  }
  
  public void saveAll() {
    load();
    for (LocationStore store : this.regions.values()) {
      try {
        store.save();
      } catch (IOException e) {
        e.printStackTrace();
      } 
    } 
  }
  
  public boolean add(int x, int z) {
    LocationStore region = getRegion(x, z);
    return region.add(x, z);
  }
  
  public boolean remove(int x, int z) {
    LocationStore region = getRegion(x, z);
    return region.remove(x, z);
  }
  
  public boolean contains(int x, int z) {
    LocationStore region = getRegion(x, z);
    return region.contains(x, z);
  }
  
  public LocationStore getRegion(int x, int z) {
    load();
    int regionX = Math.floorDiv(x, 512);
    int regionZ = Math.floorDiv(z, 512);
    Iab loc = new Iab(regionX, regionZ);
    LocationStore store = this.regions.get(loc);
    if (store != null) {
      store.markAccessed();
      return store;
    } 
    String fileStr = String.format("%s.%d.%d.json", new Object[] { this.type, 
          
          Integer.valueOf(regionX), 
          Integer.valueOf(regionZ) });
    File file = new File(this.path, fileStr);
    LocationStore locationStore1 = new LocationStore(file);
    try {
      locationStore1.load(regionX, regionZ);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } 
    LocationStore existing = this.regions.putIfAbsent(loc, locationStore1);
    if (existing == null)
      return locationStore1; 
    existing.markAccessed();
    return existing;
  }
  
  public Iab[] findNear(int x, int z) {
    LocationStore store = getRegion(x, z);
    if (store != null && 
      store.locations.size() > 0)
      return store.locations.<Iab>toArray(new Iab[0]); 
    HashSet<Iab> result = new HashSet<>();
    for (int iz = -1; iz < 2; iz++) {
      int zz = iz * 512 + z;
      for (int ix = -1; ix < 2; ix++) {
        if (ix != 0 || iz != 0) {
          int xx = ix * 512 + x;
          LocationStore locationStore = getRegion(xx, zz);
          for (Iab loc : locationStore.locations)
            result.add(loc); 
        } 
      } 
    } 
    return result.<Iab>toArray(new Iab[0]);
  }
  
  public Iab findNearest(int x, int z) {
    Iab[] near = findNear(x, z);
    if (near.length > 0) {
      double distance = Double.MAX_VALUE;
      Iab nearest = null;
      for (Iab loc : near) {
        double dist = Math.sqrt(
            Math.pow((x - loc.a), 2.0D) + 
            Math.pow((z - loc.b), 2.0D));
        if (distance > dist) {
          distance = dist;
          nearest = loc;
        } 
      } 
      return nearest;
    } 
    return null;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\locationstore\LocationStoreManager.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */