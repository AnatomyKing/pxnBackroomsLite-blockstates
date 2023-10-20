package com.poixson.backrooms.gens.hotel;

import com.poixson.backrooms.worlds.Level_000;
import com.poixson.commonmc.tools.plotter.BlockPlotter;
import com.poixson.commonmc.tools.plotter.PlotterFactory;
import com.poixson.tools.dao.Iabcd;
import com.poixson.utils.FastNoiseLiteD;
import com.poixson.utils.StringUtils;
import java.util.LinkedList;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.LimitedRegion;

public class HotelRoomGuest implements HotelRoom {
  protected final Level_000 level0;
  
  protected final FastNoiseLiteD noise;
  
  public HotelRoomGuest(Level_000 level0, FastNoiseLiteD noise) {
    this.level0 = level0;
    this.noise = noise;
  }
  
  public void build(Iabcd area, int y, BlockFace direction, LimitedRegion region, LinkedList<BlockPlotter> plots) {
    Material block_hotel_wall = Material.matchMaterial(this.level0.gen_005.block_hall_wall.get());
    if (block_hotel_wall == null)
      throw new RuntimeException("Invalid block type for level 5 HallWall"); 
    HotelRoomSpecs specs = HotelRoomSpecs.SpecsFromValue(this.noise
        .getNoise(area.a, area.b));
    if (HotelRoomSpecs.RoomTheme.CHEESE.equals(specs.theme))
      this.level0.cheese_rooms.add(area.a, area.b); 
    int x = area.a;
    int z = area.b;
    int w = area.c;
    int d = area.d;
    int h = 7;
    BlockPlotter plot = (new PlotterFactory()).placer(region).axis("use").rotate(direction.getOppositeFace()).xyz(x, y, z).whd(w, 7, d).build();
    plot.type('#', specs.walls);
    plot.type(',', specs.carpet);
    plot.type('.', Material.AIR);
    plot.type('$', block_hotel_wall);
    plot.type('&', block_hotel_wall);
    plot.type('d', specs.door);
    plot.type('D', specs.door);
    plot.type('_', specs.door_plate);
    StringBuilder[][] matrix = plot.getMatrix3D();
    for (int iy = 0; iy < 7; iy++) {
      for (int iz = 2; iz < d - 1; iz++)
        matrix[iy][iz]
          .append('#')
          .append(StringUtils.Repeat(w - 2, (char) ((iy == 0) ? 44 : 32)))
          .append('#'); 
      matrix[iy][1].append(StringUtils.Repeat(w, '#'));
      matrix[iy][d - 1].append(StringUtils.Repeat(w, '#'));
    } 
    int door_x = Math.floorDiv(w, 2) - 2;
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
}
