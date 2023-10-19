package com.poixson.commonmc.events;

import com.poixson.commonmc.pxnCommonPlugin;
import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.commonmc.tools.plugin.xListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerMoveManager extends xListener<pxnCommonPlugin> {
  public static final int WORLD_MIN_Y = -64;
  
  public static final int WORLD_MAX_Y = 319;
  
  public PlayerMoveManager(pxnCommonPlugin plugin) {
    super((xJavaPlugin)plugin);
  }
  
  @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
  public void onPlayerMove(PlayerMoveEvent event) {
    Location from = event.getFrom();
    Location to = event.getTo();
    if (from.getBlockX() != to.getBlockX() || from
      .getBlockY() != to.getBlockY() || from
      .getBlockZ() != to.getBlockZ()) {
      PluginManager pm = Bukkit.getPluginManager();
      Player player = event.getPlayer();
      PlayerMoveNormalEvent eventNormal = new PlayerMoveNormalEvent(player, from, to);
      pm.callEvent((Event)eventNormal);
      if (to.getBlockY() < -64) {
        OutsideOfWorldEvent eventOutside = new OutsideOfWorldEvent(player, from, to, OutsideOfWorldEvent.Outside.VOID, -64 - to.getBlockY());
        pm.callEvent((Event)eventOutside);
      } else if (to.getBlockY() > 319) {
        OutsideOfWorldEvent eventOutside = new OutsideOfWorldEvent(player, from, to, OutsideOfWorldEvent.Outside.SKY, to.getBlockY() - 319);
        pm.callEvent((Event)eventOutside);
      } 
    } 
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\events\PlayerMoveManager.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */