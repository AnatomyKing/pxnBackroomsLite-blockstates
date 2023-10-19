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
  
  public LobbyData(double valueWall) {
    this.valueWall = valueWall;
    this
      
      .isWall = (valueWall > Gen_000.this.thresh_wall_L.get() && valueWall < Gen_000.this.thresh_wall_H.get());
  }
}
