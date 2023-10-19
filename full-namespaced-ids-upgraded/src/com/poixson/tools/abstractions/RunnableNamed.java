package com.poixson.tools.abstractions;

import java.util.concurrent.Callable;

public interface RunnableNamed extends Runnable {
  static String GetName(Runnable run) {
    if (run == null)
      return null; 
    if (run instanceof RunnableNamed)
      return ((RunnableNamed)run).getTaskName(); 
    return null;
  }
  
  static String GetName(Callable<?> call) {
    if (call == null)
      return null; 
    if (call instanceof RunnableNamed)
      return ((RunnableNamed)call).getTaskName(); 
    return null;
  }
  
  String getTaskName();
  
  void setTaskName(String paramString);
  
  boolean equalsTaskName(String paramString);
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\abstractions\RunnableNamed.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */