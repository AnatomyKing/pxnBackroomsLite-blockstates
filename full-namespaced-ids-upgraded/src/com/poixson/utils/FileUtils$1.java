package com.poixson.utils;

import java.io.File;
import java.io.FileFilter;

class null implements FileFilter {
  private String[] exts;
  
  public FileFilter init(String[] extens) {
    this.exts = extens;
    return this;
  }
  
  public boolean accept(File path) {
    if (this.exts == null)
      return true; 
    String pathStr = path.toString();
    for (String ext : this.exts) {
      if (pathStr.endsWith(ext))
        return true; 
    } 
    return false;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixso\\utils\FileUtils$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */