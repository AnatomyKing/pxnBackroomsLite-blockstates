package com.poixson.tools.abstractions;

import com.poixson.tools.xTime;

public interface Lockable {
  xTime lock();
  
  boolean isLocked();
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\abstractions\Lockable.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */