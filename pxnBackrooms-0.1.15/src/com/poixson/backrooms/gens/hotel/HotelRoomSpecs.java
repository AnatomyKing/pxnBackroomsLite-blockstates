package com.poixson.backrooms.gens.hotel;

import org.bukkit.Material;

public class HotelRoomSpecs {
  public final double value;
  
  public final RoomTheme theme;
  
  public final Material carpet;
  
  public final Material walls;
  
  public final Material bed;
  
  public final Material door;
  
  public final Material door_plate;
  
  public enum RoomTheme {
    PLAIN, OVERGROWN, CURSED, CHEESE;
  }
  
  public HotelRoomSpecs(double value, RoomTheme theme, Material carpet, Material walls, Material bed, Material door, Material door_plate) {
    this.value = value;
    this.theme = theme;
    this.carpet = carpet;
    this.walls = walls;
    this.bed = bed;
    this.door = door;
    this.door_plate = door_plate;
  }
  
  public static HotelRoomSpecs SpecsFromValue(double value) {
    RoomTheme theme;
    Material carpet, walls, bed;
    int index, index_carpet = (int)Math.floor((value + 1.0D) * 50.0D);
    int index_walls = (int)Math.floor((value + 1.0D) * 55.0D) + 5;
    int index_bed = (int)Math.floor((value + 1.0D) * 999.0D);
    if (index_carpet < 7 && index_walls < 7)
      return new HotelRoomSpecs(value, RoomTheme.CHEESE, Material.END_STONE, Material.END_STONE_BRICKS, Material.YELLOW_BED, Material.AIR, Material.AIR); 
    int num_themes = 4;
    int index_theme = (int)Math.round((value + 1.0D) * 4.0D * 0.5D);
    switch (index_theme) {
      case 0:
      case 1:
        theme = RoomTheme.PLAIN;
        break;
      case 2:
      case 4:
        theme = RoomTheme.OVERGROWN;
        break;
      case 3:
        theme = RoomTheme.CURSED;
        break;
      default:
        throw new RuntimeException("Unknown hotel room theme index: " + Integer.toString(index_theme));
    } 
    switch (theme) {
      case PLAIN:
        index = index_carpet % 9;
        switch (index) {
          case 0:
            carpet = Material.BROWN_WOOL;
            break;
          case 1:
            carpet = Material.CYAN_WOOL;
            break;
          case 2:
            carpet = Material.GRAY_WOOL;
            break;
          case 3:
            carpet = Material.BROWN_CONCRETE_POWDER;
            break;
          case 4:
            carpet = Material.CYAN_CONCRETE_POWDER;
            break;
          case 5:
            carpet = Material.GRAY_CONCRETE_POWDER;
            break;
          case 6:
            carpet = Material.LIGHT_GRAY_CONCRETE_POWDER;
            break;
          case 7:
            carpet = Material.GRANITE;
            break;
          case 8:
            carpet = Material.CALCITE;
            break;
          default:
            throw new RuntimeException("Unknown hotel carpet index: " + Integer.toString(index));
        } 
        index = index_walls % 9;
        switch (index) {
          case 0:
            walls = Material.OAK_WOOD;
            break;
          case 1:
            walls = Material.SPRUCE_WOOD;
            break;
          case 2:
            walls = Material.OAK_PLANKS;
            break;
          case 3:
            walls = Material.SPRUCE_PLANKS;
            break;
          case 4:
            walls = Material.STRIPPED_BIRCH_WOOD;
            break;
          case 5:
            walls = Material.STRIPPED_JUNGLE_WOOD;
            break;
          case 6:
            walls = Material.TUFF;
            break;
          case 7:
            walls = Material.DRIPSTONE_BLOCK;
            break;
          case 8:
            walls = Material.BONE_BLOCK;
            break;
          default:
            throw new RuntimeException("Unknown hotel walls index: " + Integer.toString(index));
        } 
        index = index_bed % 16;
        switch (index) {
          case 0:
            bed = Material.WHITE_BED;
            return new HotelRoomSpecs(value, theme, carpet, walls, bed, Material.AIR, Material.AIR);
          case 1:
            bed = Material.LIGHT_GRAY_BED;
            return new HotelRoomSpecs(value, theme, carpet, walls, bed, Material.AIR, Material.AIR);
          case 2:
            bed = Material.GRAY_BED;
            return new HotelRoomSpecs(value, theme, carpet, walls, bed, Material.AIR, Material.AIR);
          case 3:
            bed = Material.BLACK_BED;
            return new HotelRoomSpecs(value, theme, carpet, walls, bed, Material.AIR, Material.AIR);
          case 4:
            bed = Material.BROWN_BED;
            return new HotelRoomSpecs(value, theme, carpet, walls, bed, Material.AIR, Material.AIR);
          case 5:
            bed = Material.RED_BED;
            return new HotelRoomSpecs(value, theme, carpet, walls, bed, Material.AIR, Material.AIR);
          case 6:
            bed = Material.ORANGE_BED;
            return new HotelRoomSpecs(value, theme, carpet, walls, bed, Material.AIR, Material.AIR);
          case 7:
            bed = Material.YELLOW_BED;
            return new HotelRoomSpecs(value, theme, carpet, walls, bed, Material.AIR, Material.AIR);
          case 8:
            bed = Material.LIME_BED;
            return new HotelRoomSpecs(value, theme, carpet, walls, bed, Material.AIR, Material.AIR);
          case 9:
            bed = Material.GREEN_BED;
            return new HotelRoomSpecs(value, theme, carpet, walls, bed, Material.AIR, Material.AIR);
          case 10:
            bed = Material.CYAN_BED;
            return new HotelRoomSpecs(value, theme, carpet, walls, bed, Material.AIR, Material.AIR);
          case 11:
            bed = Material.LIGHT_BLUE_BED;
            return new HotelRoomSpecs(value, theme, carpet, walls, bed, Material.AIR, Material.AIR);
          case 12:
            bed = Material.BLUE_BED;
            return new HotelRoomSpecs(value, theme, carpet, walls, bed, Material.AIR, Material.AIR);
          case 13:
            bed = Material.PURPLE_BED;
            return new HotelRoomSpecs(value, theme, carpet, walls, bed, Material.AIR, Material.AIR);
          case 14:
            bed = Material.MAGENTA_BED;
            return new HotelRoomSpecs(value, theme, carpet, walls, bed, Material.AIR, Material.AIR);
          case 15:
            bed = Material.PINK_BED;
            return new HotelRoomSpecs(value, theme, carpet, walls, bed, Material.AIR, Material.AIR);
        } 
        throw new RuntimeException("Unknown hotel bed index: " + Integer.toString(index));
      case OVERGROWN:
        index = index_carpet % 7;
        switch (index) {
          case 0:
            carpet = Material.MOSS_BLOCK;
            break;
          case 1:
            carpet = Material.MUDDY_MANGROVE_ROOTS;
            break;
          case 2:
            carpet = Material.MUSHROOM_STEM;
            break;
          case 3:
            carpet = Material.BROWN_MUSHROOM_BLOCK;
            break;
          case 4:
            carpet = Material.BROWN_WOOL;
            break;
          case 5:
            carpet = Material.GRAVEL;
            break;
          case 6:
            carpet = Material.PACKED_MUD;
            break;
          default:
            throw new RuntimeException("Unknown hotel carpet index: " + Integer.toString(index));
        } 
        index = index_walls % 5;
        switch (index) {
          case 0:
            walls = Material.MOSSY_COBBLESTONE;
            break;
          case 1:
            walls = Material.MUSHROOM_STEM;
            break;
          case 2:
            walls = Material.BROWN_MUSHROOM_BLOCK;
            break;
          case 3:
            walls = Material.BROWN_WOOL;
            break;
          case 4:
            walls = Material.RED_MUSHROOM_BLOCK;
            break;
          default:
            throw new RuntimeException("Unknown hotel walls index: " + Integer.toString(index));
        } 
        index = index_bed % 5;
        switch (index) {
          case 0:
            bed = Material.LIGHT_GRAY_BED;
            return new HotelRoomSpecs(value, theme, carpet, walls, bed, Material.AIR, Material.AIR);
          case 1:
            bed = Material.GRAY_BED;
            return new HotelRoomSpecs(value, theme, carpet, walls, bed, Material.AIR, Material.AIR);
          case 2:
            bed = Material.BROWN_BED;
            return new HotelRoomSpecs(value, theme, carpet, walls, bed, Material.AIR, Material.AIR);
          case 3:
            bed = Material.GREEN_BED;
            return new HotelRoomSpecs(value, theme, carpet, walls, bed, Material.AIR, Material.AIR);
          case 4:
            bed = Material.CYAN_BED;
            return new HotelRoomSpecs(value, theme, carpet, walls, bed, Material.AIR, Material.AIR);
        } 
        throw new RuntimeException("Unknown hotel bed index: " + Integer.toString(index));
      case CURSED:
        index = index_carpet % 11;
        switch (index) {
          case 0:
            carpet = Material.CRYING_OBSIDIAN;
            break;
          case 1:
            carpet = Material.BLACKSTONE;
            break;
          case 2:
            carpet = Material.BASALT;
            break;
          case 3:
            carpet = Material.MAGMA_BLOCK;
            break;
          case 4:
            carpet = Material.CRIMSON_NYLIUM;
            break;
          case 5:
            carpet = Material.NETHERRACK;
            break;
          case 6:
            carpet = Material.WARPED_WART_BLOCK;
            break;
          case 7:
            carpet = Material.WARPED_NYLIUM;
            break;
          case 8:
            carpet = Material.SOUL_SOIL;
            break;
          case 9:
            carpet = Material.DRIPSTONE_BLOCK;
            break;
          case 10:
            carpet = Material.SMOOTH_BASALT;
            break;
          default:
            throw new RuntimeException("Unknown hotel carpet index: " + Integer.toString(index));
        } 
        index = index_walls % 8;
        switch (index) {
          case 0:
            walls = Material.CRYING_OBSIDIAN;
            break;
          case 1:
            walls = Material.AMETHYST_BLOCK;
            break;
          case 2:
            walls = Material.BASALT;
            break;
          case 3:
            walls = Material.MAGMA_BLOCK;
            break;
          case 4:
            walls = Material.NETHERRACK;
            break;
          case 5:
            walls = Material.RED_NETHER_BRICKS;
            break;
          case 6:
            walls = Material.CRIMSON_PLANKS;
            break;
          case 7:
            walls = Material.DEEPSLATE;
            break;
          default:
            throw new RuntimeException("Unknown hotel walls index: " + Integer.toString(index));
        } 
        index = index_bed % 2;
        switch (index) {
          case 0:
            bed = Material.BLACK_BED;
            return new HotelRoomSpecs(value, theme, carpet, walls, bed, Material.AIR, Material.AIR);
          case 1:
            bed = Material.RED_BED;
            return new HotelRoomSpecs(value, theme, carpet, walls, bed, Material.AIR, Material.AIR);
        } 
        throw new RuntimeException("Unknown hotel bed index: " + Integer.toString(index));
    } 
    throw new RuntimeException("Unknown hotel room theme: " + theme.toString());
  }
}
