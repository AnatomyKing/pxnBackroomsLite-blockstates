package com.poixson.backrooms.worlds;

import com.poixson.backrooms.BackroomsGen;
import com.poixson.backrooms.BackroomsLevel;
import com.poixson.backrooms.BackroomsPlugin;
import com.poixson.backrooms.dynmap.GeneratorTemplate;
import com.poixson.backrooms.gens.Gen_033;
import com.poixson.backrooms.listeners.Listener_033;
import com.poixson.commonmc.tools.plotter.BlockPlotter;
import java.util.LinkedList;
import org.bukkit.generator.ChunkGenerator;

public class Level_033 extends BackroomsLevel {
  public static final boolean ENABLE_GEN_033 = true;
  
  public static final boolean ENABLE_TOP_033 = true;
  
  public static final int LEVEL_Y = 50;
  
  public static final int LEVEL_H = 8;
  
  public final Gen_033 gen;
  
  protected final Listener_033 listener_033;
  
  public Level_033(BackroomsPlugin plugin) {
    super(plugin, 33);
    if (plugin.enableDynmapConfigGen()) {
      GeneratorTemplate gen_tpl = new GeneratorTemplate(plugin, 0);
      gen_tpl.add(33, "run", "Run For Your Life", 59);
    } 
    this.gen = (Gen_033)register((BackroomsGen)new Gen_033(this, 50, 8));
    this.listener_033 = new Listener_033(plugin);
  }
  
  public void register() {
    super.register();
    this.listener_033.register();
  }
  
  public void unregister() {
    super.unregister();
    this.listener_033.unregister();
  }
  
  public int getY(int level) {
    return 50;
  }
  
  public int getMaxY(int level) {
    return 58;
  }
  
  public boolean containsLevel(int level) {
    return (level == 33);
  }
  
  protected void generate(int chunkX, int chunkZ, ChunkGenerator.ChunkData chunk, LinkedList<BlockPlotter> plots) {
    this.gen.generate(null, chunk, plots, chunkX, chunkZ);
  }
}
