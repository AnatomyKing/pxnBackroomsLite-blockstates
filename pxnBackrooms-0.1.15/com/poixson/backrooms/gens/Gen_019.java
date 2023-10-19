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
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;

public class Gen_019 extends BackroomsGen {
  public static final double DEFAULT_NOISE_LAMPS_FREQ = 0.045D;
  
  public static final String DEFAULT_BLOCK_WALL = "minecraft:spruce_planks";
  
  public static final String DEFAULT_BLOCK_FLOOR = "minecraft:spruce_planks";
  
  public static final String DEFAULT_BLOCK_BEAM = "minecraft:spruce_wood";
  
  public final FastNoiseLiteD noiseLamps;
  
  public final AtomicDouble noise_lamps_freq = new AtomicDouble(0.045D);
  
  public final AtomicReference<String> block_wall = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_floor = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_beam = new AtomicReference<>(null);
  
  public Gen_019(BackroomsLevel backlevel, int level_y, int level_h) {
    super(backlevel, level_y, level_h);
    this.noiseLamps = register(new FastNoiseLiteD());
  }
  
  public void initNoise() {
    this.noiseLamps.setFrequency(this.noise_lamps_freq.get());
  }
  
  public void generate(PreGenData pregen, ChunkGenerator.ChunkData chunk, LinkedList<BlockPlotter> plots, int chunkX, int chunkZ) {
    BlockData block_wall = StringToBlockData(this.block_wall, "minecraft:spruce_planks");
    BlockData block_floor = StringToBlockData(this.block_floor, "minecraft:spruce_planks");
    BlockData block_beam = StringToBlockData(this.block_beam, "minecraft:spruce_wood");
    if (block_wall == null)
      throw new RuntimeException("Invalid block type for level 19 Wall"); 
    if (block_floor == null)
      throw new RuntimeException("Invalid block type for level 19 Floor"); 
    if (block_beam == null)
      throw new RuntimeException("Invalid block type for level 19 Beam"); 
    HashMap<Iab, Gen_000.LobbyData> lobbyData = ((Level_000.PregenLevel0)pregen).lobby;
    int y = this.level_y + 3 + 1;
    for (int iz = 0; iz < 16; iz++) {
      int zz = chunkZ * 16 + iz;
      for (int ix = 0; ix < 16; ix++) {
        int xx = chunkX * 16 + ix;
        Gen_000.LobbyData dao = lobbyData.get(new Iab(ix, iz));
        if (dao != null) {
          int modX7 = ((xx < 0) ? (1 - xx) : xx) % 7;
          int modZ7 = ((zz < 0) ? (1 - zz) : zz) % 7;
          if (modX7 == 0 || modZ7 == 0)
            chunk.setBlock(ix, this.level_y + dao.wall_dist + 3, iz, block_beam); 
          if (modX7 == 0 && modZ7 == 0 && dao.wall_dist == 6) {
            double valueLamp = this.noiseLamps.getNoise(xx, zz);
            if (valueLamp < 0.0D)
              chunk.setBlock(ix, this.level_y + dao.wall_dist + 2, iz, Material.LANTERN); 
          } 
          chunk.setBlock(ix, this.level_y, iz, Material.BEDROCK);
          int iy;
          for (iy = 0; iy < 3; iy++)
            chunk.setBlock(ix, this.level_y + iy + 1, iz, block_floor); 
          if (dao.isWall)
            for (iy = 0; iy < this.level_h + 1; iy++) {
              if (iy > 6) {
                chunk.setBlock(ix, y + iy, iz, Material.BEDROCK);
              } else {
                chunk.setBlock(ix, y + iy, iz, block_wall);
              } 
            }  
        } 
      } 
    } 
  }
  
  protected void loadConfig() {
    ConfigurationSection cfg = this.plugin.getLevelParams(19);
    this.noise_lamps_freq.set(cfg.getDouble("Noise-Lamps-Freq"));
    cfg = this.plugin.getLevelBlocks(19);
    this.block_wall.set(cfg.getString("Wall"));
    this.block_floor.set(cfg.getString("Floor"));
    this.block_beam.set(cfg.getString("Beam"));
  }
  
  public static void ConfigDefaults(FileConfiguration cfg) {
    cfg.addDefault("Level19.Params.Noise-Lamps-Freq", Double.valueOf(0.045D));
    cfg.addDefault("Level19.Blocks.Wall", "minecraft:spruce_planks");
    cfg.addDefault("Level19.Blocks.Floor", "minecraft:spruce_planks");
    cfg.addDefault("Level19.Blocks.Beam", "minecraft:spruce_wood");
  }
}
