package com.poixson.commonmc.commands;

import com.poixson.commonmc.pxnCommonPlugin;
import com.poixson.commonmc.tools.commands.pxnCommand;
import com.poixson.commonmc.tools.commands.pxnCommandsHandler;
import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.commonmc.tools.tps.TicksAnnouncer;
import com.poixson.commonmc.tools.tps.TicksPerSecond;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands_TPS extends pxnCommandsHandler<pxnCommonPlugin> {
  public Commands_TPS(pxnCommonPlugin plugin) {
    super((xJavaPlugin)plugin, new String[] { "tps", "lag" });
    addCommand(new Command_TPS(plugin));
  }
  
  public class Command_TPS extends pxnCommand<pxnCommonPlugin> {
    public Command_TPS(pxnCommonPlugin plugin) {
      super((xJavaPlugin)plugin, true, new String[] { "toggle" });
    }
    
    public boolean run(CommandSender sender, String label, String[] args) {
      Player player = (sender instanceof Player) ? (Player)sender : null;
      int num_args = args.length;
      if (num_args == 0) {
        TicksPerSecond.DisplayTPS(player);
        return true;
      } 
      if ("toggle".equals(args[0])) {
        TicksAnnouncer.Toggle((pxnCommonPlugin)this.plugin, player);
        return true;
      } 
      return false;
    }
    
    public boolean isDefault() {
      return true;
    }
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\commands\Commands_TPS.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */