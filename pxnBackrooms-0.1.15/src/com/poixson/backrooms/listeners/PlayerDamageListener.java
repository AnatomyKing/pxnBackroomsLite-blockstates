package com.poixson.backrooms.listeners;

import com.poixson.backrooms.BackroomsPlugin;
import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.commonmc.tools.plugin.xListener;
import com.poixson.tools.xTime;
import com.poixson.utils.RandomUtils;
import com.poixson.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener extends xListener<BackroomsPlugin> {
  public static final long DAMAGE_TIMEOUT = xTime.Parse("5s").ms();
  
  public static final double MIN_DAMAGE = 3.0D;
  
  protected final AtomicInteger cleanup = new AtomicInteger(0);
  
  public class PlayerDamageDAO {
    public long last;
    
    public int count = 1;
    
    public PlayerDamageDAO() {
      this.last = Utils.GetMS();
    }
    
    public int increment() {
      this.last = Utils.GetMS();
      return ++this.count;
    }
  }
  
  protected final HashMap<UUID, PlayerDamageDAO> lastPlayerDamage = new HashMap<>();
  
  public PlayerDamageListener(BackroomsPlugin plugin) {
    super((xJavaPlugin)plugin);
  }
  
  @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
  public void onPlayerDamage(EntityDamageEvent event) {
    Entity entity = event.getEntity();
    if (entity instanceof Player) {
      int count, countMin;
      Player player = (Player)entity;
      int level = ((BackroomsPlugin)this.plugin).getPlayerLevel(player);
      if (level < 0) {
        if (!player.hasPermission("noclipfront"))
          return; 
      } else if (!player.hasPermission("noclipback")) {
        return;
      } 
      double health = player.getHealth();
      EntityDamageEvent.DamageCause cause = event.getCause();
      switch (cause) {
        case SUFFOCATION:
        case VOID:
          count = incrementPlayerDamageCount(player.getUniqueId());
          countMin = (health > 3.0D) ? 4 : 2;
          if (count <= countMin)
            return; 
          break;
        default:
          return;
      } 
      int rnd = RandomUtils.GetRandom(0, 9999);
      if (rnd % 10 < 5)
        return; 
      event.setCancelled(true);
      ((BackroomsPlugin)this.plugin).noclip(player);
      this.lastPlayerDamage.remove(player.getUniqueId());
      if (this.lastPlayerDamage.size() % 5 == 0) {
        long time = Utils.GetMS();
        ArrayList<UUID> remove = new ArrayList<>();
        Iterator<Map.Entry<UUID, PlayerDamageDAO>> it = this.lastPlayerDamage.entrySet().iterator();
        while (it.hasNext()) {
          Map.Entry<UUID, PlayerDamageDAO> entry = it.next();
          PlayerDamageDAO dao = entry.getValue();
          if (dao.last + DAMAGE_TIMEOUT > time)
            remove.add(entry.getKey()); 
        } 
        for (UUID uuid : remove)
          this.lastPlayerDamage.remove(uuid); 
      } 
    } 
  }
  
  public int incrementPlayerDamageCount(UUID uuid) {
    PlayerDamageDAO dao = this.lastPlayerDamage.get(uuid);
    if (dao != null && 
      dao.last + DAMAGE_TIMEOUT >= Utils.GetMS())
      return dao.increment(); 
    dao = new PlayerDamageDAO();
    this.lastPlayerDamage.put(uuid, dao);
    return dao.count;
  }
}
