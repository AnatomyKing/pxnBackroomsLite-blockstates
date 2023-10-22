package com.poixson.commonmc.tools.plotter;

import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.Material;
import org.bukkit.RegionAccessor;

import com.poixson.exceptions.InvalidValueException;

public class BlockPlacer {

    public final World                 world;
    public final ChunkData             chunk;
    public final LimitedRegion         region;
    public final BlockPlacer_WorldEdit worldedit;

    public BlockPlacer(final World                 world    ) { this(world, null,  null,   null   ); }
    public BlockPlacer(final ChunkData             chunk    ) { this(null,  chunk, null,   null   ); }
    public BlockPlacer(final LimitedRegion         region   ) { this(null,  null,  region, null   ); }
    public BlockPlacer(final BlockPlacer_WorldEdit worldedit) { this(null,  null,  null, worldedit); }
    public BlockPlacer(final World world, final ChunkData chunk,
            final LimitedRegion region, final BlockPlacer_WorldEdit worldedit) {
        this.world     = world;
        this.chunk     = chunk;
        this.region    = region;
        this.worldedit = worldedit;
    }

    public Material getMaterial(final int x, final int y, final int z) {
        if (this.world != null) {
            Material material = this.world.getBlockAt(x, y, z).getType();
            if (isSpecialMaterial(material)) {
                return material;
            }
            return Material.getMaterial(material.name());
        }
        if (this.chunk != null) {
            Material material = this.chunk.getType(x, y, z);
            if (isSpecialMaterial(material)) {
                return material;
            }
            return Material.getMaterial(material.name());
        }
        if (this.worldedit != null) {
            Material material = ((RegionAccessor) this.worldedit).getType(x, y, z);
            if (isSpecialMaterial(material)) {
                return material;
            }
            return Material.getMaterial(material.name());
        }
        if (this.region != null) {
            if (this.region.isInRegion(x, y, z)) {
                Material material = ((World) this.region).getBlockAt(x, y, z).getType();
                if (isSpecialMaterial(material)) {
                    return material;
                }
                return Material.getMaterial(material.name());
            }
            return null;
        }
        throw new InvalidValueException("world/chunk/region");
    }

    private boolean isSpecialMaterial(Material material) {
        String materialName = material.name().toUpperCase();
        return materialName.contains("DOOR") || materialName.contains("STAIRS") || materialName.contains("SLAB");
    }

    public void setMaterial(final int x, final int y, final int z, final Material type) {
        if (this.world != null) this.world.getBlockAt(x, y, z).setType(type); else
        if (this.chunk != null) ((RegionAccessor) this.chunk).setType(x, y, z, type); else
        if (this.worldedit != null) ((RegionAccessor) this.worldedit).setType(x, y, z, type); else
        if (this.region != null) {
            if (this.region.isInRegion(x, y, z)) {
                ((World) this.region).getBlockAt(x, y, z).setType(type);
            }
        } else throw new InvalidValueException("world/chunk/region");
    }

    public BlockData getBlockData(final int x, final int y, final int z) {

        if (this.world != null) return this.world.getBlockAt(x, y, z).getBlockData();
        if (this.chunk != null) return this.chunk.getBlockData(x, y, z);
        if (this.worldedit != null) return this.worldedit.getBlockData(x, y, z);
        if (this.region != null) {
            if (this.region.isInRegion(x, y, z)) {
                return ((World) this.region).getBlockAt(x, y, z).getBlockData();
            }
            return null;
        } else throw new InvalidValueException("world/chunk/region");
    }

    public void setBlockData(final int x, final int y, final int z, final BlockData data) {
        if (this.world != null) this.world.getBlockAt(x, y, z).setBlockData(data); else
        if (this.chunk != null) this.chunk.setBlock(x, y, z, data); else
        if (this.worldedit != null) this.worldedit.setBlockData(x, y, z, data); else
        if (this.region != null) {
            if (this.region.isInRegion(x, y, z)) {
                ((World) this.region).getBlockAt(x, y, z).setBlockData(data);
            }
        } else throw new InvalidValueException("world/chunk/region");
    }
}

