package com.poixson.commonmc.tools.wizards.steps;

import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.commonmc.tools.wizards.Wizard;
import com.poixson.commonmc.utils.BukkitUtils;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public abstract class WizardStep_BlockClick<T extends xJavaPlugin> extends WizardStep<T> implements Listener {
  protected final AtomicReference<Location> loc = new AtomicReference<>(null);
  
  public WizardStep_BlockClick(Wizard<T> wizard, String logPrefix, String chatPrefix) {
    super(wizard, logPrefix, chatPrefix);
  }
  
  protected abstract void sendMessage_ClickBlock();
  
  public void run() {
    sendClear();
    sendMessage_ClickBlock();
    Bukkit.getPluginManager()
      .registerEvents(this, (Plugin)getPlugin());
    this.wizard.resetTimeout();
  }
  
  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    if (isCompleted()) {
      close();
      return;
    } 
    Player player = getPlayer();
    if (event.getHand() != EquipmentSlot.HAND)
      return; 
    if (event.getAction() != Action.LEFT_CLICK_BLOCK && event
      .getAction() != Action.RIGHT_CLICK_BLOCK)
      return; 
    if (!BukkitUtils.EqualsPlayer(player, event.getPlayer()))
      return; 
    ItemStack stack = player.getInventory().getItemInMainHand();
    Material material = stack.getType();
    if (!material.isAir())
      return; 
    Block block = event.getClickedBlock();
    if (block == null)
      return; 
    Material mat = block.getType();
    if (mat == null || !mat.isSolid())
      return; 
    Location loc = block.getLocation();
    event.setCancelled(true);
    this.loc.set(loc);
    if (this.state.compareAndSet(null, Boolean.TRUE))
      this.wizard.next(); 
    close();
  }
  
  public void close() {
    super.close();
    HandlerList.unregisterAll(this);
  }
  
  public Location getLocation() {
    return this.loc.get();
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\wizards\steps\WizardStep_BlockClick.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */