package com.poixson.tools.abstractions;

import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.utils.StringUtils;
import com.poixson.utils.Utils;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class xRunnable implements RunnableNamed {
  public final Runnable task;
  
  protected final AtomicReference<String> taskName = new AtomicReference<>(null);
  
  protected final AtomicLong runCount = new AtomicLong(0L);
  
  protected final AtomicInteger active = new AtomicInteger(0);
  
  public xRunnable() {
    this(null, null);
  }
  
  public xRunnable(String taskName) {
    this(taskName, null);
  }
  
  public xRunnable(xRunnable run) {
    this(run.getTaskName(), run);
  }
  
  public xRunnable(Runnable run) {
    this(null, run);
  }
  
  public xRunnable(String taskName, Runnable run) {
    if (Utils.notEmpty(taskName)) {
      this.taskName.set(taskName);
    } else if (run instanceof RunnableNamed) {
      this.taskName.set(((RunnableNamed)run)
          .getTaskName());
    } 
    this.task = run;
  }
  
  public static xRunnable cast(Runnable run) {
    if (run == null)
      return null; 
    if (run instanceof xRunnable)
      return (xRunnable)run; 
    if (run instanceof RunnableNamed)
      return new xRunnable(((RunnableNamed)run)
          
          .getTaskName(), run); 
    return new xRunnable(run);
  }
  
  public Callable<?> toCallable() {
    return Executors.callable(this);
  }
  
  public void run() {
    Runnable task = this.task;
    if (task == null)
      throw new RequiredArgumentException("task"); 
    this.runCount.getAndIncrement();
    this.active.getAndIncrement();
    try {
      task.run();
    } finally {
      this.active.getAndDecrement();
    } 
  }
  
  public void runOnce() {
    Runnable task = this.task;
    if (task == null)
      throw new RequiredArgumentException("task"); 
    if (!this.runCount.compareAndSet(0L, 1L))
      return; 
    this.active.getAndIncrement();
    try {
      task.run();
    } finally {
      this.active.getAndDecrement();
    } 
  }
  
  public String getTaskName() {
    if (this.task != null && 
      this.task instanceof RunnableNamed) {
      String taskName = ((RunnableNamed)this.task).getTaskName();
      if (Utils.notEmpty(taskName))
        return taskName; 
    } 
    return this.taskName.get();
  }
  
  public void setTaskName(String taskName) {
    this.taskName.set(Utils.ifEmpty(taskName, null));
  }
  
  public boolean equalsTaskName(String matchName) {
    return StringUtils.MatchStringExact(getTaskName(), matchName);
  }
  
  public boolean hasRun() {
    return (this.runCount.get() > 0L);
  }
  
  public boolean notRun() {
    return (this.runCount.get() == 0L);
  }
  
  public boolean isActive() {
    return (this.active.get() > 0);
  }
  
  public boolean notActive() {
    return (this.active.get() == 0);
  }
  
  public int getActive() {
    return this.active.get();
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\abstractions\xRunnable.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */