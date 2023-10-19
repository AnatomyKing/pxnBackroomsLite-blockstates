package com.poixson.backrooms;

import java.util.LinkedList;
import java.util.List;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;

class BackroomsLevel1 extends BiomeProvider {
  private final List<Biome> biomes;
  
  BackroomsLevel1() {
    this.biomes = new LinkedList<>();
    this.biomes.add(Biome.THE_VOID);
  }
  
  public List<Biome> getBiomes(WorldInfo worldInfo) {
    return this.biomes;
  }
  
  public Biome getBiome(WorldInfo worldInfo, int x, int y, int z) {
    return Biome.THE_VOID;
  }
}
