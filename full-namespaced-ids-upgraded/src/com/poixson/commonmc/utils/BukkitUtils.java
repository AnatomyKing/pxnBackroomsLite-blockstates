package com.poixson.commonmc.utils;

import com.poixson.tools.Keeper;
import com.poixson.utils.Utils;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.map.MapView;
import org.bukkit.potion.PotionEffect;

public final class BukkitUtils {
  static {
    Keeper.add(new BukkitUtils());
  }
  
  public static ConcurrentHashMap<String, WeakReference<BlockData>> blocksCache = new ConcurrentHashMap<>();
  
  public static boolean EqualsUUID(UUID uuidA, UUID uuidB) {
    return Utils.EqualsUUID(uuidA, uuidB);
  }
  
  public static boolean EqualsPlayer(Player playerA, Player playerB) {
    if (playerA == null || playerB == null)
      return (playerA == null && playerB == null); 
    return Utils.EqualsUUID(playerA.getUniqueId(), playerB.getUniqueId());
  }
  
  public static boolean EqualsPotionEffect(PotionEffect effectA, PotionEffect effectB) {
    if (effectA == null || effectB == null)
      return (effectA == null && effectB == null); 
    return effectA.equals(effectB);
  }
  
  public static boolean EqualsLocation(Location locA, Location locB) {
    if (locA == null || locB == null)
      return (locA == null && locB == null); 
    if (!EqualsWorld(locA.getWorld(), locB.getWorld()))
      return false; 
    if (locA.getBlockX() != locB.getBlockX())
      return false; 
    if (locA.getBlockY() != locB.getBlockY())
      return false; 
    if (locA.getBlockZ() != locB.getBlockZ())
      return false; 
    return true;
  }
  
  public static boolean EqualsWorld(Location locA, Location locB) {
    return EqualsWorld(locA.getWorld(), locB.getWorld());
  }
  
  public static boolean EqualsWorld(World worldA, World worldB) {
    if (worldA == null || worldB == null)
      return (worldA == null && worldB == null); 
    return worldA.equals(worldB);
  }
  
  public static void BroadcastNear(Location loc, int distance, String msg) {
    if (loc == null)
      throw new NullPointerException(); 
    World world = loc.getWorld();
    Collection<? extends Player> players = Bukkit.getOnlinePlayers();
    for (Player player : players) {
      if (player == null)
        continue; 
      Location playerLoc = player.getLocation();
      if (playerLoc == null)
        continue; 
      if (!EqualsWorld(world, playerLoc.getWorld()))
        continue; 
      double playerDist = playerLoc.distance(loc);
      if (playerDist <= distance)
        player.sendMessage(msg); 
    } 
  }
  
  public static void BroadcastWorld(String worldName, String msg) {
    BroadcastWorld(Bukkit.getWorld(worldName), msg);
  }
  
  public static void BroadcastWorld(World world, String msg) {
    for (Player player : world.getPlayers())
      player.sendMessage(msg); 
  }
  
  public static MapView GetMapView(int mapid) {
    return Bukkit.getMap(mapid);
  }
  
  public static String GetServerPath() {
    File path = Bukkit.getWorldContainer();
    try {
      return path.getCanonicalPath();
    } catch (IOException iOException) {
      return path.getAbsolutePath();
    } 
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonm\\utils\BukkitUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */