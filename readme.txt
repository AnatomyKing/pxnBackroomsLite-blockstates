pxnBackroomsLite - Backrooms world generator for minecraft (level 0)


This is the Lite version of a much larger plugin pxnBackrooms. The full plugin has 16 different levels and adding more. It's a big project with a lot to do, so here's a lite version of the plugin. This pxnBackroomsLite plugin contains only level 0 the lobby, no other levels or mechanics of the full plugin are included.

To use this world generator, just edit your multiverse worlds.yml file and set generator: pxnBackroomsLite

After setting the generator for the world, you may need to delete the files in backrooms/region/ if any vanilla chunks have already been generated.


Requires Multiverse and pxnCommonMC plugin:
https://dev.bukkit.org/projects/pxncommonpluginmc
or https://www.spigotmc.org/resources/pxncommonpluginmc.107049/

Discord: https://discord.gg/jHgP2a3kDx
Dev Builds: https://dl.poixson.com/mcplugins/pxnBackroomsLite/

config example:

full-namespaced-ids:

lobby:
  walls:
    material: "minecraft:note_block[instrument=basedrum,note=16,powered=false]"
  floor:
    material: "minecraft:note_block[instrument=basedrum,note=17,powered=false]"
  lamp:
    material: "minecraft:note_block[instrument=basedrum,note=18,powered=false]"


raw-materials:

lobby:
  walls:
    material: "SMOOTH_SANDSTONE"
  floor:
    material: "BROWN_WOOL"
  lamp:
    material: "REDSTONE_LAMP"
