package com.poixson.backrooms.worlds;

import com.poixson.backrooms.BackroomsGen;
import com.poixson.backrooms.BackroomsLevel;
import com.poixson.backrooms.BackroomsPlugin;
import com.poixson.backrooms.dynmap.GeneratorTemplate;
import com.poixson.backrooms.gens.Gen_771;
import com.poixson.backrooms.listeners.Listener_771;
import com.poixson.commonmc.tools.locationstore.LocationStoreManager;
import com.poixson.commonmc.tools.plotter.BlockPlotter;
import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.utils.RandomUtils;
import java.util.LinkedList;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class Level_771 extends BackroomsLevel {
  public static final boolean ENABLE_GEN_771 = true;
  
  public static final int LEVEL_Y = -61;
  
  public static final int LEVEL_H = 360;
  
  public final Gen_771 gen;
  
  protected final Listener_771 listener_771;
  
  public final LocationStoreManager portal_ladder;
  
  public final LocationStoreManager portal_drop;
  
  public final LocationStoreManager portal_void;
  
  public final LocationStoreManager loot_chests_upper;
  
  public final LocationStoreManager loot_chests_lower;
  
  public Level_771(BackroomsPlugin plugin) {
    super(plugin, 771);
    if (plugin.enableDynmapConfigGen()) {
      GeneratorTemplate gen_tpl = new GeneratorTemplate(plugin, 0);
      gen_tpl.add(771, "crossroads", "Crossroads");
      gen_tpl.commit();
    } 
    this.gen = (Gen_771)register((BackroomsGen)new Gen_771(this, -61, 360));
    this.listener_771 = new Listener_771(plugin);
    this.portal_ladder = new LocationStoreManager("level771", "portal_ladder");
    this.portal_drop = new LocationStoreManager("level771", "portal_drop");
    this.portal_void = new LocationStoreManager("level771", "portal_void");
    this.loot_chests_upper = new LocationStoreManager("level771", "loot_upper");
    this.loot_chests_lower = new LocationStoreManager("level771", "loot_lower");
  }
  
  public void register() {
    super.register();
    this.portal_ladder.start((JavaPlugin)this.plugin);
    this.portal_drop.start((JavaPlugin)this.plugin);
    this.portal_void.start((JavaPlugin)this.plugin);
    this.loot_chests_upper.start((JavaPlugin)this.plugin);
    this.loot_chests_lower.start((JavaPlugin)this.plugin);
    this.listener_771.register();
  }
  
  public void unregister() {
    super.unregister();
    this.listener_771.unregister();
    this.portal_ladder.saveAll();
    this.portal_drop.saveAll();
    this.portal_void.saveAll();
    this.loot_chests_upper.saveAll();
    this.loot_chests_lower.saveAll();
  }
  
  public Location getNewSpawnArea(int level) {
    int distance = this.plugin.getSpawnDistance();
    int y = getY(level);
    int x = RandomUtils.GetRandom(0 - distance, distance);
    int z = RandomUtils.GetRandom(0 - distance, distance);
    if (Math.abs(x) > Math.abs(z)) {
      z = 0;
    } else {
      x = 0;
    } 
    World world = this.plugin.getWorldFromLevel(level);
    if (world == null)
      throw new RuntimeException("Invalid backrooms level: " + Integer.toString(level)); 
    return getSpawnNear(world.getBlockAt(x, y, z).getLocation());
  }
  
  public Location getSpawnNear(Location spawn, int distance) {
    int distanceMin = Math.floorDiv(distance, 3);
    float yaw = RandomUtils.GetRandom(0, 360);
    World world = spawn.getWorld();
    int y = spawn.getBlockY();
    boolean axis = (spawn.getBlockX() == 0);
    int x = 0;
    int z = 0;
    for (int t = 0; t < 10; t++) {
      for (int iy = 0; iy < 10; iy++) {
        if (axis) {
          z = spawn.getBlockZ() + RandomUtils.GetRandom(distanceMin, distance);
        } else {
          x = spawn.getBlockX() + RandomUtils.GetRandom(distanceMin, distance);
        } 
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
  
  public int getY(int level) {
    return 300;
  }
  
  public int getMaxY(int level) {
    return 320;
  }
  
  public boolean containsLevel(int level) {
    return (level == 771);
  }
  
  protected void generate(int chunkX, int chunkZ, ChunkGenerator.ChunkData chunk, LinkedList<BlockPlotter> plots) {
    this.gen.generate(null, chunk, plots, chunkX, chunkZ);
  }
}
