package com.poixson.tools.abstractions;

import com.poixson.exceptions.RequiredArgumentException;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

public class xRunnableMultiplier extends xRunnable {
  protected final CopyOnWriteArraySet<xRunnable> runnables = new CopyOnWriteArraySet<>();
  
  public xRunnableMultiplier() {}
  
  public xRunnableMultiplier(String taskName) {
    super(taskName);
  }
  
  public xRunnableMultiplier(xRunnable run) {
    super(run);
  }
  
  public xRunnableMultiplier(Runnable run) {
    super(run);
  }
  
  public xRunnableMultiplier(String taskName, Runnable run) {
    super(taskName, run);
  }
  
  public void add(xRunnable run) {
    if (run == null)
      throw new RequiredArgumentException("run"); 
    this.runnables.add(run);
  }
  
  public void remove(xRunnable run) {
    if (run == null)
      throw new RequiredArgumentException("run"); 
    this.runnables.remove(run);
  }
  
  public void clear() {
    this.runnables.clear();
  }
  
  public void run() {
    Iterator<xRunnable> it = this.runnables.iterator();
    while (it.hasNext()) {
      xRunnable run = it.next();
      run.run();
    } 
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\abstractions\xRunnableMultiplier.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */