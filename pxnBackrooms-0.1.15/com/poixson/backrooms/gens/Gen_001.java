package com.poixson.backrooms.gens;

import com.poixson.backrooms.BackroomsGen;
import com.poixson.backrooms.BackroomsLevel;
import com.poixson.backrooms.PreGenData;
import com.poixson.backrooms.worlds.Level_000;
import com.poixson.commonmc.tools.plotter.BlockPlotter;
import com.poixson.tools.abstractions.AtomicDouble;
import com.poixson.tools.dao.Iab;
import com.poixson.utils.FastNoiseLiteD;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;

public class Gen_001 extends BackroomsGen {
  public static final double DEFAULT_NOISE_WALL_FREQ = 0.033D;
  
  public static final int DEFAULT_NOISE_WALL_OCTAVE = 2;
  
  public static final double DEFAULT_NOISE_WALL_GAIN = 0.03D;
  
  public static final double DEFAULT_NOISE_WALL_STRENGTH = 1.2D;
  
  public static final double DEFAULT_NOISE_MOIST_FREQ = 0.015D;
  
  public static final int DEFAULT_NOISE_MOIST_OCTAVE = 2;
  
  public static final double DEFAULT_NOISE_MOIST_GAIN = 2.0D;
  
  public static final double DEFAULT_NOISE_WELL_FREQ = 0.0028D;
  
  public static final double DEFAULT_THRESH_WALL = 0.9D;
  
  public static final double DEFAULT_THRESH_MOIST = 0.4D;
  
  public static final double DEFAULT_THRESH_WELL = 0.9D;
  
  public static final int LAMP_Y = 6;
  
  public static final String DEFAULT_BLOCK_WALL = "minecraft:mud_bricks";
  
  public static final String DEFAULT_BLOCK_SUBFLOOR = "minecraft:dirt";
  
  public static final String DEFAULT_BLOCK_FLOOR_DRY = "minecraft:brown_concrete_powder";
  
  public static final String DEFAULT_BLOCK_FLOOR_WET = "minecraft:brown_concrete";
  
  public final FastNoiseLiteD noiseBasementWalls;
  
  public final FastNoiseLiteD noiseMoist;
  
  public final FastNoiseLiteD noiseWell;
  
  public final AtomicDouble noise_wall_freq = new AtomicDouble(0.033D);
  
  public final AtomicInteger noise_wall_octave = new AtomicInteger(2);
  
  public final AtomicDouble noise_wall_gain = new AtomicDouble(0.03D);
  
  public final AtomicDouble noise_wall_strength = new AtomicDouble(1.2D);
  
  public final AtomicDouble noise_moist_freq = new AtomicDouble(0.015D);
  
  public final AtomicInteger noise_moist_octave = new AtomicInteger(2);
  
  public final AtomicDouble noise_moist_gain = new AtomicDouble(2.0D);
  
  public final AtomicDouble noise_well_freq = new AtomicDouble(0.0028D);
  
  public final AtomicDouble thresh_wall = new AtomicDouble(0.9D);
  
  public final AtomicDouble thresh_moist = new AtomicDouble(0.4D);
  
  public final AtomicDouble thresh_well = new AtomicDouble(0.9D);
  
  public final AtomicReference<String> block_wall = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_subfloor = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_floor_dry = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_floor_wet = new AtomicReference<>(null);
  
  public Gen_001(BackroomsLevel backlevel, int level_y, int level_h) {
    super(backlevel, level_y, level_h);
    this.noiseBasementWalls = register(new FastNoiseLiteD());
    this.noiseMoist = register(new FastNoiseLiteD());
    this.noiseWell = register(new FastNoiseLiteD());
  }
  
  public void initNoise() {
    this.noiseBasementWalls.setFrequency(this.noise_wall_freq.get());
    this.noiseBasementWalls.setFractalOctaves(this.noise_wall_octave.get());
    this.noiseBasementWalls.setFractalGain(this.noise_wall_gain.get());
    this.noiseBasementWalls.setFractalPingPongStrength(this.noise_wall_strength.get());
    this.noiseBasementWalls.setNoiseType(FastNoiseLiteD.NoiseType.Cellular);
    this.noiseBasementWalls.setFractalType(FastNoiseLiteD.FractalType.PingPong);
    this.noiseBasementWalls.setCellularDistanceFunction(FastNoiseLiteD.CellularDistanceFunction.Manhattan);
    this.noiseBasementWalls.setCellularReturnType(FastNoiseLiteD.CellularReturnType.Distance);
    this.noiseMoist.setFrequency(this.noise_moist_freq.get());
    this.noiseMoist.setFractalOctaves(this.noise_moist_octave.get());
    this.noiseMoist.setFractalGain(this.noise_moist_gain.get());
    this.noiseWell.setFrequency(this.noise_well_freq.get());
  }
  
  public class BasementData implements PreGenData {
    public final double valueWall;
    
    public final double valueMoistA;
    
    public final double valueMoistB;
    
    public boolean isWall;
    
    public boolean isWet;
    
    public BasementData(double valueWall, double valueMoistA, double valueMoistB) {
      this.valueWall = valueWall;
      this.valueMoistA = valueMoistA;
      this.valueMoistB = valueMoistB;
      this.isWall = (valueWall > Gen_001.this.thresh_wall.get());
      double thresh_moist = Gen_001.this.thresh_moist.get();
      this.isWet = (valueMoistA > thresh_moist || valueMoistB > thresh_moist);
    }
  }
  
  public void pregenerate(Map<Iab, BasementData> data, int chunkX, int chunkZ) {
    for (int iz = 0; iz < 16; iz++) {
      int zz = chunkZ * 16 + iz;
      for (int ix = 0; ix < 16; ix++) {
        int xx = chunkX * 16 + ix;
        double valueWall = this.noiseBasementWalls.getNoiseRot(xx, zz, 0.25D);
        double valueMoistA = this.noiseMoist.getNoise(xx, zz);
        double valueMoistB = this.noiseMoist.getNoise(zz, xx);
        BasementData dao = new BasementData(valueWall, valueMoistA, valueMoistB);
        data.put(new Iab(ix, iz), dao);
      } 
    } 
  }
  
  public void generate(PreGenData pregen, ChunkGenerator.ChunkData chunk, LinkedList<BlockPlotter> plots, int chunkX, int chunkZ) {
    BlockData block_wall = StringToBlockData(this.block_wall, "minecraft:mud_bricks");
    BlockData block_subfloor = StringToBlockData(this.block_subfloor, "minecraft:dirt");
    BlockData block_floor_dry = StringToBlockData(this.block_floor_dry, "minecraft:brown_concrete_powder");
    BlockData block_floor_wet = StringToBlockData(this.block_floor_wet, "minecraft:brown_concrete");
    if (block_wall == null)
      throw new RuntimeException("Invalid block type for level 1 Wall"); 
    if (block_subfloor == null)
      throw new RuntimeException("Invalid block type for level 1 SubFloor"); 
    if (block_floor_dry == null)
      throw new RuntimeException("Invalid block type for level 1 Floor-Dry"); 
    if (block_floor_wet == null)
      throw new RuntimeException("Invalid block type for level 1 Floor-Wet"); 
    HashMap<Iab, BasementData> basementData = ((Level_000.PregenLevel0)pregen).basement;
    int y = this.level_y + 3 + 1;
    int h = this.level_h + 1;
    for (int iz = 0; iz < 16; iz++) {
      int zz = chunkZ * 16 + iz;
      for (int ix = 0; ix < 16; ix++) {
        int xx = chunkX * 16 + ix;
        chunk.setBlock(ix, this.level_y, iz, Material.BEDROCK);
        int yy;
        for (yy = 0; yy < 3; yy++)
          chunk.setBlock(ix, this.level_y + yy + 1, iz, block_subfloor); 
        BasementData dao = basementData.get(new Iab(ix, iz));
        if (dao != null)
          if (dao.isWall) {
            for (yy = 0; yy < h; yy++) {
              if (yy > 6) {
                chunk.setBlock(ix, y + yy, iz, Material.BEDROCK);
              } else {
                chunk.setBlock(ix, y + yy, iz, block_wall);
              } 
            } 
          } else {
            if (dao.isWet) {
              chunk.setBlock(ix, y, iz, block_floor_wet);
            } else {
              chunk.setBlock(ix, y, iz, block_floor_dry);
            } 
            int modX10 = Math.abs(xx) % 10;
            int modZ10 = Math.abs(zz) % 10;
            if (modZ10 == 0 && (
              modX10 < 3 || modX10 > 7)) {
              int iy;
              chunk.setBlock(ix, y + 6, iz, Material.REDSTONE_LAMP);
              switch (modX10) {
                case 0:
                  chunk.setBlock(ix, y + 6 + 1, iz, Material.BEDROCK);
                  break;
                case 1:
                case 9:
                  chunk.setBlock(ix, y + 6 + 1, iz, Material.REDSTONE_WIRE);
                  break;
                case 2:
                case 8:
                  for (iy = 0; iy < 5; iy++)
                    chunk.setBlock(ix, y + iy + 6 + 1, iz, Material.CHAIN); 
                  break;
              } 
            } 
          }  
      } 
    } 
  }
  
  protected void loadConfig() {
    ConfigurationSection cfg = this.plugin.getLevelParams(1);
    this.noise_wall_freq.set(cfg.getDouble("Noise-Wall-Freq"));
    this.noise_wall_octave.set(cfg.getInt("Noise-Wall-Octave"));
    this.noise_wall_gain.set(cfg.getDouble("Noise-Wall-Gain"));
    this.noise_wall_strength.set(cfg.getDouble("Noise-Wall-Strength"));
    this.noise_moist_freq.set(cfg.getDouble("Noise-Moist-Freq"));
    this.noise_moist_octave.set(cfg.getInt("Noise-Moist-Octave"));
    this.noise_moist_gain.set(cfg.getDouble("Noise-Moist-Gain"));
    this.noise_well_freq.set(cfg.getDouble("Noise-Well-Freq"));
    this.thresh_wall.set(cfg.getDouble("Thresh-Wall"));
    this.thresh_moist.set(cfg.getDouble("Thresh-Moist"));
    this.thresh_well.set(cfg.getDouble("Thresh-Well"));
    cfg = this.plugin.getLevelBlocks(1);
    this.block_wall.set(cfg.getString("Wall"));
    this.block_subfloor.set(cfg.getString("SubFloor"));
    this.block_floor_dry.set(cfg.getString("Floor-Dry"));
    this.block_floor_wet.set(cfg.getString("Floor-Wet"));
  }
  
  public static void ConfigDefaults(FileConfiguration cfg) {
    cfg.addDefault("Level1.Params.Noise-Wall-Freq", Double.valueOf(0.033D));
    cfg.addDefault("Level1.Params.Noise-Wall-Octave", Integer.valueOf(2));
    cfg.addDefault("Level1.Params.Noise-Wall-Gain", Double.valueOf(0.03D));
    cfg.addDefault("Level1.Params.Noise-Wall-Strength", Double.valueOf(1.2D));
    cfg.addDefault("Level1.Params.Noise-Moist-Freq", Double.valueOf(0.015D));
    cfg.addDefault("Level1.Params.Noise-Moist-Octave", Integer.valueOf(2));
    cfg.addDefault("Level1.Params.Noise-Moist-Gain", Double.valueOf(2.0D));
    cfg.addDefault("Level1.Params.Noise-Well-Freq", Double.valueOf(0.0028D));
    cfg.addDefault("Level1.Params.Thresh-Wall", Double.valueOf(0.9D));
    cfg.addDefault("Level1.Params.Thresh-Moist", Double.valueOf(0.4D));
    cfg.addDefault("Level1.Params.Thresh-Well", Double.valueOf(0.9D));
    cfg.addDefault("Level1.Blocks.Wall", "minecraft:mud_bricks");
    cfg.addDefault("Level1.Blocks.SubFloor", "minecraft:dirt");
    cfg.addDefault("Level1.Blocks.Floor-Dry", "minecraft:brown_concrete_powder");
    cfg.addDefault("Level1.Blocks.Floor-Wet", "minecraft:brown_concrete");
  }
}
