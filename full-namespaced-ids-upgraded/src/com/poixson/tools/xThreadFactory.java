package com.poixson.tools;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class xThreadFactory implements ThreadFactory {
  protected final String name;
  
  protected final ThreadGroup group;
  
  protected final boolean daemon;
  
  protected final AtomicInteger priority = new AtomicInteger(5);
  
  protected final CopyOnWriteArraySet<Thread> threads = new CopyOnWriteArraySet<>();
  
  protected final AtomicLong threadIndexCount = new AtomicLong(0L);
  
  protected final CoolDown cool = new CoolDown();
  
  public xThreadFactory(String name) {
    this(name, false);
  }
  
  public xThreadFactory(String name, boolean daemon) {
    this(name, null, daemon, 5);
  }
  
  public xThreadFactory(String name, ThreadGroup group, boolean daemon, int priority) {
    this.name = name;
    this.group = group;
    this.daemon = daemon;
    this.priority.set(priority);
    this.cool.setDuration(1000L);
  }
  
  public Thread newThread(Runnable run) {
    cleanupThreads();
    long index = getNextThreadIndex();
    Thread thread = new Thread(this.group, run);
    thread.setPriority(this.priority.get());
    thread.setDaemon(this.daemon);
    thread.setName(this.name + 
        ':' + index);
    return thread;
  }
  
  public void cleanupThreads() {
    if (!this.cool.again())
      return; 
    Iterator<Thread> it = this.threads.iterator();
    while (it.hasNext()) {
      Thread thread = it.next();
      if (!thread.isAlive())
        this.threads.remove(thread); 
    } 
  }
  
  public void setPriority(int value) {
    this.priority.set(value);
    this.group.setMaxPriority(value);
  }
  
  public long getNextThreadIndex() {
    return this.threadIndexCount.incrementAndGet();
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\xThreadFactory.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */