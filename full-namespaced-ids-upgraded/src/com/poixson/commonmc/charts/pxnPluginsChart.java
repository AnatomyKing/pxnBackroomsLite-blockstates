package com.poixson.commonmc.charts;

import com.poixson.commonmc.pxnCommonPlugin;
import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.commonmc.tools.plugin.xListener;
import java.util.concurrent.Callable;
import org.bstats.charts.SimplePie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class pxnPluginsChart extends xListener<pxnCommonPlugin> implements Callable<String> {
  public pxnPluginsChart(pxnCommonPlugin plugin) {
    super((xJavaPlugin)plugin);
    if (!(plugin instanceof pxnCommonPlugin))
      throw new RuntimeException("Class instance outside of pxnCommonPlugin"); 
  }
  
  public static SimplePie GetChart(pxnCommonPlugin plugin) {
    return new SimplePie("plugins_count", new pxnPluginsChart(plugin));
  }
  
  public String call() throws Exception {
    int count = ((pxnCommonPlugin)this.plugin).getPluginsCount();
    return Integer.toString(count);
  }
  
  @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
  public void onPluginEnable(PluginEnableEvent event) {
    Plugin p = event.getPlugin();
    if (p instanceof xJavaPlugin)
      ((pxnCommonPlugin)this.plugin).registerPluginPXN((xJavaPlugin)p); 
  }
  
  @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
  public void onPluginDisable(PluginDisableEvent event) {
    Plugin p = event.getPlugin();
    if (p instanceof xJavaPlugin)
      ((pxnCommonPlugin)this.plugin).unregisterPluginPXN((xJavaPlugin)p); 
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\charts\pxnPluginsChart.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */