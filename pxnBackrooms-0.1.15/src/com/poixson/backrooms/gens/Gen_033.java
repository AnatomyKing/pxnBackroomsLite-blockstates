package com.poixson.backrooms.gens;

import com.poixson.backrooms.BackroomsGen;
import com.poixson.backrooms.BackroomsLevel;
import com.poixson.backrooms.PreGenData;
import com.poixson.commonmc.tools.plotter.BlockPlotter;
import com.poixson.tools.abstractions.AtomicDouble;
import com.poixson.utils.FastNoiseLiteD;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;

public class Gen_033 extends BackroomsGen {
  public static final double DEFAULT_NOISE_FLOOR_FREQ = 0.1D;
  
  public static final int DEFAULT_NOISE_FLOOR_OCTAVE = 2;
  
  public static final double DEFAULT_NOISE_FLOOR_GAIN = 2.0D;
  
  public static final double DEFAULT_THRESH_FLOOR = -0.4D;
  
  public static final double DEFAULT_THRESH_HAZARD = 0.7D;
  
  public static final String DEFAULT_BLOCK_WALL = "minecraft:blackstone";
  
  public static final String DEFAULT_BLOCK_CEILING = "minecraft:glowstone";
  
  public static final String DEFAULT_BLOCK_FLOOR = "minecraft:polished_deepslate";
  
  public static final String DEFAULT_BLOCK_FLOOR_SAFE = "minecraft:cracked_deepslate_tiles";
  
  public static final String DEFAULT_BLOCK_SUBFLOOR = "minecraft:dark_oak_slab[type=top]";
  
  public static final String DEFAULT_BLOCK_PLATE = "minecraft:stone_pressure_plate";
  
  public static final String DEFAULT_BLOCK_HAZARD = "minecraft:bricks";
  
  public final FastNoiseLiteD noiseFloor;
  
  public final AtomicDouble noise_floor_freq = new AtomicDouble(0.1D);
  
  public final AtomicInteger noise_floor_octave = new AtomicInteger(2);
  
  public final AtomicDouble noise_floor_gain = new AtomicDouble(2.0D);
  
  public final AtomicDouble thresh_floor = new AtomicDouble(-0.4D);
  
  public final AtomicDouble thresh_hazard = new AtomicDouble(0.7D);
  
  public final AtomicReference<String> block_wall = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_ceiling = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_floor = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_floor_safe = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_subfloor = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_plate = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_hazard = new AtomicReference<>(null);
  
  public Gen_033(BackroomsLevel backlevel, int level_y, int level_h) {
    super(backlevel, level_y, level_h);
    this.noiseFloor = register(new FastNoiseLiteD());
  }
  
  public void initNoise() {
    this.noiseFloor.setFrequency(this.noise_floor_freq.get());
    this.noiseFloor.setFractalOctaves(this.noise_floor_octave.get());
    this.noiseFloor.setFractalGain(this.noise_floor_gain.get());
    this.noiseFloor.setFractalType(FastNoiseLiteD.FractalType.FBm);
  }
  
  public void generate(PreGenData pregen, ChunkGenerator.ChunkData chunk, LinkedList<BlockPlotter> plots, int chunkX, int chunkZ) {
    BlockData block_wall = StringToBlockData(this.block_wall, "minecraft:blackstone");
    BlockData block_ceiling = StringToBlockData(this.block_ceiling, "minecraft:glowstone");
    BlockData block_floor = StringToBlockData(this.block_floor, "minecraft:polished_deepslate");
    BlockData block_floor_safe = StringToBlockData(this.block_floor_safe, "minecraft:cracked_deepslate_tiles");
    BlockData block_subfloor = StringToBlockData(this.block_subfloor, "minecraft:dark_oak_slab[type=top]");
    BlockData block_plate = StringToBlockData(this.block_plate, "minecraft:stone_pressure_plate");
    BlockData block_hazard = StringToBlockData(this.block_hazard, "minecraft:bricks");
    if (block_wall == null)
      throw new RuntimeException("Invalid block type for level 33 Wall"); 
    if (block_ceiling == null)
      throw new RuntimeException("Invalid block type for level 33 Ceiling"); 
    if (block_floor == null)
      throw new RuntimeException("Invalid block type for level 33 Floor"); 
    if (block_floor_safe == null)
      throw new RuntimeException("Invalid block type for level 33 Floor-Safe"); 
    if (block_subfloor == null)
      throw new RuntimeException("Invalid block type for level 33 SubFloor"); 
    if (block_plate == null)
      throw new RuntimeException("Invalid block type for level 33 Plate"); 
    if (block_hazard == null)
      throw new RuntimeException("Invalid block type for level 33 Hazard"); 
    double thresh_floor = this.thresh_floor.get();
    double thresh_hazard = this.thresh_hazard.get();
    for (int iz = 0; iz < 16; iz++) {
      int zz = chunkZ * 16 + iz;
      for (int ix = 0; ix < 16; ix++) {
        double valueFloor;
        boolean safe;
        int xx = chunkX * 16 + ix;
        int iy;
        for (iy = this.level_y + this.level_h; iy < 320; iy++)
          chunk.setBlock(ix, iy, iz, Material.BEDROCK); 
        switch (ix) {
          case 0:
          case 1:
          case 14:
          case 15:
            for (iy = -64; iy < this.level_h; iy++)
              chunk.setBlock(ix, iy + this.level_y, iz, Material.BEDROCK); 
            break;
          case 2:
          case 13:
            for (iy = -2; iy < this.level_h; iy++)
              chunk.setBlock(ix, iy + this.level_y, iz, block_wall); 
            break;
          default:
            valueFloor = 0.0D - this.noiseFloor.getNoise(xx, (zz * 2));
            safe = (chunkZ % 5 == 0);
            chunk.setBlock(ix, this.level_y + this.level_h - 1, iz, block_ceiling);
            if (valueFloor > thresh_floor) {
              if (safe) {
                chunk.setBlock(ix, this.level_y, iz, block_floor_safe);
                chunk.setBlock(ix, this.level_y - 1, iz, block_floor_safe);
              } else {
                chunk.setBlock(ix, this.level_y + 1, iz, block_plate);
                chunk.setBlock(ix, this.level_y, iz, block_floor);
                chunk.setBlock(ix, this.level_y - 1, iz, Material.TNT);
                if (valueFloor > thresh_hazard) {
                  chunk.setBlock(ix, this.level_y + 2, iz, block_hazard);
                  chunk.setBlock(ix, this.level_y + 1, iz, block_hazard);
                } 
              } 
              chunk.setBlock(ix, this.level_y - 2, iz, block_subfloor);
            } 
            break;
        } 
      } 
    } 
  }
  
  protected void loadConfig() {
    ConfigurationSection cfg = this.plugin.getLevelParams(33);
    this.noise_floor_freq.set(cfg.getDouble("Noise-Floor-Freq"));
    this.noise_floor_octave.set(cfg.getInt("Noise-Floor-Octave"));
    this.noise_floor_gain.set(cfg.getDouble("Noise-Floor-Gain"));
    this.thresh_floor.set(cfg.getDouble("Thresh-Floor"));
    this.thresh_hazard.set(cfg.getDouble("Thresh-Hazard"));
    cfg = this.plugin.getLevelBlocks(33);
    this.block_wall.set(cfg.getString("Wall"));
    this.block_ceiling.set(cfg.getString("Ceiling"));
    this.block_floor.set(cfg.getString("Floor"));
    this.block_floor_safe.set(cfg.getString("Floor-Safe"));
    this.block_subfloor.set(cfg.getString("SubFloor"));
    this.block_plate.set(cfg.getString("Plate"));
    this.block_hazard.set(cfg.getString("Hazard"));
  }
  
  public static void ConfigDefaults(FileConfiguration cfg) {
    cfg.addDefault("Level33.Params.Noise-Floor-Freq", Double.valueOf(0.1D));
    cfg.addDefault("Level33.Params.Noise-Floor-Octave", Integer.valueOf(2));
    cfg.addDefault("Level33.Params.Noise-Floor-Gain", Double.valueOf(2.0D));
    cfg.addDefault("Level33.Params.Thresh-Floor", Double.valueOf(-0.4D));
    cfg.addDefault("Level33.Params.Thresh-Hazard", Double.valueOf(0.7D));
    cfg.addDefault("Level33.Blocks.Wall", "minecraft:blackstone");
    cfg.addDefault("Level33.Blocks.Ceiling", "minecraft:glowstone");
    cfg.addDefault("Level33.Blocks.Floor", "minecraft:polished_deepslate");
    cfg.addDefault("Level33.Blocks.Floor-Safe", "minecraft:cracked_deepslate_tiles");
    cfg.addDefault("Level33.Blocks.SubFloor", "minecraft:dark_oak_slab[type=top]");
    cfg.addDefault("Level33.Blocks.Plate", "minecraft:stone_pressure_plate");
    cfg.addDefault("Level33.Blocks.Hazard", "minecraft:bricks");
  }
}
