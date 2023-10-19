package com.poixson.morefoods;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;

public class MoreFoodsAPI {
  protected static final Logger LOG = Logger.getLogger("Minecraft");
  
  protected static final String NAME = "MoreFoods";
  
  protected static final String CLASS = "com.poixson.morefoods.MoreFoodsPlugin";
  
  protected final MoreFoodsPlugin plugin;
  
  protected static final AtomicInteger errcount_PluginNotFound = new AtomicInteger(0);
  
  public static MoreFoodsAPI GetAPI() {
    ServicesManager services = Bukkit.getServicesManager();
    MoreFoodsAPI api = (MoreFoodsAPI)services.load(MoreFoodsAPI.class);
    if (api != null)
      return api; 
    try {
      if (Class.forName("com.poixson.morefoods.MoreFoodsPlugin") == null)
        throw new ClassNotFoundException("com.poixson.morefoods.MoreFoodsPlugin"); 
      PluginManager manager = Bukkit.getPluginManager();
      Plugin plugin = manager.getPlugin("MoreFoods");
      if (plugin == null)
        throw new RuntimeException("MoreFoods plugin not found"); 
      return new MoreFoodsAPI(plugin);
    } catch (ClassNotFoundException e) {
      if (errcount_PluginNotFound.getAndIncrement() < 10)
        LOG.severe("Plugin not found: MoreFoods"); 
      return null;
    } 
  }
  
  protected MoreFoodsAPI(Plugin p) {
    if (p == null)
      throw new NullPointerException(); 
    this.plugin = (MoreFoodsPlugin)p;
  }
  
  public Boolean isFresh(ItemStack stack) {
    return this.plugin.isFresh(stack);
  }
  
  public Boolean isUnfresh(ItemStack stack) {
    return this.plugin.isUnfresh(stack);
  }
  
  public Boolean isFullyAged(ItemStack stack) {
    return this.plugin.isFullyAged(stack);
  }
}
