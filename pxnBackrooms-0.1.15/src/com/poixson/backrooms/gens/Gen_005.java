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
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Lightable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;

public class Gen_005 extends BackroomsGen {
  public static final double DEFAULT_NOISE_WALL_FREQ = 0.02D;
  
  public static final double DEFAULT_NOISE_WALL_JITTER = 0.3D;
  
  public static final double DEFAULT_NOISE_ROOM_FREQ = 0.01D;
  
  public static final int DEFAULT_NOISE_ROOM_OCTAVE = 2;
  
  public static final double DEFAULT_NOISE_ROOM_GAIN = 0.6D;
  
  public static final double DEFAULT_THRESH_ROOM_HALL = 0.65D;
  
  public static final String DEFAULT_BLOCK_SUBFLOOR = "minecraft:oak_planks";
  
  public static final String DEFAULT_BLOCK_SUBCEILING = "minecraft:smooth_stone";
  
  public static final String DEFAULT_BLOCK_HALL_WALL = "minecraft:stripped_spruce_wood";
  
  public static final String DEFAULT_BLOCK_HALL_CARPET = "minecraft:black_glazed_terracotta";
  
  public static final String DEFAULT_BLOCK_HALL_CEILING = "minecraft:smooth_stone_slab[type=top]";
  
  public final FastNoiseLiteD noiseHotelWalls;
  
  public final FastNoiseLiteD noiseHotelRooms;
  
  public final FastNoiseLiteD noiseHotelStairs;
  
  public final AtomicDouble noise_wall_freq = new AtomicDouble(0.02D);
  
  public final AtomicDouble noise_wall_jitter = new AtomicDouble(0.3D);
  
  public final AtomicDouble noise_room_freq = new AtomicDouble(0.01D);
  
  public final AtomicInteger noise_room_octave = new AtomicInteger(2);
  
  public final AtomicDouble noise_room_gain = new AtomicDouble(0.6D);
  
  public final AtomicDouble thresh_room_hall = new AtomicDouble(0.65D);
  
  public final AtomicReference<String> block_subfloor = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_subceiling = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_hall_wall = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_hall_carpet = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_hall_ceiling = new AtomicReference<>(null);
  
  public Gen_005(BackroomsLevel backlevel, int level_y, int level_h) {
    super(backlevel, level_y, level_h);
    this.noiseHotelWalls = register(new FastNoiseLiteD());
    this.noiseHotelRooms = register(new FastNoiseLiteD());
    this.noiseHotelStairs = register(new FastNoiseLiteD());
  }
  
  public void initNoise() {
    this.noiseHotelWalls.setFrequency(this.noise_wall_freq.get());
    this.noiseHotelWalls.setCellularJitter(this.noise_wall_jitter.get());
    this.noiseHotelWalls.setNoiseType(FastNoiseLiteD.NoiseType.Cellular);
    this.noiseHotelWalls.setFractalType(FastNoiseLiteD.FractalType.PingPong);
    this.noiseHotelWalls.setCellularDistanceFunction(FastNoiseLiteD.CellularDistanceFunction.Manhattan);
    this.noiseHotelRooms.setFrequency(this.noise_room_freq.get());
    this.noiseHotelRooms.setFractalOctaves(this.noise_room_octave.get());
    this.noiseHotelRooms.setFractalGain(this.noise_room_gain.get());
    this.noiseHotelRooms.setFractalType(FastNoiseLiteD.FractalType.FBm);
    this.noiseHotelStairs.setFrequency(0.03D);
  }
  
  public enum NodeType {
    HALL, ROOM, WALL;
  }
  
  public class HotelData implements PreGenData {
    public final double value;
    
    public Gen_005.NodeType type;
    
    public boolean hall_center = false;
    
    public HotelData(double value) {
      this.value = value;
      double thresh_room_hall = Gen_005.this.thresh_room_hall.get();
      this.type = (value > thresh_room_hall) ? Gen_005.NodeType.HALL : Gen_005.NodeType.ROOM;
    }
  }
  
  public void pregenerate(Map<Iab, HotelData> data, int chunkX, int chunkZ) {
    for (int iz = -8; iz < 24; iz++) {
      int zz = chunkZ * 16 + iz;
      for (int ix = -8; ix < 24; ix++) {
        int xx = chunkX * 16 + ix;
        double value = this.noiseHotelWalls.getNoiseRot(xx, zz, 0.25D);
        HotelData dao = new HotelData(value);
        data.put(new Iab(ix, iz), dao);
      } 
    } 
    for (int i = -8; i < 24; i++) {
      for (int ix = -8; ix < 24; ix++) {
        HotelData dao = data.get(new Iab(ix, i));
        if (NodeType.ROOM.equals(dao.type)) {
          HotelData daoN = data.get(new Iab(ix, i - 1));
          HotelData daoS = data.get(new Iab(ix, i + 1));
          HotelData daoE = data.get(new Iab(ix + 1, i));
          HotelData daoW = data.get(new Iab(ix - 1, i));
          HotelData daoNE = data.get(new Iab(ix + 1, i - 1));
          HotelData daoNW = data.get(new Iab(ix - 1, i - 1));
          HotelData daoSE = data.get(new Iab(ix + 1, i + 1));
          HotelData daoSW = data.get(new Iab(ix - 1, i + 1));
          if (daoN != null && NodeType.HALL.equals(daoN.type)) {
            dao.type = NodeType.WALL;
            for (int j = 3; j < 9; j++) {
              daoN = data.get(new Iab(ix, i - j));
              if (daoN == null)
                break; 
              if (!NodeType.HALL.equals(daoN.type)) {
                daoN = data.get(new Iab(ix, i - Math.floorDiv(j, 2)));
                daoN.hall_center = true;
                break;
              } 
            } 
          } 
          if (daoS != null && NodeType.HALL.equals(daoS.type)) {
            dao.type = NodeType.WALL;
            for (int j = 3; j < 9; j++) {
              daoS = data.get(new Iab(ix, i + j));
              if (daoS == null)
                break; 
              if (!NodeType.HALL.equals(daoS.type)) {
                daoS = data.get(new Iab(ix, i + Math.floorDiv(j, 2)));
                daoS.hall_center = true;
                break;
              } 
            } 
          } 
          if (daoE != null && NodeType.HALL.equals(daoE.type)) {
            dao.type = NodeType.WALL;
            for (int j = 3; j < 9; j++) {
              daoE = data.get(new Iab(ix + j, i));
              if (daoE == null)
                break; 
              if (!NodeType.HALL.equals(daoE.type)) {
                daoE = data.get(new Iab(ix + Math.floorDiv(j, 2), i));
                daoE.hall_center = true;
                break;
              } 
            } 
          } 
          if (daoW != null && NodeType.HALL.equals(daoW.type)) {
            dao.type = NodeType.WALL;
            for (int j = 3; j < 9; j++) {
              daoW = data.get(new Iab(ix - j, i));
              if (daoW == null)
                break; 
              if (!NodeType.HALL.equals(daoW.type)) {
                daoW = data.get(new Iab(ix - Math.floorDiv(j, 2), i));
                daoW.hall_center = true;
                break;
              } 
            } 
          } 
          if ((daoNE != null && NodeType.HALL.equals(daoNE.type)) || (daoNW != null && NodeType.HALL
            .equals(daoNW.type)) || (daoSE != null && NodeType.HALL
            .equals(daoSE.type)) || (daoSW != null && NodeType.HALL
            .equals(daoSW.type)))
            dao.type = NodeType.WALL; 
        } 
      } 
    } 
  }
  
  public void generate(PreGenData pregen, ChunkGenerator.ChunkData chunk, LinkedList<BlockPlotter> plots, int chunkX, int chunkZ) {
    BlockData block_subfloor = StringToBlockData(this.block_subfloor, "minecraft:oak_planks");
    BlockData block_subceiling = StringToBlockData(this.block_subceiling, "minecraft:smooth_stone");
    BlockData block_hall_wall = StringToBlockData(this.block_hall_wall, "minecraft:stripped_spruce_wood");
    BlockData block_hall_carpet = StringToBlockData(this.block_hall_carpet, "minecraft:black_glazed_terracotta");
    BlockData block_hall_ceiling = StringToBlockData(this.block_hall_ceiling, "minecraft:smooth_stone_slab[type=top]");
    if (block_subfloor == null)
      throw new RuntimeException("Invalid block type for level 5 SubFloor"); 
    if (block_subceiling == null)
      throw new RuntimeException("Invalid block type for level 5 SubCeiling"); 
    if (block_hall_wall == null)
      throw new RuntimeException("Invalid block type for level 5 Hall-Wall"); 
    if (block_hall_carpet == null)
      throw new RuntimeException("Invalid block type for level 5 Hall-Carpet"); 
    if (block_hall_ceiling == null)
      throw new RuntimeException("Invalid block type for level 5 Hall-Ceiling"); 
    HashMap<Iab, HotelData> hotelData = ((Level_000.PregenLevel0)pregen).hotel;
    int y = this.level_y + 3 + 1;
    int cy = this.level_y + 3 + this.level_h + 2;
    int h = this.level_h + 2;
    for (int iz = 0; iz < 16; iz++) {
      for (int ix = 0; ix < 16; ix++) {
        int xx = chunkX * 16 + ix;
        int zz = chunkZ * 16 + iz;
        chunk.setBlock(ix, this.level_y, iz, Material.BEDROCK);
        for (int yy = 0; yy < 3; yy++)
          chunk.setBlock(ix, this.level_y + yy + 1, iz, block_subfloor); 
        HotelData dao = hotelData.get(new Iab(ix, iz));
        if (dao != null) {
          int mod_x;
          int mod_z;
          int iy;
          Directional tile;
          switch (dao.type) {
            case WALL:
              for (iy = 0; iy < h; iy++)
                chunk.setBlock(ix, y + iy, iz, block_hall_wall); 
              break;
            case HALL:
              chunk.setBlock(ix, y, iz, block_hall_carpet);
              tile = (Directional)chunk.getBlockData(ix, y, iz);
              if (iz % 2 == 0) {
                if (ix % 2 == 0) {
                  tile.setFacing(BlockFace.NORTH);
                } else {
                  tile.setFacing(BlockFace.WEST);
                } 
              } else if (ix % 2 == 0) {
                tile.setFacing(BlockFace.EAST);
              } else {
                tile.setFacing(BlockFace.SOUTH);
              } 
              chunk.setBlock(ix, y, iz, (BlockData)tile);
              mod_x = xx % 5;
              mod_z = zz % 5;
              if (dao.hall_center && ((mod_x >= 0 && mod_x < 2) || (mod_z >= 1 && mod_z < 4))) {
                chunk.setBlock(ix, cy, iz, Material.REDSTONE_LAMP);
                Lightable lamp = (Lightable)chunk.getBlockData(ix, cy, iz);
                lamp.setLit(true);
                chunk.setBlock(ix, cy, iz, (BlockData)lamp);
                chunk.setBlock(ix, cy + 1, iz, Material.REDSTONE_BLOCK);
                break;
              } 
              if (NodeType.WALL.equals(((HotelData)hotelData.get(new Iab(ix, iz - 1))).type) || NodeType.WALL
                .equals(((HotelData)hotelData.get(new Iab(ix, iz + 1))).type) || NodeType.WALL
                .equals(((HotelData)hotelData.get(new Iab(ix + 1, iz))).type) || NodeType.WALL
                .equals(((HotelData)hotelData.get(new Iab(ix - 1, iz))).type)) {
                chunk.setBlock(ix, cy, iz, block_subceiling);
                break;
              } 
              chunk.setBlock(ix, cy, iz, block_hall_ceiling);
              break;
            case ROOM:
              break;
            default:
              throw new RuntimeException("Unknown hotel type: " + dao.type.toString());
          } 
          for (int i = 0; i < 3; i++)
            chunk.setBlock(ix, cy + i + 1, iz, block_subceiling); 
        } 
      } 
    } 
  }
  
  protected void loadConfig() {
    ConfigurationSection cfg = this.plugin.getLevelParams(5);
    this.noise_wall_freq.set(cfg.getDouble("Noise-Wall-Freq"));
    this.noise_wall_jitter.set(cfg.getDouble("Noise-Wall-Jitter"));
    this.noise_room_freq.set(cfg.getDouble("Noise-Room-Freq"));
    this.noise_room_octave.set(cfg.getInt("Noise-Room-Octave"));
    this.noise_room_gain.set(cfg.getDouble("Noise-Room-Gain"));
    this.thresh_room_hall.set(cfg.getDouble("Thresh-Room-Or-Hall"));
    cfg = this.plugin.getLevelBlocks(5);
    this.block_subfloor.set(cfg.getString("SubFloor"));
    this.block_subceiling.set(cfg.getString("SubCeiling"));
    this.block_hall_wall.set(cfg.getString("Hall-Wall"));
    this.block_hall_carpet.set(cfg.getString("Hall-Carpet"));
    this.block_hall_ceiling.set(cfg.getString("Hall-Ceiling"));
  }
  
  public static void ConfigDefaults(FileConfiguration cfg) {
    cfg.addDefault("Level5.Params.Noise-Wall-Freq", Double.valueOf(0.02D));
    cfg.addDefault("Level5.Params.Noise-Wall-Jitter", Double.valueOf(0.3D));
    cfg.addDefault("Level5.Params.Noise-Room-Freq", Double.valueOf(0.01D));
    cfg.addDefault("Level5.Params.Noise-Room-Octave", Integer.valueOf(2));
    cfg.addDefault("Level5.Params.Noise-Room-Gain", Double.valueOf(0.6D));
    cfg.addDefault("Level5.Params.Thresh-Room-Or-Hall", Double.valueOf(0.65D));
    cfg.addDefault("Level5.Blocks.SubFloor", "minecraft:oak_planks");
    cfg.addDefault("Level5.Blocks.SubCeiling", "minecraft:smooth_stone");
    cfg.addDefault("Level5.Blocks.Hall-Wall", "minecraft:stripped_spruce_wood");
    cfg.addDefault("Level5.Blocks.Hall-Carpet", "minecraft:black_glazed_terracotta");
    cfg.addDefault("Level5.Blocks.Hall-Ceiling", "minecraft:smooth_stone_slab[type=top]");
  }
}
