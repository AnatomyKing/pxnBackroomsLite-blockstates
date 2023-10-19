package com.poixson.backrooms.worlds;

import com.poixson.backrooms.BackroomsGen;
import com.poixson.backrooms.BackroomsLevel;
import com.poixson.backrooms.BackroomsPlugin;
import com.poixson.backrooms.BackroomsPop;
import com.poixson.backrooms.PreGenData;
import com.poixson.backrooms.dynmap.GeneratorTemplate;
import com.poixson.backrooms.gens.Gen_000;
import com.poixson.backrooms.gens.Gen_001;
import com.poixson.backrooms.gens.Gen_005;
import com.poixson.backrooms.gens.Gen_006;
import com.poixson.backrooms.gens.Gen_019;
import com.poixson.backrooms.gens.Gen_023;
import com.poixson.backrooms.gens.Gen_037;
import com.poixson.backrooms.gens.Gen_309;
import com.poixson.backrooms.gens.Pop_001;
import com.poixson.backrooms.gens.Pop_005;
import com.poixson.backrooms.gens.Pop_037;
import com.poixson.backrooms.gens.Pop_309;
import com.poixson.backrooms.listeners.Listener_000;
import com.poixson.backrooms.listeners.Listener_001;
import com.poixson.backrooms.listeners.Listener_006;
import com.poixson.backrooms.listeners.Listener_023;
import com.poixson.commonmc.tools.locationstore.LocationStoreManager;
import com.poixson.commonmc.tools.plotter.BlockPlotter;
import com.poixson.tools.dao.Iab;
import com.poixson.utils.RandomUtils;
import java.util.HashMap;
import java.util.LinkedList;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class Level_000 extends BackroomsLevel {
  public static final boolean ENABLE_GEN_309 = true;
  
  public static final boolean ENABLE_GEN_019 = true;
  
  public static final boolean ENABLE_GEN_005 = true;
  
  public static final boolean ENABLE_GEN_037 = true;
  
  public static final boolean ENABLE_GEN_006 = true;
  
  public static final boolean ENABLE_GEN_000 = true;
  
  public static final boolean ENABLE_GEN_023 = true;
  
  public static final boolean ENABLE_GEN_001 = true;
  
  public static final boolean ENABLE_TOP_309 = true;
  
  public static final boolean ENABLE_TOP_005 = true;
  
  public static final boolean ENABLE_TOP_037 = true;
  
  public static final boolean ENABLE_TOP_000 = true;
  
  public static final boolean ENABLE_TOP_023 = true;
  
  public static final boolean ENABLE_TOP_001 = true;
  
  public static final int SUBFLOOR = 3;
  
  public static final int SUBCEILING = 3;
  
  public static final int Y_001 = 0;
  
  public static final int H_001 = 30;
  
  public static final int Y_023 = 35;
  
  public static final int H_023 = 5;
  
  public static final int Y_000 = 49;
  
  public static final int H_000 = 5;
  
  public static final int Y_006 = 63;
  
  public static final int H_006 = 5;
  
  public static final int Y_037 = 69;
  
  public static final int H_037 = 10;
  
  public static final int Y_005 = 88;
  
  public static final int H_005 = 5;
  
  public static final int Y_019 = 100;
  
  public static final int H_019 = 10;
  
  public static final int Y_309 = 114;
  
  public final Gen_001 gen_001;
  
  public final Gen_023 gen_023;
  
  public final Gen_000 gen_000;
  
  public final Gen_006 gen_006;
  
  public final Gen_037 gen_037;
  
  public final Gen_005 gen_005;
  
  public final Gen_019 gen_019;
  
  public final Gen_309 gen_309;
  
  public final Pop_001 pop_001;
  
  public final Pop_005 pop_005;
  
  public final Pop_037 pop_037;
  
  public final Pop_309 pop_309;
  
  protected final Listener_000 listener_000;
  
  protected final Listener_001 listener_001;
  
  protected final Listener_006 listener_006;
  
  protected final Listener_023 listener_023;
  
  public final LocationStoreManager portal_0_to_1;
  
  public final LocationStoreManager portal_0_to_6;
  
  public final LocationStoreManager portal_0_to_37;
  
  public final LocationStoreManager portal_1_to_771;
  
  public final LocationStoreManager portal_5_to_19;
  
  public final LocationStoreManager portal_5_to_37;
  
  public final LocationStoreManager cheese_rooms;
  
  public final LocationStoreManager loot_chests_0;
  
  public Level_000(BackroomsPlugin plugin) {
    super(plugin, 0);
    if (plugin.enableDynmapConfigGen()) {
      GeneratorTemplate gen_tpl = new GeneratorTemplate(plugin, 0);
      gen_tpl.add(1, "basement", "Basement", 10);
      gen_tpl.add(23, "overgrow", "Overgrowth", 43);
      gen_tpl.add(0, "lobby", "Lobby", 58);
      gen_tpl.add(6, "lightsout", "Lights Out", 68);
      gen_tpl.add(37, "poolrooms", "Poolrooms", 80);
      gen_tpl.add(5, "hotel", "Hotel", 97);
      gen_tpl.add(19, "attic", "Attic", 114);
      gen_tpl.add(309, "radio", "Radio Station");
      gen_tpl.commit();
    } 
    this.gen_001 = (Gen_001)register((BackroomsGen)new Gen_001(this, 0, 30));
    this.gen_023 = (Gen_023)register((BackroomsGen)new Gen_023(this, 35, 5));
    this.gen_000 = (Gen_000)register((BackroomsGen)new Gen_000(this, 49, 5));
    this.gen_006 = (Gen_006)register((BackroomsGen)new Gen_006(this, 63, 5));
    this.gen_037 = (Gen_037)register((BackroomsGen)new Gen_037(this, 69, 10));
    this.gen_005 = (Gen_005)register((BackroomsGen)new Gen_005(this, 88, 5));
    this.gen_019 = (Gen_019)register((BackroomsGen)new Gen_019(this, 100, 10));
    this.gen_309 = (Gen_309)register((BackroomsGen)new Gen_309(this, 114, 0));
    this.pop_001 = (Pop_001)register((BackroomsPop)new Pop_001(this));
    this.pop_005 = (Pop_005)register((BackroomsPop)new Pop_005(this));
    this.pop_037 = (Pop_037)register((BackroomsPop)new Pop_037(this));
    this.pop_309 = (Pop_309)register((BackroomsPop)new Pop_309(this));
    this.listener_000 = new Listener_000(plugin);
    this.listener_001 = new Listener_001(plugin);
    this.listener_006 = new Listener_006(plugin, this);
    this.listener_023 = new Listener_023(plugin);
    this.portal_0_to_1 = new LocationStoreManager("level0", "portal_0_to_1");
    this.portal_0_to_6 = new LocationStoreManager("level0", "portal_0_to_6");
    this.portal_0_to_37 = new LocationStoreManager("level0", "portal_0_to_37");
    this.portal_1_to_771 = new LocationStoreManager("level0", "portal_1_to_771");
    this.portal_5_to_19 = new LocationStoreManager("level0", "portal_5_to_19");
    this.portal_5_to_37 = new LocationStoreManager("level0", "portal_5_to_37");
    this.cheese_rooms = new LocationStoreManager("level0", "cheese_rooms");
    this.loot_chests_0 = new LocationStoreManager("level0", "loot_0");
  }
  
  public void register() {
    super.register();
    this.portal_0_to_1.start((JavaPlugin)this.plugin);
    this.portal_0_to_6.start((JavaPlugin)this.plugin);
    this.portal_0_to_37.start((JavaPlugin)this.plugin);
    this.portal_1_to_771.start((JavaPlugin)this.plugin);
    this.portal_5_to_19.start((JavaPlugin)this.plugin);
    this.portal_5_to_37.start((JavaPlugin)this.plugin);
    this.cheese_rooms.start((JavaPlugin)this.plugin);
    this.loot_chests_0.start((JavaPlugin)this.plugin);
    this.listener_000.register();
    this.listener_001.register();
    this.listener_006.register();
    this.listener_023.register();
  }
  
  public void unregister() {
    super.unregister();
    this.listener_000.unregister();
    this.listener_001.unregister();
    this.listener_006.unregister();
    this.listener_023.unregister();
    this.portal_0_to_1.saveAll();
    this.portal_0_to_6.saveAll();
    this.portal_0_to_37.saveAll();
    this.portal_1_to_771.saveAll();
    this.portal_5_to_19.saveAll();
    this.portal_5_to_37.saveAll();
    this.cheese_rooms.saveAll();
    this.loot_chests_0.saveAll();
  }
  
  public Location getNewSpawnArea(int level) {
    int distance;
    int y;
    int z;
    int x;
    World world;
    switch (level) {
      case 0:
      case 1:
      case 5:
      case 6:
      case 19:
      case 23:
      case 37:
        return super.getNewSpawnArea(level);
      case 309:
        distance = this.plugin.getSpawnDistance();
        y = getY(level);
        z = RandomUtils.GetRandom(0, distance);
        x = this.gen_309.getPathX(z);
        world = this.plugin.getWorldFromLevel(level);
        if (world == null)
          throw new RuntimeException("Invalid backrooms level: " + Integer.toString(level)); 
        return getSpawnNear(world.getBlockAt(x, y, z).getLocation());
    } 
    throw new RuntimeException("Invalid backrooms level: " + Integer.toString(level));
  }
  
  public int getLevelFromY(int y) {
    if (y < 35)
      return 1; 
    if (y < 49)
      return 23; 
    if (y < 63)
      return 0; 
    if (y < 69)
      return 6; 
    if (y < 88)
      return 37; 
    if (y < 100)
      return 5; 
    if (y < 114)
      return 19; 
    return 309;
  }
  
  public int getY(int level) {
    switch (level) {
      case 1:
        return 0;
      case 23:
        return 35;
      case 0:
        return 49;
      case 6:
        return 63;
      case 37:
        return 69;
      case 5:
        return 88;
      case 19:
        return 100;
      case 309:
        return 114;
    } 
    throw new RuntimeException("Invalid backrooms level: " + Integer.toString(level));
  }
  
  public int getMaxY(int level) {
    switch (level) {
      case 1:
        return 34;
      case 23:
        return 48;
      case 0:
        return 62;
      case 6:
        return 68;
      case 37:
        return 87;
      case 5:
        return 99;
      case 19:
        return 113;
      case 309:
        return 320;
    } 
    throw new RuntimeException("Invalid backrooms level: " + Integer.toString(level));
  }
  
  public boolean containsLevel(int level) {
    switch (level) {
      case 0:
      case 1:
      case 5:
      case 6:
      case 19:
      case 23:
      case 37:
      case 309:
        return true;
    } 
    return false;
  }
  
  public class PregenLevel0 implements PreGenData {
    public final HashMap<Iab, Gen_000.LobbyData> lobby = new HashMap<>();
    
    public final HashMap<Iab, Gen_001.BasementData> basement = new HashMap<>();
    
    public final HashMap<Iab, Gen_005.HotelData> hotel = new HashMap<>();
    
    public final HashMap<Iab, Gen_037.PoolData> pools = new HashMap<>();
  }
  
  protected void generate(int chunkX, int chunkZ, ChunkGenerator.ChunkData chunk, LinkedList<BlockPlotter> plots) {
    PregenLevel0 pregen = new PregenLevel0();
    this.gen_000.pregenerate(pregen.lobby, chunkX, chunkZ);
    this.gen_001.pregenerate(pregen.basement, chunkX, chunkZ);
    this.gen_005.pregenerate(pregen.hotel, chunkX, chunkZ);
    this.gen_037.pregenerate(pregen.pools, chunkX, chunkZ);
    this.gen_001.generate(pregen, chunk, plots, chunkX, chunkZ);
    this.gen_023.generate(pregen, chunk, plots, chunkX, chunkZ);
    this.gen_000.generate(pregen, chunk, plots, chunkX, chunkZ);
    this.gen_006.generate(pregen, chunk, plots, chunkX, chunkZ);
    this.gen_037.generate(pregen, chunk, plots, chunkX, chunkZ);
    this.gen_005.generate(pregen, chunk, plots, chunkX, chunkZ);
    this.gen_019.generate(pregen, chunk, plots, chunkX, chunkZ);
    this.gen_309.generate(pregen, chunk, plots, chunkX, chunkZ);
  }
}
