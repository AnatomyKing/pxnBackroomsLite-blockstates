package com.poixson.backrooms.tasks;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.poixson.backrooms.BackroomsLevel;
import com.poixson.backrooms.BackroomsPlugin;
import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.utils.NumberUtils;
import com.poixson.utils.RandomUtils;
import com.poixson.utils.Utils;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.Location;

public class TeleportManager {
  protected final BackroomsPlugin plugin;
  
  protected final HashMap<Integer, HashMap<Integer, Integer>> weights;
  
  protected final HashMap<Integer, Location> cachedSpawns = new HashMap<>();
  
  protected final HashMap<Integer, Integer> cachedToLevel = new HashMap<>();
  
  protected int rndLast = 0;
  
  public TeleportManager(BackroomsPlugin plugin, HashMap<Integer, HashMap<Integer, Integer>> weights) {
    this.plugin = plugin;
    this.weights = weights;
  }
  
  public static TeleportManager Load(BackroomsPlugin plugin) {
    HashMap<Integer, HashMap<Integer, Integer>> chances = new HashMap<>();
    InputStream input = plugin.getResource("chances.json");
    if (input == null)
      throw new RuntimeException("Failed to load chances.json"); 
    InputStreamReader reader = new InputStreamReader(input);
    JsonElement json = JsonParser.parseReader(reader);
    Utils.SafeClose(reader);
    Utils.SafeClose(input);
    Iterator<Map.Entry<String, JsonElement>> it = json.getAsJsonObject().entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, JsonElement> entry = it.next();
      String key = entry.getKey();
      int level = NumberUtils.IsNumeric(key) ? Integer.parseInt(key) : Integer.MIN_VALUE;
      HashMap<Integer, Integer> weights = new HashMap<>();
      Iterator<Map.Entry<String, JsonElement>> it2 = ((JsonElement)entry.getValue()).getAsJsonObject().entrySet().iterator();
      while (it2.hasNext()) {
        Map.Entry<String, JsonElement> entry2 = it2.next();
        int lvl = Integer.parseInt(entry2.getKey());
        int w = ((JsonElement)entry2.getValue()).getAsInt();
        weights.put(Integer.valueOf(lvl), Integer.valueOf(w));
      } 
      chances.put(Integer.valueOf(level), weights);
    } 
    return new TeleportManager(plugin, chances);
  }
  
  public void markUsed() {
    this.plugin.getHourlyTask()
      .markUsed();
  }
  
  public void flush() {
    int count = this.cachedSpawns.size();
    this.cachedSpawns.clear();
    this.cachedToLevel.clear();
    if (count > 0)
      xJavaPlugin.LOG.info("[pxnBackrooms] Rolling the teleport dice.."); 
  }
  
  public int getDestinationLevel(int level_from) {
    markUsed();
    Integer integer = this.cachedToLevel.get(Integer.valueOf(level_from));
    if (integer != null)
      return integer.intValue(); 
    int level = findDestinationLevel(level_from);
    this.cachedToLevel.putIfAbsent(Integer.valueOf(level_from), Integer.valueOf(level));
    return level;
  }
  
  protected int findDestinationLevel(int level_from) {
    HashMap<Integer, Integer> weights = this.weights.get(Integer.valueOf(level_from));
    if (weights == null)
      throw new RuntimeException("Unknown backrooms level: " + Integer.toString(level_from)); 
    if (weights.isEmpty())
      throw new RuntimeException("Backrooms level has no weights set: " + Integer.toString(level_from)); 
    int total = 0;
    for (Integer i : weights.values())
      total += i.intValue(); 
    if (total < 1)
      return 0; 
    if (total == 1) {
      Iterator<Map.Entry<Integer, Integer>> iterator = weights.entrySet().iterator();
      Map.Entry<Integer, Integer> entry = iterator.next();
      if (((Integer)entry.getValue()).intValue() == 1)
        return ((Integer)entry.getKey()).intValue(); 
    } 
    int rnd = RandomUtils.GetNewRandom(0, total, this.rndLast);
    this.rndLast = rnd;
    Iterator<Map.Entry<Integer, Integer>> it = weights.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<Integer, Integer> entry = it.next();
      int level = ((Integer)entry.getKey()).intValue();
      int weight = ((Integer)entry.getValue()).intValue();
      total -= weight;
      if (total <= rnd)
        return level; 
    } 
    xJavaPlugin.LOG.warning("[pxnBackrooms] Failed to find random level");
    return 0;
  }
  
  public Location getSpawnArea(int level) {
    Location spawn = this.cachedSpawns.get(Integer.valueOf(level));
    if (spawn != null)
      return spawn; 
    BackroomsLevel backlevel = this.plugin.getBackroomsLevel(level);
    if (backlevel == null) {
      xJavaPlugin.LOG.warning("[pxnBackrooms] Unknown backrooms level: " + Integer.toString(level));
      return null;
    } 
    Location location1 = backlevel.getNewSpawnArea(level);
    this.cachedSpawns.put(Integer.valueOf(level), location1);
    return location1;
  }
  
  public Location getSpawnLocation(int level) {
    Location spawn = getSpawnArea(level);
    return getSpawnLocation(spawn, level);
  }
  
  public Location getSpawnLocation(Location spawn, int level) {
    BackroomsLevel backlevel = this.plugin.getBackroomsLevel(level);
    if (backlevel == null) {
      xJavaPlugin.LOG.warning("[pxnBackrooms] Unknown backrooms level: " + Integer.toString(level));
      return null;
    } 
    return backlevel.getSpawnNear(spawn);
  }
}
