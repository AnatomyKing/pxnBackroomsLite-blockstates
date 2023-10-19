package com.poixson.tools;

import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.tools.abstractions.xStartable;
import com.poixson.utils.ThreadUtils;
import com.poixson.utils.Utils;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class HangCatcher implements xStartable, Runnable {
  public static final long DEFAULT_TIMEOUT = 10000L;
  
  public static final long DEFAULT_SLEEP = 100L;
  
  protected final AtomicReference<Thread> thread = new AtomicReference<>(null);
  
  protected final String name;
  
  protected final CoolDown timeout;
  
  protected final long sleep;
  
  protected final AtomicBoolean triggered = new AtomicBoolean(false);
  
  protected final AtomicBoolean canceled = new AtomicBoolean(false);
  
  protected final Runnable runWhenHung;
  
  public HangCatcher(Runnable runWhenHung) {
    this(null, runWhenHung);
  }
  
  public HangCatcher(String name, Runnable runWhenHung) {
    this(10000L, 100L, name, runWhenHung);
  }
  
  public HangCatcher(long timeout, long sleep, Runnable runWhenHung) {
    this(timeout, sleep, null, runWhenHung);
  }
  
  public HangCatcher(long timeout, long sleep, String name, Runnable runWhenHung) {
    if (runWhenHung == null)
      throw new RequiredArgumentException("runWhenHung"); 
    this.name = name;
    this
      
      .timeout = new CoolDown((timeout <= 0L) ? 10000L : timeout);
    this
      
      .sleep = (sleep <= 0L) ? 100L : sleep;
    this.runWhenHung = runWhenHung;
  }
  
  public void start() {
    if (hasCanceled())
      return; 
    if (hasTriggered())
      return; 
    resetTimeout();
    if (this.thread.get() != null)
      return; 
    Thread thread = new Thread(this);
    if (!this.thread.compareAndSet(null, thread))
      return; 
    thread.setDaemon(true);
    if (Utils.isEmpty(this.name)) {
      thread.setName("HangCatcher");
    } else {
      thread.setName("HangCatcher" + 
          
          '-' + 
          this.name);
    } 
    thread.start();
  }
  
  public void stop() {
    this.canceled.set(true);
  }
  
  public void run() {
    try {
      while (!this.canceled.get() && 
        !this.triggered.get()) {
        if (this.timeout.again()) {
          trigger();
          break;
        } 
        ThreadUtils.Sleep(this.sleep);
      } 
    } finally {
      this.thread.set(null);
    } 
  }
  
  public void trigger() {
    this.triggered.set(true);
    this.runWhenHung.run();
  }
  
  public boolean isRunning() {
    return (this.thread.get() != null);
  }
  
  public boolean isStopping() {
    return !isRunning();
  }
  
  public boolean hasTriggered() {
    return this.triggered.get();
  }
  
  public boolean hasCanceled() {
    return this.canceled.get();
  }
  
  public void resetTimeout() {
    this.timeout
      .reset();
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\HangCatcher.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */