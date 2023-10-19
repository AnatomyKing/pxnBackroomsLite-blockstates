package com.poixson.commonmc.tools.updatechecker;

import com.google.common.util.concurrent.AtomicDouble;
import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.utils.StringUtils;
import com.poixson.utils.Utils;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class UpdateCheckerTask implements Runnable {
  protected final JavaPlugin plugin;
  
  protected final int plugin_id;
  
  protected final String plugin_version;
  
  protected final AtomicLong check_count = new AtomicLong(0L);
  
  protected final AtomicDouble version_diff = new AtomicDouble(2.2250738585072014E-308D);
  
  protected final AtomicReference<String> updateMsg = new AtomicReference<>(null);
  
  protected final AtomicBoolean msgToPlayers = new AtomicBoolean(false);
  
  public UpdateCheckerTask(JavaPlugin plugin, int plugin_id, String plugin_version) {
    if (plugin == null)
      throw new NullPointerException(); 
    if (plugin_id <= 0)
      throw new RuntimeException("Plugin ID not set in: " + plugin.getName()); 
    if (Utils.isEmpty(plugin_version))
      throw new RequiredArgumentException("plugin_version"); 
    this.plugin = plugin;
    this.plugin_id = plugin_id;
    this.plugin_version = plugin_version;
  }
  
  public void run() {
    this.check_count.incrementAndGet();
    SpigotWebAPI api = SpigotWebAPI.Get(this.plugin_id);
    if (api == null)
      return; 
    double diff = StringUtils.CompareVersions(this.plugin_version, api.current_version);
    this.version_diff.set(diff);
    if (diff > 0.0D) {
      String msg;
      double server_diff = api.diffServerVersion();
      if (Math.abs(server_diff) > 100.0D) {
        this.msgToPlayers.set(false);
        msg = String.format("%s[%s] New version available but requires a %s server version", new Object[] { ChatColor.WHITE, api.title, 
              
              (server_diff > 0.0D) ? "newer" : "older" });
      } else {
        this.msgToPlayers.set(true);
        msg = String.format("%s[%s]%s New version available: %s\n  %sAvailable at:%s %s", new Object[] { ChatColor.RED, api.title, ChatColor.WHITE, api.current_version, ChatColor.RED, ChatColor.WHITE, 
              
              String.format("https://www.spigotmc.org/resources/%s/", new Object[] { Integer.valueOf(api.id) }) });
      } 
      this.updateMsg.set(msg);
      String str = msg.toString().replace("" + ChatColor.RED, "").replace("" + ChatColor.WHITE, "");
      for (String line : str.split("\n"))
        xJavaPlugin.LOG.info(line); 
      if (this.check_count.get() == 1L && 
        this.msgToPlayers.get())
        for (Player p : Bukkit.getOnlinePlayers()) {
          if (p.isOp() || p.hasPermission("pxncommon.updates"))
            p.sendMessage(msg); 
        }  
    } else {
      this.updateMsg.set(null);
    } 
  }
  
  public boolean hasUpdate() {
    return (this.updateMsg.get() != null);
  }
  
  public boolean isToPlayers() {
    return this.msgToPlayers.get();
  }
  
  public String getUpdateMessage() {
    return this.updateMsg.get();
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tool\\updatechecker\UpdateCheckerTask.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */