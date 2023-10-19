package com.poixson.backrooms.worlds;

import java.util.LinkedList;

import com.poixson.backrooms.BackroomsLevel;
import com.poixson.backrooms.BackroomsPlugin;
import com.poixson.backrooms.dynmap.GeneratorTemplate;
import com.poixson.backrooms.gens.Gen_033;
import com.poixson.backrooms.listeners.Listener_033;
import com.poixson.commonmc.tools.plotter.BlockPlotter;


// 33 | Run For Your Life!
public class Level_033 extends BackroomsLevel {

	public static final boolean ENABLE_GEN_033 = true;
	public static final boolean ENABLE_TOP_033 = true;

	public static final int LEVEL_Y = 50;
	public static final int LEVEL_H = 8;

	// generators
	public final Gen_033 gen;

	// listeners
	protected final Listener_033 listener_033;



	public Level_033(final BackroomsPlugin plugin) {
		super(plugin, 33);
		// dynmap
		if (plugin.enableDynmapConfigGen()) {
			final GeneratorTemplate gen_tpl = new GeneratorTemplate(plugin, 0);
			gen_tpl.add(33, "run", "Run For Your Life", LEVEL_Y+LEVEL_H+1);
		}
		// generators
		this.gen = this.register(new Gen_033(this, LEVEL_Y, LEVEL_H));
		// listeners
		this.listener_033 = new Listener_033(plugin);
	}



	@Override
	public void register() {
		super.register();
		this.listener_033.register();
	}
	@Override
	public void unregister() {
		super.unregister();
		this.listener_033.unregister();
	}



	@Override
	public int getY(final int level) {
		return LEVEL_Y;
	}
	@Override
	public int getMaxY(final int level) {
		return LEVEL_Y + LEVEL_H;
	}
	@Override
	public boolean containsLevel(final int level) {
		return (level == 33);
	}



	@Override
	protected void generate(final int chunkX, final int chunkZ,
			final ChunkData chunk, final LinkedList<BlockPlotter> plots) {
		this.gen.generate(null, chunk, plots, chunkX, chunkZ);
	}



}
