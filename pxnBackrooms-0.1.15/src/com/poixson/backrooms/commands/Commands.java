package com.poixson.backrooms.commands;

import com.poixson.backrooms.BackroomsPlugin;
import com.poixson.commonmc.tools.commands.pxnCommandsHandler;
import com.poixson.commonmc.tools.plugin.xJavaPlugin;

public class Commands extends pxnCommandsHandler<BackroomsPlugin> {
  public Commands(BackroomsPlugin plugin) {
    super((xJavaPlugin)plugin, new String[] { "backrooms" });
    addCommand(new Command_TP(plugin));
  }
}
