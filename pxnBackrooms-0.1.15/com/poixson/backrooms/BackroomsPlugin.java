package com.poixson.backrooms;

import com.poixson.backrooms.commands.Commands;
import com.poixson.backrooms.dynmap.GeneratorPerspective;
import com.poixson.backrooms.gens.Gen_000;
import com.poixson.backrooms.gens.Gen_001;
import com.poixson.backrooms.gens.Gen_005;
import com.poixson.backrooms.gens.Gen_006;
import com.poixson.backrooms.gens.Gen_019;
import com.poixson.backrooms.gens.Gen_023;
import com.poixson.backrooms.gens.Gen_033;
import com.poixson.backrooms.gens.Gen_037;
import com.poixson.backrooms.gens.Gen_094;
import com.poixson.backrooms.gens.Gen_309;
import com.poixson.backrooms.gens.Gen_771;
import com.poixson.backrooms.listeners.PlayerDamageListener;
import com.poixson.backrooms.tasks.QuoteAnnouncer;
import com.poixson.backrooms.tasks.TaskHourly;
import com.poixson.backrooms.tasks.TeleportManager;
import com.poixson.backrooms.worlds.Level_000;
import com.poixson.backrooms.worlds.Level_033;
import com.poixson.backrooms.worlds.Level_094;
import com.poixson.backrooms.worlds.Level_771;
import com.poixson.commonmc.tools.DelayedChestFiller;
import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BackroomsPlugin extends xJavaPlugin {
  public static final String LOG_PREFIX = "[pxnBackrooms] ";
  
  public int getSpigotPluginID() {
    return 108148;
  }
  
  public int getBStatsID() {
    return 17231;
  }
  
  public static final String CHAT_PREFIX = "" + ChatColor.AQUA + "[Backrooms] " + ChatColor.AQUA;
  
  public static final String GENERATOR_NAME = "pxnBackrooms";
  
  protected static final String DEFAULT_RESOURCE_PACK = "https://dl.poixson.com/mcplugins/pxnBackrooms/pxnBackrooms-resourcepack-{VERSION}.zip";
  
  protected static final int DEFAULT_SPAWN_DISTANCE = 10000;
  
  protected final HashMap<Integer, BackroomsLevel> backlevels = new HashMap<>();
  
  protected final ConcurrentHashMap<UUID, CopyOnWriteArraySet<Integer>> visitLevels = new ConcurrentHashMap<>();
  
  protected final AtomicReference<TaskHourly> hourlyTask = new AtomicReference<>(null);
  
  protected final AtomicReference<QuoteAnnouncer> quoteAnnouncer = new AtomicReference<>(null);
  
  protected final AtomicReference<TeleportManager> tpManager = new AtomicReference<>(null);
  
  protected final AtomicReference<Commands> commands = new AtomicReference<>(null);
  
  protected final AtomicReference<PlayerDamageListener> playerDamageListener = new AtomicReference<>(null);
  
  protected final AtomicReference<GeneratorPerspective> dynmap_perspective = new AtomicReference<>(null);
  
  public BackroomsPlugin() {
    super(BackroomsPlugin.class);
  }
  
  public void onEnable() {
    super.onEnable();
    String pack = Bukkit.getResourcePack();
    if (pack == null || pack.isEmpty()) {
      LOG.warning(String.format("%sResource pack not set; You can use this one: %s", new Object[] { "[pxnBackrooms] ", "https://dl.poixson.com/mcplugins/pxnBackrooms/pxnBackrooms-resourcepack-{VERSION}.zip"
              
              .replace("{VERSION}", getPluginVersion()) }));
    } else {
      LOG.info(String.format("%sUsing resource pack: %s", new Object[] { "[pxnBackrooms] ", 
              
              Bukkit.getResourcePack() }));
    } 
    new Level_000(this);
    new Level_033(this);
    new Level_094(this);
    new Level_771(this);
    if (enableDynmapConfigGen())
      getDynmapPerspective().commit(new File(getDataFolder(), "../dynmap/")); 
    (new BukkitRunnable() {
        public void run() {
          String seed = Long.toString(Bukkit.getWorld("world").getSeed());
          Iterator<Map.Entry<Integer, BackroomsLevel>> it = BackroomsPlugin.this.backlevels.entrySet().iterator();
          while (it.hasNext()) {
            Map.Entry<Integer, BackroomsLevel> entry = it.next();
            int level = ((Integer)entry.getKey()).intValue();
            if (((BackroomsLevel)entry.getValue()).isWorldMain(level))
              BackroomsLevel.MakeWorld(level, seed); 
          } 
        }
      }).runTask((Plugin)this);
    for (BackroomsLevel level : this.backlevels.values())
      level.register(); 
    Commands commands1 = new Commands(this);
    Commands commands2 = this.commands.getAndSet(commands1);
    if (commands2 != null)
      commands2.unregister(); 
    commands1.register();
    this.tpManager.set(TeleportManager.Load(this));
    this.quoteAnnouncer.set(QuoteAnnouncer.Load(this));
    TaskHourly task = new TaskHourly(this);
    TaskHourly taskHourly1 = this.hourlyTask.getAndSet(task);
    if (taskHourly1 != null)
      taskHourly1.stop(); 
    task.start();
    PlayerDamageListener listener = new PlayerDamageListener(this);
    PlayerDamageListener previous = this.playerDamageListener.getAndSet(listener);
    if (previous != null)
      previous.unregister(); 
    listener.register();
  }
  
  public void onDisable() {
    super.onDisable();
    TaskHourly task = this.hourlyTask.getAndSet(null);
    if (task != null)
      task.stop(); 
    DelayedChestFiller.stop();
    for (BackroomsLevel lvl : this.backlevels.values())
      lvl.unregister(); 
    this.backlevels.clear();
    Commands commands = this.commands.getAndSet(null);
    if (commands != null)
      commands.unregister(); 
    PlayerDamageListener listener = this.playerDamageListener.getAndSet(null);
    if (listener != null)
      listener.unregister(); 
    this.tpManager.set(null);
    this.dynmap_perspective.set(null);
    this.quoteAnnouncer.set(null);
  }
  
  protected void loadConfigs() {
    mkPluginDir();
    FileConfiguration cfg = getConfig();
    this.config.set(cfg);
    configDefaults(cfg);
    cfg.options().copyDefaults(true);
    saveConfig();
    File file = new File(getDataFolder(), "levels-visited.yml");
    YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
    Set<String> keys = yamlConfiguration.getKeys(false);
    for (String key : keys) {
      UUID uuid = UUID.fromString(key);
      CopyOnWriteArraySet<Integer> visited = new CopyOnWriteArraySet<>();
      visited.addAll(yamlConfiguration.getIntegerList(key));
      this.visitLevels.put(uuid, visited);
    } 
  }
  
  protected void saveConfigs() {
    saveConfig();
    File file = new File(getDataFolder(), "levels-visited.yml");
    YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
    Iterator<Map.Entry<UUID, CopyOnWriteArraySet<Integer>>> it = this.visitLevels.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<UUID, CopyOnWriteArraySet<Integer>> entry = it.next();
      String uuid = ((UUID)entry.getKey()).toString();
      HashSet<Integer> set = new HashSet<>();
      set.addAll(entry.getValue());
      yamlConfiguration.set(uuid, set);
    } 
    try {
      yamlConfiguration.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  protected void configDefaults(FileConfiguration cfg) {
    cfg.addDefault("Enable Dynmap Config Gen", Boolean.FALSE);
    cfg.addDefault("Spawn Distance", Integer.valueOf(10000));
    Gen_000.ConfigDefaults(cfg);
    Gen_001.ConfigDefaults(cfg);
    Gen_005.ConfigDefaults(cfg);
    Gen_006.ConfigDefaults(cfg);
    Gen_019.ConfigDefaults(cfg);
    Gen_023.ConfigDefaults(cfg);
    Gen_033.ConfigDefaults(cfg);
    Gen_037.ConfigDefaults(cfg);
    Gen_094.ConfigDefaults(cfg);
    Gen_309.ConfigDefaults(cfg);
    Gen_771.ConfigDefaults(cfg);
  }
  
  public boolean enableDynmapConfigGen() {
    return ((FileConfiguration)this.config.get()).getBoolean("Enable Dynmap Config Gen");
  }
  
  public int getSpawnDistance() {
    return ((FileConfiguration)this.config.get()).getInt("Spawn Distance");
  }
  
  public ConfigurationSection getLevelParams(int level) {
    return ((FileConfiguration)this.config.get())
      .getConfigurationSection(
        String.format("Level%d.Params", new Object[] { Integer.valueOf(level) }));
  }
  
  public ConfigurationSection getLevelBlocks(int level) {
    return ((FileConfiguration)this.config.get())
      .getConfigurationSection(
        String.format("Level%d.Blocks", new Object[] { Integer.valueOf(level) }));
  }
  
  public TaskHourly getHourlyTask() {
    return this.hourlyTask.get();
  }
  
  public TeleportManager getTeleportManager() {
    return this.tpManager.get();
  }
  
  public QuoteAnnouncer getQuoteAnnouncer() {
    return this.quoteAnnouncer.get();
  }
  
  public BackroomsLevel register(int level, BackroomsLevel backlevel) {
    this.backlevels.put(Integer.valueOf(level), backlevel);
    return backlevel;
  }
  
  public int[] getLevels() {
    int num = this.backlevels.size();
    int[] levels = new int[num];
    int i = 0;
    for (Integer lvl : this.backlevels.keySet()) {
      levels[i] = lvl.intValue();
      i++;
    } 
    return levels;
  }
  
  public int getMainLevel(int level) {
    if (this.backlevels.containsKey(Integer.valueOf(level)))
      return level; 
    for (Map.Entry<Integer, BackroomsLevel> entry : this.backlevels.entrySet()) {
      if (((BackroomsLevel)entry.getValue()).containsLevel(level))
        return ((Integer)entry.getKey()).intValue(); 
    } 
    return Integer.MIN_VALUE;
  }
  
  public int getPlayerLevel(UUID uuid) {
    Player player = Bukkit.getPlayer(uuid);
    return 
      (player == null) ? Integer
      .MIN_VALUE : 
      getPlayerLevel(player);
  }
  
  public int getPlayerLevel(Player player) {
    if (player != null) {
      int lvl = getLevelFromWorld(player.getWorld());
      if (lvl >= 0) {
        BackroomsLevel backlevel = getBackroomsLevel(lvl);
        if (backlevel != null)
          return backlevel.getLevelFromY(player.getLocation().getBlockY()); 
      } 
    } 
    return Integer.MIN_VALUE;
  }
  
  public int getLevelFromWorld(World world) {
    return 
      (world == null) ? Integer
      .MIN_VALUE : 
      getLevelFromWorld(world.getName());
  }
  
  public int getLevelFromWorld(String worldName) {
    if (worldName != null && !worldName.isEmpty() && 
      worldName.startsWith("level")) {
      int level = Integer.parseInt(worldName.substring(5));
      if (isValidLevel(level))
        return level; 
    } 
    return Integer.MIN_VALUE;
  }
  
  public World getWorldFromLevel(int level) {
    int lvl = getMainLevel(level);
    if (lvl == Integer.MIN_VALUE)
      return null; 
    return Bukkit.getWorld("level" + Integer.toString(lvl));
  }
  
  public void assertValidLevel(int level) {
    if (!isValidLevel(level))
      throw new RuntimeException("Invalid backrooms level: " + Integer.toString(level)); 
  }
  
  public boolean isValidLevel(int level) {
    if (isValidWorld(level))
      return true; 
    for (BackroomsLevel backlevel : this.backlevels.values()) {
      if (backlevel.containsLevel(level))
        return true; 
    } 
    return false;
  }
  
  public boolean isValidWorld(int level) {
    return this.backlevels.containsKey(Integer.valueOf(level));
  }
  
  public boolean isValidWorld(World world) {
    int level = getLevelFromWorld(world);
    return isValidWorld(level);
  }
  
  public void noclip(Player player, int level_to) {
    if (level_to == Integer.MIN_VALUE) {
      noclip(player);
      return;
    } 
    TeleportManager manager = this.tpManager.get();
    Location loc = manager.getSpawnLocation(level_to);
    if (loc == null) {
      LOG.warning("[pxnBackrooms] Failed to find spawn for level: " + Integer.toString(level_to));
      World world = getWorldFromLevel(level_to);
      if (world == null) {
        LOG.warning("[pxnBackrooms] Unknown backrooms world for level: " + Integer.toString(level_to));
        return;
      } 
      loc = world.getSpawnLocation();
    } 
    LOG.info("[pxnBackrooms] No-clip player: " + player.getName() + " to level: " + Integer.toString(level_to));
    player.teleport(loc);
  }
  
  public int noclip(Player player) {
    int level_from = getPlayerLevel(player);
    int level_to = noclip(level_from);
    noclip(player, level_to);
    return level_to;
  }
  
  public int noclip(int level_from) {
    TeleportManager manager = this.tpManager.get();
    if (manager == null) {
      LOG.warning("[pxnBackrooms] teleport chance weights not loaded");
      return 0;
    } 
    return manager.getDestinationLevel(level_from);
  }
  
  public BackroomsLevel getBackroomsLevel(int level) {
    BackroomsLevel backlevel = this.backlevels.get(Integer.valueOf(level));
    if (backlevel != null)
      return backlevel; 
    for (BackroomsLevel backroomsLevel : this.backlevels.values()) {
      if (backroomsLevel.containsLevel(level))
        return backroomsLevel; 
    } 
    return null;
  }
  
  public boolean addVisitedLevel(Player player) {
    return addVisitedLevel(player.getUniqueId());
  }
  
  public boolean addVisitedLevel(UUID uuid) {
    int level = getPlayerLevel(uuid);
    if (level < 0)
      return false; 
    CopyOnWriteArraySet<Integer> visited = this.visitLevels.get(uuid);
    if (visited == null) {
      visited = new CopyOnWriteArraySet<>();
      this.visitLevels.put(uuid, visited);
    } 
    int sizeLast = visited.size();
    visited.add(Integer.valueOf(level));
    if (visited.size() > sizeLast) {
      for (Integer lvl : this.backlevels.keySet()) {
        if (!visited.contains(lvl))
          return false; 
      } 
      return true;
    } 
    return false;
  }
  
  public ChunkGenerator getDefaultWorldGenerator(String worldName, String argsStr) {
    if (!worldName.startsWith("level"))
      throw new RuntimeException("Invalid world name, must be level# found: " + worldName); 
    LOG.info(String.format("%s%s world: %s", new Object[] { "[pxnBackrooms] ", "pxnBackrooms", worldName }));
    int level = getLevelFromWorld(worldName);
    return getBackroomsLevel(level);
  }
  
  public GeneratorPerspective getDynmapPerspective() {
    GeneratorPerspective gen = this.dynmap_perspective.get();
    if (gen != null)
      return gen; 
    gen = new GeneratorPerspective();
    this.dynmap_perspective.set(gen);
    return gen;
  }
}
