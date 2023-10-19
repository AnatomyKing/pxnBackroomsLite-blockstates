package com.poixson.tools.abstractions;

import com.poixson.exceptions.RequiredArgumentException;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

public class RunnableMultiplier implements Runnable {
  private final CopyOnWriteArraySet<Runnable> runnables = new CopyOnWriteArraySet<>();
  
  public void add(Runnable run) {
    if (run == null)
      throw new RequiredArgumentException("run"); 
    this.runnables.add(run);
  }
  
  public void remove(Runnable run) {
    if (run == null)
      throw new RequiredArgumentException("run"); 
    this.runnables.remove(run);
  }
  
  public void clear() {
    this.runnables.clear();
  }
  
  public void run() {
    Iterator<Runnable> it = this.runnables.iterator();
    while (it.hasNext()) {
      Runnable run = it.next();
      run.run();
    } 
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\abstractions\RunnableMultiplier.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */