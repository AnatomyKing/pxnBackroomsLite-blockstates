package com.poixson.backrooms.gens;

import com.poixson.backrooms.PreGenData;
import org.bukkit.block.BlockFace;

public class LobbyData implements PreGenData {
  public final double valueWall;
  public final boolean isWall;
  public boolean wall_n = false;
  public boolean wall_s = false;
  public boolean wall_e = false;
  public boolean wall_w = false;
  public int wall_dist = 6;
  public int boxed = 0;
  public BlockFace box_dir = null;
  
  // Pass an instance of Gen_000 to the constructor
  public LobbyData(double valueWall, Gen_000 gen) {
    this.valueWall = valueWall;
    this.isWall = (valueWall > gen.thresh_wall_L.get() && valueWall < gen.thresh_wall_H.get());
  }
}
