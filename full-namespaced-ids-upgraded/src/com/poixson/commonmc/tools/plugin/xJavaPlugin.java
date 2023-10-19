package com.poixson.commonmc.tools.plugin;

import com.poixson.commonmc.events.PluginSaveEvent;
import com.poixson.commonmc.tools.updatechecker.UpdateCheckManager;
import com.poixson.tools.AppProps;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class xJavaPlugin extends JavaPlugin {
  public static final Logger LOG = Logger.getLogger("Minecraft");
  
  public static final String LOG_PREFIX = "[pxn] ";
  
  public static final String CHAT_PREFIX = "" + ChatColor.AQUA + "[pxn] " + ChatColor.AQUA;
  
  protected final AtomicReference<Metrics> metrics = new AtomicReference<>(null);
  
  protected final AppProps props;
  
  protected final AtomicReference<FileConfiguration> config = new AtomicReference<>(null);
  
  protected final AtomicReference<LocalPluginSaveListener> saveListener = new AtomicReference<>(null);
  
  public xJavaPlugin(Class<? extends xJavaPlugin> clss) {
    try {
      this.props = AppProps.LoadFromClassRef(clss);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } 
  }
  
  public void onEnable() {
    super.onEnable();
    loadConfigs();
    int id = getBStatsID();
    if (id > 0) {
      System.setProperty("bstats.relocatecheck", "false");
      this.metrics.set(new Metrics(this, id));
    } 
    UpdateCheckManager.Register(this);
    LocalPluginSaveListener listener = new LocalPluginSaveListener(this);
    LocalPluginSaveListener previous = this.saveListener.getAndSet(listener);
    if (previous != null)
      previous.unregister(); 
    listener.register();
  }
  
  public void onDisable() {
    LocalPluginSaveListener listener = this.saveListener.getAndSet(null);
    if (listener != null)
      listener.unregister(); 
    super.onDisable();
    this.metrics.set(null);
    UpdateCheckManager.Unregister(this);
    try {
      Bukkit.getScheduler()
        .cancelTasks((Plugin)this);
    } catch (Exception exception) {}
    HandlerList.unregisterAll((Plugin)this);
    saveConfigs();
    this.config.set(null);
    Bukkit.getServicesManager()
      .unregisterAll((Plugin)this);
  }
  
  public void onSave() {
    saveConfigs();
  }
  
  protected void loadConfigs() {}
  
  protected void saveConfigs() {}
  
  protected void configDefaults(FileConfiguration cfg) {}
  
  protected void mkPluginDir() {
    File path = getDataFolder();
    if (!path.isDirectory()) {
      if (!path.mkdir())
        throw new RuntimeException("Failed to create directory: " + path.toString()); 
      LOG.info("[pxn] Created directory: " + path.toString());
    } 
  }
  
  public int getSpigotPluginID() {
    return 0;
  }
  
  public int getBStatsID() {
    return 0;
  }
  
  public String getPluginVersion() {
    return this.props.version;
  }
  
  class LocalPluginSaveListener extends xListener<xJavaPlugin> {
    public LocalPluginSaveListener(xJavaPlugin plugin) {
      super(plugin);
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPluginSave(PluginSaveEvent event) {
      xJavaPlugin.this.onSave();
    }
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\plugin\xJavaPlugin.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */