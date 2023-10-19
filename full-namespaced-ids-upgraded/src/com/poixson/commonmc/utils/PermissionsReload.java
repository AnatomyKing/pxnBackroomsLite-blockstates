package com.poixson.commonmc.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public final class PermissionsReload {
  public static void Reload() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    Server server = Bukkit.getServer();
    PluginManager pm = Bukkit.getPluginManager();
    Field field = pm.getClass().getDeclaredField("permissions");
    field.setAccessible(true);
    field.set(pm, new HashMap<>());
    Method meth = server.getClass().getDeclaredMethod("loadCustomPermissions", new Class[0]);
    meth.setAccessible(true);
    meth.invoke(server, new Object[0]);
    for (Plugin plugin : pm.getPlugins()) {
      for (Permission perm : plugin.getDescription().getPermissions()) {
        try {
          pm.addPermission(perm);
        } catch (IllegalArgumentException e) {
          e.printStackTrace();
        } 
      } 
    } 
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonm\\utils\PermissionsReload.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */