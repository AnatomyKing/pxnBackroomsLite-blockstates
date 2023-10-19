package com.poixson.commonmc.tools.locationstore;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.tools.dao.Iab;
import com.poixson.tools.xTime;
import com.poixson.utils.Utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class LocationStore {
  public static final int DELAY_SAVE = (int)(new xTime("10s")).get(TimeUnit.SECONDS);
  
  public static final int DELAY_UNLOAD = (int)(new xTime("3m")).get(TimeUnit.SECONDS);
  
  protected final File file;
  
  public final CopyOnWriteArraySet<Iab> locations = new CopyOnWriteArraySet<>();
  
  protected final AtomicBoolean changed = new AtomicBoolean(false);
  
  protected final AtomicInteger state = new AtomicInteger(0);
  
  public LocationStore(File file) {
    this.file = file;
  }
  
  public synchronized void load(int regionX, int regionZ) throws IOException {
    this.locations.clear();
    if (this.file.isFile()) {
      BufferedReader reader = Files.newBufferedReader(this.file.toPath());
      Type token = (new TypeToken<HashSet<String>>() {
        
        }).getType();
      Set<String> set = (Set<String>)(new Gson()).fromJson(reader, token);
      for (String entry : set) {
        try {
          String[] split = entry.split(",");
          if (split.length != 2)
            throw new NumberFormatException(); 
          int x = Integer.parseInt(split[0].trim());
          int z = Integer.parseInt(split[1].trim());
          this.locations.add(new Iab(x, z));
        } catch (NumberFormatException e) {
          xJavaPlugin.LOG.warning(String.format("%sInvalid entry '%s' in file: %s", new Object[] { "[pxn] ", entry, this.file
                  
                  .toString() }));
        } 
      } 
      Utils.SafeClose(reader);
    } 
  }
  
  public boolean save() throws IOException {
    if (this.changed.getAndSet(false)) {
      Set<String> result = new HashSet<>();
      Iab[] set = this.locations.<Iab>toArray(new Iab[0]);
      for (Iab loc : set)
        result.add(loc.toString()); 
      if (result.size() > 0) {
        String data = (new Gson()).toJson(result);
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.file));
        writer.write(data);
        Utils.SafeClose(writer);
        return true;
      } 
    } 
    return false;
  }
  
  protected boolean should_unload() {
    int state = this.state.incrementAndGet();
    if (this.changed.get()) {
      if (state == DELAY_SAVE)
        try {
          save();
        } catch (IOException e) {
          e.printStackTrace();
        }  
      return false;
    } 
    if (state > DELAY_UNLOAD) {
      try {
        save();
      } catch (IOException iOException) {}
      return true;
    } 
    return false;
  }
  
  public void markAccessed() {
    this.state.set(0);
  }
  
  public boolean add(int x, int z) {
    this.state.set(0);
    boolean result = this.locations.add(new Iab(x, z));
    this.changed.set(true);
    return result;
  }
  
  public boolean remove(int x, int z) {
    this.state.set(0);
    boolean result = this.locations.remove(new Iab(x, z));
    this.changed.set(true);
    return result;
  }
  
  public boolean contains(int x, int z) {
    this.state.set(0);
    return this.locations.contains(new Iab(x, z));
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\locationstore\LocationStore.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */