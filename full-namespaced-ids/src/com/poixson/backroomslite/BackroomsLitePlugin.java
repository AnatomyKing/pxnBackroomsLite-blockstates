package com.poixson.backroomslite;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;


public class BackroomsLitePlugin extends JavaPlugin {

    public static final Logger LOG        = Bukkit.getLogger();
    public static final String LOG_PREFIX = "[pxnBackroomsLite] ";
    public static final String CHAT_PREFIX = "" + ChatColor.AQUA + "[Backrooms] " + ChatColor.AQUA;
    protected static final String GENERATOR_NAME = "BackroomsLite";
    protected static final String DEFAULT_RESOURCE_PACK = "http://dl.poixson.com/mcplugins/pxnBackrooms/pxnBackrooms-resourcepack.zip";
    private static BackroomsLitePlugin instance;
    protected Level0Generator generator;

    public static BackroomsLitePlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        this.getConfig();
        this.generator = new Level0Generator();
        String pack = Bukkit.getResourcePack();
        if (pack == null || pack.isEmpty()) {
            LOG.warning("[pxnBackroomsLite] Resource pack not set");
            LOG.warning(
                    "[pxnBackroomsLite] You can use this one: http://dl.poixson.com/mcplugins/pxnBackrooms/pxnBackrooms-resourcepack.zip");
        } else {
            LOG.info(String.format("%sUsing resource pack: %s", new Object[]{"[pxnBackroomsLite] ",

                    Bukkit.getResourcePack()}));
        }
    }

    @Override
    public void onDisable() {
    }


    public ChunkGenerator getDefaultWorldGenerator(String worldName, String argsStr) {
        LOG.info(String.format("%s%s world: %s", new Object[]{"[pxnBackroomsLite] ", "BackroomsLite", worldName}));
        return this.generator;
    }
}
