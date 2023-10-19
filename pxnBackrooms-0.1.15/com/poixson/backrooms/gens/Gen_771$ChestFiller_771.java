package com.poixson.backrooms.gens;

import com.poixson.commonmc.tools.DelayedChestFiller;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestFiller_771 extends DelayedChestFiller {
  public ChestFiller_771(JavaPlugin plugin, String worldName, int x, int y, int z) {
    super(plugin, worldName, x, y, z);
  }
  
  public void fill(Inventory chest) {
    double thresh_loot = Gen_771.this.thresh_loot.get();
    ItemStack item = new ItemStack(Material.BREAD);
    Location loc = chest.getLocation();
    int xx = loc.getBlockX();
    int zz = loc.getBlockZ();
    for (int i = 0; i < 27; i++) {
      int x = xx + i % 9;
      int y = zz + Math.floorDiv(i, 9);
      double value = Gen_771.this.noiseLoot.getNoise(x, y);
      if (value > thresh_loot)
        chest.setItem(i, item); 
    } 
  }
}
