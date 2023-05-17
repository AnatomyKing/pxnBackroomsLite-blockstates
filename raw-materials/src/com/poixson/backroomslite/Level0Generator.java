package com.poixson.backroomslite;

import java.util.Random;

import com.poixson.backroomslite.utils.FastNoiseLiteD;
import io.th0rgal.oraxen.api.OraxenBlocks;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.type.Slab;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;


public class Level0Generator extends ChunkGenerator {

    public static final double              THRESH_WALL_L = 0.38;
    public static final double              THRESH_WALL_H = 0.5;
    protected final     int                 level_y       = 80;
    protected final     int                 level_h       = 5;
    // noise
    protected final     FastNoiseLiteD      noiseLobbyWalls;
    private final       BackroomsLitePlugin plugin        = BackroomsLitePlugin.getInstance();


    public Level0Generator() {
        // lobby walls
        this.noiseLobbyWalls = new FastNoiseLiteD();
        this.noiseLobbyWalls.setFrequency(0.022);
        this.noiseLobbyWalls.setFractalOctaves(2);
        this.noiseLobbyWalls.setFractalGain(0.1);
        this.noiseLobbyWalls.setFractalLacunarity(0.4);
        this.noiseLobbyWalls.setNoiseType(FastNoiseLiteD.NoiseType.Cellular);
        this.noiseLobbyWalls.setFractalType(FastNoiseLiteD.FractalType.PingPong);
        this.noiseLobbyWalls.setFractalPingPongStrength(2.28);
        this.noiseLobbyWalls.setCellularDistanceFunction(FastNoiseLiteD.CellularDistanceFunction.Manhattan);
        this.noiseLobbyWalls.setCellularReturnType(FastNoiseLiteD.CellularReturnType.Distance);
        this.noiseLobbyWalls.setRotationType3D(FastNoiseLiteD.RotationType3D.ImproveXYPlanes);
    }


	@Override
	public void generateSurface(final WorldInfo worldInfo, final Random random,
			final int chunkX, final int chunkZ, final ChunkData chunk) {
		double valueWall;
		boolean isWall;
		int xx, zz;
		final int wh = this.level_h + 4;
		final int cy = this.level_y + this.level_h + 2;
		for (int z=0; z<16; z++) {
			zz = (chunkZ * 16) + z;
			for (int x=0; x<16; x++) {
				xx = (chunkX * 16) + x;
				valueWall = this.noiseLobbyWalls.getNoiseRot(xx, zz, 0.25);
				isWall = (valueWall > THRESH_WALL_L && valueWall < THRESH_WALL_H);
				chunk.setBlock(x, this.level_y, z, Material.BEDROCK);
				chunk.setBlock(x, cy+2,         z, Material.BEDROCK);
				if (isWall) {
					for (int iy=1; iy<wh; iy++) {
						chunk.setBlock(x, this.level_y+iy, z, Material.valueOf(plugin.getConfig().getString("lobby.walls.material")));
					}
				} else {
					chunk.setBlock(x, this.level_y+1, z, Material.valueOf(plugin.getConfig().getString("lobby.floor.material")));
					final int  modX7 = (xx < 0 ? 1-xx : xx) % 7;
					final int  modZ7 = (zz < 0 ? -zz : zz) % 7;
					if (modZ7 == 0 && modX7 < 2) {
						// ceiling lights
						chunk.setBlock(x, cy, z, Material.valueOf(plugin.getConfig().getString("lobby.lamp.material")));
						chunk.setBlock(x, cy+1, z, Material.REDSTONE_BLOCK);
					} else {
						// ceiling
						chunk.setBlock(x, cy, z, Material.SMOOTH_STONE_SLAB);
						final Slab slab = (Slab) chunk.getBlockData(x, cy, z);
						slab.setType(Slab.Type.TOP);
						chunk.setBlock(x, cy,   z, slab);
						chunk.setBlock(x, cy+1, z, Material.STONE);
                        chunk.setBlock(x, cy+2, z, Material.BEDROCK);
					}
				}
			}
		}
	}



    @Override
    public Location getFixedSpawnLocation(final World world, final Random random) {
        final int y = this.level_y + 2;
        return world.getBlockAt(0, y, 0).getLocation();
    }
}
