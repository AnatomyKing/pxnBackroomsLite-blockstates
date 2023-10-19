package com.poixson.backrooms.listeners;

import com.poixson.backrooms.BackroomsPlugin;
import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.commonmc.tools.plugin.xListener;
import com.poixson.morefoods.MoreFoodsAPI;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class Listener_023 extends xListener<BackroomsPlugin> {
  public Listener_023(BackroomsPlugin plugin) {
    super((xJavaPlugin)plugin);
  }
  
  @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
  public void onPlayerConsume(PlayerItemConsumeEvent event) {
    Player player = event.getPlayer();
    int level = ((BackroomsPlugin)this.plugin).getPlayerLevel(player);
    if (level == 0 || level == 23) {
      ItemStack stack = event.getItem();
      Material type = stack.getType();
      MoreFoodsAPI morefoods = MoreFoodsAPI.GetAPI();
      boolean aged = true;
      if (morefoods != null) {
        Boolean result = morefoods.isFullyAged(stack);
        if (result != null)
          aged = result.booleanValue(); 
      } 
      switch (level) {
        case 0:
          if (Material.APPLE.equals(type) && 
            aged) {
            int delta_y = -14;
            Location loc = player.getLocation();
            loc.add(0.0D, -14.0D, 0.0D);
            player.teleport(loc);
            player.playEffect(EntityEffect.TELEPORT_ENDER);
            player.playSound((Entity)player, Sound.AMBIENT_CRIMSON_FOREST_ADDITIONS, 1.0F, 0.7F);
            player.playSound((Entity)player, Sound.AMBIENT_CRIMSON_FOREST_MOOD, 0.7F, 2.0F);
          } 
          break;
        case 23:
          if (Material.CARROT.equals(type) && 
            aged) {
            int delta_y = 14;
            Location loc = player.getLocation();
            loc.add(0.0D, 14.0D, 0.0D);
            player.teleport(loc);
            player.playEffect(EntityEffect.TELEPORT_ENDER);
            player.playSound((Entity)player, Sound.AMBIENT_BASALT_DELTAS_ADDITIONS, 1.0F, 1.85F);
            player.playSound((Entity)player, Sound.AMBIENT_BASALT_DELTAS_MOOD, 0.85F, 0.65F);
          } 
          break;
      } 
    } 
  }
}
