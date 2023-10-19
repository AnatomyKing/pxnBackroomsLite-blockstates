package com.poixson.commonmc.tools.commands;

import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.commonmc.tools.plugin.xListener;
import com.poixson.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

public abstract class pxnCommandsHandler<T extends xJavaPlugin> extends xListener<xJavaPlugin> implements CommandExecutor, TabCompleter {
  protected final String[] labels;
  
  protected final CopyOnWriteArraySet<pxnCommand<T>> cmds = new CopyOnWriteArraySet<>();
  
  protected final CopyOnWriteArraySet<PluginCommand> pcs = new CopyOnWriteArraySet<>();
  
  protected final AtomicReference<TabCompleter> tabComp = new AtomicReference<>(null);
  
  public pxnCommandsHandler(T plugin, String... labels) {
    super((xJavaPlugin)plugin);
    this.labels = labels;
  }
  
  public void register() {
    if (hasOverrides())
      super.register(); 
    for (String label : this.labels) {
      PluginCommand pc = this.plugin.getCommand(label);
      if (pc != null) {
        pc.setExecutor(this);
        this.pcs.add(pc);
      } 
    } 
  }
  
  public void unregister() {
    super.unregister();
    for (PluginCommand pc : this.pcs)
      pc.setExecutor(null); 
    this.pcs.clear();
    this.cmds.clear();
    this.tabComp.set(null);
  }
  
  public void addCommand(pxnCommand<T> cmd) {
    this.cmds.add(cmd);
  }
  
  public boolean match(String match) {
    if (Utils.isEmpty(match))
      return false; 
    String matchLower = match.toLowerCase();
    for (String label : this.labels) {
      if (matchLower.equals(label))
        return true; 
    } 
    return false;
  }
  
  public pxnCommand<T> getDefaultCommand() {
    for (pxnCommand<T> cmd : this.cmds) {
      if (cmd.isDefault())
        return cmd; 
    } 
    return null;
  }
  
  public boolean hasOverrides() {
    for (pxnCommand<T> cmd : this.cmds) {
      if (cmd.override)
        return true; 
    } 
    return false;
  }
  
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    return handleCommand(sender, label, args, false);
  }
  
  @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
  public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
    String msg = event.getMessage();
    if (msg.startsWith("/")) {
      String label, args[];
      int pos = msg.indexOf(' ');
      if (pos == -1) {
        label = msg.substring(1);
        args = new String[0];
      } else {
        label = msg.substring(1, pos);
        args = msg.substring(pos + 1).split(" ");
      } 
      if (handleCommand((CommandSender)event.getPlayer(), label, args, true))
        event.setCancelled(true); 
    } 
  }
  
  @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
  public void onSendCommandEvent(ServerCommandEvent event) {
    String label, args[], msg = event.getCommand();
    int pos = msg.indexOf(' ');
    if (pos == -1) {
      label = msg;
      args = new String[0];
    } else {
      label = msg.substring(0, pos);
      args = msg.substring(pos + 1).split(" ");
    } 
    if (handleCommand(event.getSender(), label, args, true))
      event.setCancelled(true); 
  }
  
  protected boolean handleCommand(CommandSender sender, String label, String[] args, boolean isOverride) {
    if (match(label))
      for (pxnCommand<T> cmd : this.cmds) {
        if (cmd.match(args)) {
          cmd.run(sender, label, args);
          return true;
        } 
      }  
    return false;
  }
  
  public void setTabCompleter(TabCompleter tabComp) {
    this.tabComp.set(tabComp);
    for (PluginCommand pc : this.pcs)
      pc.setTabCompleter(tabComp); 
  }
  
  public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
    List<String> matches = new ArrayList<>();
    int size = args.length;
    switch (size) {
      case 1:
        for (pxnCommand<T> c : this.cmds) {
          for (String match : c.getMatches(args[0]))
            matches.add(match); 
        } 
        break;
    } 
    return matches;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\commands\pxnCommandsHandler.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */