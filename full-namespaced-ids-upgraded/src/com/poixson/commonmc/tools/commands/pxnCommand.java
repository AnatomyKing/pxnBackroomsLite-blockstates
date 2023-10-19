package com.poixson.commonmc.tools.commands;

import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.utils.Utils;
import java.util.LinkedList;
import org.bukkit.command.CommandSender;

public abstract class pxnCommand<T extends xJavaPlugin> {
  protected final T plugin;
  
  public final String[] labels;
  
  public final boolean override;
  
  public pxnCommand(T plugin, String... labels) {
    this(plugin, false, labels);
  }
  
  public pxnCommand(T plugin, boolean enableOverride, String... labels) {
    this.plugin = plugin;
    this.override = enableOverride;
    this.labels = labels;
  }
  
  public abstract boolean run(CommandSender paramCommandSender, String paramString, String[] paramArrayOfString);
  
  public String[] getMatches(String arg) {
    LinkedList<String> list = new LinkedList<>();
    for (String lbl : this.labels) {
      if (lbl.startsWith(arg))
        list.add(lbl); 
    } 
    return list.<String>toArray(new String[0]);
  }
  
  public boolean match(String[] args) {
    if (Utils.isEmpty(args))
      return isDefault(); 
    return match(args[0]);
  }
  
  public boolean match(String match) {
    if (Utils.isEmpty(match))
      return isDefault(); 
    String matchLower = match.toLowerCase();
    for (String label : this.labels) {
      if (matchLower.equals(label))
        return true; 
    } 
    return false;
  }
  
  public boolean isDefault() {
    return Utils.isEmpty(this.labels);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\commands\pxnCommand.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */