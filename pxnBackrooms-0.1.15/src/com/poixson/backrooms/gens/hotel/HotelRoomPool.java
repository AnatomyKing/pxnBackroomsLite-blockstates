package com.poixson.backrooms.gens.hotel;

import com.poixson.backrooms.worlds.Level_000;
import com.poixson.commonmc.tools.plotter.BlockPlotter;
import com.poixson.commonmc.tools.plotter.PlotterFactory;
import com.poixson.tools.dao.Iabcd;
import com.poixson.utils.StringUtils;
import java.util.LinkedList;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.LimitedRegion;

public class HotelRoomPool implements HotelRoom {
  protected final Level_000 level0;
  
  public HotelRoomPool(Level_000 level0) {
    this.level0 = level0;
  }
  
  public BlockFace canBuildHere(Iabcd area, LimitedRegion region) {
    int x = area.a;
    int z = area.b;
    int w = area.c;
    int d = area.d;
    int wh = Math.floorDiv(w, 2);
    int dh = Math.floorDiv(d, 2);
    int y = 82;
    if (!region.isInRegion(x + w - 1, 82, z) || Material.AIR.equals(region.getType(x + w - 1, 82, z)))
      return null; 
    if (!region.isInRegion(x, 82, z) || Material.AIR.equals(region.getType(x, 82, z)))
      return null; 
    if (!region.isInRegion(x + w - 1, 82, z + d - 1) || Material.AIR.equals(region.getType(x + w - 1, 82, z + d - 1)))
      return null; 
    if (!region.isInRegion(x, 82, z + d - 1) || Material.AIR.equals(region.getType(x, 82, z + d - 1)))
      return null; 
    if (region.isInRegion(x + wh, 82, z - 1) && Material.AIR.equals(region.getType(x + wh, 82, z - 1)))
      return BlockFace.NORTH; 
    if (region.isInRegion(x + wh, 82, z + d + 1) && Material.AIR.equals(region.getType(x + wh, 82, z + d + 1)))
      return BlockFace.SOUTH; 
    if (region.isInRegion(x + w + 1, 82, z + dh) && Material.AIR.equals(region.getType(x + w + 1, 82, z + dh)))
      return BlockFace.EAST; 
    if (region.isInRegion(x - 1, 82, z + dh) && Material.AIR.equals(region.getType(x - 1, 82, z + dh)))
      return BlockFace.WEST; 
    return null;
  }
  
  public void build(Iabcd area, int y, BlockFace direction, LimitedRegion region, LinkedList<BlockPlotter> plots) {
    Material block_hotel_wall = Material.matchMaterial(this.level0.gen_005.block_hall_wall.get());
    Material block_pool_wall_a = Material.matchMaterial(this.level0.gen_037.block_wall_a.get());
    Material block_pool_wall_b = Material.matchMaterial(this.level0.gen_037.block_wall_b.get());
    if (block_hotel_wall == null)
      throw new RuntimeException("Invalid block type for level 5 HallWall"); 
    if (block_pool_wall_a == null)
      throw new RuntimeException("Invalid block type for level 37 WallA"); 
    if (block_pool_wall_b == null)
      throw new RuntimeException("Invalid block type for level 37 WallB"); 
    int x = area.a;
    int z = area.b;
    int w = area.c;
    int d = area.d;
    int yy = 73;
    int th = 26;
    BlockPlotter plot = (new PlotterFactory()).placer(region).axis("use").rotate(direction.getOppositeFace()).xyz(x, 73, z).whd(w, 26, d).build();
    plot.type('#', block_pool_wall_a);
    plot.type('@', block_pool_wall_b);
    plot.type('.', Material.AIR);
    plot.type(',', "minecraft:water[level=0]");
    plot.type('g', Material.SEA_LANTERN);
    plot.type('X', Material.BEDROCK);
    plot.type('-', Material.WATER);
    plot.type('$', block_hotel_wall);
    plot.type('&', block_hotel_wall);
    plot.type('d', Material.AIR);
    plot.type('D', Material.AIR);
    plot.type('_', Material.AIR);
    StringBuilder[][] matrix = plot.getMatrix3D();
    int hy = 19;
    for (int iy = 0; iy < 26; iy++) {
      if (iy == 0) {
        matrix[iy][0].append(StringUtils.Repeat(w, 'g'));
        matrix[iy][d - 1].append(matrix[0][0].toString());
        for (int iz = 1; iz < d - 1; iz++)
          matrix[iy][iz].append('g').append(StringUtils.Repeat(w - 2, '#')).append('g'); 
      } else if (iy < 3) {
        matrix[iy][0].append('#').append(StringUtils.Repeat(w - 2, ',')).append('#');
        matrix[iy][d - 1].append(matrix[iy][0].toString());
        for (int iz = 1; iz < d - 1; iz++)
          matrix[iy][iz].append(StringUtils.Repeat(w, ',')); 
      } else if (iy == 3) {
        matrix[iy][0].append("#-").append(StringUtils.Repeat(w - 4, ',')).append("-#");
        matrix[iy][1].append('-').append(StringUtils.Repeat(w - 2, ',')).append('-');
        matrix[iy][d - 1].append(matrix[iy][0].toString());
        matrix[iy][d - 2].append(matrix[iy][1].toString());
        for (int iz = 2; iz < d - 2; iz++)
          matrix[iy][iz].append(StringUtils.Repeat(w, ',')); 
      } else if (iy == 4) {
        matrix[iy][0].append('@').append(StringUtils.Repeat(w - 2, '#')).append('@');
        matrix[iy][d - 1].append(matrix[iy][0].toString());
        for (int iz = 1; iz < d - 1; iz++)
          matrix[iy][iz].append('#').append(StringUtils.Repeat(w - 2, ',')).append('#'); 
      } else if (iy < 19) {
        matrix[iy][0].append(StringUtils.Repeat(w, '@'));
        matrix[iy][1].append('@').append(StringUtils.Repeat(w - 2, 'X')).append('@');
        matrix[iy][d - 1].append(matrix[iy][0].toString());
        matrix[iy][d - 2].append(matrix[iy][1].toString());
        for (int iz = 2; iz < d - 2; iz++)
          matrix[iy][iz].append("@X").append(StringUtils.Repeat(w - 4, ',')).append("X@"); 
      } else if (iy == 19) {
        matrix[iy][0].append(StringUtils.Repeat(w, '@'));
        matrix[iy][1].append('@').append(StringUtils.Repeat(w - 2, '#')).append('@');
        matrix[iy][2].append(matrix[iy][1].toString());
        matrix[iy][d - 1].append(matrix[iy][0].toString());
        matrix[iy][d - 2].append(matrix[iy][1].toString());
        for (int iz = 3; iz < d - 2; iz++)
          matrix[iy][iz].append("@#").append(StringUtils.Repeat(w - 4, ',')).append("#@"); 
      } else if (iy == 25) {
        matrix[iy][d - 1].append(StringUtils.Repeat(w, '@'));
        for (int iz = 1; iz < d - 1; iz++)
          matrix[iy][iz].append('@').append(StringUtils.Repeat(w - 2, 'g')).append('@'); 
      } else {
        matrix[iy][0].append(StringUtils.Repeat(w, ' '));
        matrix[iy][d - 1].append(StringUtils.Repeat(w, '@'));
        for (int iz = 1; iz < d - 1; iz++)
          matrix[iy][iz].append('@').append(StringUtils.Repeat(w - 2, ' ')).append('@'); 
      } 
    } 
    int door_x = Math.floorDiv(w, 2) - 2;
    StringUtils.ReplaceInString(matrix[23][0], "&&&&&", door_x);
    StringUtils.ReplaceInString(matrix[22][0], "$...$", door_x);
    StringUtils.ReplaceInString(matrix[21][0], "$.d.$", door_x);
    StringUtils.ReplaceInString(matrix[20][0], "$.D.$", door_x);
    StringUtils.ReplaceInString(matrix[19][0], "$&&&$", door_x);
    StringUtils.ReplaceInString(matrix[22][1], "&&&", door_x + 1);
    StringUtils.ReplaceInString(matrix[21][1], "$.$", door_x + 1);
    StringUtils.ReplaceInString(matrix[20][1], "$_$", door_x + 1);
    plot.run();
  }
}
