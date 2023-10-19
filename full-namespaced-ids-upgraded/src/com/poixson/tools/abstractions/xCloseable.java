package com.poixson.tools.abstractions;

import java.io.Closeable;

public interface xCloseable extends Closeable {
  boolean isClosed();
  
  boolean notClosed();
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\abstractions\xCloseable.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */