package com.poixson.commonmc.tools.mapstore;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poixson.commonmc.events.PluginSaveEvent;
import com.poixson.commonmc.pxnCommonPlugin;
import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.commonmc.tools.plugin.xListener;
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
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.map.MapView;

public class FreedMapStore extends xListener<pxnCommonPlugin> {
  public static final int MAX_MAP_ID = 2147483647;
  
  protected final ConcurrentSkipListSet<Integer> freed = new ConcurrentSkipListSet<>();
  
  protected final File file;
  
  protected final AtomicBoolean changed = new AtomicBoolean(false);
  
  public FreedMapStore(pxnCommonPlugin plugin, String path) {
    super((xJavaPlugin)plugin);
    this.file = new File(path, "freed-maps.json");
  }
  
  public synchronized void load() throws IOException {
    this.freed.clear();
    if (this.file.isFile()) {
      xJavaPlugin.LOG.info("[pxn] Loading: freed-maps.json");
      BufferedReader reader = Files.newBufferedReader(this.file.toPath());
      Type token = (new TypeToken<HashSet<Integer>>() {
        
        }).getType();
      Set<Integer> set = (Set<Integer>)(new Gson()).fromJson(reader, token);
      for (Integer id : set)
        this.freed.add(id); 
      this.changed.set(false);
      Utils.SafeClose(reader);
    } else {
      xJavaPlugin.LOG.info("[pxn] File not found: freed-maps.json");
      this.changed.set(true);
    } 
  }
  
  public boolean save() throws IOException {
    if (this.changed.getAndSet(false)) {
      Integer[] list = (Integer[])this.freed.toArray((Object[])new Integer[0]);
      int[] result = new int[list.length];
      if (list.length > 0) {
        int i = 0;
        for (Integer id : list)
          result[i++] = id.intValue(); 
      } 
      xJavaPlugin.LOG.info(String.format("%sSaving [%d] freed maps", new Object[] { "[pxn] ", Integer.valueOf(result.length) }));
      String data = (new Gson()).toJson(result);
      BufferedWriter writer = new BufferedWriter(new FileWriter(this.file));
      writer.write(data);
      Utils.SafeClose(writer);
      return true;
    } 
    return false;
  }
  
  @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
  public void onPluginSave(PluginSaveEvent event) {
    try {
      save();
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  public int get() {
    Integer next = this.freed.pollFirst();
    if (next != null) {
      this.changed.set(true);
      return next.intValue();
    } 
    MapView map = Bukkit.createMap(Bukkit.getWorld("world"));
    return map.getId();
  }
  
  public boolean release(int map_id) {
    boolean result = this.freed.add(Integer.valueOf(map_id));
    this.changed.set(true);
    return result;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\mapstore\FreedMapStore.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */