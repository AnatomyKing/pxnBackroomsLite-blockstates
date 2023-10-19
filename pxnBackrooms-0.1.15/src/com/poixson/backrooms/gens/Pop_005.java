package com.poixson.backrooms.gens;

import com.poixson.backrooms.BackroomsPop;
import com.poixson.backrooms.gens.hotel.HotelRoomGuest;
import com.poixson.backrooms.gens.hotel.HotelRoomPool;
import com.poixson.backrooms.gens.hotel.HotelRoomStairs;
import com.poixson.backrooms.worlds.Level_000;
import com.poixson.commonmc.tools.plotter.BlockPlotter;
import com.poixson.tools.dao.Iabcd;
import java.util.LinkedList;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.LimitedRegion;

public class Pop_005 implements BackroomsPop {
  public static final int ROOM_SIZE = 7;
  
  protected final Level_000 level0;
  
  protected final Gen_005 gen;
  
  public Pop_005(Level_000 level0) {
    this.level0 = level0;
    this.gen = level0.gen_005;
  }
  
  public void populate(int chunkX, int chunkZ, LimitedRegion region, LinkedList<BlockPlotter> plots) {
    int x = chunkX * 16;
    int z = chunkZ * 16;
    int y = this.gen.level_y + 3 + 1;
    Iabcd area = findRoomWalls(x, y, z, region);
    if (area == null)
      area = findRoomWalls(x + 9, y, z + 9, region); 
    if (area == null)
      return; 
    buildHotelRooms(area, y, region, plots);
  }
  
  public Iabcd findRoomWalls(int x, int y, int z, LimitedRegion region) {
    Material block_hall_wall = Material.matchMaterial(this.gen.block_hall_wall.get());
    if (block_hall_wall == null)
      throw new RuntimeException("Invalid block type for level 5 HallWall"); 
    if (!Material.AIR.equals(region.getType(x, y, z)))
      return null; 
    if (Material.BLACK_GLAZED_TERRACOTTA.equals(region.getType(x, y - 1, z)))
      return null; 
    int foundN = Integer.MIN_VALUE;
    int foundS = Integer.MIN_VALUE;
    int foundE = Integer.MIN_VALUE;
    int foundW = Integer.MIN_VALUE;
    for (int i = 2; i < 34 && (
      foundN == Integer.MIN_VALUE || foundS == Integer.MIN_VALUE || foundE == Integer.MIN_VALUE || foundW == Integer.MIN_VALUE); i++) {
      if (foundN == Integer.MIN_VALUE && region
        .isInRegion(x, y, z - i)) {
        Material type = region.getType(x, y, z - i);
        if (Material.BLACK_GLAZED_TERRACOTTA.equals(type))
          return null; 
        if (block_hall_wall.equals(type)) {
          foundN = z - i + 1;
        } else if (!Material.AIR.equals(type)) {
          return null;
        } 
      } 
      if (foundS == Integer.MIN_VALUE && region
        .isInRegion(x, y, z + i)) {
        Material type = region.getType(x, y, z + i);
        if (Material.BLACK_GLAZED_TERRACOTTA.equals(type))
          return null; 
        if (block_hall_wall.equals(type)) {
          foundS = z + i - 1;
        } else if (!Material.AIR.equals(type)) {
          return null;
        } 
      } 
      if (foundE == Integer.MIN_VALUE && region
        .isInRegion(x + i, y, z)) {
        Material type = region.getType(x + i, y, z);
        if (Material.BLACK_GLAZED_TERRACOTTA.equals(type))
          return null; 
        if (block_hall_wall.equals(type)) {
          foundE = x + i - 1;
        } else if (!Material.AIR.equals(type)) {
          return null;
        } 
      } 
      if (foundW == Integer.MIN_VALUE && region
        .isInRegion(x - i, y, z)) {
        Material type = region.getType(x - i, y, z);
        if (Material.BLACK_GLAZED_TERRACOTTA.equals(type))
          return null; 
        if (block_hall_wall.equals(type)) {
          foundW = x - i + 1;
        } else if (!Material.AIR.equals(type)) {
          return null;
        } 
      } 
    } 
    if (foundN == Integer.MIN_VALUE || foundS == Integer.MIN_VALUE || foundE == Integer.MIN_VALUE || foundW == Integer.MIN_VALUE)
      return null; 
    return new Iabcd(foundW, foundN, foundE - foundW, foundS - foundN);
  }
  
  public void buildHotelRooms(Iabcd area, int y, LimitedRegion region, LinkedList<BlockPlotter> plots) {
    HotelRoomGuest room_guest = new HotelRoomGuest(this.level0, this.gen.noiseHotelRooms);
    HotelRoomPool room_pool = new HotelRoomPool(this.level0);
    HotelRoomStairs room_stairs = new HotelRoomStairs(this.level0);
    int num_rooms_wide = Math.floorDiv(area.c, 7);
    int num_rooms_deep = Math.floorDiv(area.d, 7);
    if (num_rooms_deep < 1 || num_rooms_wide < 1)
      return; 
    int room_width = Math.floorDiv(area.c, num_rooms_wide);
    int room_depth = Math.floorDiv(area.d, num_rooms_deep);
    int rooms_mid_w = Math.floorDiv(num_rooms_wide, 2);
    int rooms_mid_d = Math.floorDiv(num_rooms_deep, 2);
    int extra_x = area.c - num_rooms_wide * room_width + 1;
    int extra_z = area.d - num_rooms_deep * room_depth + 1;
    for (int room_z = 0; room_z < num_rooms_deep; room_z++) {
      int d = room_depth;
      int z = area.b + room_z * d;
      if (room_z == rooms_mid_d) {
        d += extra_z;
      } else if (room_z > rooms_mid_d) {
        z += extra_z;
      } 
      for (int room_x = 0; room_x < num_rooms_wide; room_x++) {
        if (room_x == 0 || room_x == num_rooms_wide - 1 || room_z == 0 || room_z == num_rooms_deep - 1) {
          BlockFace direction;
          Iabcd room_area;
          int w = room_width;
          int x = area.a + room_x * w;
          if (room_x == rooms_mid_w) {
            w += extra_x;
          } else if (room_x > rooms_mid_w) {
            x += extra_x;
          } 
          if (room_z == 0) {
            if (room_x == 0) {
              if (0.0D < this.gen.noiseHotelRooms.getNoise(x, z)) {
                direction = BlockFace.NORTH;
              } else {
                direction = BlockFace.WEST;
              } 
            } else if (room_x == num_rooms_wide - 1) {
              if (0.0D < this.gen.noiseHotelRooms.getNoise(x, z)) {
                direction = BlockFace.NORTH;
              } else {
                direction = BlockFace.EAST;
              } 
            } else {
              direction = BlockFace.NORTH;
            } 
          } else if (room_z == num_rooms_deep - 1) {
            if (room_x == 0) {
              if (0.0D < this.gen.noiseHotelRooms.getNoise(x, z)) {
                direction = BlockFace.SOUTH;
              } else {
                direction = BlockFace.WEST;
              } 
            } else if (room_x == num_rooms_wide - 1) {
              if (0.0D < this.gen.noiseHotelRooms.getNoise(x, z)) {
                direction = BlockFace.SOUTH;
              } else {
                direction = BlockFace.EAST;
              } 
            } else {
              direction = BlockFace.SOUTH;
            } 
          } else if (room_x == 0) {
            direction = BlockFace.WEST;
          } else {
            direction = BlockFace.EAST;
          } 
          switch (direction) {
            case NORTH:
              room_area = new Iabcd(x, z - 1, w, d + 1);
              break;
            case SOUTH:
              room_area = new Iabcd(x - 1, z - 1, w, d + 1);
              break;
            case EAST:
              room_area = new Iabcd(x - 1, z - 1, d, w + 1);
              break;
            case WEST:
              room_area = new Iabcd(x - 1, z, d, w + 1);
              break;
            default:
              throw new RuntimeException("Unknown room direction: " + direction.toString());
          } 
          if (room_area.d == 9 && 
            room_stairs.checkAtticWall(region, room_area, direction)) {
            this.level0.portal_5_to_19.add(room_area.a, room_area.b);
            room_stairs.build(room_area, y, direction, region, plots);
          } else {
            BlockFace pool_direction = room_pool.canBuildHere(room_area, region);
            if (pool_direction != null) {
              this.level0.portal_5_to_37.add(room_area.a, room_area.b);
              room_pool.build(room_area, y, direction, region, plots);
            } else {
              room_guest.build(room_area, y, direction, region, plots);
            } 
          } 
        } 
      } 
    } 
  }
}
