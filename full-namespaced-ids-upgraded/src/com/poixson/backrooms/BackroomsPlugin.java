package com.poixson.backrooms;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.backrooms.commands.Commands;
import com.poixson.backrooms.dynmap.GeneratorPerspective;
import com.poixson.backrooms.gens.Gen_000;
import com.poixson.backrooms.gens.Gen_001;
import com.poixson.backrooms.gens.Gen_005;
import com.poixson.backrooms.gens.Gen_006;
import com.poixson.backrooms.gens.Gen_019;
import com.poixson.backrooms.gens.Gen_023;
import com.poixson.backrooms.gens.Gen_033;
import com.poixson.backrooms.gens.Gen_037;
import com.poixson.backrooms.gens.Gen_094;
import com.poixson.backrooms.gens.Gen_309;
import com.poixson.backrooms.gens.Gen_771;
import com.poixson.backrooms.listeners.PlayerDamageListener;
import com.poixson.backrooms.tasks.QuoteAnnouncer;
import com.poixson.backrooms.tasks.TaskHourly;
import com.poixson.backrooms.tasks.TeleportManager;
import com.poixson.backrooms.worlds.Level_000;
import com.poixson.backrooms.worlds.Level_033;
import com.poixson.backrooms.worlds.Level_094;
import com.poixson.backrooms.worlds.Level_771;
import com.poixson.commonmc.tools.DelayedChestFiller;
import com.poixson.commonmc.tools.plugin.xJavaPlugin;


public class BackroomsPlugin extends xJavaPlugin {
	@Override public int getSpigotPluginID() { return 108148; }
	@Override public int getBStatsID() {       return 17231;  }
	public static final String LOG_PREFIX  = "[pxnBackrooms] ";
	public static final String CHAT_PREFIX = ChatColor.AQUA + "[Backrooms] " + ChatColor.WHITE;

	public static final String GENERATOR_NAME = "pxnBackrooms";
	protected static final String DEFAULT_RESOURCE_PACK = "https://dl.poixson.com/mcplugins/pxnBackrooms/pxnBackrooms-resourcepack-{VERSION}.zip";
//	protected static final String DEFAULT_RESOURCE_PACK = "https://backrooms.poixson.com/pxnBackrooms-resourcepack.zip";
	protected static final int DEFAULT_SPAWN_DISTANCE = 10000;

	// backrooms levels
	protected final HashMap<Integer, BackroomsLevel> backlevels = new HashMap<Integer, BackroomsLevel>();
	protected final ConcurrentHashMap<UUID, CopyOnWriteArraySet<Integer>> visitLevels = new ConcurrentHashMap<UUID, CopyOnWriteArraySet<Integer>>();

	// hourly task
	protected final AtomicReference<TaskHourly> hourlyTask = new AtomicReference<TaskHourly>(null);

	// quotes
	protected final AtomicReference<QuoteAnnouncer> quoteAnnouncer = new AtomicReference<QuoteAnnouncer>(null);

	// chance to teleport to levels
	protected final AtomicReference<TeleportManager> tpManager = new AtomicReference<TeleportManager>(null);

	// listeners
	protected final AtomicReference<Commands> commands = new AtomicReference<Commands>(null);
	protected final AtomicReference<PlayerDamageListener> playerDamageListener = new AtomicReference<PlayerDamageListener>(null);

	// dynmap config generator
	protected final AtomicReference<GeneratorPerspective> dynmap_perspective = new AtomicReference<GeneratorPerspective>(null);



	public BackroomsPlugin() {
		super(BackroomsPlugin.class);
	}



	@Override
	public void onEnable() {
		super.onEnable();
		// resource pack
		{
			final String pack = Bukkit.getResourcePack();
			if (pack == null || pack.isEmpty()) {
				LOG.warning(String.format(
					"%sResource pack not set; You can use this one: %s",
					LOG_PREFIX,
					DEFAULT_RESOURCE_PACK.replace("{VERSION}", this.getPluginVersion())
				));
			} else {
				LOG.info(String.format(
					"%sUsing resource pack: %s",
					LOG_PREFIX,
					Bukkit.getResourcePack()
				));
			}
		}
		// backrooms levels
		new Level_000(this); // lobby, overgrowth, lights out, basement, hotel, attic, poolrooms, radio station
//		new Level_007(this); // thalassophobia
//		new Level_009(this); // suburbs
//		new Level_010(this); // field of wheat
//		new Level_011(this); // concrete jungle, abandoned office, arcade, ikea
		new Level_033(this); // run for your life
//		new Level_078(this); // space
		new Level_094(this); // motion
//		new Level_151(this); // dollhouse
		new Level_771(this); // crossroads
//		new Level_866(this); // dirtfield
		if (this.enableDynmapConfigGen())
			this.getDynmapPerspective().commit( new File(this.getDataFolder(), "../dynmap/") );
		// create worlds (after server starts)
		(new BukkitRunnable() {
			@Override
			public void run() {
//TODO: this is converting long to string
				final String seed = Long.toString( Bukkit.getWorld("world").getSeed() );
				final Iterator<Entry<Integer, BackroomsLevel>> it = BackroomsPlugin.this.backlevels.entrySet().iterator();
				while (it.hasNext()) {
					final Entry<Integer, BackroomsLevel> entry = it.next();
					final int level = entry.getKey().intValue();
					if (entry.getValue().isWorldMain(level))
						BackroomsLevel.MakeWorld(level, seed);
				}
			}
		}).runTask(this);
		// register levels
		for (final BackroomsLevel level : this.backlevels.values())
			level.register();
		// commands listener
		{
			final Commands listener = new Commands(this);
			final Commands previous = this.commands.getAndSet(listener);
			if (previous != null)
				previous.unregister();
			listener.register();
		}
		// load teleport chance
		this.tpManager.set(TeleportManager.Load(this));
		// load quotes
		this.quoteAnnouncer.set(QuoteAnnouncer.Load(this));
		// hourly task
		{
			final TaskHourly task = new TaskHourly(this);
			final TaskHourly previous = this.hourlyTask.getAndSet(task);
			if (previous != null)
				previous.stop();
			task.start();
		}
		// player damage listener
		{
			final PlayerDamageListener listener = new PlayerDamageListener(this);
			final PlayerDamageListener previous = this.playerDamageListener.getAndSet(listener);
			if (previous != null)
				previous.unregister();
			listener.register();
		}
	}

	@Override
	public void onDisable() {
		super.onDisable();
		// hourly task
		{
			final TaskHourly task = this.hourlyTask.getAndSet(null);
			if (task != null)
				task.stop();
		}
		// finish filling chests
		DelayedChestFiller.stop();
		// unload levels
		for (final BackroomsLevel lvl : this.backlevels.values()) {
			lvl.unregister();
		}
		this.backlevels.clear();
		// commands listener
		{
			final Commands listener = this.commands.getAndSet(null);
			if (listener != null)
				listener.unregister();
		}
		// player damage listener
		{
			final PlayerDamageListener listener = this.playerDamageListener.getAndSet(null);
			if (listener != null)
				listener.unregister();
		}
		// teleport chance
		this.tpManager.set(null);
		this.dynmap_perspective.set(null);
		// quotes
		this.quoteAnnouncer.set(null);
	}



	// -------------------------------------------------------------------------------
	// configs



	@Override
	protected void loadConfigs() {
		this.mkPluginDir();
		// config.yml
		{
			final FileConfiguration cfg = this.getConfig();
			this.config.set(cfg);
			this.configDefaults(cfg);
			cfg.options().copyDefaults(true);
			super.saveConfig();
		}
		// levels-visited.yml
		{
			final File file = new File(this.getDataFolder(), "levels-visited.yml");
			final FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			final Set<String> keys = cfg.getKeys(false);
			for (final String key : keys) {
				final UUID uuid = UUID.fromString(key);
				final CopyOnWriteArraySet<Integer> visited = new CopyOnWriteArraySet<Integer>();
				visited.addAll(cfg.getIntegerList(key));
				this.visitLevels.put(uuid, visited);
			}
		}
	}
	@Override
	protected void saveConfigs() {
		// config.yml
		super.saveConfig();
		// levels-visited.yml
		{
			final File file = new File(this.getDataFolder(), "levels-visited.yml");
			final FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			final Iterator<Entry<UUID, CopyOnWriteArraySet<Integer>>> it = this.visitLevels.entrySet().iterator();
			while (it.hasNext()) {
				final Entry<UUID, CopyOnWriteArraySet<Integer>> entry = it.next();
				final String uuid = entry.getKey().toString();
				final HashSet<Integer> set = new HashSet<Integer>();
				set.addAll(entry.getValue());
				cfg.set(uuid, set);
			}
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	protected void configDefaults(final FileConfiguration cfg) {
		cfg.addDefault("Enable Dynmap Config Gen", Boolean.FALSE);
		cfg.addDefault("Spawn Distance", Integer.valueOf(DEFAULT_SPAWN_DISTANCE));
		Gen_000.ConfigDefaults(cfg); // lobby
		Gen_001.ConfigDefaults(cfg); // basement
//		Gen_002.ConfigDefaults(cfg); // pipe dreams
//		Gen_004.ConfigDefaults(cfg); // abandoned office
		Gen_005.ConfigDefaults(cfg); // hotel
		Gen_006.ConfigDefaults(cfg); // lights out
//		Gen_007.ConfigDefaults(cfg); // thalassophobia
//		Gen_009.ConfigDefaults(cfg); // suburbs
//		Gen_010.ConfigDefaults(cfg); // field of wheat
//		Gen_011.ConfigDefaults(cfg); // concrete jungle
		Gen_019.ConfigDefaults(cfg); // attic
		Gen_023.ConfigDefaults(cfg); // overgrowth
		Gen_033.ConfigDefaults(cfg); // run for your life
//		Gen_036.ConfigDefaults(cfg); // airport
		Gen_037.ConfigDefaults(cfg); // poolrooms
//		Gen_040.ConfigDefaults(cfg); // arcade
//		Gen_078.ConfigDefaults(cfg); // space
		Gen_094.ConfigDefaults(cfg); // motion
//		Gen_151.ConfigDefaults(cfg); // dollhouse
//		Gen_308.ConfigDefaults(cfg); // ikea
		Gen_309.ConfigDefaults(cfg); // radio station
		Gen_771.ConfigDefaults(cfg); // crossroads
//		Gen_866.ConfigDefaults(cfg); // dirtfield
	}



	public boolean enableDynmapConfigGen() {
		return this.config.get().getBoolean("Enable Dynmap Config Gen");
	}



	public int getSpawnDistance() {
		return this.config.get().getInt("Spawn Distance");
	}

	public ConfigurationSection getLevelParams(final int level) {
		return this.config.get()
			.getConfigurationSection(
				String.format("Level%d.Params", Integer.valueOf(level)));
	}
	public ConfigurationSection getLevelBlocks(final int level) {
		return this.config.get()
			.getConfigurationSection(
				String.format("Level%d.Blocks", Integer.valueOf(level)));
	}



	// -------------------------------------------------------------------------------



	public TaskHourly getHourlyTask() {
		return this.hourlyTask.get();
	}
	public TeleportManager getTeleportManager() {
		return this.tpManager.get();
	}
	public QuoteAnnouncer getQuoteAnnouncer() {
		return this.quoteAnnouncer.get();
	}



	// -------------------------------------------------------------------------------
	// levels



	public BackroomsLevel register(final int level, final BackroomsLevel backlevel) {
		this.backlevels.put(Integer.valueOf(level), backlevel);
		return backlevel;
	}
	public int[] getLevels() {
		final int num = this.backlevels.size();
		final int[] levels = new int[num];
		int i = 0;
		for (final Integer lvl : this.backlevels.keySet()) {
			levels[i] = lvl.intValue();
			i++;
		}
		return levels;
	}
	public int getMainLevel(final int level) {
		if (this.backlevels.containsKey(Integer.valueOf(level)))
			return level;
		for (final Entry<Integer, BackroomsLevel> entry : this.backlevels.entrySet()) {
			if (entry.getValue().containsLevel(level))
				return entry.getKey().intValue();
		}
		return Integer.MIN_VALUE;
	}
	public int getPlayerLevel(final UUID uuid) {
		final Player player = Bukkit.getPlayer(uuid);
		return (
			player == null
			? Integer.MIN_VALUE
			: this.getPlayerLevel(player)
		);
	}
	public int getPlayerLevel(final Player player) {
		if (player != null) {
			final int lvl = this.getLevelFromWorld(player.getWorld());
			if (lvl >= 0) {
				final BackroomsLevel backlevel = this.getBackroomsLevel(lvl);
				if (backlevel != null)
					return backlevel.getLevelFromY(player.getLocation().getBlockY());
			}
		}
		return Integer.MIN_VALUE;
	}
	public int getLevelFromWorld(final World world) {
		return (
			world == null
			? Integer.MIN_VALUE
			: this.getLevelFromWorld(world.getName())
		);
	}
	public int getLevelFromWorld(final String worldName) {
		if (worldName != null && !worldName.isEmpty()) {
			if (worldName.startsWith("level")) {
				final int level = Integer.parseInt(worldName.substring(5));
				if (isValidLevel(level))
					return level;
			}
		}
		return Integer.MIN_VALUE;
	}
	public World getWorldFromLevel(final int level) {
		final int lvl = this.getMainLevel(level);
		if (lvl == Integer.MIN_VALUE)
			return null;
		return Bukkit.getWorld("level" + Integer.toString(lvl));
	}



	public void assertValidLevel(final int level) {
		if (!this.isValidLevel(level))
			throw new RuntimeException("Invalid backrooms level: " + Integer.toString(level));
	}
	public boolean isValidLevel(final int level) {
		if (this.isValidWorld(level))
			return true;
		for (final BackroomsLevel backlevel : this.backlevels.values()) {
			if (backlevel.containsLevel(level))
				return true;
		}
		return false;
	}
	public boolean isValidWorld(final int level) {
		return this.backlevels.containsKey(Integer.valueOf(level));
	}
	public boolean isValidWorld(final World world) {
		final int level = this.getLevelFromWorld(world);
		return this.isValidWorld(level);
	}



	public void noclip(final Player player, final int level_to) {
		if (level_to == Integer.MIN_VALUE) {
			this.noclip(player);
			return;
		}
		final TeleportManager manager = this.tpManager.get();
		Location loc = manager.getSpawnLocation(level_to);
		if (loc == null) {
			LOG.warning(LOG_PREFIX + "Failed to find spawn for level: " + Integer.toString(level_to));
			final World world = this.getWorldFromLevel(level_to);
			if (world == null) {
				LOG.warning(LOG_PREFIX + "Unknown backrooms world for level: " + Integer.toString(level_to));
				return;
			}
			loc = world.getSpawnLocation();
		}
		LOG.info(LOG_PREFIX+"No-clip player: "+player.getName()+" to level: "+Integer.toString(level_to));
		player.teleport(loc);
	}
	public int noclip(final Player player) {
		final int level_from = this.getPlayerLevel(player);
		final int level_to = this.noclip(level_from);
		this.noclip(player, level_to);
		return level_to;
	}
	public int noclip(final int level_from) {
		final TeleportManager manager = this.tpManager.get();
		if (manager == null) {
			LOG.warning(LOG_PREFIX+"teleport chance weights not loaded");
			return 0;
		}
		return manager.getDestinationLevel(level_from);
	}



	public BackroomsLevel getBackroomsLevel(final int level) {
		// main level
		{
			final BackroomsLevel backlevel = this.backlevels.get(Integer.valueOf(level));
			if (backlevel != null)
				return backlevel;
		}
		// level in world
		{
			for (final BackroomsLevel backlevel : this.backlevels.values()) {
				if (backlevel.containsLevel(level))
					return backlevel;
			}
		}
		return null;
	}



	public boolean addVisitedLevel(final Player player) {
		return this.addVisitedLevel(player.getUniqueId());
	}
	public boolean addVisitedLevel(final UUID uuid) {
		final int level = this.getPlayerLevel(uuid);
		if (level < 0) return false;
		CopyOnWriteArraySet<Integer> visited = this.visitLevels.get(uuid);
		if (visited == null) {
			visited = new CopyOnWriteArraySet<Integer>();
			this.visitLevels.put(uuid, visited);
		}
		final int sizeLast = visited.size();
		visited.add(Integer.valueOf(level));
		if (visited.size() > sizeLast) {
			// check if visited all levels
			for (final Integer lvl : this.backlevels.keySet()) {
				if (!visited.contains(lvl))
					return false;
			}
			return true;
		}
		return false;
	}



	@Override
	public ChunkGenerator getDefaultWorldGenerator(final String worldName, final String argsStr) {
		if (!worldName.startsWith("level"))
			throw new RuntimeException("Invalid world name, must be level# found: "+worldName);
		LOG.info(String.format("%s%s world: %s", LOG_PREFIX, GENERATOR_NAME, worldName));
		final int level = this.getLevelFromWorld(worldName);
		return this.getBackroomsLevel(level);
	}



	public GeneratorPerspective getDynmapPerspective() {
		// existing
		{
			final GeneratorPerspective gen = this.dynmap_perspective.get();
			if (gen != null)
				return gen;
		}
		// new instance
		{
			final GeneratorPerspective gen = new GeneratorPerspective();
			this.dynmap_perspective.set(gen);
			return gen;
		}
	}



}
