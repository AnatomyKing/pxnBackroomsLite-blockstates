package com.poixson.logger;

public interface xLogRecord {
  String toString();
  
  xLevel getLevel();
  
  String[] getNameTree();
  
  long getTimestamp();
  
  boolean isEmpty();
  
  boolean notEmpty();
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\logger\xLogRecord.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */