package com.poixson.commonmc.tools.scripts.loader;

import java.io.FileNotFoundException;

public interface ScriptLoader {
  boolean hasChanged();
  
  void reload();
  
  String getName();
  
  ScriptSourceDAO[] getSources() throws FileNotFoundException;
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\scripts\loader\ScriptLoader.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */