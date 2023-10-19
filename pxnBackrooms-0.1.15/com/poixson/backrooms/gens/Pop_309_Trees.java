package com.poixson.backrooms.gens;

import com.poixson.commonmc.tools.TreePopulator;
import org.bukkit.Material;

public class Pop_309_Trees extends TreePopulator {
  protected final Gen_309 gen;
  
  protected final int path_clearing;
  
  public Pop_309_Trees(Gen_309 gen) {
    super(gen
        .getTreeNoise(), gen.level_y + 3 + 1, 
        
        Material.matchMaterial(gen.block_tree_trunk.get()), 
        Material.matchMaterial(gen.block_tree_leaves.get()));
    this.gen = gen;
    this.path_clearing = gen.path_clearing.get();
  }
  
  public boolean isTree(int x, int z) {
    if (!super.isTree(x, z))
      return false; 
    if (this.gen.getCenterClearingDistance(x, z, 8.0D) < 80.0D)
      return false; 
    if (this.gen.pathTrace.isPath(x, z, this.path_clearing))
      return false; 
    return true;
  }
}
