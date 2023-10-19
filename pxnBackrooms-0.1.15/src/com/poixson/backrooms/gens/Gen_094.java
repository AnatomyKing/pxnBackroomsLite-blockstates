package com.poixson.backrooms.gens;

import com.poixson.backrooms.BackroomsGen;
import com.poixson.backrooms.BackroomsLevel;
import com.poixson.backrooms.PreGenData;
import com.poixson.backrooms.worlds.Level_094;
import com.poixson.commonmc.tools.plotter.BlockPlotter;
import com.poixson.commonmc.tools.plotter.PlotterFactory;
import com.poixson.tools.abstractions.AtomicDouble;
import com.poixson.tools.dao.Iab;
import com.poixson.utils.FastNoiseLiteD;
import com.poixson.utils.RandomUtils;
import com.poixson.utils.StringUtils;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;

public class Gen_094 extends BackroomsGen {
  public static final double DEFAULT_NOISE_HILLS_FREQ = 0.015D;
  
  public static final int DEFAULT_NOISE_HILLS_OCTAVE = 2;
  
  public static final double DEFAULT_NOISE_HILLS_STRENGTH = 1.8D;
  
  public static final double DEFAULT_NOISE_HILLS_LACUN = 0.8D;
  
  public static final double DEFAULT_NOISE_HOUSE_FREQ = 0.07D;
  
  public static final double DEFAULT_VALLEY_DEPTH = 0.33D;
  
  public static final double DEFAULT_VALLEY_GAIN = 0.3D;
  
  public static final double DEFAULT_HILLS_GAIN = 12.0D;
  
  public static final double DEFAULT_ROSE_CHANCE = 0.01D;
  
  public static final int DEFAULT_WATER_DEPTH = 3;
  
  public static final int DEFAULT_HOUSE_WIDTH = 8;
  
  public static final int DEFAULT_HOUSE_HEIGHT = 5;
  
  public static final String DEFAULT_BLOCK_DIRT = "minecraft:dirt";
  
  public static final String DEFAULT_BLOCK_GRASS_BLOCK = "minecraft:moss_block";
  
  public static final String DEFAULT_BLOCK_GRASS_SLAB = "minecraft:mud_brick_slab";
  
  public static final String DEFAULT_BLOCK_GRASS = "minecraft:grass";
  
  public static final String DEFAULT_BLOCK_FERN = "minecraft:fern";
  
  public static final String DEFAULT_BLOCK_ROSE = "minecraft:wither_rose";
  
  public static final String DEFAULT_BLOCK_HOUSE_WALL = "minecraft:stripped_birch_wood";
  
  public static final String DEFAULT_BLOCK_HOUSE_ROOF_STAIRS = "minecraft:deepslate_tile_stairs";
  
  public static final String DEFAULT_BLOCK_HOUSE_ROOF_SOLID = "minecraft:deepslate_tiles";
  
  public static final String DEFAULT_BLOCK_HOUSE_WINDOW = "minecraft:black_stained_glass";
  
  public final FastNoiseLiteD noiseHills;
  
  public final FastNoiseLiteD noiseHouse;
  
  public final AtomicDouble noise_hills_freq = new AtomicDouble(0.015D);
  
  public final AtomicInteger noise_hills_octave = new AtomicInteger(2);
  
  public final AtomicDouble noise_hills_strength = new AtomicDouble(1.8D);
  
  public final AtomicDouble noise_hills_lacun = new AtomicDouble(0.8D);
  
  public final AtomicDouble noise_house_freq = new AtomicDouble(0.07D);
  
  public final AtomicDouble valley_depth = new AtomicDouble(0.33D);
  
  public final AtomicDouble valley_gain = new AtomicDouble(0.3D);
  
  public final AtomicDouble hills_gain = new AtomicDouble(12.0D);
  
  public final AtomicDouble rose_chance = new AtomicDouble(0.01D);
  
  public final AtomicInteger water_depth = new AtomicInteger(3);
  
  public final AtomicInteger house_width = new AtomicInteger(8);
  
  public final AtomicInteger house_height = new AtomicInteger(5);
  
  public final AtomicReference<String> block_dirt = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_grass_block = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_grass_slab = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_grass = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_fern = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_rose = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_house_wall = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_house_roof_stairs = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_house_roof_solid = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_house_window = new AtomicReference<>(null);
  
  public Gen_094(BackroomsLevel backlevel, int level_y, int level_h) {
    super(backlevel, level_y, level_h);
    this.noiseHills = register(new FastNoiseLiteD());
    this.noiseHouse = register(new FastNoiseLiteD());
  }
  
  public void initNoise() {
    this.noiseHills.setFrequency(this.noise_hills_freq.get());
    this.noiseHills.setFractalOctaves(this.noise_hills_octave.get());
    this.noiseHills.setFractalPingPongStrength(this.noise_hills_strength.get());
    this.noiseHills.setFractalLacunarity(this.noise_hills_lacun.get());
    this.noiseHills.setNoiseType(FastNoiseLiteD.NoiseType.Cellular);
    this.noiseHills.setFractalType(FastNoiseLiteD.FractalType.PingPong);
    this.noiseHouse.setFrequency(this.noise_house_freq.get());
  }
  
  public class HillsData implements PreGenData {
    public final double valueHill;
    
    public final double depth;
    
    public final double valueHouse;
    
    public boolean isHouse = false;
    
    public int house_y = 0;
    
    public boolean house_direction;
    
    public HillsData(double valueHill, double valueHouse, double valley_depth, double valley_gain, double hills_gain) {
      this.valueHill = valueHill;
      double depth = 1.0D - valueHill;
      if (depth < valley_depth)
        depth *= valley_gain; 
      this.depth = depth * hills_gain;
      this.valueHouse = valueHouse;
      this.house_direction = ((int)Math.floor(valueHouse * 100000.0D) % 2 == 1);
    }
  }
  
  public void pregenerate(Map<Iab, HillsData> data, int chunkX, int chunkZ) {
    double valley_depth = this.valley_depth.get();
    double valley_gain = this.valley_gain.get();
    double hills_gain = this.hills_gain.get();
    int house_width = this.house_width.get();
    int house_lowest_y = 7;
    for (int iz = -1; iz < 17; iz++) {
      int zz = chunkZ * 16 + iz;
      for (int ix = -1; ix < 17; ix++) {
        int xx = chunkX * 16 + ix;
        double valueHill = this.noiseHills.getNoise(xx, zz);
        double valueHouse = this.noiseHouse.getNoise(xx, zz);
        HillsData dao = new HillsData(valueHill, valueHouse, valley_depth, valley_gain, hills_gain);
        data.put(new Iab(ix, iz), dao);
      } 
    } 
    int search_width = 16 - house_width;
    int i;
    label50: for (i = 0; i < search_width; i++) {
      for (int ix = 0; ix < search_width; ix++) {
        HillsData dao = data.get(new Iab(ix, i));
        double valueHouse = dao.valueHouse;
        if (valueHouse > ((HillsData)data.get(new Iab(ix, i - 1))).valueHouse && valueHouse > ((HillsData)data
          .get(new Iab(ix, i + 1))).valueHouse && valueHouse > ((HillsData)data
          .get(new Iab(ix + 1, i))).valueHouse && valueHouse > ((HillsData)data
          .get(new Iab(ix - 1, i))).valueHouse) {
          int lowest = Integer.MAX_VALUE;
          for (int izz = 0; izz < search_width; izz++) {
            for (int ixx = 0; ixx < search_width; ixx++) {
              int depth = (int)Math.floor(((HillsData)data.get(new Iab(ix + ixx, i + izz))).depth);
              if (lowest > depth)
                lowest = depth; 
            } 
          } 
          if (lowest != Integer.MAX_VALUE) {
            if (lowest > 7) {
              dao.house_y = lowest;
              dao.isHouse = true;
              break label50;
            } 
            break label50;
          } 
          break label50;
        } 
      } 
    } 
  }
  
  public void generate(PreGenData pregen, ChunkGenerator.ChunkData chunk, LinkedList<BlockPlotter> plots, int chunkX, int chunkZ) {
    BlockData block_dirt = StringToBlockData(this.block_dirt, "minecraft:dirt");
    BlockData block_grass_block = StringToBlockData(this.block_grass_block, "minecraft:moss_block");
    BlockData block_grass_slab = StringToBlockData(this.block_grass_slab, "minecraft:mud_brick_slab");
    BlockData block_grass = StringToBlockData(this.block_grass, "minecraft:grass");
    BlockData block_fern = StringToBlockData(this.block_fern, "minecraft:fern");
    BlockData block_rose = StringToBlockData(this.block_rose, "minecraft:wither_rose");
    BlockData block_house_wall = StringToBlockData(this.block_house_wall, "minecraft:stripped_birch_wood");
    BlockData block_house_roofA = StringToBlockData(this.block_house_roof_stairs, "minecraft:deepslate_tile_stairs");
    BlockData block_house_roofB = StringToBlockData(this.block_house_roof_stairs, "minecraft:deepslate_tile_stairs");
    BlockData block_house_roof_solid = StringToBlockData(this.block_house_roof_solid, "minecraft:deepslate_tiles");
    BlockData block_house_window = StringToBlockData(this.block_house_window, "minecraft:black_stained_glass");
    if (block_dirt == null)
      throw new RuntimeException("Invalid block type for level 94 Dirt"); 
    if (block_grass_block == null)
      throw new RuntimeException("Invalid block type for level 94 Grass-Block"); 
    if (block_grass_slab == null)
      throw new RuntimeException("Invalid block type for level 94 Grass-Slab"); 
    if (block_grass == null)
      throw new RuntimeException("Invalid block type for level 94 Grass"); 
    if (block_fern == null)
      throw new RuntimeException("Invalid block type for level 94 Fern"); 
    if (block_rose == null)
      throw new RuntimeException("Invalid block type for level 94 Rose"); 
    if (block_house_wall == null)
      throw new RuntimeException("Invalid block type for level 94 House-Wall"); 
    if (block_house_roofA == null)
      throw new RuntimeException("Invalid block type for level 94 House-Roof-Stairs"); 
    if (block_house_roofB == null)
      throw new RuntimeException("Invalid block type for level 94 House-Roof-Stairs"); 
    if (block_house_roof_solid == null)
      throw new RuntimeException("Invalid block type for level 94 House-Roof-Solid"); 
    if (block_house_window == null)
      throw new RuntimeException("Invalid block type for level 94 House-Window"); 
    int depth_water = this.water_depth.get();
    double rose_chance = this.rose_chance.get();
    int house_width = this.house_width.get();
    int house_height = this.house_height.get();
    HashMap<Iab, HillsData> hillsData = ((Level_094.PregenLevel94)pregen).hills;
    int y = this.level_y + 1;
    int last_rnd = 0;
    Iab house_loc = null;
    int house_y = 0;
    boolean house_dir = false;
    for (int iz = 0; iz < 16; iz++) {
      for (int ix = 0; ix < 16; ix++) {
        chunk.setBlock(ix, this.level_y, iz, Material.BEDROCK);
        HillsData dao = hillsData.get(new Iab(ix, iz));
        int depth_dirt = (int)Math.floor(dao.depth - 0.7D);
        if (depth_dirt < 0)
          depth_dirt = 0; 
        int iy;
        for (iy = 0; iy < depth_dirt; iy++)
          chunk.setBlock(ix, y + iy, iz, block_dirt); 
        if (depth_dirt < depth_water) {
          for (iy = depth_dirt; iy < depth_water; iy++)
            chunk.setBlock(ix, y + iy, iz, Material.WATER); 
        } else if (dao.depth % 1.0D > 0.7D) {
          chunk.setBlock(ix, y + depth_dirt, iz, block_grass_slab);
        } else {
          chunk.setBlock(ix, y + depth_dirt, iz, block_grass_block);
          int mod_grass = (int)Math.floor(dao.valueHill * 1000.0D) % 3;
          int chance = (int)Math.round(1.0D / rose_chance);
          int rnd = RandomUtils.GetNewRandom(0, chance, last_rnd);
          last_rnd += rnd;
          if (rnd == 1) {
            chunk.setBlock(ix, y + depth_dirt + 1, iz, block_rose);
          } else if (mod_grass == 0) {
            chunk.setBlock(ix, y + depth_dirt + 1, iz, block_grass);
          } else if (mod_grass == 1) {
            chunk.setBlock(ix, y + depth_dirt + 1, iz, block_fern);
          } 
        } 
        if (dao.isHouse) {
          house_loc = new Iab(ix, iz);
          house_y = dao.house_y;
          house_dir = dao.house_direction;
        } 
      } 
    } 
    if (house_loc != null) {
      int house_half = Math.floorDiv(house_width, 2);
      if (house_dir) {
        ((Stairs)block_house_roofA).setFacing(BlockFace.NORTH);
        ((Stairs)block_house_roofB).setFacing(BlockFace.SOUTH);
      } else {
        ((Stairs)block_house_roofA).setFacing(BlockFace.WEST);
        ((Stairs)block_house_roofB).setFacing(BlockFace.EAST);
      } 
      BlockPlotter plot = (new PlotterFactory()).placer(chunk).axis("use").xyz(house_loc.a, y + house_y, house_loc.b).whd(house_width, house_height + house_half + 1, house_width).build();
      plot.type('#', block_house_wall);
      plot.type('<', block_house_roofA);
      plot.type('>', block_house_roofB);
      plot.type('X', block_house_roof_solid);
      plot.type('w', block_house_window);
      plot.type('.', Material.AIR);
      plot.rotation = house_dir ? BlockFace.EAST : BlockFace.SOUTH;
      StringBuilder[][] matrix = plot.getMatrix3D();
      for (int iy = 0; iy < house_height; iy++) {
        matrix[iy][0].append("#".repeat(house_width));
        matrix[iy][house_width - 1].append("#".repeat(house_width));
        for (int k = 1; k < house_width - 1; k++)
          matrix[iy][k].append('#').append(".".repeat(house_width - 2)).append('#'); 
      } 
      for (int j = 0; j < house_half; j++) {
        for (int k = 0; k < house_width; k++) {
          int yy = j + house_height;
          int fill = house_width - j * 2 - 2;
          matrix[yy][k].append(".".repeat(j)).append('>');
          if (k == 0 || k == house_width - 1) {
            if (j < house_half - 1)
              matrix[yy][k].append('X'); 
            if (fill > 2)
              matrix[yy][k].append("#".repeat(fill - 2)); 
            if (j < house_half - 1)
              matrix[yy][k].append('X'); 
          } else {
            matrix[yy][k].append(".".repeat(fill));
          } 
          matrix[yy][k].append('<').append(".".repeat(j));
        } 
      } 
      for (int i = 2; i < 4; i++) {
        StringUtils.ReplaceInString(matrix[house_height - i][0], "w", house_half - 2);
        StringUtils.ReplaceInString(matrix[house_height - i][0], "w", house_half + 1);
        StringUtils.ReplaceInString(matrix[house_height - i][house_width - 1], "w", house_half - 2);
        StringUtils.ReplaceInString(matrix[house_height - i][house_width - 1], "w", house_half + 1);
        StringUtils.ReplaceInString(matrix[house_height - i][house_half - 2], "w", 0);
        StringUtils.ReplaceInString(matrix[house_height - i][house_half - 2], "w", house_width - 1);
        StringUtils.ReplaceInString(matrix[house_height - i][house_half + 1], "w", 0);
        StringUtils.ReplaceInString(matrix[house_height - i][house_half + 1], "w", house_width - 1);
      } 
      plot.run();
    } 
  }
  
  protected void loadConfig() {
    ConfigurationSection cfg = this.plugin.getLevelParams(94);
    this.noise_hills_freq.set(cfg.getDouble("Noise-Hills-Freq"));
    this.noise_hills_octave.set(cfg.getInt("Noise-Hills-Octave"));
    this.noise_hills_strength.set(cfg.getDouble("Noise-Hills-Strength"));
    this.noise_hills_lacun.set(cfg.getDouble("Noise-Hills-Lacun"));
    this.noise_house_freq.set(cfg.getDouble("Noise-House-Freq"));
    this.valley_depth.set(cfg.getDouble("Valley-Depth"));
    this.valley_gain.set(cfg.getDouble("Valley-Gain"));
    this.hills_gain.set(cfg.getDouble("Hills-Gain"));
    this.rose_chance.set(cfg.getDouble("Rose-Chance"));
    this.water_depth.set(cfg.getInt("Water-Depth"));
    this.house_width.set(cfg.getInt("House-Width"));
    this.house_height.set(cfg.getInt("House-Height"));
    cfg = this.plugin.getLevelBlocks(94);
    this.block_dirt.set(cfg.getString("Dirt"));
    this.block_grass_block.set(cfg.getString("Grass-Block"));
    this.block_grass_slab.set(cfg.getString("Grass-Slab"));
    this.block_grass.set(cfg.getString("Grass"));
    this.block_fern.set(cfg.getString("Fern"));
    this.block_rose.set(cfg.getString("Rose"));
    this.block_house_wall.set(cfg.getString("House-Wall"));
    this.block_house_roof_stairs.set(cfg.getString("House-Roof-Stairs"));
    this.block_house_roof_solid.set(cfg.getString("House-Roof-Solid"));
    this.block_house_window.set(cfg.getString("House-Window"));
  }
  
  public static void ConfigDefaults(FileConfiguration cfg) {
    cfg.addDefault("Level94.Params.Noise-Hills-Freq", Double.valueOf(0.015D));
    cfg.addDefault("Level94.Params.Noise-Hills-Octave", Integer.valueOf(2));
    cfg.addDefault("Level94.Params.Noise-Hills-Strength", Double.valueOf(1.8D));
    cfg.addDefault("Level94.Params.Noise-Hills-Lacun", Double.valueOf(0.8D));
    cfg.addDefault("Level94.Params.Noise-House-Freq", Double.valueOf(0.07D));
    cfg.addDefault("Level94.Params.Valley-Depth", Double.valueOf(0.33D));
    cfg.addDefault("Level94.Params.Valley-Gain", Double.valueOf(0.3D));
    cfg.addDefault("Level94.Params.Hills-Gain", Double.valueOf(12.0D));
    cfg.addDefault("Level94.Params.Rose-Chance", Double.valueOf(0.01D));
    cfg.addDefault("Level94.Params.Water-Depth", Integer.valueOf(3));
    cfg.addDefault("Level94.Params.House-Width", Integer.valueOf(8));
    cfg.addDefault("Level94.Params.House-Height", Integer.valueOf(5));
    cfg.addDefault("Level94.Blocks.Dirt", "minecraft:dirt");
    cfg.addDefault("Level94.Blocks.Grass-Block", "minecraft:moss_block");
    cfg.addDefault("Level94.Blocks.Grass-Slab", "minecraft:mud_brick_slab");
    cfg.addDefault("Level94.Blocks.Grass", "minecraft:grass");
    cfg.addDefault("Level94.Blocks.Fern", "minecraft:fern");
    cfg.addDefault("Level94.Blocks.Rose", "minecraft:wither_rose");
    cfg.addDefault("Level94.Blocks.House-Wall", "minecraft:stripped_birch_wood");
    cfg.addDefault("Level94.Blocks.House-Roof-Stairs", "minecraft:deepslate_tile_stairs");
    cfg.addDefault("Level94.Blocks.House-Roof-Solid", "minecraft:deepslate_tiles");
    cfg.addDefault("Level94.Blocks.House-Window", "minecraft:black_stained_glass");
  }
}
