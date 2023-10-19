package com.poixson.commonmc.tools.plotter;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.scripting.CraftScriptContext;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import org.bukkit.block.data.BlockData;

public class BlockPlacer_WorldEdit extends BlockPlacer {
  protected final BlockVector3 origin;
  
  protected final CraftScriptContext context;
  
  protected final EditSession session;
  
  public BlockPlacer_WorldEdit(BlockVector3 origin, CraftScriptContext context, EditSession session) {
    super(null, null, null, null);
    this.origin = origin;
    this.context = context;
    this.session = session;
  }
  
  public BlockData getBlock(int x, int y, int z) {
    return BukkitAdapter.adapt((BlockStateHolder)this.session
        .getBlock(this.origin.add(x, y, z)));
  }
  
  public void setBlock(int x, int y, int z, BlockData type) {
    try {
      this.session.setBlock(this.origin
          .add(x, y, z), 
          (BlockStateHolder)BukkitAdapter.adapt(type));
    } catch (MaxChangedBlocksException e) {
      e.printStackTrace();
    } 
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\plotter\BlockPlacer_WorldEdit.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */