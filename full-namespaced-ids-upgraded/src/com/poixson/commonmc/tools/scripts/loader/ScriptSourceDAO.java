package com.poixson.commonmc.tools.scripts.loader;

import com.poixson.utils.FileUtils;
import com.poixson.utils.Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class ScriptSourceDAO {
  protected static final Logger LOG = Logger.getLogger("Minecraft");
  
  public final String path_local;
  
  public final String path_resource;
  
  public final String filename;
  
  public final boolean isReal;
  
  public final String code;
  
  public final long timestamp;
  
  public static ScriptSourceDAO Find(JavaPlugin plugin, String path_local, String path_resource, String filename) throws FileNotFoundException {
    InputStream in = null;
    boolean isReal = false;
    if (path_local != null) {
      File file = new File(path_local, filename);
      if (file.isFile()) {
        in = new FileInputStream(file);
        isReal = true;
      } 
    } 
    if (path_resource != null) {
      StringBuilder resFile = new StringBuilder();
      if (Utils.notEmpty(path_resource))
        resFile.append(path_resource).append('/'); 
      resFile.append(filename);
      if (in == null)
        in = plugin.getResource(resFile.toString()); 
      if (in == null)
        throw new FileNotFoundException(filename); 
      String code = FileUtils.ReadInputStream(in);
      Utils.SafeClose(in);
      ScriptSourceDAO dao = new ScriptSourceDAO(isReal, path_local, path_resource, filename, code);
      LOG.info(String.format("%sLoaded %s script: %s", new Object[] { "[pxnCommon] ", isReal ? "local" : "resource", filename }));
      return dao;
    } 
    throw new FileNotFoundException(filename);
  }
  
  public ScriptSourceDAO(boolean isReal, String path_local, String path_resource, String filename, String code) {
    this.isReal = isReal;
    this.path_local = path_local;
    this.path_resource = path_resource;
    this.filename = filename;
    this.code = code;
    this.timestamp = Utils.GetMS();
  }
  
  public boolean hasFileChanged() {
    if (!this.isReal)
      return false; 
    File file = new File(this.path_local, this.filename);
    try {
      long last = FileUtils.GetLastModified(file) * 1000L;
      return (last > this.timestamp);
    } catch (IOException iOException) {
      return false;
    } 
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\scripts\loader\ScriptSourceDAO.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */