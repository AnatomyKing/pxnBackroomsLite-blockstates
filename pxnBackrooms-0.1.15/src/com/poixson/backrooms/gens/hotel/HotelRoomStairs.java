package com.poixson.backrooms.gens.hotel;

import com.poixson.backrooms.worlds.Level_000;
import com.poixson.commonmc.tools.plotter.BlockPlotter;
import com.poixson.commonmc.tools.plotter.PlotterFactory;
import com.poixson.commonmc.utils.LocationUtils;
import com.poixson.tools.dao.Iab;
import com.poixson.tools.dao.Iabcd;
import com.poixson.utils.FastNoiseLiteD;
import com.poixson.utils.StringUtils;
import java.util.LinkedList;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.LimitedRegion;

public class HotelRoomStairs implements HotelRoom {
  protected static final double THRESH_ATTIC_STAIRS = 0.8D;
  
  protected final Level_000 level0;
  
  protected final FastNoiseLiteD noiseHotelStairs;
  
  public HotelRoomStairs(Level_000 level0) {
    this.level0 = level0;
    this.noiseHotelStairs = level0.gen_005.noiseHotelStairs;
  }
  
  public void build(Iabcd area, int y, BlockFace direction, LimitedRegion region, LinkedList<BlockPlotter> plots) {
    buildHotelRoomStairs(area, y, direction, region, plots);
    buildAtticStairs(area, y, direction, region, plots);
  }
  
  protected void buildHotelRoomStairs(Iabcd area, int y, BlockFace direction, LimitedRegion region, LinkedList<BlockPlotter> plots) {
    Material block_hall_wall = Material.matchMaterial(this.level0.gen_005.block_hall_wall.get());
    if (block_hall_wall == null)
      throw new RuntimeException("Invalid block type for level 5 HallWall"); 
    int x = area.a;
    int z = area.b;
    int w = area.c;
    int d = area.d;
    int h = 7;
    BlockPlotter plot = (new PlotterFactory()).placer(region).axis("use").rotate(direction.getOppositeFace()).xyz(x, y, z).whd(w, 7, d).build();
    plot.type('.', Material.AIR);
    plot.type('#', Material.BEDROCK);
    plot.type('=', Material.DARK_OAK_PLANKS);
    plot.type('$', block_hall_wall);
    plot.type('&', block_hall_wall);
    plot.type('d', Material.SPRUCE_DOOR);
    plot.type('D', Material.SPRUCE_DOOR);
    plot.type('_', Material.DARK_OAK_PRESSURE_PLATE);
    plot.type('L', Material.DARK_OAK_STAIRS);
    StringBuilder[][] matrix = plot.getMatrix3D();
    int door_x = Math.floorDiv(w, 2) - 2;
    for (int iy = 0; iy < 7; iy++) {
      for (int iz = 2; iz < d - 1; iz++) {
        char fill;
        if (iy == 0) {
          fill = '=';
        } else if (iy == iz - 2) {
          fill = 'L';
        } else if (iy > iz - 2) {
          fill = ' ';
        } else {
          fill = '#';
        } 
        matrix[iy][iz]
          .append(StringUtils.Repeat(door_x + 1, '#'))
          .append(StringUtils.Repeat(3, fill))
          .append(StringUtils.Repeat(w - door_x - 4, '#'));
      } 
      if (iy == 0) {
        matrix[iy][1].append('#').append(StringUtils.Repeat(w - 2, '=')).append('#');
      } else {
        matrix[iy][1].append(StringUtils.Repeat(w, '#'));
      } 
      if (iy < 6) {
        matrix[iy][d - 1].append(StringUtils.Repeat(w, '#'));
      } else {
        matrix[iy][d - 1]
          .append(StringUtils.Repeat(door_x + 1, '#'))
          .append(StringUtils.Repeat(3, 'L'))
          .append(StringUtils.Repeat(w - door_x - 4, '#'));
      } 
    } 
    matrix[4][0].append(StringUtils.Repeat(door_x, ' ')).append("&&&&&");
    matrix[3][0].append(StringUtils.Repeat(door_x, ' ')).append("$...$");
    matrix[2][0].append(StringUtils.Repeat(door_x, ' ')).append("$.d.$");
    matrix[1][0].append(StringUtils.Repeat(door_x, ' ')).append("$.D.$");
    matrix[0][0].append(StringUtils.Repeat(door_x, ' ')).append("$&&&$");
    StringUtils.ReplaceInString(matrix[3][1], "&&&", door_x + 1);
    StringUtils.ReplaceInString(matrix[2][1], "$.$", door_x + 1);
    StringUtils.ReplaceInString(matrix[1][1], "$_$", door_x + 1);
    plot.run();
  }
  
  protected void buildAtticStairs(Iabcd area, int y, BlockFace direction, LimitedRegion region, LinkedList<BlockPlotter> plots) {
    int offset = 9;
    int x = area.a + (BlockFace.EAST.equals(direction) ? -5 : 0);
    int z = area.b + (BlockFace.SOUTH.equals(direction) ? -5 : 0);
    int w = area.c;
    int d = area.d + 9 - 4;
    int yy = 99;
    int h = 5;
    BlockPlotter plot = (new PlotterFactory()).placer(region).axis("use").rotate(direction.getOppositeFace()).xyz(x, 99, z).whd(w, 5, d).build();
    plot.type('.', Material.AIR);
    plot.type('#', Material.BEDROCK);
    plot.type('L', Material.DARK_OAK_STAIRS);
    StringBuilder[][] matrix = plot.getMatrix3D();
    int door_x = Math.floorDiv(w, 2) - 2;
    for (int iy = 0; iy < 5; iy++) {
      matrix[iy][1]
        .append(StringUtils.Repeat(door_x, ' '))
        .append(StringUtils.Repeat(5, '#'));
      for (int iz = 2; iz < d; iz++) {
        char fill;
        if (iy == iz - 9) {
          fill = 'L';
        } else if (iy > iz - 9) {
          fill = '.';
        } else {
          fill = '#';
        } 
        matrix[iy][iz]
          .append(StringUtils.Repeat(door_x, ' ')).append('#')
          .append(StringUtils.Repeat(3, fill)).append('#');
      } 
    } 
    plots.add(plot);
  }
  
  public boolean checkAtticWall(LimitedRegion region, Iabcd room_area, BlockFace direction) {
    double value = this.noiseHotelStairs.getNoise(room_area.a, room_area.b);
    if (value < 0.8D || value < this.noiseHotelStairs
      .getNoise(room_area.a, (room_area.b - 8)) || value < this.noiseHotelStairs
      .getNoise(room_area.a, (room_area.b + 8)) || value < this.noiseHotelStairs
      .getNoise((room_area.a + 8), room_area.b) || value < this.noiseHotelStairs
      .getNoise((room_area.a - 8), room_area.b))
      return false; 
    Material block_attic_wall = Material.matchMaterial(this.level0.gen_019.block_wall.get());
    if (block_attic_wall == null)
      throw new RuntimeException("Invalid block type for level 19 Wall"); 
    Iab ab = LocationUtils.FaceToIxz(direction);
    int xx = room_area.a + room_area.c - 8 * ab.a;
    int zz = room_area.b + room_area.d - 8 * ab.b;
    int yy = 104;
    if (region.isInRegion(xx, 0, zz) && region
      .isInRegion(xx - room_area.c, 0, zz - room_area.d))
      for (int iz = 0; iz < room_area.d; iz++) {
        for (int ix = 0; ix < room_area.c; ix++) {
          Material type = region.getType(xx - ix, 105, zz - iz);
          if (block_attic_wall.equals(type))
            return false; 
        } 
      }  
    return true;
  }
}
