package com.poixson.backrooms.gens;

import com.poixson.backrooms.BackroomsGen;
import com.poixson.backrooms.BackroomsLevel;
import com.poixson.backrooms.PreGenData;
import com.poixson.backrooms.worlds.Level_000;
import com.poixson.commonmc.tools.DelayedChestFiller;
import com.poixson.commonmc.tools.plotter.BlockPlotter;
import com.poixson.commonmc.tools.plotter.PlotterFactory;
import com.poixson.tools.abstractions.AtomicDouble;
import com.poixson.tools.dao.Iab;
import com.poixson.tools.dao.Iabc;
import com.poixson.utils.FastNoiseLiteD;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.type.Barrel;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Gen_000 extends BackroomsGen {
  public static final double DEFAULT_NOISE_WALL_FREQ = 0.022D;
  
  public static final int DEFAULT_NOISE_WALL_OCTAVE = 2;
  
  public static final double DEFAULT_NOISE_WALL_GAIN = 0.1D;
  
  public static final double DEFAULT_NOISE_WALL_LACUN = 0.4D;
  
  public static final double DEFAULT_NOISE_WALL_STRENGTH = 2.28D;
  
  public static final double DEFAULT_NOISE_LOOT_FREQ = 0.1D;
  
  public static final double DEFAULT_THRESH_WALL_L = 0.38D;
  
  public static final double DEFAULT_THRESH_WALL_H = 0.5D;
  
  public static final double DEFAULT_THRESH_LOOT = 0.65D;
  
  public static final int WALL_SEARCH_DIST = 6;
  
  public static final String DEFAULT_BLOCK_WALL = "minecraft:yellow_terracotta";
  
  public static final String DEFAULT_BLOCK_WALL_BASE = "minecraft:orange_terracotta";
  
  public static final String DEFAULT_BLOCK_SUBFLOOR = "minecraft:oak_planks";
  
  public static final String DEFAULT_BLOCK_SUBCEILING = "minecraft:oak_planks";
  
  public static final String DEFAULT_BLOCK_CARPET = "minecraft:light_gray_wool";
  
  public static final String DEFAULT_BLOCK_CEILING = "minecraft:smooth_stone_slab[type=top]";
  
  public final FastNoiseLiteD noiseLobbyWalls;
  
  public final FastNoiseLiteD noiseLoot;
  
  public final AtomicDouble noise_wall_freq = new AtomicDouble(0.022D);
  
  public final AtomicInteger noise_wall_octave = new AtomicInteger(2);
  
  public final AtomicDouble noise_wall_gain = new AtomicDouble(0.1D);
  
  public final AtomicDouble noise_wall_lacun = new AtomicDouble(0.4D);
  
  public final AtomicDouble noise_wall_strength = new AtomicDouble(2.28D);
  
  public final AtomicDouble noise_loot_freq = new AtomicDouble(0.1D);
  
  public final AtomicDouble thresh_wall_L = new AtomicDouble(0.38D);
  
  public final AtomicDouble thresh_wall_H = new AtomicDouble(0.5D);
  
  public final AtomicDouble thresh_loot = new AtomicDouble(0.65D);
  
  public final AtomicReference<String> block_wall = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_wall_base = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_subfloor = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_subceiling = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_carpet = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_ceiling = new AtomicReference<>(null);
  
  public Gen_000(BackroomsLevel backlevel, int level_y, int level_h) {
    super(backlevel, level_y, level_h);
    this.noiseLobbyWalls = register(new FastNoiseLiteD());
    this.noiseLoot = register(new FastNoiseLiteD());
  }
  
  public void initNoise() {
    this.noiseLobbyWalls.setFrequency(this.noise_wall_freq.get());
    this.noiseLobbyWalls.setFractalOctaves(this.noise_wall_octave.get());
    this.noiseLobbyWalls.setFractalGain(this.noise_wall_gain.get());
    this.noiseLobbyWalls.setFractalLacunarity(this.noise_wall_lacun.get());
    this.noiseLobbyWalls.setFractalPingPongStrength(this.noise_wall_strength.get());
    this.noiseLobbyWalls.setNoiseType(FastNoiseLiteD.NoiseType.Cellular);
    this.noiseLobbyWalls.setFractalType(FastNoiseLiteD.FractalType.PingPong);
    this.noiseLobbyWalls.setCellularDistanceFunction(FastNoiseLiteD.CellularDistanceFunction.Manhattan);
    this.noiseLobbyWalls.setCellularReturnType(FastNoiseLiteD.CellularReturnType.Distance);
    this.noiseLoot.setFrequency(this.noise_loot_freq.get());
  }
  
  public class LobbyData implements PreGenData {
    public final double valueWall;
    
    public final boolean isWall;
    
    public boolean wall_n = false;
    
    public boolean wall_s = false;
    
    public boolean wall_e = false;
    
    public boolean wall_w = false;
    
    public int wall_dist = 6;
    
    public int boxed = 0;
    
    public BlockFace box_dir = null;
    
    public LobbyData(double valueWall) {
      this.valueWall = valueWall;
      this
        
        .isWall = (valueWall > Gen_000.this.thresh_wall_L.get() && valueWall < Gen_000.this.thresh_wall_H.get());
    }
  }
  
  public void pregenerate(Map<Iab, LobbyData> data, int chunkX, int chunkZ) {
    for (int iz = -1; iz < 17; iz++) {
      int zz = chunkZ * 16 + iz;
      for (int ix = -1; ix < 17; ix++) {
        int xx = chunkX * 16 + ix;
        double valueWall = this.noiseLobbyWalls.getNoiseRot(xx, zz, 0.25D);
        LobbyData dao = new LobbyData(valueWall);
        data.put(new Iab(ix, iz), dao);
      } 
    } 
    for (int i = 0; i < 16; i++) {
      for (int ix = 0; ix < 16; ix++) {
        LobbyData dao = data.get(new Iab(ix, i));
        if (dao.isWall) {
          dao.wall_dist = 0;
        } else {
          for (int j = 1; j < 6; j++) {
            dao.boxed = 0;
            LobbyData dao_near = data.get(new Iab(ix, i - j));
            if (dao_near != null && dao_near.isWall) {
              if (dao.wall_dist > j)
                dao.wall_dist = j; 
              dao.wall_n = true;
              dao.boxed++;
            } 
            dao_near = data.get(new Iab(ix, i + j));
            if (dao_near != null && dao_near.isWall) {
              if (dao.wall_dist > j)
                dao.wall_dist = j; 
              dao.wall_s = true;
              dao.boxed++;
            } 
            dao_near = data.get(new Iab(ix + j, i));
            if (dao_near != null && dao_near.isWall) {
              if (dao.wall_dist > j)
                dao.wall_dist = j; 
              dao.wall_e = true;
              dao.boxed++;
            } 
            dao_near = data.get(new Iab(ix - j, i));
            if (dao_near != null && dao_near.isWall) {
              if (dao.wall_dist > j)
                dao.wall_dist = j; 
              dao.wall_w = true;
              dao.boxed++;
            } 
            if (dao.boxed > 2) {
              if (dao.wall_n || dao.wall_e) {
                dao_near = data.get(new Iab(ix + j, i - j));
                if (dao_near != null && dao_near.isWall)
                  dao.boxed++; 
              } 
              if (dao.wall_n || dao.wall_w) {
                dao_near = data.get(new Iab(ix - j, i - j));
                if (dao_near != null && dao_near.isWall)
                  dao.boxed++; 
              } 
              if (dao.wall_s || dao.wall_e) {
                dao_near = data.get(new Iab(ix + j, i + j));
                if (dao_near != null && dao_near.isWall)
                  dao.boxed++; 
              } 
              if (dao.wall_s || dao.wall_w) {
                dao_near = data.get(new Iab(ix - j, i + j));
                if (dao_near != null && dao_near.isWall)
                  dao.boxed++; 
              } 
              if (dao.boxed > 2) {
                if (!dao.wall_n) {
                  dao.box_dir = BlockFace.NORTH;
                  break;
                } 
                if (!dao.wall_s) {
                  dao.box_dir = BlockFace.SOUTH;
                  break;
                } 
                if (!dao.wall_e) {
                  dao.box_dir = BlockFace.EAST;
                  break;
                } 
                if (!dao.wall_w)
                  dao.box_dir = BlockFace.WEST; 
                break;
              } 
            } 
          } 
        } 
      } 
    } 
  }
  
  public void generate(PreGenData pregen, ChunkGenerator.ChunkData chunk, LinkedList<BlockPlotter> plots, int chunkX, int chunkZ) {
    BlockData block_wall = StringToBlockData(this.block_wall, "minecraft:yellow_terracotta");
    BlockData block_wall_base = StringToBlockData(this.block_wall_base, "minecraft:orange_terracotta");
    BlockData block_subfloor = StringToBlockData(this.block_subfloor, "minecraft:oak_planks");
    BlockData block_subceiling = StringToBlockData(this.block_subceiling, "minecraft:oak_planks");
    BlockData block_carpet = StringToBlockData(this.block_carpet, "minecraft:light_gray_wool");
    BlockData block_ceiling = StringToBlockData(this.block_ceiling, "minecraft:smooth_stone_slab[type=top]");
    BlockData block_overgrowth_wall = StringToBlockData(((Level_000)this.backlevel).gen_023.block_wall, "minecraft:moss_block");
    if (block_wall == null)
      throw new RuntimeException("Invalid block type for level 0 Wall"); 
    if (block_wall_base == null)
      throw new RuntimeException("Invalid block type for level 0 Wall-Base"); 
    if (block_subfloor == null)
      throw new RuntimeException("Invalid block type for level 0 SubFloor"); 
    if (block_subceiling == null)
      throw new RuntimeException("Invalid block type for level 0 SubCeiling"); 
    if (block_carpet == null)
      throw new RuntimeException("Invalid block type for level 0 Carpet"); 
    if (block_ceiling == null)
      throw new RuntimeException("Invalid block type for level 0 Ceiling"); 
    if (block_overgrowth_wall == null)
      throw new RuntimeException("Invalid block type for level 23 Wall"); 
    HashMap<Iab, LobbyData> lobbyData = ((Level_000.PregenLevel0)pregen).lobby;
    HashMap<Iab, Gen_001.BasementData> basementData = ((Level_000.PregenLevel0)pregen).basement;
    LinkedList<Iabc> chests = new LinkedList<>();
    int y = this.level_y + 3 + 1;
    int cy = this.level_h + y + 1;
    for (int iz = 0; iz < 16; iz++) {
      int zz = chunkZ * 16 + iz;
      for (int ix = 0; ix < 16; ix++) {
        int xx = chunkX * 16 + ix;
        chunk.setBlock(ix, this.level_y, iz, Material.BEDROCK);
        for (int iy = 0; iy < 3; iy++)
          chunk.setBlock(ix, this.level_y + iy + 1, iz, block_subfloor); 
        LobbyData dao = lobbyData.get(new Iab(ix, iz));
        if (dao != null) {
          if (dao.isWall) {
            int h = this.level_h + 3;
            chunk.setBlock(ix, y, iz, block_subfloor);
            chunk.setBlock(ix, y + 1, iz, block_wall_base);
            for (int j = 2; j < h; j++)
              chunk.setBlock(ix, y + j, iz, block_wall); 
          } else {
            chunk.setBlock(ix, y, iz, block_carpet);
            int modX7 = ((xx < 0) ? (1 - xx) : xx) % 7;
            int modZ7 = ((zz < 0) ? (0 - zz) : zz) % 7;
            if (modZ7 == 0 && modX7 < 2 && dao.wall_dist > 2) {
              chunk.setBlock(ix, cy, iz, Material.REDSTONE_LAMP);
              BlockData block = chunk.getBlockData(ix, cy, iz);
              ((Lightable)block).setLit(true);
              chunk.setBlock(ix, cy, iz, block);
              chunk.setBlock(ix, cy + 1, iz, Material.REDSTONE_BLOCK);
            } else {
              chunk.setBlock(ix, cy, iz, block_ceiling);
              chunk.setBlock(ix, cy + 1, iz, block_subceiling);
            } 
            if (dao.boxed > 4)
              if (dao.wall_dist == 1) {
                ((Level_000)this.backlevel).loot_chests_0.add(xx, zz);
                chunk.setBlock(ix, y + 1, iz, Material.BARREL);
                Barrel barrel = (Barrel)chunk.getBlockData(ix, y + 1, iz);
                barrel.setFacing(BlockFace.UP);
                chunk.setBlock(ix, y + 1, iz, (BlockData)barrel);
                chests.add(new Iabc(xx, y + 1, zz));
              } else if (dao.boxed == 7 && dao.wall_dist == 2 && dao.box_dir != null) {
                boolean found_basement_wall = false;
                for (int izb = -2; izb < 3; izb++) {
                  for (int ixb = -2; ixb < 3; ixb++) {
                    Gen_001.BasementData base = basementData.get(new Iab(ixb + ix, izb + iz));
                    if (base != null && base.isWall) {
                      found_basement_wall = true;
                      break;
                    } 
                  } 
                } 
                if (!found_basement_wall) {
                  ((Level_000)this.backlevel).portal_0_to_1.add(xx, zz);
                  int h = 5 + this.level_h + 9 + 6;
                  PlotterFactory factory = (new PlotterFactory()).placer(chunk).axis("use").rotate(dao.box_dir).y(cy - h + 1).whd(5, h, 6);
                  switch (dao.box_dir) {
                    case NORTH:
                      factory.xz(ix - 3, iz - 4);
                      break;
                    case SOUTH:
                      factory.xz(ix, iz);
                      break;
                    case EAST:
                      factory.xz(ix - 2, iz - 2);
                      break;
                    case WEST:
                      factory.xz(ix - 4, iz - 3);
                      break;
                    default:
                      throw new RuntimeException("Unknown boxed walls direction: " + dao.box_dir.toString());
                  } 
                  BlockPlotter plot = factory.build();
                  plot.type('.', Material.AIR);
                  plot.type('=', block_wall);
                  plot.type('x', Material.BEDROCK);
                  plot.type('g', Material.GLOWSTONE);
                  plot.type('m', block_overgrowth_wall);
                  StringBuilder[][] matrix = plot.getMatrix3D();
                  matrix[0][0].append("xxxxx");
                  matrix[0][1].append("xxxxx");
                  matrix[0][2].append("xxxxx");
                  matrix[0][3].append("xxgxx");
                  matrix[0][4].append("xg.gx");
                  matrix[0][5].append("xxgxx");
                  int j = 0;
                  int k;
                  for (k = 1; k < 3; k++) {
                    j++;
                    matrix[j][1].append(" xxx");
                    matrix[j][2].append(" x.x");
                    matrix[j][3].append(" x.x");
                    matrix[j][4].append(" x.x");
                    matrix[j][5].append(" xxx");
                  } 
                  j++;
                  matrix[j][1].append(" xxx");
                  matrix[j][2].append(" x.x");
                  matrix[j][3].append(" xxx");
                  matrix[j][4].append(" xxx");
                  matrix[j][5].append(" xxx");
                  j++;
                  matrix[j][1].append(" xxx");
                  matrix[j][2].append(" x.x");
                  matrix[j][3].append(" xxx");
                  for (k = -1; k < 5; k++) {
                    j++;
                    matrix[j][0].append("mmmmm");
                    matrix[j][1].append("mxxxm");
                    matrix[j][2].append("mx.xm");
                    matrix[j][3].append("mxxxm");
                    matrix[j][4].append("mmmmm");
                  } 
                  int hh = 8;
                  int m;
                  for (m = 0; m < 8; m++) {
                    j++;
                    matrix[j][1].append(" xxx");
                    matrix[j][2].append(" x.x");
                    matrix[j][3].append(" xxx");
                  } 
                  for (m = 0; m < 5; m++) {
                    j++;
                    matrix[j][0].append("=====");
                    matrix[j][1].append("=xxx=");
                    matrix[j][2].append("=x.x=");
                    matrix[j][3].append("=x.x=");
                    matrix[j][4].append("==.==");
                  } 
                  j++;
                  matrix[j][0].append("=====");
                  matrix[j][1].append("=xxx=");
                  matrix[j][2].append("=xxx=");
                  matrix[j][3].append("=xxx=");
                  matrix[j][4].append("== ==");
                  plots.add(plot);
                } 
              }  
          } 
          for (int i = 1; i < 3; i++)
            chunk.setBlock(ix, cy + i + 1, iz, block_subceiling); 
        } 
      } 
    } 
    if (!chests.isEmpty())
      for (Iabc loc : chests)
        (new ChestFiller_000((JavaPlugin)this.plugin, "level0", loc.a, loc.b, loc.c))
          .start();  
  }
  
  public class ChestFiller_000 extends DelayedChestFiller {
    public ChestFiller_000(JavaPlugin plugin, String worldName, int x, int y, int z) {
      super(plugin, worldName, x, y, z);
    }
    
    public void fill(Inventory chest) {
      ItemStack item = new ItemStack(Material.BREAD);
      Location loc = chest.getLocation();
      int xx = loc.getBlockX();
      int zz = loc.getBlockZ();
      for (int i = 0; i < 27; i++) {
        int x = xx + i % 9;
        int y = zz + Math.floorDiv(i, 9);
        double value = Gen_000.this.noiseLoot.getNoise(x, y);
        if (value > Gen_000.this.thresh_loot.get())
          chest.setItem(i, item); 
      } 
    }
  }
  
  protected void loadConfig() {
    ConfigurationSection cfg = this.plugin.getLevelParams(0);
    this.noise_wall_freq.set(cfg.getDouble("Noise-Wall-Freq"));
    this.noise_wall_octave.set(cfg.getInt("Noise-Wall-Octave"));
    this.noise_wall_gain.set(cfg.getDouble("Noise-Wall-Gain"));
    this.noise_wall_lacun.set(cfg.getDouble("Noise-Wall-Lacun"));
    this.noise_wall_strength.set(cfg.getDouble("Noise-Wall-Strength"));
    this.noise_loot_freq.set(cfg.getDouble("Noise-Loot-Freq"));
    this.thresh_wall_L.set(cfg.getDouble("Thresh-Wall-L"));
    this.thresh_wall_H.set(cfg.getDouble("Thresh-Wall-H"));
    this.thresh_loot.set(cfg.getDouble("Thresh-Loot"));
    cfg = this.plugin.getLevelBlocks(0);
    this.block_wall.set(cfg.getString("Wall"));
    this.block_wall_base.set(cfg.getString("Wall-Base"));
    this.block_subfloor.set(cfg.getString("SubFloor"));
    this.block_subceiling.set(cfg.getString("SubCeiling"));
    this.block_carpet.set(cfg.getString("Carpet"));
    this.block_ceiling.set(cfg.getString("Ceiling"));
  }
  
  public static void ConfigDefaults(FileConfiguration cfg) {
    cfg.addDefault("Level0.Params.Noise-Wall-Freq", Double.valueOf(0.022D));
    cfg.addDefault("Level0.Params.Noise-Wall-Octave", Integer.valueOf(2));
    cfg.addDefault("Level0.Params.Noise-Wall-Gain", Double.valueOf(0.1D));
    cfg.addDefault("Level0.Params.Noise-Wall-Lacun", Double.valueOf(0.4D));
    cfg.addDefault("Level0.Params.Noise-Wall-Strength", Double.valueOf(2.28D));
    cfg.addDefault("Level0.Params.Noise-Loot-Freq", Double.valueOf(0.1D));
    cfg.addDefault("Level0.Params.Thresh-Wall-L", Double.valueOf(0.38D));
    cfg.addDefault("Level0.Params.Thresh-Wall-H", Double.valueOf(0.5D));
    cfg.addDefault("Level0.Params.Thresh-Loot", Double.valueOf(0.65D));
    cfg.addDefault("Level0.Blocks.Wall", "minecraft:yellow_terracotta");
    cfg.addDefault("Level0.Blocks.Wall-Base", "minecraft:orange_terracotta");
    cfg.addDefault("Level0.Blocks.SubFloor", "minecraft:oak_planks");
    cfg.addDefault("Level0.Blocks.SubCeiling", "minecraft:oak_planks");
    cfg.addDefault("Level0.Blocks.Carpet", "minecraft:light_gray_wool");
    cfg.addDefault("Level0.Blocks.Ceiling", "minecraft:smooth_stone_slab[type=top]");
  }
}
