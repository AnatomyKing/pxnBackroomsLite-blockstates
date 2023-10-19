package com.poixson.backrooms.listeners;

import com.poixson.backrooms.BackroomsPlugin;
import com.poixson.commonmc.events.PlayerMoveNormalEvent;
import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.commonmc.tools.plugin.xListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class Listener_001 extends xListener<BackroomsPlugin> {
  public static final int BASEMENT_LIGHT_RADIUS = 20;
  
  public static final int LAMP_Y = 6;
  
  protected final HashMap<UUID, List<Location>> playerLights = new HashMap<>();
  
  public Listener_001(BackroomsPlugin plugin) {
    super((BackroomsPlugin)plugin);
  }
  
  public void unregister() {
    super.unregister();
    synchronized (this.playerLights) {
      for (List<Location> list : this.playerLights.values()) {
        for (Location loc : list) {
          Block blk = loc.getBlock();
          if (Material.REDSTONE_TORCH.equals(blk.getType()))
            blk.setType(Material.BEDROCK); 
        } 
      } 
      this.playerLights.clear();
    } 
  }
  
  @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
  public void onPlayerMoveNormal(PlayerMoveNormalEvent event) {
    Player player = event.getPlayer();
    int level = ((BackroomsPlugin)this.plugin).getPlayerLevel(player);
    if (level == 1) {
      Location to = event.getTo();
      World world = to.getWorld();
      List<Location> lights = getPlayerLightsList(player.getUniqueId());
      Iterator<Location> it = lights.iterator();
      while (it.hasNext()) {
        Location loc = it.next();
        if (to.distance(loc) > 20.0D) {
          it.remove();
          if (canTurnOff(loc)) {
            Block blk = loc.getBlock();
            if (Material.REDSTONE_TORCH.equals(blk.getType()))
              blk.setType(Material.BEDROCK); 
          } 
        } 
      } 
      int y = 11;
      int r = 20;
      for (int iz = -21; iz < 20; iz += 10) {
        int zz = Math.floorDiv(iz + to.getBlockZ(), 10) * 10;
        for (int ix = -21; ix < 20; ix += 10) {
          int xx = Math.floorDiv(ix + to.getBlockX(), 10) * 10;
          Block blk = world.getBlockAt(xx, 11, zz);
          if (to.distance(blk.getLocation()) < 20.0D && (
            Material.BEDROCK.equals(blk.getType()) || Material.REDSTONE_TORCH
            .equals(blk.getType()))) {
            lights.add(blk.getLocation());
            world.setType(xx, 11, zz, Material.REDSTONE_TORCH);
          } 
        } 
      } 
    } else {
      UUID uuid = player.getUniqueId();
      if (this.playerLights.containsKey(uuid)) {
        List<Location> list = this.playerLights.get(uuid);
        this.playerLights.remove(uuid);
        for (Location loc : list)
          lightTurnOff(loc); 
        this.playerLights.remove(uuid);
      } 
    } 
  }
  
  protected void lightTurnOff(Location loc) {
    if (canTurnOff(loc)) {
      Block block = loc.getBlock();
      if (Material.REDSTONE_TORCH.equals(block.getType()))
        block.setType(Material.BEDROCK); 
    } 
  }
  
  protected boolean canTurnOff(Location loc) {
    for (List<Location> list : this.playerLights.values()) {
      if (list.contains(loc))
        return false; 
    } 
    return true;
  }
  
  protected List<Location> getPlayerLightsList(UUID uuid) {
    List<Location> list = this.playerLights.get(uuid);
    if (list != null)
      return list; 
    list = new ArrayList<>();
    this.playerLights.put(uuid, list);
    return list;
  }
}
