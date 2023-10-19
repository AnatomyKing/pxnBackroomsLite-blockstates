package com.poixson.backrooms.gens;

import com.poixson.backrooms.BackroomsGen;
import com.poixson.backrooms.BackroomsLevel;
import com.poixson.backrooms.PreGenData;
import com.poixson.commonmc.tools.PathTracer;
import com.poixson.commonmc.tools.plotter.BlockPlotter;
import com.poixson.tools.abstractions.AtomicDouble;
import com.poixson.utils.FastNoiseLiteD;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;

public class Gen_309 extends BackroomsGen {
  public static final double DEFAULT_NOISE_PATH_FREQ = 0.01D;
  
  public static final double DEFAULT_NOISE_GROUND_FREQ = 0.002D;
  
  public static final int DEFAULT_NOISE_GROUND_OCTAVE = 3;
  
  public static final double DEFAULT_NOISE_GROUND_GAIN = 0.5D;
  
  public static final double DEFAULT_NOISE_GROUND_LACUN = 2.0D;
  
  public static final double DEFAULT_NOISE_TREES_FREQ = 0.2D;
  
  public static final int DEFAULT_PATH_WIDTH = 3;
  
  public static final int DEFAULT_PATH_CLEARING = 10;
  
  public static final int PATH_START_X = 14;
  
  public static final int PATH_START_Z = 32;
  
  public static final String DEFAULT_BLOCK_DIRT = "minecraft:dirt";
  
  public static final String DEFAULT_BLOCK_PATH = "minecraft:dirt_path";
  
  public static final String DEFAULT_BLOCK_GRASS = "minecraft:grass_block";
  
  public static final String DEFAULT_BLOCK_SUBFLOOR = "minecraft:stone";
  
  public static final String DEFAULT_BLOCK_TREE_TRUNK = "minecraft:birch_log";
  
  public static final String DEFAULT_BLOCK_TREE_LEAVES = "minecraft:birch_leaves";
  
  public final FastNoiseLiteD noisePath;
  
  public final FastNoiseLiteD noiseGround;
  
  public final FastNoiseLiteD noiseTrees;
  
  public final AtomicDouble noise_path_freq = new AtomicDouble(0.01D);
  
  public final AtomicDouble noise_ground_freq = new AtomicDouble(0.002D);
  
  public final AtomicInteger noise_ground_octave = new AtomicInteger(3);
  
  public final AtomicDouble noise_ground_gain = new AtomicDouble(0.5D);
  
  public final AtomicDouble noise_ground_lacun = new AtomicDouble(2.0D);
  
  public final AtomicDouble noise_trees_freq = new AtomicDouble(0.2D);
  
  public final AtomicInteger path_width = new AtomicInteger(3);
  
  public final AtomicInteger path_clearing = new AtomicInteger(10);
  
  protected final PathTracer pathTrace;
  
  protected final AtomicReference<ConcurrentHashMap<Integer, Double>> pathCache = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_dirt = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_path = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_grass = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_subfloor = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_tree_trunk = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_tree_leaves = new AtomicReference<>(null);
  
  public Gen_309(BackroomsLevel backlevel, int level_y, int level_h) {
    super(backlevel, level_y, level_h);
    this.noisePath = register(new FastNoiseLiteD());
    this.noiseGround = register(new FastNoiseLiteD());
    this.noiseTrees = register(new FastNoiseLiteD());
    this.pathTrace = new PathTracer(this.noisePath, 14, 32, getPathCacheMap());
  }
  
  public void initNoise() {
    this.noisePath.setFrequency(this.noise_path_freq.get());
    this.noiseGround.setFrequency(this.noise_ground_freq.get());
    this.noiseGround.setFractalOctaves(this.noise_ground_octave.get());
    this.noiseGround.setFractalGain(this.noise_ground_gain.get());
    this.noiseGround.setFractalLacunarity(this.noise_ground_lacun.get());
    this.noiseGround.setFractalType(FastNoiseLiteD.FractalType.Ridged);
    this.noiseTrees.setFrequency(this.noise_trees_freq.get());
  }
  
  public void unregister() {
    super.unregister();
    this.pathCache.set(null);
  }
  
  public FastNoiseLiteD getTreeNoise() {
    return this.noiseTrees;
  }
  
  public void generate(PreGenData pregen, ChunkGenerator.ChunkData chunk, LinkedList<BlockPlotter> plots, int chunkX, int chunkZ) {
    int path_width = this.path_width.get();
    BlockData block_dirt = StringToBlockData(this.block_dirt, "minecraft:dirt");
    BlockData block_path = StringToBlockData(this.block_path, "minecraft:dirt_path");
    BlockData block_grass = StringToBlockData(this.block_grass, "minecraft:grass_block");
    BlockData block_subfloor = StringToBlockData(this.block_subfloor, "minecraft:stone");
    if (block_dirt == null)
      throw new RuntimeException("Invalid block type for level 309 Dirt"); 
    if (block_path == null)
      throw new RuntimeException("Invalid block type for level 309 Path"); 
    if (block_grass == null)
      throw new RuntimeException("Invalid block type for level 309 Grass"); 
    if (block_subfloor == null)
      throw new RuntimeException("Invalid block type for level 309 SubFloor"); 
    int y = this.level_y + 3 + 1;
    for (int iz = 0; iz < 16; iz++) {
      for (int ix = 0; ix < 16; ix++) {
        int xx = chunkX * 16 + ix;
        int zz = chunkZ * 16 + iz;
        chunk.setBlock(ix, this.level_y, iz, Material.BEDROCK);
        for (int iy = 0; iy < 3; iy++)
          chunk.setBlock(ix, this.level_y + iy + 1, iz, block_subfloor); 
        double g = this.noiseGround.getNoise(xx, zz);
        double ground = 1.0D + ((g < 0.0D) ? (g * 0.6000000238418579D) : g);
        int elevation = (int)(ground * 2.5D);
        for (int i = 0; i < elevation; i++) {
          if (i >= elevation - 1) {
            if (this.pathTrace.isPath(xx, zz, path_width)) {
              chunk.setBlock(ix, y + i, iz, block_path);
            } else {
              chunk.setBlock(ix, y + i, iz, block_grass);
            } 
          } else {
            chunk.setBlock(ix, y + i, iz, block_dirt);
          } 
        } 
      } 
    } 
  }
  
  public int getPathX(int z) {
    if (z < 0)
      return 0; 
    return this.pathTrace.getPathX(z);
  }
  
  public ConcurrentHashMap<Integer, Double> getPathCacheMap() {
    ConcurrentHashMap<Integer, Double> cache = this.pathCache.get();
    if (cache != null)
      return cache; 
    cache = new ConcurrentHashMap<>();
    if (this.pathCache.compareAndSet(null, cache))
      return cache; 
    return getPathCacheMap();
  }
  
  public double getCenterClearingDistance(int x, int z, double strength) {
    if (Math.abs(x) > 100 || Math.abs(z) > 100)
      return Double.MAX_VALUE; 
    return Math.sqrt(Math.pow(x, 2.0D) + Math.pow(z, 2.0D)) + this.noisePath
      .getNoise((x * 5), (z * 5)) * strength;
  }
  
  protected void loadConfig() {
    ConfigurationSection cfg = this.plugin.getLevelParams(309);
    this.noise_path_freq.set(cfg.getDouble("Noise-Path-Freq"));
    this.noise_ground_freq.set(cfg.getDouble("Noise-Ground-Freq"));
    this.noise_ground_octave.set(cfg.getInt("Noise-Ground-Octave"));
    this.noise_ground_gain.set(cfg.getDouble("Noise-Ground-Gain"));
    this.noise_ground_lacun.set(cfg.getDouble("Noise-Ground-Lacun"));
    this.noise_trees_freq.set(cfg.getDouble("Noise-Trees-Freq"));
    this.path_width.set(cfg.getInt("Path-Width"));
    this.path_clearing.set(cfg.getInt("Path-Clearing"));
    cfg = this.plugin.getLevelBlocks(309);
    this.block_tree_trunk.set(cfg.getString("Tree-Trunk"));
    this.block_tree_leaves.set(cfg.getString("Tree-Leaves"));
  }
  
  public static void ConfigDefaults(FileConfiguration cfg) {
    cfg.addDefault("Level309.Params.Noise-Path-Freq", Double.valueOf(0.01D));
    cfg.addDefault("Level309.Params.Noise-Ground-Freq", Double.valueOf(0.002D));
    cfg.addDefault("Level309.Params.Noise-Ground-Octave", Integer.valueOf(3));
    cfg.addDefault("Level309.Params.Noise-Ground-Gain", Double.valueOf(0.5D));
    cfg.addDefault("Level309.Params.Noise-Ground-Lacun", Double.valueOf(2.0D));
    cfg.addDefault("Level309.Params.Noise-Trees-Freq", Double.valueOf(0.2D));
    cfg.addDefault("Level309.Params.Path-Width", Integer.valueOf(3));
    cfg.addDefault("Level309.Params.Path-Clearing", Integer.valueOf(10));
    cfg.addDefault("Level309.Blocks.Dirt", "minecraft:dirt");
    cfg.addDefault("Level309.Blocks.Path", "minecraft:dirt_path");
    cfg.addDefault("Level309.Blocks.Grass", "minecraft:grass_block");
    cfg.addDefault("Level309.Blocks.SubFloor", "minecraft:stone");
    cfg.addDefault("Level309.Blocks.Tree-Trunk", "minecraft:birch_log");
    cfg.addDefault("Level309.Blocks.Tree-Leaves", "minecraft:birch_leaves");
  }
}
