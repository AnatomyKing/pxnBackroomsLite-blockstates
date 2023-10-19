package com.poixson.backrooms.gens;

import com.poixson.backrooms.BackroomsGen;
import com.poixson.backrooms.BackroomsLevel;
import com.poixson.backrooms.PreGenData;
import com.poixson.backrooms.worlds.Level_000;
import com.poixson.commonmc.tools.plotter.BlockPlotter;
import com.poixson.commonmc.tools.plotter.PlotterFactory;
import com.poixson.tools.abstractions.AtomicDouble;
import com.poixson.tools.dao.Iab;
import com.poixson.utils.FastNoiseLiteD;
import com.poixson.utils.StringUtils;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;

public class Gen_037 extends BackroomsGen {
  public static final double DEFAULT_NOISE_ROOM_FREQ = 0.004D;
  
  public static final int DEFAULT_NOISE_ROOM_OCTAVE = 2;
  
  public static final double DEFAULT_NOISE_ROOM_GAIN = 0.1D;
  
  public static final double DEFAULT_NOISE_ROOM_STRENGTH = 2.8D;
  
  public static final double DEFAULT_NOISE_TUNNEL_FREQ = 0.015D;
  
  public static final double DEFAULT_NOISE_TUNNEL_STRENGTH = 5.0D;
  
  public static final double DEFAULT_NOISE_PORTAL_LOBBY_FREQ = 0.02D;
  
  public static final int DEFAULT_NOISE_PORTAL_LOBBY_OCTAVE = 2;
  
  public static final double DEFAULT_NOISE_PORTAL_HOTEL_FREQ = 0.01D;
  
  public static final double DEFAULT_THRESH_ROOM = 0.2D;
  
  public static final double DEFAULT_THRESH_PORTAL = 0.5D;
  
  public static final int WATER_DEPTH = 3;
  
  public static final String DEFAULT_BLOCK_WALL_A = "minecraft:prismarine_bricks";
  
  public static final String DEFAULT_BLOCK_WALL_B = "minecraft:prismarine";
  
  public static final String DEFAULT_BLOCK_SUBFLOOR = "minecraft:dark_prismarine";
  
  public static final String DEFAULT_BLOCK_SUBCEILING = "minecraft:dark_prismarine";
  
  public static final String DEFAULT_BLOCK_CEILING = "minecraft:glowstone";
  
  public final FastNoiseLiteD noisePoolRooms;
  
  public final FastNoiseLiteD noiseTunnels;
  
  public final FastNoiseLiteD noisePortalLobby;
  
  public final FastNoiseLiteD noisePortalHotel;
  
  public final AtomicDouble noise_room_freq = new AtomicDouble(0.004D);
  
  public final AtomicInteger noise_room_octave = new AtomicInteger(2);
  
  public final AtomicDouble noise_room_gain = new AtomicDouble(0.1D);
  
  public final AtomicDouble noise_room_strength = new AtomicDouble(2.8D);
  
  public final AtomicDouble noise_tunnel_freq = new AtomicDouble(0.015D);
  
  public final AtomicDouble noise_tunnel_strength = new AtomicDouble(5.0D);
  
  public final AtomicDouble noise_portal_lobby_freq = new AtomicDouble(0.02D);
  
  public final AtomicInteger noise_portal_lobby_octave = new AtomicInteger(2);
  
  public final AtomicDouble noise_portal_hotel_freq = new AtomicDouble(0.01D);
  
  public final AtomicDouble thresh_room = new AtomicDouble(0.2D);
  
  public final AtomicDouble thresh_portal = new AtomicDouble(0.5D);
  
  public final AtomicReference<String> block_wall_a = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_wall_b = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_subfloor = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_subceiling = new AtomicReference<>(null);
  
  public final AtomicReference<String> block_ceiling = new AtomicReference<>(null);
  
  public Gen_037(BackroomsLevel backlevel, int level_y, int level_h) {
    super(backlevel, level_y, level_h);
    this.noisePoolRooms = register(new FastNoiseLiteD());
    this.noiseTunnels = register(new FastNoiseLiteD());
    this.noisePortalLobby = register(new FastNoiseLiteD());
    this.noisePortalHotel = register(new FastNoiseLiteD());
  }
  
  public void initNoise() {
    this.noisePoolRooms.setFrequency(this.noise_room_freq.get());
    this.noisePoolRooms.setFractalOctaves(this.noise_room_octave.get());
    this.noisePoolRooms.setFractalGain(this.noise_room_gain.get());
    this.noisePoolRooms.setFractalPingPongStrength(this.noise_room_strength.get());
    this.noisePoolRooms.setNoiseType(FastNoiseLiteD.NoiseType.OpenSimplex2);
    this.noisePoolRooms.setFractalType(FastNoiseLiteD.FractalType.PingPong);
    this.noiseTunnels.setFrequency(this.noise_tunnel_freq.get());
    this.noiseTunnels.setFractalPingPongStrength(this.noise_tunnel_strength.get());
    this.noiseTunnels.setNoiseType(FastNoiseLiteD.NoiseType.Cellular);
    this.noiseTunnels.setFractalType(FastNoiseLiteD.FractalType.PingPong);
    this.noiseTunnels.setCellularDistanceFunction(FastNoiseLiteD.CellularDistanceFunction.Manhattan);
    this.noisePortalLobby.setFrequency(this.noise_portal_lobby_freq.get());
    this.noisePortalLobby.setFractalOctaves(this.noise_portal_lobby_octave.get());
    this.noisePortalLobby.setFractalType(FastNoiseLiteD.FractalType.FBm);
    this.noisePortalHotel.setFrequency(this.noise_portal_hotel_freq.get());
  }
  
  public enum RoomType {
    OPEN, SOLID;
  }
  
  public class PoolData implements PreGenData {
    public final double valueRoom;
    
    public final double valuePortalHotel;
    
    public final double valuePortalLobby;
    
    public final boolean possiblePortalHotel;
    
    public final boolean possiblePortalLobby;
    
    public Gen_037.RoomType type;
    
    public PoolData(int x, int z) {
      this.valueRoom = Gen_037.this.noisePoolRooms.getNoise(x, z);
      this.valuePortalHotel = Gen_037.this.noisePortalHotel.getNoise(x, z);
      this.valuePortalLobby = Gen_037.this.noisePortalLobby.getNoise(x, z);
      if (this.valueRoom < Gen_037.this.thresh_room.get()) {
        this.type = Gen_037.RoomType.SOLID;
        this.possiblePortalHotel = (this.valuePortalHotel > Gen_037.this.thresh_portal.get());
        this.possiblePortalLobby = false;
      } else {
        this.type = Gen_037.RoomType.OPEN;
        this.possiblePortalHotel = false;
        this
          
          .possiblePortalLobby = (this.valuePortalLobby > Gen_037.this.noisePortalLobby.getNoise(x, (z - 1)) && this.valuePortalLobby > Gen_037.this.noisePortalLobby.getNoise(x, (z + 1)) && this.valuePortalLobby > Gen_037.this.noisePortalLobby.getNoise((x + 1), z) && this.valuePortalLobby > Gen_037.this.noisePortalLobby.getNoise((x - 1), z));
      } 
    }
    
    public boolean isSolid() {
      return Gen_037.RoomType.SOLID.equals(this.type);
    }
  }
  
  public void pregenerate(Map<Iab, PoolData> data, int chunkX, int chunkZ) {
    for (int iz = -1; iz < 3; iz++) {
      int zz = chunkZ * 16 + iz * 8 + 4;
      for (int ix = -1; ix < 3; ix++) {
        int xx = chunkX * 16 + ix * 8 + 4;
        PoolData dao = new PoolData(xx, zz);
        data.put(new Iab(ix, iz), dao);
      } 
    } 
  }
  
  public void generate(PreGenData pregen, ChunkGenerator.ChunkData chunk, LinkedList<BlockPlotter> plots, int chunkX, int chunkZ) {
    BlockData block_wall_a = StringToBlockData(this.block_wall_a, "minecraft:prismarine_bricks");
    BlockData block_wall_b = StringToBlockData(this.block_wall_b, "minecraft:prismarine");
    BlockData block_subfloor = StringToBlockData(this.block_subfloor, "minecraft:dark_prismarine");
    BlockData block_subceiling = StringToBlockData(this.block_subceiling, "minecraft:dark_prismarine");
    BlockData block_ceiling = StringToBlockData(this.block_ceiling, "minecraft:glowstone");
    if (block_wall_a == null)
      throw new RuntimeException("Invalid block type for level 37 Wall A"); 
    if (block_wall_b == null)
      throw new RuntimeException("Invalid block type for level 37 Wall B"); 
    if (block_subfloor == null)
      throw new RuntimeException("Invalid block type for level 37 SubFloor"); 
    if (block_subceiling == null)
      throw new RuntimeException("Invalid block type for level 37 SubCeiling"); 
    if (block_ceiling == null)
      throw new RuntimeException("Invalid block type for level 37 Ceiling"); 
    Map<Iab, PoolData> poolData = ((Level_000.PregenLevel0)pregen).pools;
    Map<Iab, Gen_000.LobbyData> lobbyData = ((Level_000.PregenLevel0)pregen).lobby;
    int y = this.level_y + 3 + 1;
    int cy = this.level_h + y + 1;
    int h = this.level_h + 2;
    for (int iz = 0; iz < 16; iz++) {
      for (int ix = 0; ix < 16; ix++) {
        chunk.setBlock(ix, this.level_y, iz, Material.BEDROCK);
        int iy;
        for (iy = 0; iy < 3; iy++)
          chunk.setBlock(ix, this.level_y + iy + 1, iz, block_subfloor); 
        for (iy = 0; iy < 3; iy++)
          chunk.setBlock(ix, cy + iy + 1, iz, block_subceiling); 
      } 
    } 
    for (int rz = 0; rz < 2; rz++) {
      for (int rx = 0; rx < 2; rx++) {
        BlockPlotter plot = (new PlotterFactory()).placer(chunk).axis("use").xyz(rx * 8, y, rz * 8).whd(8, h, 8).build();
        plot.type('#', block_wall_a);
        plot.type('@', block_wall_b);
        plot.type('w', "minecraft:water[level=0]");
        plot.type('g', block_ceiling);
        PoolData dao = poolData.get(new Iab(rx, rz));
        boolean solid_n = ((PoolData)poolData.get(new Iab(rx, rz - 1))).isSolid();
        boolean solid_s = ((PoolData)poolData.get(new Iab(rx, rz + 1))).isSolid();
        boolean solid_e = ((PoolData)poolData.get(new Iab(rx + 1, rz))).isSolid();
        boolean solid_w = ((PoolData)poolData.get(new Iab(rx - 1, rz))).isSolid();
        boolean solid_ne = ((PoolData)poolData.get(new Iab(rx + 1, rz - 1))).isSolid();
        boolean solid_nw = ((PoolData)poolData.get(new Iab(rx - 1, rz - 1))).isSolid();
        boolean solid_se = ((PoolData)poolData.get(new Iab(rx + 1, rz + 1))).isSolid();
        boolean solid_sw = ((PoolData)poolData.get(new Iab(rx - 1, rz + 1))).isSolid();
        StringBuilder[][] matrix = plot.getMatrix3D();
        switch (dao.type) {
          case SOLID:
            for (i = 0; i < 8; i++) {
              for (int iy = 0; iy < h; iy++)
                matrix[iy][i].append(StringUtils.Repeat(8, '@')); 
            } 
            if (!solid_n && !solid_e && !solid_ne) {
              StringUtils.ReplaceInString(matrix[0][0], "####", 4);
              StringUtils.ReplaceInString(matrix[0][1], "##", 6);
              StringUtils.ReplaceInString(matrix[0][2], "#", 7);
              for (int iy = 1; iy < h; iy++) {
                StringUtils.ReplaceInString(matrix[iy][0], "    ", 4);
                StringUtils.ReplaceInString(matrix[iy][1], "  ", 6);
                StringUtils.ReplaceInString(matrix[iy][2], " ", 7);
              } 
            } 
            if (!solid_n && !solid_w && !solid_nw) {
              StringUtils.ReplaceInString(matrix[0][0], "####", 0);
              StringUtils.ReplaceInString(matrix[0][1], "##", 0);
              StringUtils.ReplaceInString(matrix[0][2], "#", 0);
              for (int iy = 1; iy < h; iy++) {
                StringUtils.ReplaceInString(matrix[iy][0], "    ", 0);
                StringUtils.ReplaceInString(matrix[iy][1], "  ", 0);
                StringUtils.ReplaceInString(matrix[iy][2], " ", 0);
              } 
            } 
            if (!solid_s && !solid_e && !solid_se) {
              StringUtils.ReplaceInString(matrix[0][7], "####", 4);
              StringUtils.ReplaceInString(matrix[0][6], "##", 6);
              StringUtils.ReplaceInString(matrix[0][5], "#", 7);
              for (int iy = 1; iy < h; iy++) {
                StringUtils.ReplaceInString(matrix[iy][7], "    ", 4);
                StringUtils.ReplaceInString(matrix[iy][6], "  ", 6);
                StringUtils.ReplaceInString(matrix[iy][5], " ", 7);
              } 
            } 
            if (!solid_s && !solid_w && !solid_sw) {
              StringUtils.ReplaceInString(matrix[0][7], "####", 0);
              StringUtils.ReplaceInString(matrix[0][6], "##", 0);
              StringUtils.ReplaceInString(matrix[0][5], "#", 0);
              for (int iy = 1; iy < h; iy++) {
                StringUtils.ReplaceInString(matrix[iy][7], "    ", 0);
                StringUtils.ReplaceInString(matrix[iy][6], "  ", 0);
                StringUtils.ReplaceInString(matrix[iy][5], " ", 0);
              } 
            } 
            if (!solid_n && !solid_e && solid_ne) {
              StringUtils.ReplaceInString(matrix[0][0], "#####", 3);
              StringUtils.ReplaceInString(matrix[0][1], "###", 5);
              StringUtils.ReplaceInString(matrix[0][2], "##", 6);
              StringUtils.ReplaceInString(matrix[0][3], "#", 7);
              StringUtils.ReplaceInString(matrix[0][4], "#", 7);
              for (int iy = 1; iy < h; iy++) {
                StringUtils.ReplaceInString(matrix[iy][0], "     ", 3);
                StringUtils.ReplaceInString(matrix[iy][1], "   ", 5);
                StringUtils.ReplaceInString(matrix[iy][2], "  ", 6);
                StringUtils.ReplaceInString(matrix[iy][3], " ", 7);
                StringUtils.ReplaceInString(matrix[iy][4], " ", 7);
              } 
            } 
            if (!solid_n && !solid_w && solid_nw) {
              StringUtils.ReplaceInString(matrix[0][0], "#####", 0);
              StringUtils.ReplaceInString(matrix[0][1], "###", 0);
              StringUtils.ReplaceInString(matrix[0][2], "##", 0);
              StringUtils.ReplaceInString(matrix[0][3], "#", 0);
              StringUtils.ReplaceInString(matrix[0][4], "#", 0);
              for (int iy = 1; iy < h; iy++) {
                StringUtils.ReplaceInString(matrix[iy][0], "     ", 0);
                StringUtils.ReplaceInString(matrix[iy][1], "   ", 0);
                StringUtils.ReplaceInString(matrix[iy][2], "  ", 0);
                StringUtils.ReplaceInString(matrix[iy][3], " ", 0);
                StringUtils.ReplaceInString(matrix[iy][4], " ", 0);
              } 
            } 
            if (!solid_s && !solid_e && solid_se) {
              StringUtils.ReplaceInString(matrix[0][7], "#####", 3);
              StringUtils.ReplaceInString(matrix[0][6], "###", 5);
              StringUtils.ReplaceInString(matrix[0][5], "##", 6);
              StringUtils.ReplaceInString(matrix[0][4], "#", 7);
              StringUtils.ReplaceInString(matrix[0][3], "#", 7);
              for (int iy = 1; iy < h; iy++) {
                StringUtils.ReplaceInString(matrix[iy][7], "     ", 3);
                StringUtils.ReplaceInString(matrix[iy][6], "   ", 5);
                StringUtils.ReplaceInString(matrix[iy][5], "  ", 6);
                StringUtils.ReplaceInString(matrix[iy][4], " ", 7);
                StringUtils.ReplaceInString(matrix[iy][3], " ", 7);
              } 
            } 
            if (!solid_s && !solid_w && solid_sw) {
              StringUtils.ReplaceInString(matrix[0][7], "#####", 0);
              StringUtils.ReplaceInString(matrix[0][6], "###", 0);
              StringUtils.ReplaceInString(matrix[0][5], "##", 0);
              StringUtils.ReplaceInString(matrix[0][4], "#", 0);
              StringUtils.ReplaceInString(matrix[0][3], "#", 0);
              for (int iy = 1; iy < h; iy++) {
                StringUtils.ReplaceInString(matrix[iy][7], "     ", 0);
                StringUtils.ReplaceInString(matrix[iy][6], "   ", 0);
                StringUtils.ReplaceInString(matrix[iy][5], "  ", 0);
                StringUtils.ReplaceInString(matrix[iy][4], " ", 0);
                StringUtils.ReplaceInString(matrix[iy][3], " ", 0);
              } 
            } 
            break;
          case OPEN:
            for (i = 0; i < 8; i++) {
              matrix[0][i].append(StringUtils.Repeat(8, '#'));
              for (int iy = 1; iy < h; iy++)
                matrix[iy][i].append(StringUtils.Repeat(8, ' ')); 
            } 
            if (solid_n && solid_e && solid_ne)
              for (int iy = 0; iy < h; iy++) {
                StringUtils.ReplaceInString(matrix[iy][0], "@@", 6);
                StringUtils.ReplaceInString(matrix[iy][1], "@", 7);
              }  
            if (solid_n && solid_w && solid_nw)
              for (int iy = 0; iy < h; iy++) {
                StringUtils.ReplaceInString(matrix[iy][0], "@@", 0);
                StringUtils.ReplaceInString(matrix[iy][1], "@", 0);
              }  
            if (solid_s && solid_e && solid_se)
              for (int iy = 0; iy < h; iy++) {
                StringUtils.ReplaceInString(matrix[iy][7], "@@", 6);
                StringUtils.ReplaceInString(matrix[iy][6], "@", 7);
              }  
            if (solid_s && solid_w && solid_sw)
              for (int iy = 0; iy < h; iy++) {
                StringUtils.ReplaceInString(matrix[iy][7], "@@", 0);
                StringUtils.ReplaceInString(matrix[iy][6], "@", 0);
              }  
            if (dao.possiblePortalLobby) {
              boolean foundWall = false;
              int j;
              label222: for (j = 0; j < 8; j++) {
                int zz = rz * 8 + j;
                for (int ix = 0; ix < 8; ix++) {
                  int xx = rx * 8 + ix;
                  Gen_000.LobbyData lobby = lobbyData.get(new Iab(xx, zz));
                  if (lobby.isWall) {
                    foundWall = true;
                    break label222;
                  } 
                } 
              } 
              if (!foundWall) {
                int xx = chunkX * 16 + rx * 8;
                int zz = chunkZ * 16 + rz * 8;
                ((Level_000)this.backlevel).portal_0_to_37.add(xx, zz);
                int hh = 21;
                BlockPlotter pp = (new PlotterFactory()).placer(chunk).axis("use").xz(rx * 8, rz * 8).y(52).whd(6, 22, 6).build();
                pp.type('#', Material.BEDROCK);
                pp.type('g', Material.GLOWSTONE);
                pp.type('.', Material.AIR);
                pp.type(',', "minecraft:water[level=8]");
                StringBuilder[][] mtx = pp.getMatrix3D();
                mtx[0][0].append(" #### ");
                mtx[1][0].append(" #### ");
                mtx[0][1].append("##gg##");
                mtx[1][1].append("##,,##");
                mtx[0][2].append("#gggg#");
                mtx[1][2].append("#,,,,#");
                mtx[0][3].append("#gggg#");
                mtx[1][3].append("#,,,,#");
                mtx[0][4].append("##gg##");
                mtx[1][4].append("##,,##");
                mtx[0][5].append(" #### ");
                mtx[1][5].append(" #### ");
                int hhh = 8;
                int yi;
                for (yi = 2; yi < 8; yi++) {
                  mtx[yi][0].append(" .... ");
                  mtx[yi][1].append("..,,..");
                  mtx[yi][2].append(".,,,,.");
                  mtx[yi][3].append(".,,,,.");
                  mtx[yi][4].append("..,,..");
                  mtx[yi][5].append(" .... ");
                } 
                for (yi = 8; yi < 21; yi++) {
                  mtx[yi][0].append(" #### ");
                  mtx[yi][1].append("##,,##");
                  mtx[yi][2].append("#,,,,#");
                  mtx[yi][3].append("#,,,,#");
                  mtx[yi][4].append("##,,##");
                  mtx[yi][5].append(" #### ");
                } 
                mtx[21][0].append("  gg  ");
                mtx[21][1].append(" g,,g ");
                mtx[21][2].append("g,,,,g");
                mtx[21][3].append("g,,,,g");
                mtx[21][4].append(" g,,g ");
                mtx[21][5].append("  gg  ");
                plots.add(pp);
              } 
            } 
            break;
          default:
            throw new RuntimeException("Unknown pool room type: " + dao.type.toString());
        } 
        int i;
        for (i = 0; i < 8; i++) {
          for (int iy = 0; iy <= 3; iy++)
            StringUtils.ReplaceWith(matrix[iy][i], ' ', 'w'); 
        } 
        for (i = 0; i < 8; i++)
          StringUtils.ReplaceWith(matrix[h - 1][i], ' ', 'g'); 
        plot.run();
      } 
    } 
  }
  
  protected void loadConfig() {
    ConfigurationSection cfg = this.plugin.getLevelParams(37);
    this.noise_room_freq.set(cfg.getDouble("Noise-Room-Freq"));
    this.noise_room_octave.set(cfg.getInt("Noise-Room-Octave"));
    this.noise_room_gain.set(cfg.getDouble("Noise-Room-Gain"));
    this.noise_room_strength.set(cfg.getDouble("Noise-Room-Strength"));
    this.noise_tunnel_freq.set(cfg.getDouble("Noise-Tunnel-Freq"));
    this.noise_tunnel_strength.set(cfg.getDouble("Noise-Tunnel-Strength"));
    this.noise_portal_lobby_freq.set(cfg.getDouble("Noise-Portal-Lobby-Freq"));
    this.noise_portal_lobby_octave.set(cfg.getInt("Noise-Portal-Lobby-Octave"));
    this.noise_portal_hotel_freq.set(cfg.getDouble("Noise-Portal-Hotel-Freq"));
    this.thresh_room.set(cfg.getDouble("Thresh-Room"));
    this.thresh_portal.set(cfg.getDouble("Thresh-Portal"));
    cfg = this.plugin.getLevelBlocks(37);
    this.block_wall_a.set(cfg.getString("WallA"));
    this.block_wall_b.set(cfg.getString("WallB"));
    this.block_subfloor.set(cfg.getString("SubFloor"));
    this.block_subceiling.set(cfg.getString("SubCeiling"));
    this.block_ceiling.set(cfg.getString("Ceiling"));
  }
  
  public static void ConfigDefaults(FileConfiguration cfg) {
    cfg.addDefault("Level37.Params.Noise-Room-Freq", Double.valueOf(0.004D));
    cfg.addDefault("Level37.Params.Noise-Room-Octave", Integer.valueOf(2));
    cfg.addDefault("Level37.Params.Noise-Room-Gain", Double.valueOf(0.1D));
    cfg.addDefault("Level37.Params.Noise-Room-Strength", Double.valueOf(2.8D));
    cfg.addDefault("Level37.Params.Noise-Tunnel-Freq", Double.valueOf(0.015D));
    cfg.addDefault("Level37.Params.Noise-Tunnel-Strength", Double.valueOf(5.0D));
    cfg.addDefault("Level37.Params.Noise-Portal-Lobby-Freq", Double.valueOf(0.02D));
    cfg.addDefault("Level37.Params.Noise-Portal-Lobby-Octave", Integer.valueOf(2));
    cfg.addDefault("Level37.Params.Noise-Portal-Hotel-Freq", Double.valueOf(0.01D));
    cfg.addDefault("Level37.Params.Thresh-Room", Double.valueOf(0.2D));
    cfg.addDefault("Level37.Params.Thresh-Portal", Double.valueOf(0.5D));
    cfg.addDefault("Level37.Blocks.WallA", "minecraft:prismarine_bricks");
    cfg.addDefault("Level37.Blocks.WallB", "minecraft:prismarine");
    cfg.addDefault("Level37.Blocks.SubFloor", "minecraft:dark_prismarine");
    cfg.addDefault("Level37.Blocks.SubCeiling", "minecraft:dark_prismarine");
    cfg.addDefault("Level37.Blocks.Ceiling", "minecraft:glowstone");
  }
}
