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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.FaceAttachable;
import org.bukkit.block.data.type.Switch;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;

public class Gen_006 extends BackroomsGen {
  public static final double DEFAULT_NOISE_SWITCH_FREQ = 0.008D;
  
  public static final int DEFAULT_NOISE_SWITCH_OCTAVE = 2;
  
  public static final String DEFAULT_BLOCK_WALL = "minecraft:glowstone";
  
  public final FastNoiseLiteD noiseLightSwitch;
  
  public final AtomicDouble noise_switch_freq = new AtomicDouble(0.008D);
  
  public final AtomicInteger noise_switch_octave = new AtomicInteger(2);
  
  public final AtomicReference<String> block_wall = new AtomicReference<>(null);
  
  public Gen_006(BackroomsLevel backlevel, int level_y, int level_h) {
    super(backlevel, level_y, level_h);
    this.noiseLightSwitch = register(new FastNoiseLiteD());
  }
  
  public void initNoise() {
    this.noiseLightSwitch.setFrequency(this.noise_switch_freq.get());
    this.noiseLightSwitch.setFractalOctaves(this.noise_switch_octave.get());
  }
  
  public void generate(PreGenData pregen, ChunkGenerator.ChunkData chunk, LinkedList<BlockPlotter> plots, int chunkX, int chunkZ) {
    BlockData block_wall = StringToBlockData(this.block_wall, "minecraft:glowstone");
    if (block_wall == null)
      throw new RuntimeException("Invalid block type for level 6 Wall"); 
    HashMap<Iab, Gen_000.LobbyData> lobbyData = ((Level_000.PregenLevel0)pregen).lobby;
    for (int iz = 0; iz < 16; iz++) {
      for (int ix = 0; ix < 16; ix++) {
        chunk.setBlock(ix, this.level_y, iz, Material.BEDROCK);
        Gen_000.LobbyData dao = lobbyData.get(new Iab(ix, iz));
        if (dao != null)
          if (dao.isWall) {
            for (int iy = 0; iy < this.level_h; iy++)
              chunk.setBlock(ix, this.level_y + iy + 1, iz, block_wall); 
            if (ix > 0 && ix < 15 && iz > 0 && iz < 15) {
              Gen_000.LobbyData daoN = lobbyData.get(new Iab(ix, iz - 1));
              Gen_000.LobbyData daoS = lobbyData.get(new Iab(ix, iz + 1));
              Gen_000.LobbyData daoE = lobbyData.get(new Iab(ix + 1, iz));
              Gen_000.LobbyData daoW = lobbyData.get(new Iab(ix - 1, iz));
              if (!daoN.isWall) {
                generateLightSwitch(BlockFace.NORTH, chunk, chunkX, chunkZ, ix, iz - 1);
              } else if (!daoS.isWall) {
                generateLightSwitch(BlockFace.SOUTH, chunk, chunkX, chunkZ, ix, iz + 1);
              } else if (!daoE.isWall) {
                generateLightSwitch(BlockFace.EAST, chunk, chunkX, chunkZ, ix + 1, iz);
              } else if (!daoW.isWall) {
                generateLightSwitch(BlockFace.WEST, chunk, chunkX, chunkZ, ix - 1, iz);
              } 
            } 
          }  
      } 
    } 
  }
  
  protected void generateLightSwitch(BlockFace facing, ChunkGenerator.ChunkData chunk, int chunkX, int chunkZ, int ix, int iz) {
    int xx = chunkX * 16 + ix;
    int zz = chunkZ * 16 + iz;
    double value = this.noiseLightSwitch.getNoise(xx, zz);
    if (value > this.noiseLightSwitch.getNoise(xx, (zz - 1)) && value > this.noiseLightSwitch
      .getNoise(xx, (zz + 1)) && value > this.noiseLightSwitch
      .getNoise((xx + 1), zz) && value > this.noiseLightSwitch
      .getNoise((xx - 1), zz)) {
      ((Level_000)this.backlevel).portal_0_to_6.add(xx, zz);
      int y = ((Level_000)this.backlevel).gen_000.level_y + 3 + 3;
      chunk.setBlock(ix, y, iz, Material.LEVER);
      Switch lever = (Switch)chunk.getBlockData(ix, y, iz);
      lever.setAttachedFace(FaceAttachable.AttachedFace.WALL);
      lever.setFacing(facing);
      lever.setPowered(false);
      chunk.setBlock(ix, y, iz, (BlockData)lever);
      y = this.level_y + 2;
      chunk.setBlock(ix, y, iz, Material.LEVER);
      lever = (Switch)chunk.getBlockData(ix, y, iz);
      lever.setAttachedFace(FaceAttachable.AttachedFace.WALL);
      lever.setFacing(facing);
      lever.setPowered(true);
      chunk.setBlock(ix, y, iz, (BlockData)lever);
    } 
  }
  
  protected void loadConfig() {
    ConfigurationSection cfg = this.plugin.getLevelParams(6);
    this.noise_switch_freq.set(cfg.getDouble("Noise-Switch-Freq"));
    this.noise_switch_octave.set(cfg.getInt("Noise-Switch-Octave"));
    cfg = this.plugin.getLevelBlocks(6);
    this.block_wall.set(cfg.getString("Wall"));
  }
  
  public static void ConfigDefaults(FileConfiguration cfg) {
    cfg.addDefault("Level6.Params.Noise-Switch-Freq", Double.valueOf(0.008D));
    cfg.addDefault("Level6.Params.Noise-Switch-Octave", Integer.valueOf(2));
    cfg.addDefault("Level6.Blocks.Wall", "minecraft:glowstone");
  }
}
