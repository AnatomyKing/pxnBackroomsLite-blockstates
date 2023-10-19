package com.poixson.backrooms.gens;

import com.poixson.commonmc.tools.DelayedChestFiller;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestFiller_000 extends DelayedChestFiller {
  public ChestFiller_000(JavaPlugin plugin, String worldName, int x, int y, int z) {
    super(plugin, worldName, x, y, z);
  }
  
  public void fill(Inventory chest) {
    ItemStack item = new ItemStack(Material.BREAD);
    Location loc = chest.getLocation();
    int xx = loc.getBlockX();
    int zz = loc.getBlockZ();
    for (int i = 0; i < 27; i++) {
      int x = xx + i % 9;
      int y = zz + Math.floorDiv(i, 9);
      double value = Gen_000.this.noiseLoot.getNoise(x, y);
      if (value > Gen_000.this.thresh_loot.get())
        chest.setItem(i, item); 
    } 
  }
}
