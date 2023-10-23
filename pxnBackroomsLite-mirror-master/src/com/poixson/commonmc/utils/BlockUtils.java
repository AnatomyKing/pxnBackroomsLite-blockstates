package com.poixson.commonmc.utils;

import com.poixson.tools.Keeper;
import com.poixson.utils.Utils;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;

public class BlockUtils {
  static {
    Keeper.add(new BlockUtils());
  }
  
  public static BlockData StringToBlockData(AtomicReference<String> atomic, String def) {
    String blockStr = atomic.get();
    if (Utils.notEmpty(blockStr))
      return StringToBlockData(blockStr); 
    return StringToBlockData(def);
  }
  
  public static BlockData StringToBlockData(String blockStr) {
    return Bukkit.createBlockData(blockStr);
  }
}
