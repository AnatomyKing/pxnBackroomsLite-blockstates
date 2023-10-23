package com.poixson.commonmc;

import com.poixson.commonmc.charts.pxnPluginsChart;
import com.poixson.commonmc.commands.Commands_Memory;
import com.poixson.commonmc.commands.Commands_TPS;
import com.poixson.commonmc.events.PlayerMoveManager;
import com.poixson.commonmc.events.PluginSaveManager;
import com.poixson.commonmc.tools.mapstore.FreedMapStore;
import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.commonmc.tools.tps.TicksPerSecond;
import com.poixson.commonmc.tools.updatechecker.UpdateCheckManager;
import com.poixson.tools.Keeper;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReference;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.CustomChart;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

public class pxnCommonPlugin extends xJavaPlugin {
  public static final String LOG_PREFIX = "[pxnCommon] ";
  
  public int getSpigotPluginID() {
    return 107049;
  }
  
  public int getBStatsID() {
    return 17785;
  }
  
  public static final String CHAT_PREFIX = "" + ChatColor.AQUA + "[pxnCommon] " + ChatColor.AQUA;
  
  protected final Keeper keeper;
  
  protected final CopyOnWriteArraySet<xJavaPlugin> plugins = new CopyOnWriteArraySet<>();
  
  protected final AtomicReference<pxnPluginsChart> pluginsListener = new AtomicReference<>(null);
  
  protected final AtomicReference<UpdateCheckManager> checkManager = new AtomicReference<>(null);
  
  protected final AtomicReference<PlayerMoveManager> moveManager = new AtomicReference<>(null);
  
  protected final AtomicReference<FreedMapStore> freedMaps = new AtomicReference<>(null);
  
  protected final AtomicReference<PluginSaveManager> saveListener = new AtomicReference<>(null);
  
  protected final AtomicReference<TicksPerSecond> tpsManager = new AtomicReference<>(null);
  
  protected final AtomicReference<Commands_TPS> commandsTPS = new AtomicReference<>(null);
  
  protected final AtomicReference<Commands_Memory> commandsMem = new AtomicReference<>(null);
  
  public pxnCommonPlugin() {
    super(pxnCommonPlugin.class);
    this.keeper = Keeper.get();
  }
  
  public void onEnable() {
    ServicesManager services = Bukkit.getServicesManager();
    services.register(pxnCommonPlugin.class, this, (Plugin)this, ServicePriority.Normal);
    pxnPluginsChart pxnPluginsChart1 = new pxnPluginsChart(this);
    pxnPluginsChart pxnPluginsChart2 = this.pluginsListener.getAndSet(pxnPluginsChart1);
    if (pxnPluginsChart2 != null)
      pxnPluginsChart2.unregister(); 
    pxnPluginsChart1.register();
    TicksPerSecond ticksPerSecond1 = new TicksPerSecond(this);
    TicksPerSecond ticksPerSecond2 = this.tpsManager.getAndSet(ticksPerSecond1);
    if (ticksPerSecond2 != null)
      ticksPerSecond2.stop(); 
    ticksPerSecond1.start();
    services.register(TicksPerSecond.class, ticksPerSecond1, (Plugin)this, ServicePriority.Normal);
    UpdateCheckManager updateCheckManager1 = new UpdateCheckManager(this);
    UpdateCheckManager updateCheckManager2 = this.checkManager.getAndSet(updateCheckManager1);
    if (updateCheckManager2 != null)
      updateCheckManager2.stop(); 
    services.register(UpdateCheckManager.class, updateCheckManager1, (Plugin)this, ServicePriority.Normal);
    updateCheckManager1.addPlugin((JavaPlugin)this, getSpigotPluginID(), getPluginVersion());
    updateCheckManager1.startLater();
    super.onEnable();
    Commands_TPS commands_TPS1 = new Commands_TPS(this);
    Commands_TPS commands_TPS2 = this.commandsTPS.getAndSet(commands_TPS1);
    if (commands_TPS2 != null)
      commands_TPS2.unregister(); 
    commands_TPS1.register();
    Commands_Memory commands = new Commands_Memory(this);
    Commands_Memory commands_Memory1 = this.commandsMem.getAndSet(commands);
    if (commands_Memory1 != null)
      commands_Memory1.unregister(); 
    commands.register();
    PlayerMoveManager manager = new PlayerMoveManager(this);
    PlayerMoveManager playerMoveManager1 = this.moveManager.getAndSet(manager);
    if (playerMoveManager1 != null)
      playerMoveManager1.unregister(); 
    manager.register();
    PluginSaveManager listener = new PluginSaveManager(this);
    PluginSaveManager previous = this.saveListener.getAndSet(listener);
    if (previous != null)
      previous.unregister(); 
    listener.register();
    Metrics metrics = this.metrics.get();
    if (metrics != null)
      metrics.addCustomChart((CustomChart)pxnPluginsChart.GetChart(this)); 
  }
  
  public void onDisable() {
    super.onDisable();
    ServicesManager services = Bukkit.getServicesManager();
    PluginSaveManager pluginSaveManager = this.saveListener.getAndSet(null);
    if (pluginSaveManager != null)
      pluginSaveManager.unregister(); 
    TicksPerSecond ticksPerSecond = this.tpsManager.getAndSet(null);
    if (ticksPerSecond != null) {
      ticksPerSecond.stop();
      services.unregister(ticksPerSecond);
    } 
    UpdateCheckManager manager = this.checkManager.getAndSet(null);
    if (manager != null)
      manager.stop(); 
    FreedMapStore store = this.freedMaps.getAndSet(null);
    if (store != null) {
      store.unregister();
      try {
        store.save();
      } catch (IOException e) {
        e.printStackTrace();
      } 
    } 
    pxnPluginsChart listener = this.pluginsListener.getAndSet(null);
    if (listener != null)
      listener.unregister(); 
    Commands_TPS commands_TPS = this.commandsTPS.getAndSet(null);
    if (commands_TPS != null)
      commands_TPS.unregister(); 
    Commands_Memory commands = this.commandsMem.getAndSet(null);
    if (commands != null)
      commands.unregister(); 
  }
  
  protected void loadConfigs() {
    mkPluginDir();
    FileConfiguration cfg = getConfig();
    this.config.set(cfg);
    configDefaults(cfg);
    cfg.options().copyDefaults(true);
    saveConfig();
  }
  
  protected void saveConfigs() {
    saveConfig();
  }
  
  protected void configDefaults(FileConfiguration cfg) {}
  
  public static pxnCommonPlugin GetCommonPlugin() {
    pxnCommonPlugin plugin = (pxnCommonPlugin)Bukkit.getServicesManager().load(pxnCommonPlugin.class);
    if (plugin == null)
      throw new RuntimeException("pxnCommonPlugin not loaded"); 
    return plugin;
  }
  
  public static TicksPerSecond GetTicksManager() {
    pxnCommonPlugin plugin = GetCommonPlugin();
    return plugin.tpsManager.get();
  }
  
  public static FreedMapStore GetFreedMapStore() {
    pxnCommonPlugin plugin = GetCommonPlugin();
    FreedMapStore store = plugin.freedMaps.get();
    if (store != null)
      return store; 
    String path = plugin.getDataFolder().getAbsolutePath();
    FreedMapStore freedMapStore1 = new FreedMapStore(plugin, path);
    if (plugin.freedMaps.compareAndSet(null, freedMapStore1)) {
      try {
        freedMapStore1.load();
      } catch (IOException e) {
        throw new RuntimeException(e);
      } 
      freedMapStore1.register();
      ServicesManager services = Bukkit.getServicesManager();
      services.register(FreedMapStore.class, freedMapStore1, (Plugin)plugin, ServicePriority.Normal);
      return freedMapStore1;
    } 
    return GetFreedMapStore();
  }
  
  public static <T extends xJavaPlugin> boolean RegisterPluginPXN(T plugin) {
    return GetCommonPlugin().registerPluginPXN(plugin);
  }
  
  public <T extends xJavaPlugin> boolean registerPluginPXN(T plugin) {
    for (xJavaPlugin p : this.plugins) {
      if (p.getClass().isInstance(plugin))
        throw new RuntimeException("Plugin already registered? " + plugin.getClass().getName()); 
    } 
    return this.plugins.add((xJavaPlugin)plugin);
  }
  
  public static <T extends xJavaPlugin> boolean UnregisterPluginPXN(T plugin) {
    return GetCommonPlugin().unregisterPluginPXN(plugin);
  }
  
  public <T extends xJavaPlugin> boolean unregisterPluginPXN(T plugin) {
    return this.plugins.remove(plugin);
  }
  
  public int getPluginsCount() {
    return this.plugins.size();
  }
}
