package com.poixson.backrooms.gens;

import com.poixson.backrooms.BackroomsGen;
import com.poixson.backrooms.BackroomsLevel;
import com.poixson.backrooms.PreGenData;
import com.poixson.backrooms.worlds.Level_000;
import com.poixson.commonmc.tools.plotter.BlockPlotter;
import com.poixson.tools.dao.Iab;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;

public class Gen_023 extends BackroomsGen {
  public static final String DEFAULT_BLOCK_WALL = "minecraft:moss_block";
  
  public static final String DEFAULT_BLOCK_FLOOR = "minecraft:mossy_cobblestone";
  
  public static final String DEFAULT_BLOCK_CEILING = "minecraft:smooth_stone_slab[type=top]";
  
  public static final String DEFAULT_BLOCK_SUBFLOOR = "minecraft:dirt";
  
  public static final String DEFAULT_BLOCK_SUBCEILING = "minecraft:stone";
  
  public static final String DEFAULT_BLOCK_CARPET = "minecraft:moss_carpet";
  
  public final AtomicReference<String> block_wall = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_floor = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_ceiling = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_subfloor = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_subceiling = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_carpet = new AtomicReference<>(null);
  
  public Gen_023(BackroomsLevel backlevel, int level_y, int level_h) {
    super(backlevel, level_y, level_h);
  }
  
  public void generate(PreGenData pregen, ChunkGenerator.ChunkData chunk, LinkedList<BlockPlotter> plots, int chunkX, int chunkZ) {
    BlockData block_wall = StringToBlockData(this.block_wall, "minecraft:moss_block");
    BlockData block_floor = StringToBlockData(this.block_floor, "minecraft:mossy_cobblestone");
    BlockData block_ceiling = StringToBlockData(this.block_ceiling, "minecraft:smooth_stone_slab[type=top]");
    BlockData block_subfloor = StringToBlockData(this.block_subfloor, "minecraft:dirt");
    BlockData block_subceiling = StringToBlockData(this.block_subceiling, "minecraft:stone");
    BlockData block_carpet = StringToBlockData(this.block_carpet, "minecraft:moss_carpet");
    if (block_wall == null)
      throw new RuntimeException("Invalid block type for level 23 Wall"); 
    if (block_floor == null)
      throw new RuntimeException("Invalid block type for level 23 Floor"); 
    if (block_ceiling == null)
      throw new RuntimeException("Invalid block type for level 23 Ceiling"); 
    if (block_subfloor == null)
      throw new RuntimeException("Invalid block type for level 23 SubFloor"); 
    if (block_subceiling == null)
      throw new RuntimeException("Invalid block type for level 23 SubCeiling"); 
    if (block_carpet == null)
      throw new RuntimeException("Invalid block type for level 23 Carpet"); 
    Level_000.PregenLevel0 pregen0 = (Level_000.PregenLevel0)pregen;
    HashMap<Iab, Gen_000.LobbyData> lobbyData = pregen0.lobby;
    HashMap<Iab, Gen_001.BasementData> basementData = pregen0.basement;
    int y = this.level_y + 3 + 1;
    int cy = this.level_y + 3 + this.level_h + 2;
    for (int iz = 0; iz < 16; iz++) {
      int zz = chunkZ * 16 + iz;
      for (int ix = 0; ix < 16; ix++) {
        int xx = chunkX * 16 + ix;
        Gen_000.LobbyData dao_lobby = lobbyData.get(new Iab(ix, iz));
        Gen_001.BasementData dao_basement = basementData.get(new Iab(ix, iz));
        chunk.setBlock(ix, this.level_y, iz, Material.BEDROCK);
        if (dao_basement.isWet) {
          chunk.setBlock(ix, this.level_y + 1, iz, Material.WATER);
        } else {
          chunk.setBlock(ix, this.level_y + 1, iz, block_subfloor);
        } 
        int iy;
        for (iy = 1; iy < 3; iy++)
          chunk.setBlock(ix, this.level_y + iy + 1, iz, block_subfloor); 
        chunk.setBlock(ix, this.level_y + 3 + 1, iz, block_floor);
        if (dao_lobby.isWall) {
          int h = this.level_h + 1;
          for (int yi = 0; yi < h; yi++)
            chunk.setBlock(ix, y + yi + 1, iz, block_wall); 
        } else {
          if (dao_basement.isWet)
            chunk.setBlock(ix, this.level_y + 3 + 2, iz, block_carpet); 
          int modX6 = Math.abs(xx) % 7;
          int modZ6 = Math.abs(zz) % 7;
          if (modZ6 == 0 && modX6 < 2 && dao_lobby.wall_dist > 1) {
            for (int i = 0; i < 4; i++) {
              chunk.setBlock(ix, cy - i - 2, iz, Material.POLISHED_DEEPSLATE_WALL);
              }
              chunk.setBlock(ix, cy, iz, Material.LANTERN);
          } else {
            chunk.setBlock(ix, cy, iz, block_ceiling);
          } 
        } 
        for (iy = 0; iy < 3; iy++)
          chunk.setBlock(ix, cy + iy + 1, iz, block_subceiling); 
      } 
    } 
  }
  
  protected void loadConfig() {
    ConfigurationSection cfg = this.plugin.getLevelBlocks(23);
    this.block_wall.set(cfg.getString("Wall"));
    this.block_floor.set(cfg.getString("Floor"));
    this.block_ceiling.set(cfg.getString("Ceiling"));
    this.block_subfloor.set(cfg.getString("SubFloor"));
    this.block_subceiling.set(cfg.getString("SubCeiling"));
    this.block_carpet.set(cfg.getString("Carpet"));
  }
  
  public static void ConfigDefaults(FileConfiguration cfg) {
    cfg.addDefault("Level23.Blocks.Wall", "minecraft:moss_block");
    cfg.addDefault("Level23.Blocks.Floor", "minecraft:mossy_cobblestone");
    cfg.addDefault("Level23.Blocks.Ceiling", "minecraft:smooth_stone_slab[type=top]");
    cfg.addDefault("Level23.Blocks.SubFloor", "minecraft:dirt");
    cfg.addDefault("Level23.Blocks.SubCeiling", "minecraft:stone");
    cfg.addDefault("Level23.Blocks.Carpet", "minecraft:moss_carpet");
  }
}
