package com.poixson.tools;

import java.io.InputStream;
import java.io.PrintStream;

public final class StdIO {
  static {
    Keeper.add(new StdIO());
  }
  
  public static void init() {}
  
  public static final PrintStream OriginalOut = System.out;
  
  public static final PrintStream OriginalErr = System.err;
  
  public static final InputStream OriginalIn = System.in;
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\StdIO.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */