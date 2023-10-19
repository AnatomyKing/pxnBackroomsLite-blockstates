package com.poixson.commonmc.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;

public final class ItemUtils {
  public static int GetCustomModel(ItemStack stack) {
    if (stack.hasItemMeta()) {
      ItemMeta meta = stack.getItemMeta();
      if (meta.hasCustomModelData())
        return meta.getCustomModelData(); 
    } 
    return 0;
  }
  
  public static void SetMapID(ItemStack map, int id) {
    MapMeta meta = (MapMeta)map.getItemMeta();
    meta.setMapId(id);
    map.setItemMeta((ItemMeta)meta);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonm\\utils\ItemUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */