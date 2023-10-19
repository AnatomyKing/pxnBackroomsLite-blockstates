package com.poixson.backrooms;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.poixson.commonmc.tools.plotter.BlockPlotter;
import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.utils.RandomUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArraySet;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.bukkit.plugin.PluginManager;

public abstract class BackroomsLevel extends ChunkGenerator {
  public static final int DEFAULT_SPAWN_SEARCH_HEIGHT = 10;
  
  public static final int DEFAULT_SPAWN_NEAR_DISTANCE = 100;
  
  protected final BackroomsPlugin plugin;
  
  protected final CopyOnWriteArraySet<BackroomsGen> gens = new CopyOnWriteArraySet<>();
  
  protected final CopyOnWriteArraySet<BackroomsPop> pops = new CopyOnWriteArraySet<>();
  
  protected final PopulatorManager popman = new PopulatorManager();
  
  protected final int mainlevel;
  
  public BackroomsLevel(BackroomsPlugin plugin, int mainlevel) {
    this.plugin = plugin;
    this.mainlevel = mainlevel;
    plugin.register(getMainLevel(), this);
  }
  
  public void register() {
    for (BackroomsGen gen : this.gens)
      gen.register(); 
  }
  
  public void unregister() {
    for (BackroomsGen gen : this.gens)
      gen.unregister(); 
  }
  
  protected class PopulatorManager extends BlockPopulator {
    public void populate(WorldInfo worldInfo, Random rnd, int chunkX, int chunkZ, LimitedRegion region) {
      LinkedList<BlockPlotter> delayed_plotters = new LinkedList<>();
      for (BackroomsPop pop : BackroomsLevel.this.pops)
        pop.populate(chunkX, chunkZ, region, delayed_plotters); 
      if (!delayed_plotters.isEmpty()) {
        for (BlockPlotter plot : delayed_plotters)
          plot.run(); 
        delayed_plotters.clear();
      } 
    }
  }
  
  public List<BlockPopulator> getDefaultPopulators(World world) {
    return Arrays.asList(new BlockPopulator[] { this.popman });
  }
  
  public int getMainLevel() {
    return this.mainlevel;
  }
  
  public boolean isWorldMain(int level) {
    return (getMainLevel() == level);
  }
  
  public boolean isWorldStacked() {
    return (this.gens.size() > 1);
  }
  
  public BackroomsPlugin getPlugin() {
    return this.plugin;
  }
  
  protected <T extends BackroomsGen> T register(T gen) {
    this.gens.add((BackroomsGen)gen);
    gen.loadConfig();
    gen.initNoise();
    return gen;
  }
  
  protected <T extends BackroomsPop> T register(T pop) {
    this.pops.add((BackroomsPop)pop);
    return pop;
  }
  
  public void generateSurface(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkGenerator.ChunkData chunk) {
    int seed = Long.valueOf(worldInfo.getSeed()).intValue();
    for (BackroomsGen gen : this.gens)
      gen.setSeed(seed); 
    LinkedList<BlockPlotter> delayed_plotters = new LinkedList<>();
    generate(chunkX, chunkZ, chunk, delayed_plotters);
    if (!delayed_plotters.isEmpty()) {
      for (BlockPlotter plot : delayed_plotters)
        plot.run(); 
      delayed_plotters.clear();
    } 
  }
  
  protected abstract void generate(int paramInt1, int paramInt2, ChunkGenerator.ChunkData paramChunkData, LinkedList<BlockPlotter> paramLinkedList);
  
public BiomeProvider getDefaultBiomeProvider(WorldInfo worldInfo) {
    return new BiomeProvider() {
        private final List<Biome> biomes = new ArrayList<>();
        
        public List<Biome> getBiomes(WorldInfo worldInfo) {
            return this.biomes;
        }
        
        public Biome getBiome(WorldInfo worldInfo, int x, int y, int z) {
            return Biome.THE_VOID;
        }
    };
}

  
  public int getLevelFromY(int y) {
    return getMainLevel();
  }
  
  public abstract int getY(int paramInt);
  
  public abstract int getMaxY(int paramInt);
  
  public abstract boolean containsLevel(int paramInt);
  
  public Location validateSpawn(Location loc) {
    return validateSpawn(loc, 10);
  }
  
  public Location validateSpawn(Location loc, int height) {
    World world = loc.getWorld();
    Block blockA = loc.getBlock();
    int x = loc.getBlockX();
    int y = loc.getBlockY();
    int z = loc.getBlockZ();
    for (int i = 0; i < height; i++) {
      Block blockB = world.getBlockAt(x, y + i + 1, z);
      if (blockA.isPassable() && blockB
        .isPassable())
        return blockA.getLocation(); 
      blockA = blockB;
    } 
    return null;
  }
  
  public Location getNewSpawnArea(int level) {
    int distance = this.plugin.getSpawnDistance();
    int y = getY(level);
    int x = RandomUtils.GetRandom(0 - distance, distance);
    int z = RandomUtils.GetRandom(0 - distance, distance);
    World world = this.plugin.getWorldFromLevel(level);
    if (world == null)
      throw new RuntimeException("Invalid backrooms level: " + Integer.toString(level)); 
    return getSpawnNear(world.getBlockAt(x, y, z).getLocation());
  }
  
  public Location getSpawnNear(Location spawn) {
    return getSpawnNear(spawn, 100);
  }
  
  public Location getSpawnNear(Location spawn, int distance) {
    int distanceMin = Math.floorDiv(distance, 3);
    float yaw = RandomUtils.GetRandom(0, 360);
    World world = spawn.getWorld();
    int y = spawn.getBlockY();
    for (int t = 0; t < 10; t++) {
      for (int iy = 0; iy < 10; iy++) {
        int x = spawn.getBlockX() + RandomUtils.GetRandom(distanceMin, distance);
        int z = spawn.getBlockZ() + RandomUtils.GetRandom(distanceMin, distance);
        Location near = world.getBlockAt(x, y + iy, z).getLocation();
        Location valid = validateSpawn(near);
        if (valid != null) {
          valid.setYaw(yaw);
          return valid;
        } 
      } 
    } 
    xJavaPlugin.LOG.warning("[pxnBackrooms] Failed to find a safe spawn location: " + spawn.toString());
    return spawn;
  }
  
  public Location getFixedSpawnLocation(World world, Random random) {
    int level = this.plugin.getLevelFromWorld(world);
    int y = getY(level);
    Location loc = world.getBlockAt(0, y, 0).getLocation();
    return getSpawnNear(loc);
  }
  
  public static void MakeWorld(int level, String seed) {
    MVWorldManager manager = GetMVCore().getMVWorldManager();
    String name = "level" + Integer.toString(level);
    if (!manager.isMVWorld(name, false)) {
      World.Environment env;
      int y;
      xJavaPlugin.LOG.warning("[pxnBackrooms] Creating backrooms level: " + Integer.toString(level));
      switch (level) {
        case 78:
          env = World.Environment.THE_END;
          break;
        default:
          env = World.Environment.NORMAL;
          break;
      } 
      if (!manager.addWorld(name, env, seed, WorldType.NORMAL, Boolean.FALSE, "pxnBackrooms", true))
        throw new RuntimeException("Failed to create world: " + name); 
      MultiverseWorld mvworld = manager.getMVWorld(name, false);
      World world1 = mvworld.getCBWorld();
      mvworld.setAlias("backrooms");
      mvworld.setAutoLoad(true);
      mvworld.setHidden(true);
      mvworld.setKeepSpawnInMemory(false);
      mvworld.setAllowAnimalSpawn(true);
      mvworld.setAllowMonsterSpawn(true);
      mvworld.setAutoHeal(false);
      mvworld.setHunger(true);
      mvworld.setBedRespawn(true);
      mvworld.setDifficulty(Difficulty.HARD);
      mvworld.setPVPMode(true);
      mvworld.setGenerator("pxnBackrooms");
      mvworld.setRespawnToWorld("level0");
      world1.setGameRule(GameRule.KEEP_INVENTORY, Boolean.TRUE);
      world1.setGameRule(GameRule.FORGIVE_DEAD_PLAYERS, Boolean.TRUE);
      world1.setGameRule(GameRule.DROWNING_DAMAGE, Boolean.TRUE);
      world1.setGameRule(GameRule.FREEZE_DAMAGE, Boolean.FALSE);
      world1.setGameRule(GameRule.MOB_GRIEFING, Boolean.FALSE);
      world1.setGameRule(GameRule.DO_ENTITY_DROPS, Boolean.TRUE);
      world1.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, Boolean.FALSE);
      world1.setGameRule(GameRule.SHOW_DEATH_MESSAGES, Boolean.TRUE);
      world1.setGameRule(GameRule.BLOCK_EXPLOSION_DROP_DECAY, Boolean.FALSE);
      world1.setGameRule(GameRule.TNT_EXPLOSION_DROP_DECAY, Boolean.FALSE);
      world1.setGameRule(GameRule.MOB_EXPLOSION_DROP_DECAY, Boolean.FALSE);
      world1.setGameRule(GameRule.FIRE_DAMAGE, Boolean.TRUE);
      world1.setGameRule(GameRule.DO_FIRE_TICK, Boolean.TRUE);
      world1.setGameRule(GameRule.DO_TILE_DROPS, Boolean.TRUE);
      world1.setGameRule(GameRule.DO_TRADER_SPAWNING, Boolean.TRUE);
      world1.setGameRule(GameRule.DO_WARDEN_SPAWNING, Boolean.TRUE);
      world1.setGameRule(GameRule.SPAWN_RADIUS, Integer.valueOf(50));
      world1.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, Integer.valueOf(1));
      world1.setGameRule(GameRule.SNOW_ACCUMULATION_HEIGHT, Integer.valueOf(8));
      switch (level) {
        case 771:
          mvworld.setGameMode(GameMode.ADVENTURE);
          break;
        default:
          mvworld.setGameMode(GameMode.SURVIVAL);
          break;
      } 
      switch (level) {
        case 0:
          y = 49;
          break;
        case 33:
          y = 50;
          break;
        case 78:
          y = 200;
          break;
        case 94:
          y = 0;
          break;
        case 771:
          y = -61;
          break;
        default:
          y = 0;
          break;
      } 
      mvworld.setSpawnLocation(world1.getBlockAt(0, y, 0).getLocation());
      switch (level) {
        case 0:
        case 7:
        case 33:
        case 78:
          world1.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, Boolean.FALSE);
          mvworld.setTime("midnight");
          break;
        case 151:
          world1.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, Boolean.FALSE);
          mvworld.setTime("noon");
          break;
        default:
          world1.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, Boolean.TRUE);
          mvworld.setTime("noon");
          break;
      } 
      switch (level) {
        case 0:
        case 7:
        case 33:
        case 78:
        case 151:
        case 771:
        case 866:
          world1.setGameRule(GameRule.DO_WEATHER_CYCLE, Boolean.FALSE);
          break;
        default:
          world1.setGameRule(GameRule.DO_WEATHER_CYCLE, Boolean.TRUE);
          break;
      } 
      switch (level) {
        case 0:
        case 9:
        case 11:
        case 78:
        case 151:
        case 771:
        case 866:
          world1.setGameRule(GameRule.DO_INSOMNIA, Boolean.TRUE);
          break;
        default:
          world1.setGameRule(GameRule.DO_INSOMNIA, Boolean.FALSE);
          break;
      } 
      switch (level) {
        case 33:
          world1.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, Boolean.TRUE);
          break;
        default:
          world1.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, Boolean.FALSE);
          break;
      } 
      switch (level) {
        case 0:
          world1.setGameRule(GameRule.FALL_DAMAGE, Boolean.FALSE);
          break;
        default:
          world1.setGameRule(GameRule.FALL_DAMAGE, Boolean.TRUE);
          break;
      } 
      switch (level) {
        case 0:
        case 7:
        case 9:
        case 10:
        case 11:
        case 78:
        case 151:
        case 771:
        case 866:
          world1.setGameRule(GameRule.NATURAL_REGENERATION, Boolean.FALSE);
          break;
        default:
          world1.setGameRule(GameRule.NATURAL_REGENERATION, Boolean.TRUE);
          break;
      } 
      switch (level) {
        case 0:
        case 7:
        case 9:
        case 10:
        case 11:
        case 151:
        case 866:
          world1.setGameRule(GameRule.REDUCED_DEBUG_INFO, Boolean.TRUE);
          break;
        default:
          world1.setGameRule(GameRule.REDUCED_DEBUG_INFO, Boolean.FALSE);
          break;
      } 
    } 
    World world = Bukkit.getWorld(name);
    if (world == null)
      throw new NullPointerException("Failed to find world: " + name); 
    switch (level) {
      case 33:
        world.setTicksPerSpawns(SpawnCategory.MONSTER, 1);
        return;
    } 
    world.setTicksPerSpawns(SpawnCategory.MONSTER, 100);
  }
  
  public static MultiverseCore GetMVCore() {
    PluginManager pm = Bukkit.getServer().getPluginManager();
    MultiverseCore mvcore = (MultiverseCore)pm.getPlugin("Multiverse-Core");
    if (mvcore == null)
      throw new RuntimeException("Plugin not found: Multiverse-Core"); 
    return mvcore;
  }
}
