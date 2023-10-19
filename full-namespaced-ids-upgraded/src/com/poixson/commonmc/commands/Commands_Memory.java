package com.poixson.commonmc.commands;

import com.poixson.commonmc.pxnCommonPlugin;
import com.poixson.commonmc.tools.commands.pxnCommand;
import com.poixson.commonmc.tools.commands.pxnCommandsHandler;
import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import org.bukkit.command.CommandSender;

public class Commands_Memory extends pxnCommandsHandler<pxnCommonPlugin> {
  public Commands_Memory(pxnCommonPlugin plugin) {
    super((xJavaPlugin)plugin, new String[] { "mem", "memory" });
    addCommand(new Command_Memory(plugin));
  }
  
  public class Command_Memory extends pxnCommand<pxnCommonPlugin> {
    public Command_Memory(pxnCommonPlugin plugin) {
      super((xJavaPlugin)plugin, true, new String[0]);
    }
    
    public boolean run(CommandSender sender, String label, String[] args) {
      sender.sendMessage("MEMORY COMMAND UNFINISHED");
      System.out.println("MEMORY COMMAND UNFINISHED");
      for (String arg : args)
        System.out.println("ARG: " + arg); 
      return true;
    }
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\commands\Commands_Memory.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */