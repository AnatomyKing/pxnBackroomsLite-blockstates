package com.poixson.backrooms.commands;

import com.poixson.backrooms.BackroomsPlugin;
import com.poixson.commonmc.tools.commands.pxnCommand;
import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.utils.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_TP extends pxnCommand<BackroomsPlugin> {
  protected final BackroomsPlugin plugin;
  
  public Command_TP(BackroomsPlugin plugin) {
    super((BackroomsPlugin)plugin, new String[] { "tp", "teleport" });
    this.plugin = plugin;
  }
  
  public boolean run(CommandSender sender, String label, String[] args) {
    Player player = (sender instanceof Player) ? (Player)sender : null;
    if (player != null && !player.hasPermission("backrooms.tp")) {
      player.sendMessage(BackroomsPlugin.CHAT_PREFIX + "You don't have permission to use this.");
      return true;
    } 
    int numargs = args.length;
    if (numargs == 1 && player != null) {
      this.plugin.noclip(player);
      return true;
    } 
    int level = Integer.MIN_VALUE;
    int i = 1;
    if (numargs > 1 && NumberUtils.IsNumeric(args[1])) {
      level = Integer.parseInt(args[1]);
      if (!this.plugin.isValidLevel(level)) {
        sender.sendMessage(BackroomsPlugin.CHAT_PREFIX + "Invalid backrooms level: " + BackroomsPlugin.CHAT_PREFIX);
        return true;
      } 
      i = 2;
    } 
    if (numargs > i) {
      if (player != null && !player.hasPermission("backrooms.tp.others")) {
        player.sendMessage(BackroomsPlugin.CHAT_PREFIX + "You don't have permission to use this.");
        return true;
      } 
      for (; i < numargs; i++) {
        Player p = Bukkit.getPlayer(args[i]);
        if (p == null) {
          sender.sendMessage(BackroomsPlugin.CHAT_PREFIX + "Unknown player: " + BackroomsPlugin.CHAT_PREFIX);
        } else {
          this.plugin.noclip(p, level);
        } 
      } 
    } else if (player == null) {
      sender.sendMessage("[pxnBackrooms] Cannot teleport");
    } else {
      this.plugin.noclip(player, level);
    } 
    return true;
  }
}
