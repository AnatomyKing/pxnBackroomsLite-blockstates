package com.poixson.commonmc.tools.scripts.loader;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class ScriptLoader_File implements ScriptLoader {
  protected static final Logger LOG = Logger.getLogger("Minecraft");
  
  protected final JavaPlugin plugin;
  
  protected final String path_local;
  
  protected final String path_resource;
  
  protected final String filename;
  
  protected final AtomicReference<ScriptSourceDAO[]> sources = (AtomicReference)new AtomicReference<>(null);
  
  public ScriptLoader_File(JavaPlugin plugin, String path_local, String path_resource, String filename) {
    this.plugin = plugin;
    this.path_local = path_local;
    this.path_resource = path_resource;
    this.filename = filename;
  }
  
  public void reload() {
    this.sources.set(null);
  }
  
  public String getName() {
    return this.filename;
  }
  
  public boolean hasChanged() {
    ScriptSourceDAO[] sources = this.sources.get();
    if (sources != null)
      for (ScriptSourceDAO src : sources) {
        if (src.hasFileChanged())
          return true; 
      }  
    return false;
  }
  
  public ScriptSourceDAO[] getSources() throws FileNotFoundException {
    ScriptSourceDAO[] sources = this.sources.get();
    if (sources != null)
      return sources; 
    LinkedList<ScriptSourceDAO> list = new LinkedList<>();
    loadSourcesRecursive(list, this.filename);
    ScriptSourceDAO[] arrayOfScriptSourceDAO1 = list.<ScriptSourceDAO>toArray(new ScriptSourceDAO[0]);
    if (this.sources.compareAndSet(null, arrayOfScriptSourceDAO1))
      return arrayOfScriptSourceDAO1; 
    return getSources();
  }
  
  protected void loadSourcesRecursive(LinkedList<ScriptSourceDAO> list, String filename) throws FileNotFoundException {
    ScriptSourceDAO found = ScriptSourceDAO.Find(this.plugin, this.path_local, this.path_resource, filename);
    if (found == null)
      throw new FileNotFoundException(filename); 
    list.add(found);
    if (found.code.startsWith("//#")) {
      String code = found.code;
      while (code.startsWith("//#")) {
        int pos = code.indexOf('\n');
        if (pos == -1) {
          line = code;
          code = "";
        } else {
          line = code.substring(0, pos);
          code = code.substring(pos + 1);
        } 
        String line = line.substring(3).trim();
        if (line.length() == 0)
          continue; 
        pos = line.indexOf('=');
        if (pos == -1) {
          String str = line;
          byte b = -1;
          str.hashCode();
          switch (b) {
          
          } 
          LOG.warning(String.format("%sUnknown statement: %s  in file: %s", new Object[] { "[pxnCommon] ", line, filename }));
          continue;
        } 
        String[] parts = line.split("=", 2);
        String key = parts[0].trim();
        switch (key) {
          case "include":
            loadSourcesRecursive(list, parts[1].trim());
            continue;
        } 
        LOG.warning(String.format("%sUnknown statement: %s  in file: %s", new Object[] { "[pxnCommon] ", line, filename }));
      } 
    } 
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\scripts\loader\ScriptLoader_File.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */