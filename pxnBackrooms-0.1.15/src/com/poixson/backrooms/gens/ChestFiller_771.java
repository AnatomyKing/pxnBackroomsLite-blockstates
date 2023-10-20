package com.poixson.backrooms.gens;

import com.poixson.commonmc.tools.DelayedChestFiller;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestFiller_771 extends DelayedChestFiller {
    private final Gen_771 gen;

    public ChestFiller_771(JavaPlugin plugin, String worldName, int x, int y, int z, Gen_771 gen) {
        super(plugin, worldName, x, y, z);
        this.gen = gen;
    }

    public void fill(Inventory chest) {
        double thresh_loot = this.gen.thresh_loot.get();
        ItemStack item = new ItemStack(Material.BREAD);
        Location loc = chest.getLocation();
        int xx = loc.getBlockX();
        int zz = loc.getBlockZ();
        for (int i = 0; i < 27; i++) {
            int x = xx + i % 9;
            int y = zz + Math.floorDiv(i, 9);
            double value = this.gen.noiseLoot.getNoise(x, y);
            if (value > thresh_loot)
                chest.setItem(i, item);
        }
    }
}
