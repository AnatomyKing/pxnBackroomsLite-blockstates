package com.poixson.backrooms.gens;

import com.poixson.backrooms.BackroomsPop;
import com.poixson.backrooms.worlds.Level_000;
import com.poixson.commonmc.tools.plotter.BlockPlotter;
import com.poixson.commonmc.tools.plotter.PlotterFactory;
import java.util.LinkedList;
import org.bukkit.Material;
import org.bukkit.generator.LimitedRegion;

public class Pop_001 implements BackroomsPop {
  public static final int WELL_SIZE = 5;
  
  public static final int WELL_HEIGHT = 2;
  
  protected final Level_000 level0;
  
  protected final Gen_001 gen;
  
  public Pop_001(Level_000 level0) {
    this.level0 = level0;
    this.gen = level0.gen_001;
  }
  
  public void populate(int chunkX, int chunkZ, LimitedRegion region, LinkedList<BlockPlotter> plots) {
    for (int iz = 0; iz < 16; iz++) {
      for (int ix = 0; ix < 16; ix++) {
        int xx = chunkX * 16 + ix;
        int zz = chunkZ * 16 + iz;
        double value = this.gen.noiseWell.getNoise(xx, zz);
        if (value > this.gen.noiseWell.getNoise((xx + 1), zz) && value > this.gen.noiseWell
          .getNoise((xx - 1), zz) && value > this.gen.noiseWell
          .getNoise(xx, (zz + 1)) && value > this.gen.noiseWell
          .getNoise(xx, (zz - 1))) {
          generateWell(region, xx, zz);
          return;
        } 
      } 
    } 
  }
  
  protected void generateWell(LimitedRegion region, int x, int z) {
    int y = this.gen.level_y + 3 + 2;
    int halfL = (int)Math.floor(2.5D);
    int halfH = (int)Math.ceil(2.5D);
    for (int iz = 0 - halfL; iz < halfH; iz++) {
      for (int ix = 0 - halfL; ix < halfH; ix++) {
        if (!Material.AIR.equals(region.getType(x + ix, y + 1, z + iz)))
          return; 
      } 
    } 
    this.level0.portal_1_to_771.add(x, z);
    BlockPlotter plot = (new PlotterFactory()).placer(region).axis("use").xyz(x, this.gen.level_y, z).wd(5, 5).h(10).build();
    plot.type('#', Material.BEDROCK);
    plot.type('x', Material.MOSSY_STONE_BRICKS);
    plot.type('.', Material.AIR);
    StringBuilder[][] matrix = plot.getMatrix3D();
    matrix[0][1].append(" ...");
    matrix[0][2].append(" ...");
    matrix[0][3].append(" ...");
    int iy = 1;
    int i;
    for (i = 0; i < 4; i++) {
      matrix[iy + i][0].append(" ###");
      matrix[iy + i][1].append("#...#");
      matrix[iy + i][2].append("#...#");
      matrix[iy + i][3].append("#...#");
      matrix[iy + i][4].append(" ###");
    } 
    iy += 4;
    for (i = 0; i < 2; i++) {
      matrix[iy + i][0].append(" xxx");
      matrix[iy + i][1].append("x...x");
      matrix[iy + i][2].append("x...x");
      matrix[iy + i][3].append("x...x");
      matrix[iy + i][4].append(" xxx");
    } 
    iy += 2;
    for (i = 0; i < 3; i++) {
      for (int j = 0; j < 5; j++)
        matrix[iy + i][j].append("....."); 
    } 
    plot.run();
  }
}
