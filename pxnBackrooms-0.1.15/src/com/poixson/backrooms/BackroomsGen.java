package com.poixson.backrooms;

import com.poixson.commonmc.tools.plotter.BlockPlotter;
import com.poixson.utils.FastNoiseLiteD;
import com.poixson.utils.Utils;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator;

public abstract class BackroomsGen {
  protected final BackroomsPlugin plugin;
  
  protected final BackroomsLevel backlevel;
  
  protected final CopyOnWriteArraySet<FastNoiseLiteD> noises = new CopyOnWriteArraySet<>();
  
  public final int level_y;
  
  public final int level_h;
  
  public BackroomsGen(BackroomsLevel backlevel, int level_y, int level_h) {
    this.plugin = backlevel.plugin;
    this.backlevel = backlevel;
    this.level_y = level_y;
    this.level_h = level_h;
  }
  
  public void register() {}
  
  public void unregister() {}
  
  protected FastNoiseLiteD register(FastNoiseLiteD noise) {
    this.noises.add(noise);
    return noise;
  }
  
  public void setSeed(int seed) {
    for (FastNoiseLiteD noise : this.noises)
      noise.setSeed(seed); 
  }
  
  public void initNoise() {}
  
  public abstract void generate(PreGenData paramPreGenData, ChunkGenerator.ChunkData paramChunkData, LinkedList<BlockPlotter> paramLinkedList, int paramInt1, int paramInt2);
  
  protected abstract void loadConfig();
  
  public static BlockData StringToBlockData(AtomicReference<String> atomic, String def) {
    String blockStr = atomic.get();
    if (Utils.notEmpty(blockStr))
      return StringToBlockData(blockStr); 
    return StringToBlockData(def);
  }
  
  public static BlockData StringToBlockData(String blockStr) {
    return Bukkit.createBlockData(blockStr);
  }
}
