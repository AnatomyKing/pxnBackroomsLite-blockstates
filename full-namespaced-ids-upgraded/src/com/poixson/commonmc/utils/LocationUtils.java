package com.poixson.commonmc.utils;

import com.poixson.tools.Keeper;
import com.poixson.tools.dao.Iab;
import com.poixson.tools.dao.Iabc;
import com.poixson.tools.dao.Iabcd;
import org.bukkit.Axis;
import org.bukkit.Rotation;
import org.bukkit.block.BlockFace;

public final class LocationUtils {
  static {
    Keeper.add(new LocationUtils());
  }
  
  public static Iabcd Rotate(Iabcd loc, BlockFace face) {
    switch (face) {
      case SOUTH:
        return loc;
      case NORTH:
        return new Iabcd(loc.c - loc.a, loc.d - loc.b, loc.c, 0 - loc.d);
      case EAST:
        return new Iabcd(loc.b, loc.a, loc.d, loc.c);
      case WEST:
        return new Iabcd(loc.d - loc.b, loc.c - loc.a, 0 - loc.d, loc.c);
    } 
    return null;
  }
  
  public static BlockFace Rotate(BlockFace face, double rotation) {
    if (rotation < 0.0D)
      throw new RuntimeException("Invalid rotation: " + Double.toString(rotation)); 
    double rot = rotation % 1.0D;
    if (rot == 0.0D)
      return face; 
    BlockFace dir = face;
    while (rot > 0.0D) {
      switch (dir) {
        case NORTH:
          dir = BlockFace.NORTH_EAST;
          break;
        case NORTH_EAST:
          dir = BlockFace.EAST;
          break;
        case EAST:
          dir = BlockFace.SOUTH_EAST;
          break;
        case SOUTH_EAST:
          dir = BlockFace.SOUTH;
          break;
        case SOUTH:
          dir = BlockFace.SOUTH_WEST;
          break;
        case SOUTH_WEST:
          dir = BlockFace.WEST;
          break;
        case WEST:
          dir = BlockFace.NORTH_WEST;
          break;
        case NORTH_WEST:
          dir = BlockFace.NORTH;
          break;
        default:
          throw new RuntimeException("Invalid direction: " + face.toString());
      } 
      rot -= 0.125D;
    } 
    return dir;
  }
  
  public static char Rotate(char ax, double rotation) {
    if (rotation < 0.0D)
      throw new RuntimeException("Invalid rotation: " + Double.toString(rotation)); 
    double rot = rotation % 1.0D;
    if (rot == 0.0D)
      return ax; 
    char dir = ax;
    while (rot > 0.0D) {
      switch (dir) {
        case 'd':
        case 'u':
          break;
        case 'n':
          dir = 'e';
          break;
        case 'e':
          dir = 's';
          break;
        case 's':
          dir = 'w';
          break;
        case 'w':
          dir = 'n';
          break;
        default:
          throw new RuntimeException("Invalid direction: " + dir);
      } 
      rot -= 0.25D;
    } 
    return dir;
  }
  
  public static String Rotate(String axis, BlockFace rotate) {
    if (BlockFace.SOUTH.equals(rotate))
      return axis; 
    StringBuilder result = new StringBuilder();
    int len = axis.length();
    for (int i = 0; i < len; i++) {
      switch (rotate) {
        case WEST:
          result.append(Rotate(axis.charAt(i), 0.25D));
          break;
        case NORTH:
          result.append(Rotate(axis.charAt(i), 0.5D));
          break;
        case EAST:
          result.append(Rotate(axis.charAt(i), 0.75D));
          break;
      } 
    } 
    return result.toString();
  }
  
  public static BlockFace YawToFace(float yaw) {
    float way = yaw + 22.5F;
    while (way < 0.0F)
      way += 360.0F; 
    way %= 360.0F;
    if (way < 90.0F)
      return BlockFace.SOUTH; 
    if (way < 180.0F)
      return BlockFace.WEST; 
    if (way < 270.0F)
      return BlockFace.NORTH; 
    return BlockFace.EAST;
  }
  
  public static Rotation YawToRotation(float yaw) {
    float way = yaw + 22.5F;
    while (way < 0.0F)
      way += 360.0F; 
    way %= 360.0F;
    if (way < 45.0F)
      return Rotation.NONE; 
    if (way < 90.0F)
      return Rotation.CLOCKWISE_45; 
    if (way < 135.0F)
      return Rotation.CLOCKWISE; 
    if (way < 180.0F)
      return Rotation.CLOCKWISE_135; 
    if (way < 225.0F)
      return Rotation.FLIPPED; 
    if (way < 270.0F)
      return Rotation.FLIPPED_45; 
    if (way < 315.0F)
      return Rotation.COUNTER_CLOCKWISE; 
    return Rotation.COUNTER_CLOCKWISE_45;
  }
  
  public static Rotation YawToRotation90(float yaw) {
    float way = yaw + 67.5F;
    while (way < 0.0F)
      way += 360.0F; 
    way %= 360.0F;
    if (way < 90.0F)
      return Rotation.NONE; 
    if (way < 180.0F)
      return Rotation.CLOCKWISE; 
    if (way < 270.0F)
      return Rotation.FLIPPED; 
    return Rotation.COUNTER_CLOCKWISE;
  }
  
  public static Rotation FaceToRotation(BlockFace face) {
    switch (face) {
      case SOUTH:
        return Rotation.NONE;
      case WEST:
        return Rotation.CLOCKWISE;
      case NORTH:
        return Rotation.FLIPPED;
      case EAST:
        return Rotation.COUNTER_CLOCKWISE;
    } 
    return null;
  }
  
  public static Axis FaceToAxis(BlockFace face) {
    switch (face) {
      case UP:
      case DOWN:
        return Axis.Y;
      case SOUTH:
      case NORTH:
        return Axis.Z;
      case EAST:
      case WEST:
        return Axis.X;
    } 
    return null;
  }
  
  public static char FaceToAxChar(BlockFace face) {
    switch (face) {
      case UP:
        return 'u';
      case DOWN:
        return 'd';
      case NORTH:
        return 'n';
      case SOUTH:
        return 's';
      case EAST:
        return 'e';
      case WEST:
        return 'w';
    } 
    return Character.MIN_VALUE;
  }
  
  public static String FaceToAxString(BlockFace face) {
    switch (face) {
      case UP:
        return "u";
      case DOWN:
        return "d";
      case NORTH:
        return "n";
      case SOUTH:
        return "s";
      case EAST:
        return "e";
      case WEST:
        return "w";
      case NORTH_EAST:
        return "ne";
      case NORTH_WEST:
        return "nw";
      case SOUTH_EAST:
        return "se";
      case SOUTH_WEST:
        return "sw";
    } 
    return null;
  }
  
  public static BlockFace AxToFace(char ax) {
    switch (ax) {
      case 'Y':
      case 'u':
        return BlockFace.UP;
      case 'd':
      case 'y':
        return BlockFace.DOWN;
      case 'n':
      case 'z':
        return BlockFace.NORTH;
      case 'Z':
      case 's':
        return BlockFace.SOUTH;
      case 'X':
      case 'e':
        return BlockFace.EAST;
      case 'w':
      case 'x':
        return BlockFace.WEST;
    } 
    return null;
  }
  
  public static BlockFace AxToFace(String ax) {
    switch (ax) {
      case "u":
      case "Y":
        return BlockFace.UP;
      case "d":
      case "y":
        return BlockFace.DOWN;
      case "n":
      case "z":
        return BlockFace.NORTH;
      case "s":
      case "Z":
        return BlockFace.SOUTH;
      case "e":
      case "X":
        return BlockFace.EAST;
      case "w":
      case "x":
        return BlockFace.WEST;
      case "ne":
        return BlockFace.NORTH_EAST;
      case "nw":
        return BlockFace.NORTH_WEST;
      case "se":
        return BlockFace.SOUTH_EAST;
      case "sw":
        return BlockFace.SOUTH_WEST;
    } 
    return null;
  }
  
  public static Iabc FaceToIxyz(BlockFace face) {
    switch (face) {
      case NORTH:
        return new Iabc(0, 0, -1);
      case SOUTH:
        return new Iabc(0, 0, 1);
      case EAST:
        return new Iabc(1, 0, 0);
      case WEST:
        return new Iabc(-1, 0, 0);
      case UP:
        return new Iabc(0, 1, 0);
      case DOWN:
        return new Iabc(0, -1, 0);
      case NORTH_EAST:
        return new Iabc(1, 0, -1);
      case NORTH_WEST:
        return new Iabc(-1, 0, -1);
      case SOUTH_EAST:
        return new Iabc(1, 0, 1);
      case SOUTH_WEST:
        return new Iabc(-1, 0, 1);
    } 
    return null;
  }
  
  public static Iab FaceToIxz(BlockFace face) {
    switch (face) {
      case NORTH:
        return new Iab(0, -1);
      case SOUTH:
        return new Iab(0, 1);
      case EAST:
        return new Iab(1, 0);
      case WEST:
        return new Iab(-1, 0);
      case NORTH_EAST:
        return new Iab(1, -1);
      case NORTH_WEST:
        return new Iab(-1, -1);
      case SOUTH_EAST:
        return new Iab(1, 1);
      case SOUTH_WEST:
        return new Iab(-1, 1);
    } 
    return null;
  }
  
  public static Iabc AxToIxyz(char ax) {
    switch (ax) {
      case 'n':
      case 'z':
        return new Iabc(0, 0, -1);
      case 'Z':
      case 's':
        return new Iabc(0, 0, 1);
      case 'X':
      case 'e':
        return new Iabc(1, 0, 0);
      case 'w':
      case 'x':
        return new Iabc(-1, 0, 0);
      case 'Y':
      case 'u':
        return new Iabc(0, 1, 0);
      case 'd':
      case 'y':
        return new Iabc(0, -1, 0);
    } 
    return null;
  }
  
  public static Iab AxToIxz(char ax) {
    switch (ax) {
      case 'n':
      case 'z':
        return new Iab(0, -1);
      case 'Z':
      case 's':
        return new Iab(0, 1);
      case 'X':
      case 'e':
        return new Iab(1, 0);
      case 'w':
      case 'x':
        return new Iab(-1, 0);
    } 
    return null;
  }
  
  public static BlockFace ValueToFaceQuarter(int x, int z) {
    if (x < 0)
      return (z < 0) ? BlockFace.NORTH_WEST : BlockFace.SOUTH_WEST; 
    return (z < 0) ? BlockFace.NORTH_EAST : BlockFace.SOUTH_EAST;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonm\\utils\LocationUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */