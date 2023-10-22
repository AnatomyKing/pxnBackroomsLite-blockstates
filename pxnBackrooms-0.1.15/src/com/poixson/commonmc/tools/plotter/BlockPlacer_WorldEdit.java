package com.poixson.commonmc.tools.plotter;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.scripting.CraftScriptContext;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import com.sk89q.worldedit.world.block.BlockState;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import com.sk89q.worldedit.bukkit.BukkitAdapter;

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

    public Material getMaterial(int x, int y, int z) {
        BlockState blockState = this.session.getBlock(this.origin.add(x, y, z));
        return BukkitAdapter.adapt((BlockStateHolder) blockState).getType();
    }

    public void setMaterial(int x, int y, int z, Material type) {
        BlockState blockState = BukkitAdapter.adapt(type).getDefaultBlockState();
        try {
            this.session.setBlock(this.origin.add(x, y, z), blockState);
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }
    }

    public BlockData getBlockData(int x, int y, int z) {
        BlockState blockState = this.session.getBlock(this.origin.add(x, y, z));
        return (BlockData) blockState;
    }

    public void setBlockData(int x, int y, int z, BlockData data) {
        try {
            this.session.setBlock(this.origin.add(x, y, z), (BlockStateHolder) data);
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }
    }
}





