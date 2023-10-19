package com.poixson.tools.abstractions;

import java.io.IOException;

public interface xOpenable extends xCloseable {
  void open() throws IOException;
  
  boolean isOpen();
  
  boolean notOpen();
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\abstractions\xOpenable.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */