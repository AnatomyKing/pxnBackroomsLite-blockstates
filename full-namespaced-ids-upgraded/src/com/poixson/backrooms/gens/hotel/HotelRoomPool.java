package com.poixson.backrooms.gens.hotel;

import static com.poixson.backrooms.gens.Gen_037.WATER_DEPTH;
import static com.poixson.backrooms.worlds.Level_000.SUBCEILING;
import static com.poixson.backrooms.worlds.Level_000.SUBFLOOR;

import java.util.LinkedList;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.LimitedRegion;

import com.poixson.backrooms.worlds.Level_000;
import com.poixson.commonmc.tools.plotter.BlockPlotter;
import com.poixson.commonmc.tools.plotter.PlotterFactory;
import com.poixson.tools.dao.Iabcd;
import com.poixson.utils.StringUtils;


public class HotelRoomPool implements HotelRoom {

	protected final Level_000 level0;



	public HotelRoomPool(final Level_000 level0) {
		this.level0 = level0;
	}



	public BlockFace canBuildHere(final Iabcd area, final LimitedRegion region) {
		final int x = area.a;
		final int z = area.b;
		final int w = area.c;
		final int d = area.d;
		final int wh = Math.floorDiv(w, 2);
		final int dh = Math.floorDiv(d, 2);
		final int y = Level_000.Y_037 + Level_000.H_037 + SUBFLOOR;
		// check for pool wall
		if (!region.isInRegion(x+w-1, y, z    ) || Material.AIR.equals(region.getType(x+w-1, y, z    ))) return null; // north-east
		if (!region.isInRegion(x,     y, z    ) || Material.AIR.equals(region.getType(x,     y, z    ))) return null; // north-west
		if (!region.isInRegion(x+w-1, y, z+d-1) || Material.AIR.equals(region.getType(x+w-1, y, z+d-1))) return null; // south-east
		if (!region.isInRegion(x,     y, z+d-1) || Material.AIR.equals(region.getType(x,     y, z+d-1))) return null; // south-west
		// check pool opening near
		if (region.isInRegion(x+wh,  y, z-1  ) && Material.AIR.equals(region.getType(x+wh,  y, z-1  ))) return BlockFace.NORTH;
		if (region.isInRegion(x+wh,  y, z+d+1) && Material.AIR.equals(region.getType(x+wh,  y, z+d+1))) return BlockFace.SOUTH;
		if (region.isInRegion(x+w+1, y, z+dh ) && Material.AIR.equals(region.getType(x+w+1, y, z+dh ))) return BlockFace.EAST;
		if (region.isInRegion(x-1,   y, z+dh ) && Material.AIR.equals(region.getType(x-1,   y, z+dh ))) return BlockFace.WEST;
		return null;
	}



	@Override
	public void build(final Iabcd area, final int y, final BlockFace direction,
			final LimitedRegion region, final LinkedList<BlockPlotter> plots) {
		final Material block_hotel_wall  = Material.matchMaterial(this.level0.gen_005.block_hall_wall.get());
		final Material block_pool_wall_a = Material.matchMaterial(this.level0.gen_037.block_wall_a.get());
		final Material block_pool_wall_b = Material.matchMaterial(this.level0.gen_037.block_wall_b.get());
		if (block_hotel_wall  == null) throw new RuntimeException("Invalid block type for level 5 HallWall");
		if (block_pool_wall_a == null) throw new RuntimeException("Invalid block type for level 37 WallA");
		if (block_pool_wall_b == null) throw new RuntimeException("Invalid block type for level 37 WallB");
		final int x = area.a;
		final int z = area.b;
		final int w = area.c;
		final int d = area.d;
		final int yy = Level_000.Y_037 + SUBFLOOR + 1;
		final int th = Level_000.H_037 + Level_000.H_005 + SUBCEILING + SUBFLOOR + 5;
		final BlockPlotter plot =
			(new PlotterFactory())
			.placer(region)
			.axis("use")
			.rotate(direction.getOppositeFace())
			.xyz(x, yy, z)
			.whd(w, th, d)
			.build();
		plot.type('#', block_pool_wall_a);
		plot.type('@', block_pool_wall_b);
		plot.type('.', Material.AIR              );
		plot.type(',', "minecraft:water[level=0]");
		plot.type('g', Material.GLOWSTONE        );
		plot.type('X', Material.BEDROCK          );
//TODO
plot.type('-', Material.PRISMARINE_BRICK_SLAB);
plot.type('$', block_hotel_wall);
plot.type('&', block_hotel_wall);
plot.type('d', Material.ACACIA_DOOR);
plot.type('D', Material.ACACIA_DOOR);
//		plot.type('-', Material.PRISMARINE_BRICK_SLAB, "top", "logged");
//		plot.type('$', block_hotel_wall, "up");
//		plot.type('&', block_hotel_wall, FaceToAxString(Rotate(direction, 0.25)));
//		plot.type('d', Material.ACACIA_DOOR, "top",    "right", "closed", FaceToAxString(direction));
//		plot.type('D', Material.ACACIA_DOOR, "bottom", "right", "closed", FaceToAxString(direction));
		plot.type('_', Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
		final StringBuilder[][] matrix = plot.getMatrix3D();
		final int hy = Level_000.H_037 + SUBCEILING + SUBFLOOR + 3;
		LOOP_Y:
		for (int iy=0; iy<th; iy++) {
			// poolrooms exit
			if (iy == 0) {
				matrix[iy][0  ].append(StringUtils.Repeat(w, 'g'));
				matrix[iy][d-1].append(matrix[0][0].toString());
				for (int iz=1; iz<d-1; iz++)
					matrix[iy][iz].append('g').append(StringUtils.Repeat(w-2, '#')).append('g');
				continue LOOP_Y;
			}
			if (iy < WATER_DEPTH) {
				matrix[iy][0  ].append('#').append(StringUtils.Repeat(w-2, ',')).append('#');
				matrix[iy][d-1].append(matrix[iy][0].toString());
				for (int iz=1; iz<d-1; iz++)
					matrix[iy][iz].append(StringUtils.Repeat(w, ','));
				continue LOOP_Y;
			}
			if (iy == WATER_DEPTH) {
				matrix[iy][0  ].append("#-").append(StringUtils.Repeat(w-4, ',')).append("-#");
				matrix[iy][1  ].append('-' ).append(StringUtils.Repeat(w-2, ',')).append( '-');
				matrix[iy][d-1].append(matrix[iy][0].toString());
				matrix[iy][d-2].append(matrix[iy][1].toString());
				for (int iz=2; iz<d-2; iz++)
					matrix[iy][iz].append(StringUtils.Repeat(w, ','));
				continue LOOP_Y;
			}
			if (iy == WATER_DEPTH+1) {
				matrix[iy][0  ].append('@').append(StringUtils.Repeat(w-2, '#')).append('@');
				matrix[iy][d-1].append(matrix[iy][0].toString());
				for (int iz=1; iz<d-1; iz++)
					matrix[iy][iz].append('#').append(StringUtils.Repeat(w-2, ',')).append('#');
				continue LOOP_Y;
			}
			// tunnel between levels
			if (iy < hy) {
				matrix[iy][0  ].append(StringUtils.Repeat(w, '@'));
				matrix[iy][1  ].append('@').append(StringUtils.Repeat(w-2, 'X')).append('@');
				matrix[iy][d-1].append(matrix[iy][0].toString());
				matrix[iy][d-2].append(matrix[iy][1].toString());
				for (int iz=2; iz<d-2; iz++)
					matrix[iy][iz].append("@X").append(StringUtils.Repeat(w-4, ',')).append("X@");
				continue LOOP_Y;
			}
			// hotel pool floor
			if (iy == hy) {
				matrix[iy][0  ].append(StringUtils.Repeat(w, '@'));
				matrix[iy][1  ].append('@').append(StringUtils.Repeat(w-2, '#')).append('@');
				matrix[iy][2  ].append(matrix[iy][1].toString());
				matrix[iy][d-1].append(matrix[iy][0].toString());
				matrix[iy][d-2].append(matrix[iy][1].toString());
				for (int iz=3; iz<d-2; iz++)
					matrix[iy][iz].append("@#").append(StringUtils.Repeat(w-4, ',')).append("#@");
				continue LOOP_Y;
			}
			// hotel room ceiling
			if (iy == th-1) {
				matrix[iy][d-1].append(StringUtils.Repeat(w, '@'));
				for (int iz=1; iz<d-1; iz++)
					matrix[iy][iz].append('@').append(StringUtils.Repeat(w-2, 'g')).append('@');
				continue LOOP_Y;
			}
			// hotel room walls
			{
				matrix[iy][0  ].append(StringUtils.Repeat(w, ' '));
				matrix[iy][d-1].append(StringUtils.Repeat(w, '@'));
				for (int iz=1; iz<d-1; iz++)
					matrix[iy][iz].append('@').append(StringUtils.Repeat(w-2, ' ')).append('@');
			}
		} // end LOOP_Y
		// door
		final int door_x = Math.floorDiv(w, 2) - 2;
		StringUtils.ReplaceInString(matrix[hy+4][0], "&&&&&", door_x);
		StringUtils.ReplaceInString(matrix[hy+3][0], "$...$", door_x);
		StringUtils.ReplaceInString(matrix[hy+2][0], "$.d.$", door_x);
		StringUtils.ReplaceInString(matrix[hy+1][0], "$.D.$", door_x);
		StringUtils.ReplaceInString(matrix[hy  ][0], "$&&&$", door_x);
		// front wall
		StringUtils.ReplaceInString(matrix[hy+3][1], "&&&", door_x+1);
		StringUtils.ReplaceInString(matrix[hy+2][1], "$.$", door_x+1);
		StringUtils.ReplaceInString(matrix[hy+1][1], "$_$", door_x+1);
		plot.run();
	}



}
