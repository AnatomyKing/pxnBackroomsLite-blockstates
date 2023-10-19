package com.poixson.backrooms.gens;

import com.poixson.backrooms.BackroomsGen;
import com.poixson.backrooms.BackroomsLevel;
import com.poixson.backrooms.PreGenData;
import com.poixson.backrooms.worlds.Level_771;
import com.poixson.commonmc.tools.DelayedChestFiller;
import com.poixson.commonmc.tools.plotter.BlockPlotter;
import com.poixson.commonmc.tools.plotter.PlotterFactory;
import com.poixson.commonmc.utils.LocationUtils;
import com.poixson.tools.abstractions.AtomicDouble;
import com.poixson.tools.dao.Iab;
import com.poixson.utils.FastNoiseLiteD;
import com.poixson.utils.StringUtils;
import java.util.LinkedList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Barrel;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;


public class Gen_771 extends BackroomsGen {
  public static final double DEFAULT_NOISE_LAMPS_FREQ = 0.3D;
  
  public static final double DEFAULT_NOISE_EXITS_FREQ = 0.5D;
  
  public static final double DEFAULT_NOISE_LOOT_FREQ = 0.1D;
  
  public static final double DEFAULT_THRESH_LAMPS = 0.42D;
  
  public static final double DEFAULT_THRESH_LADDER = 0.81D;
  
  public static final double DEFAULT_THRESH_VOID = 0.85D;
  
  public static final double DEFAULT_THRESH_LOOT = 0.7D;
  
  public static final int PILLAR_B_OFFSET = 10;
  
  public final FastNoiseLiteD noiseRoadLights;
  
  public final FastNoiseLiteD noiseSpecial;
  
  public final FastNoiseLiteD noiseLoot;
  
  public final AtomicDouble noise_lamps_freq = new AtomicDouble(0.3D);
  
  public final AtomicDouble noise_exits_freq = new AtomicDouble(0.5D);
  
  public final AtomicDouble noise_loot_freq = new AtomicDouble(0.1D);
  
  public final AtomicDouble thresh_lamps = new AtomicDouble(0.42D);
  
  public final AtomicDouble thresh_ladder = new AtomicDouble(0.81D);
  
  public final AtomicDouble thresh_void = new AtomicDouble(0.85D);
  
  public final AtomicDouble thresh_loot = new AtomicDouble(0.7D);
  
  public enum PillarType {
    PILLAR_NORM, PILLAR_LADDER, PILLAR_LOOT_UPPER, PILLAR_LOOT_LOWER, PILLAR_DROP, PILLAR_VOID;
  }
  
  public Gen_771(BackroomsLevel backlevel, int level_y, int level_h) {
    super(backlevel, level_y, level_h);
    this.noiseRoadLights = register(new FastNoiseLiteD());
    this.noiseSpecial = register(new FastNoiseLiteD());
    this.noiseLoot = register(new FastNoiseLiteD());
  }
  
  public void initNoise() {
    this.noiseRoadLights.setFrequency(this.noise_lamps_freq.get());
    this.noiseSpecial.setFrequency(this.noise_exits_freq.get());
    this.noiseLoot.setFrequency(this.noise_loot_freq.get());
  }
  
  public void generate(PreGenData pregen, ChunkGenerator.ChunkData chunk, LinkedList<BlockPlotter> plots, int chunkX, int chunkZ) {
    for (int iz = 0; iz < 16; iz++) {
      for (int ix = 0; ix < 16; ix++)
        chunk.setBlock(ix, this.level_y, iz, Material.BARRIER); 
    } 
    boolean centerX = (chunkX == 0 || chunkX == -1);
    boolean centerZ = (chunkZ == 0 || chunkZ == -1);
    if (centerX && centerZ) {
      generateWorldCenter(chunk, chunkX, chunkZ);
    } else if (centerX || centerZ) {
      generateRoad(chunk, chunkX, chunkZ);
    } 
  }
  
  protected void generateWorldCenter(ChunkGenerator.ChunkData chunk, int chunkX, int chunkZ) {
    BlockFace quarter = LocationUtils.ValueToFaceQuarter(chunkX, chunkZ);
    String axis = LocationUtils.FaceToAxString(quarter);
    generateCenterArches(chunk, chunkX, chunkZ, "u" + axis);
    generateCenterArches(chunk, chunkX, chunkZ, "u" + axis.charAt(1) + axis.charAt(0));
    generateCenterFloor(chunk, chunkX, chunkZ, "u" + axis);
  }
  
  protected void generateCenterArches(ChunkGenerator.ChunkData chunk, int chunkX, int chunkZ, String axis) {
    BlockPlotter plot = (new PlotterFactory()).placer(chunk).axis(axis).xz((0 - chunkX) * 15, (0 - chunkZ) * 15).y(this.level_y + this.level_h + 2).whd(16, 14, 16).build();
    plot.type('#', "minecraft:polished_blackstone_bricks");
    plot.type('-', "minecraft:polished_blackstone_brick_slab[type=top]");
    plot.type('_', "minecraft:polished_blackstone_brick_slab[type=bottom]");
    plot.type('L', "minecraft:polished_blackstone_brick_stairs");
    plot.type('^', "minecraft:polished_blackstone_brick_stairs");
    plot.type('|', "minecraft:polished_blackstone_brick_wall");
    plot.type('@', "minecraft:chiseled_polished_blackstone");
    plot.type('!', Material.LIGHTNING_ROD);
    plot.type('8', Material.CHAIN);
    plot.type('G', Material.SHROOMLIGHT);
    StringBuilder[][] matrix = plot.getMatrix3D();
    matrix[13][0].append("!");
    matrix[12][0].append("#");
    matrix[11][2].append("#");
    matrix[11][1].append("-");
    matrix[12][1].append("_");
    matrix[10][4].append("#");
    matrix[10][3].append("-");
    matrix[11][3].append("_");
    matrix[9][6].append("#");
    matrix[9][5].append("-");
    matrix[10][5].append("_");
    matrix[8][8].append("#");
    matrix[8][7].append("-");
    matrix[9][7].append("_");
    matrix[7][10].append("#");
    matrix[7][9].append("-");
    matrix[8][9].append("_");
    matrix[6][12].append("#");
    matrix[6][11].append("-");
    matrix[7][11].append("_");
    matrix[5][14].append("#");
    matrix[5][13].append("-");
    matrix[6][13].append("_");
    for (int i = 7; i < 12; i++)
      matrix[i][0].append("8"); 
    matrix[6][0].append("G");
    matrix[5][15].append("_");
    matrix[4][15].append("#L");
    matrix[3][15].append(" ^L");
    matrix[2][15].append("  ^L");
    matrix[1][15].append("   |");
    matrix[0][15].append("   @");
    plot.run();
  }
  
  protected void generateCenterFloor(ChunkGenerator.ChunkData chunk, int chunkX, int chunkZ, String axis) {
    BlockPlotter plot = (new PlotterFactory()).placer(chunk).axis(axis).xz((0 - chunkX) * 15, (0 - chunkZ) * 15).y(this.level_y + this.level_h - 3).whd(16, 5, 16).build();
    plot.type('#', Material.POLISHED_BLACKSTONE);
    plot.type('x', Material.CHISELED_POLISHED_BLACKSTONE);
    plot.type('X', Material.GILDED_BLACKSTONE);
    plot.type('*', Material.BLACKSTONE);
    plot.type('+', Material.POLISHED_BLACKSTONE_BRICK_WALL);
    plot.type('-', Material.POLISHED_BLACKSTONE_SLAB);
    plot.type('.', "minecraft:light[level=15]");
    plot.type(',', "minecraft:light[level=9]");
    StringBuilder[][] matrix = plot.getMatrix3D();
    matrix[0][0].append("###########---");
    matrix[1][0].append(" , , , , , ,  #");
    matrix[2][0].append("              ##");
    matrix[3][0].append("x***************");
    matrix[4][0].append("                ");
    matrix[0][1].append("###########---");
    matrix[1][1].append("              #");
    matrix[2][1].append("              ##");
    matrix[3][1].append("***#############");
    matrix[4][1].append("    .   .   .   ");
    matrix[0][2].append("##########---");
    matrix[1][2].append(" , , , , , , ##");
    matrix[2][2].append("             ###");
    matrix[3][2].append("**X##X##########");
    matrix[4][2].append("                ");
    matrix[0][3].append("##########---");
    matrix[1][3].append("             #");
    matrix[2][3].append("             ##");
    matrix[3][3].append("*##*###*#######");
    matrix[4][3].append("  .   .   .   . ");
    matrix[0][4].append("#########---");
    matrix[1][4].append(" , , , , ,  ##");
    matrix[2][4].append("            ###");
    matrix[3][4].append("*###X####X#####");
    matrix[4][4].append("               +");
    matrix[0][5].append("########----");
    matrix[1][5].append("            #");
    matrix[2][5].append("            ##");
    matrix[3][5].append("*#X##*#####*##");
    matrix[4][5].append("              ++");
    matrix[0][6].append("#######----");
    matrix[1][6].append(" , , , , , ##");
    matrix[2][6].append("           ###");
    matrix[3][6].append("*#####*#######");
    matrix[4][6].append("              +");
    matrix[0][7].append("######-----");
    matrix[1][7].append("           #");
    matrix[2][7].append("           ##");
    matrix[3][7].append("*##*###X#####");
    matrix[4][7].append("  .   .   .  ++");
    matrix[0][8].append("#####-----");
    matrix[1][8].append(" , , , ,  ##");
    matrix[2][8].append("          ###");
    matrix[3][8].append("*#######*####");
    matrix[4][8].append("             +");
    matrix[0][9].append("####-----");
    matrix[1][9].append("         ##");
    matrix[2][9].append("         ###");
    matrix[3][9].append("*###X####*##");
    matrix[4][9].append("            ++");
    matrix[0][10].append("##------");
    matrix[1][10].append(" , , ,  ##");
    matrix[2][10].append("        ###");
    matrix[3][10].append("*##########");
    matrix[4][10].append("           ++");
    matrix[0][11].append("-------");
    matrix[1][11].append("       ##");
    matrix[2][11].append("       ###");
    matrix[3][11].append("*####*####");
    matrix[4][11].append("  .   .   ++");
    matrix[0][12].append("-----");
    matrix[1][12].append(" ,   ###");
    matrix[2][12].append("     ####");
    matrix[3][12].append("*########");
    matrix[4][12].append("         ++");
    matrix[0][13].append("--");
    matrix[1][13].append("  ####");
    matrix[2][13].append("  #####");
    matrix[3][13].append("*######");
    matrix[4][13].append("       +++");
    matrix[0][14].append("");
    matrix[1][14].append("###");
    matrix[2][14].append("#####");
    matrix[3][14].append("*####");
    matrix[4][14].append("     +++");
    matrix[0][15].append("");
    matrix[1][15].append("");
    matrix[2][15].append("###");
    matrix[3][15].append("*##");
    matrix[4][15].append("  . ++");
    plot.run();
  }
  
  protected void generateRoad(ChunkGenerator.ChunkData chunk, int chunkX, int chunkZ) {
    BlockFace direction, side;
    int x = 0;
    int z = 0;
    if (chunkZ < -1) {
      direction = BlockFace.NORTH;
      z = 15;
    } else if (chunkZ > 0) {
      direction = BlockFace.SOUTH;
      z = 0;
    } else if (chunkX > 0) {
      direction = BlockFace.EAST;
      x = 0;
    } else if (chunkX < -1) {
      direction = BlockFace.WEST;
      x = 15;
    } else {
      throw new RuntimeException("Unknown direction");
    } 
    switch (direction) {
      case PILLAR_LOOT_UPPER:
      case PILLAR_LOOT_LOWER:
        if (chunkX == 0) {
          side = BlockFace.EAST;
          x = 0;
          break;
        } 
        if (chunkX == -1) {
          side = BlockFace.WEST;
          x = 15;
          break;
        } 
        throw new RuntimeException("Unknown side for chunk x: " + Integer.toString(chunkX));
      case PILLAR_NORM:
      case PILLAR_LADDER:
        if (chunkZ == -1) {
          side = BlockFace.NORTH;
          z = 15;
          break;
        } 
        if (chunkZ == 0) {
          side = BlockFace.SOUTH;
          z = 0;
          break;
        } 
        throw new RuntimeException("Unknown side for chunk z: " + Integer.toString(chunkZ));
      default:
        throw new RuntimeException("Unknown direction: " + direction.toString());
    } 
    generateRoadTop(chunk, direction, side, chunkX, chunkZ, x, z);
    generateRoadBottom(chunk, direction, side, chunkX, chunkZ, x, z);
    generatePillars(chunk, direction, side, chunkX, chunkZ, x, z);
  }
  
  protected void generateRoadTop(ChunkGenerator.ChunkData chunk, BlockFace direction, BlockFace side, int chunkX, int chunkZ, int x, int z) {
    double thresh_lamps = this.thresh_lamps.get();
    BlockPlotter plot = (new PlotterFactory()).placer(chunk).axis("u" + LocationUtils.FaceToAxString(direction) + LocationUtils.FaceToAxString(side)).xz(x, z).y(this.level_y + this.level_h).whd(16, 3, 16).build();
    plot.type('#', Material.POLISHED_BLACKSTONE);
    plot.type('*', Material.BLACKSTONE);
    plot.type('+', Material.POLISHED_BLACKSTONE_BRICK_WALL);
    plot.type('i', Material.LANTERN);
    plot.type('L', "minecraft:light[level=15]");
    StringBuilder[][] matrix = plot.getMatrix3D();
    Iab dir = LocationUtils.FaceToIxz(direction);
    int cx = chunkX * 16;
    int cz = chunkZ * 16;
    for (int i = 0; i < 16; i++) {
      matrix[1][i].append("   +");
      matrix[0][i].append("*##");
      double value_light = this.noiseRoadLights.getNoise((cx + dir.a * i), (cz + dir.b * i)) % 0.5D;
      if (value_light > thresh_lamps) {
        matrix[2][i].append("   i");
        StringUtils.ReplaceInString(matrix[1][i], "L", 2);
      } 
    } 
    plot.run();
  }
  
  protected void generateRoadBottom(ChunkGenerator.ChunkData chunk, BlockFace direction, BlockFace side, int chunkX, int chunkZ, int x, int z) {
    double thresh_light = this.thresh_lamps.get();
    BlockPlotter plot = (new PlotterFactory()).placer(chunk).axis("u" + LocationUtils.FaceToAxString(direction) + LocationUtils.FaceToAxString(side)).xz(x, z).y(this.level_y).whd(16, 3, 16).build();
    plot.type('#', Material.POLISHED_BLACKSTONE);
    plot.type('*', Material.BLACKSTONE);
    plot.type('+', Material.POLISHED_BLACKSTONE_BRICK_WALL);
    plot.type('i', Material.SOUL_LANTERN);
    plot.type('L', "minecraft:light[level=15]");
    StringBuilder[][] matrix = plot.getMatrix3D();
    Iab dir = LocationUtils.FaceToIxz(direction);
    int cx = chunkX * 16;
    int cz = chunkZ * 16;
    for (int i = 0; i < 16; i++) {
      matrix[1][i].append("   +");
      matrix[0][i].append("*##");
      double value_light = this.noiseRoadLights.getNoise((cx + dir.a * i), (cz + dir.b * i)) % 0.5D;
      if (value_light > thresh_light) {
        matrix[2][i].append("   i");
        StringUtils.ReplaceInString(matrix[1][i], "L", 2);
      } 
    } 
    plot.run();
  }
  
  protected void generatePillars(ChunkGenerator.ChunkData chunk, BlockFace direction, BlockFace side, int chunkX, int chunkZ, int x, int z) {
    PillarType pillar_type;
    int mod_pillar;
    double thresh_ladder = this.thresh_ladder.get();
    double thresh_void = this.thresh_void.get();
    double thresh_loot = this.thresh_loot.get();
    int px = Math.floorDiv(chunkX + 1, 4) * 64;
    int pz = Math.floorDiv(chunkZ + 1, 4) * 64;
    double valB = this.noiseSpecial.getNoise(px, pz);
    double valC = this.noiseSpecial.getNoise((px + 10), (pz + 10));
    if (Math.abs(chunkX) < 30 && 
      Math.abs(chunkZ) < 30) {
      pillar_type = PillarType.PILLAR_NORM;
    } else if (valB > thresh_ladder) {
      if (valC > thresh_loot) {
        if (valC > thresh_void) {
          pillar_type = PillarType.PILLAR_VOID;
        } else {
          pillar_type = PillarType.PILLAR_DROP;
        } 
      } else {
        pillar_type = PillarType.PILLAR_LADDER;
      } 
    } else if (valC < thresh_loot) {
      pillar_type = PillarType.PILLAR_NORM;
    } else if ((int)Math.floor(valC * 10000.0D) % 2 == 0) {
      pillar_type = PillarType.PILLAR_LOOT_UPPER;
    } else {
      pillar_type = PillarType.PILLAR_LOOT_LOWER;
    } 
    BlockFace mirrored = direction.getOppositeFace();
    switch (direction) {
      case PILLAR_LOOT_UPPER:
      case PILLAR_LOOT_LOWER:
        mod_pillar = Math.abs(chunkZ) % 4;
        break;
      case PILLAR_NORM:
      case PILLAR_LADDER:
        mod_pillar = Math.abs(chunkX) % 4;
        break;
      default:
        throw new RuntimeException("Unknown direction: " + direction.toString());
    } 
    switch (direction) {
      case PILLAR_LOOT_UPPER:
        if (mod_pillar == 0) {
          generatePillar(pillar_type, chunk, mirrored, side, chunkX, chunkZ, x, 15 - z);
        } else if (mod_pillar == 1) {
          generatePillar(pillar_type, chunk, direction, side, chunkX, chunkZ, x, z);
        } 
        return;
      case PILLAR_LOOT_LOWER:
        if (mod_pillar == 3) {
          generatePillar(pillar_type, chunk, mirrored, side, chunkX, chunkZ, x, 15 - z);
        } else if (mod_pillar == 0) {
          generatePillar(pillar_type, chunk, direction, side, chunkX, chunkZ, x, z);
        } 
        return;
      case PILLAR_NORM:
        if (mod_pillar == 3) {
          generatePillar(pillar_type, chunk, mirrored, side, chunkX, chunkZ, 15 - x, z);
        } else if (mod_pillar == 0) {
          generatePillar(pillar_type, chunk, direction, side, chunkX, chunkZ, x, z);
        } 
        return;
      case PILLAR_LADDER:
        if (mod_pillar == 0) {
          generatePillar(pillar_type, chunk, mirrored, side, chunkX, chunkZ, 15 - x, z);
        } else if (mod_pillar == 1) {
          generatePillar(pillar_type, chunk, direction, side, chunkX, chunkZ, x, z);
        } 
        return;
    } 
    throw new RuntimeException("Unknown direction: " + direction.toString());
  }
  
  protected void generatePillar(PillarType type, ChunkGenerator.ChunkData chunk, BlockFace direction, BlockFace side, int chunkX, int chunkZ, int x, int z) {
    int iy;
    BlockPlotter plot = (new PlotterFactory()).placer(chunk).axis("u" + LocationUtils.FaceToAxString(direction) + LocationUtils.FaceToAxString(side)).xz(x, z).y(this.level_y).whd(2, this.level_h + 2, 5).build();
    plot.type('#', Material.DEEPSLATE_BRICKS);
    plot.type('w', Material.DARK_OAK_PLANKS);
    plot.type('%', Material.DEEPSLATE_BRICK_STAIRS);
    plot.type('<', Material.DEEPSLATE_BRICK_STAIRS);
    plot.type('$', Material.DEEPSLATE_BRICK_STAIRS);
    plot.type('&', Material.DEEPSLATE_BRICK_STAIRS);
    plot.type('H', Material.LADDER);
    plot.type('/', Material.SPRUCE_TRAPDOOR);
    plot.type('~', Material.CRIMSON_TRAPDOOR);
    plot.type('d', Material.SPRUCE_DOOR);
    plot.type('D', Material.SPRUCE_DOOR);
    plot.type('_', Material.POLISHED_BLACKSTONE_PRESSURE_PLATE);
    plot.type('-', Material.DARK_OAK_PRESSURE_PLATE);
    plot.type('+', Material.DEEPSLATE_TILE_WALL);
    plot.type('S', Material.DARK_OAK_WALL_SIGN);
    plot.type(',', "minecraft:light[level=15]");
    plot.type('W', Material.WATER);
    plot.type('.', Material.AIR);
    int h = this.level_h;
    StringBuilder[][] matrix = plot.getMatrix3D();
    switch (type) {
      case PILLAR_LOOT_UPPER:
      case PILLAR_LOOT_LOWER:
      case PILLAR_NORM:
        matrix[h][0].append("   %");
        matrix[--h][0].append("###");
        matrix[h][1].append("##");
        matrix[--h][0].append("###");
        matrix[h][1].append("##");
        matrix[--h][0].append("###");
        matrix[h][1].append("#%");
        matrix[--h][0].append("##%");
        matrix[h][1].append("#");
        matrix[--h][0].append("##");
        matrix[h][1].append("#");
        matrix[--h][0].append("##");
        matrix[h][1].append("%");
        matrix[--h][0].append("#%");
        for (iy = 8; iy < h; iy++)
          matrix[iy][0].append("#"); 
        matrix[7][0].append("#&");
        matrix[6][0].append("##");
        matrix[5][0].append("##&");
        matrix[4][0].append("  <&");
        matrix[3][0].append("   #");
        matrix[2][0].append("   #");
        matrix[1][0].append("   #");
        matrix[0][0].append("   $");
        if (x == 0 && z == 0 && (
          PillarType.PILLAR_LOOT_UPPER.equals(type) || PillarType.PILLAR_LOOT_LOWER
          .equals(type))) {
          int yy;
          Iab dir = LocationUtils.FaceToIxz(side);
          int xx = dir.a * 2 + x;
          int zz = dir.b * 2 + z;
          if (PillarType.PILLAR_LOOT_UPPER.equals(type)) {
            ((Level_771)this.backlevel).loot_chests_upper.add(chunkX * 16 + xx, chunkZ * 16 + zz);
            yy = this.level_y + this.level_h + 1;
          } else {
            ((Level_771)this.backlevel).loot_chests_lower.add(chunkX * 16 + xx, chunkZ * 16 + zz);
            yy = this.level_y + 1;
          } 
          chunk.setBlock(xx, yy, zz, Material.BARREL);
          BlockData data = chunk.getBlockData(xx, yy, zz);
          ((Barrel)data).setFacing(BlockFace.UP);
          chunk.setBlock(xx, yy, zz, data);
          (new ChestFiller_771((JavaPlugin)this.plugin, "level771", xx, yy, zz))
            .start();
        } 
        break;
      case PILLAR_LADDER:
        ((Level_771)this.backlevel).portal_ladder.add(chunkX * 16 + x, chunkZ * 16 + z);
        matrix[h][0].append("   $");
        if (x == 0 && z == 0) {
          matrix[h + 1][0].append("_ _");
          matrix[h + 1][1].append(" _");
          StringUtils.ReplaceInString(matrix[h][0], "/", 1);
        } 
        if (x == -1 && z == -1)
          matrix[h + 1][1].append(" _"); 
        matrix[--h][0].append("  #");
        matrix[h][1].append("##");
        if (x == 0 && z == 0)
          StringUtils.ReplaceInString(matrix[h][0], "H", 1); 
        matrix[--h][0].append("  #");
        matrix[h][1].append("##");
        if (x == 0 && z == 0)
          StringUtils.ReplaceInString(matrix[h][0], "H", 1); 
        matrix[--h][0].append("  #");
        matrix[h][1].append("##");
        if (x == 0 && z == 0)
          StringUtils.ReplaceInString(matrix[h][0], "H", 1); 
        matrix[--h][0].append(" ##");
        matrix[h][1].append("#%");
        if (x == 0 && z == 0)
          StringUtils.ReplaceInString(matrix[h][0], "H", 0); 
        matrix[--h][0].append(" #%");
        matrix[h][1].append("#");
        if (x == 0 && z == 0)
          StringUtils.ReplaceInString(matrix[h][0], "H", 0); 
        for (iy = 0; iy < h; iy++) {
          matrix[iy][0].append(" #");
          matrix[iy][1].append("#");
          if (x == 0 && z == 0) {
            StringUtils.ReplaceInString(matrix[iy][0], "H", 0);
          } else if (iy % 10 == 0) {
            StringUtils.ReplaceInString(matrix[iy][0], ",", 0);
          } 
        } 
        if (x != 0 && z != 0) {
          StringUtils.ReplaceInString(matrix[2][1], "d", 0);
          StringUtils.ReplaceInString(matrix[1][1], "D", 0);
          StringUtils.ReplaceInString(matrix[1][0], "-", 0);
          StringUtils.ReplaceInString(matrix[0][1], " ", 0);
        } 
        StringUtils.ReplaceInString(matrix[0][0], "w", 0);
        break;
      case PILLAR_DROP:
        ((Level_771)this.backlevel).portal_drop.add(chunkX * 16 + x, chunkZ * 16 + z);
        matrix[h + 1][1].append("_");
        matrix[h][0].append("~  $");
        for (iy = 0; iy < h; iy++) {
          matrix[iy][0].append("  #");
          matrix[iy][1].append("##");
        } 
        StringUtils.ReplaceInString(matrix[4][0], "WW", 0);
        StringUtils.ReplaceInString(matrix[3][0], "SS", 0);
        StringUtils.ReplaceInString(matrix[2][1], ".+", 0);
        StringUtils.ReplaceInString(matrix[1][1], ".+", 0);
        StringUtils.ReplaceInString(matrix[0][0], "ww", 0);
        break;
      case PILLAR_VOID:
        ((Level_771)this.backlevel).portal_void.add(chunkX * 16 + x, chunkZ * 16 + z);
        matrix[h + 1][1].append("_");
        matrix[h][0].append("~  $");
        for (iy = 0; iy < h; iy++) {
          matrix[iy][0].append(" #");
          matrix[iy][1].append("#");
        } 
        StringUtils.ReplaceInString(matrix[0][0], ".", 0);
        break;
    } 
    plot.run();
  }
  
  public class ChestFiller_771 extends DelayedChestFiller {
    public ChestFiller_771(JavaPlugin plugin, String worldName, int x, int y, int z) {
      super(plugin, worldName, x, y, z);
    }
    
    public void fill(Inventory chest) {
      double thresh_loot = Gen_771.this.thresh_loot.get();
      ItemStack item = new ItemStack(Material.BREAD);
      Location loc = chest.getLocation();
      int xx = loc.getBlockX();
      int zz = loc.getBlockZ();
      for (int i = 0; i < 27; i++) {
        int x = xx + i % 9;
        int y = zz + Math.floorDiv(i, 9);
        double value = Gen_771.this.noiseLoot.getNoise(x, y);
        if (value > thresh_loot)
          chest.setItem(i, item); 
      } 
    }
  }
  
  protected void loadConfig() {
    ConfigurationSection cfg = this.plugin.getLevelParams(771);
    this.noise_lamps_freq.set(cfg.getDouble("Noise-Lamps-Freq"));
    this.noise_exits_freq.set(cfg.getDouble("Noise-Exits-Freq"));
    this.noise_loot_freq.set(cfg.getDouble("Noise-Loot-Freq"));
    this.thresh_lamps.set(cfg.getDouble("Thresh-Lamps"));
    this.thresh_ladder.set(cfg.getDouble("Thresh-Ladder"));
    this.thresh_void.set(cfg.getDouble("Thresh-Void"));
    this.thresh_loot.set(cfg.getDouble("Thresh-Loot"));
  }
  
  public static void ConfigDefaults(FileConfiguration cfg) {
    cfg.addDefault("Level771.Params.Noise-Lamps-Freq", Double.valueOf(0.3D));
    cfg.addDefault("Level771.Params.Noise-Exits-Freq", Double.valueOf(0.5D));
    cfg.addDefault("Level771.Params.Noise-Loot-Freq", Double.valueOf(0.1D));
    cfg.addDefault("Level771.Params.Thresh-Lamps", Double.valueOf(0.42D));
    cfg.addDefault("Level771.Params.Thresh-Ladder", Double.valueOf(0.81D));
    cfg.addDefault("Level771.Params.Thresh-Void", Double.valueOf(0.85D));
    cfg.addDefault("Level771.Params.Thresh-Loot", Double.valueOf(0.7D));
  }
}
