package com.poixson.backrooms.worlds;

import com.poixson.backrooms.BackroomsGen;
import com.poixson.backrooms.BackroomsLevel;
import com.poixson.backrooms.BackroomsPlugin;
import com.poixson.backrooms.PreGenData;
import com.poixson.backrooms.dynmap.GeneratorTemplate;
import com.poixson.backrooms.gens.Gen_094;
import com.poixson.commonmc.tools.plotter.BlockPlotter;
import com.poixson.tools.dao.Iab;
import java.util.HashMap;
import java.util.LinkedList;
import org.bukkit.generator.ChunkGenerator;

public class Level_094 extends BackroomsLevel {
  public static final boolean ENABLE_GEN_094 = true;
  
  public static final int LEVEL_Y = 0;
  
  public final Gen_094 gen;
  
  public Level_094(BackroomsPlugin plugin) {
    super(plugin, 94);
    if (plugin.enableDynmapConfigGen()) {
      GeneratorTemplate gen_tpl = new GeneratorTemplate(plugin, 0);
      gen_tpl.add(94, "motion", "Motion");
      gen_tpl.commit();
    } 
    this.gen = (Gen_094)register((BackroomsGen)new Gen_094(this, 0, 0));
  }
  
  public void register() {
    super.register();
  }
  
  public void unregister() {
    super.unregister();
  }
  
  public int getY(int level) {
    return 255;
  }
  
  public int getMaxY(int level) {
    return 319;
  }
  
  public boolean containsLevel(int level) {
    return (level == 94);
  }
  
  public class PregenLevel94 implements PreGenData {
    public final HashMap<Iab, Gen_094.HillsData> hills = new HashMap<>();
  }
  
  protected void generate(int chunkX, int chunkZ, ChunkGenerator.ChunkData chunk, LinkedList<BlockPlotter> plots) {
    PregenLevel94 pregen = new PregenLevel94();
    this.gen.pregenerate(pregen.hills, chunkX, chunkZ);
    this.gen.generate(pregen, chunk, plots, chunkX, chunkZ);
  }
}
