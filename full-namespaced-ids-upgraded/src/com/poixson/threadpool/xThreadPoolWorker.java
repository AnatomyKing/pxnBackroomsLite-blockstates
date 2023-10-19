package com.poixson.threadpool;

import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.logger.xLog;
import com.poixson.tools.CoolDown;
import com.poixson.tools.abstractions.xStartable;
import com.poixson.utils.ThreadUtils;
import com.poixson.utils.Utils;
import java.lang.ref.SoftReference;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class xThreadPoolWorker implements xStartable, Runnable {
  public static final long WORKER_START_TIMEOUT = 500L;
  
  protected final xThreadPool pool;
  
  protected final AtomicReference<Thread> thread = new AtomicReference<>(null);
  
  protected final long workerIndex;
  
  protected final String workerName;
  
  protected final AtomicReference<String> workerNameCached = new AtomicReference<>(null);
  
  protected final AtomicBoolean running = new AtomicBoolean(false);
  
  protected final AtomicBoolean active = new AtomicBoolean(false);
  
  protected final AtomicBoolean stopping = new AtomicBoolean(false);
  
  protected final AtomicBoolean keepAlive = new AtomicBoolean(false);
  
  protected final AtomicLong runCount = new AtomicLong(0L);
  
  private final AtomicReference<SoftReference<xLog>> _log;
  
  public void start() {
    if (isRunning())
      return; 
    try {
      Thread thread = getThread();
      thread.start();
    } catch (IllegalThreadStateException illegalThreadStateException) {}
  }
  
  public void waitForStart() {
    waitForStart(500L);
  }
  
  public void waitForStart(long timeout) {
    long sleep = 0L;
    CoolDown cool = null;
    while (!isRunning()) {
      if (cool == null) {
        cool = new CoolDown(timeout);
        cool.reset();
      } else if (cool.again()) {
        log().warning("Timeout waiting for thread pool to start", new Object[0]);
        break;
      } 
      sleep += 5L;
      ThreadUtils.Sleep(sleep);
    } 
  }
  
  public void stop() {
    this.stopping.set(true);
  }
  
  public void run() {
    if (isStopping())
      return; 
    if (!this.running.compareAndSet(false, true))
      throw new RuntimeException("Thread pool worker already running: " + getWorkerName()); 
    if (isStopping()) {
      this.running.set(false);
      return;
    } 
    if (this.thread.get() == null) {
      Thread thread = Thread.currentThread();
      if (this.thread.compareAndSet(null, thread))
        configureThread(thread); 
    } 
    if (!((Thread)this.thread.get()).equals(Thread.currentThread()))
      throw new IllegalStateException("Invalid thread state!"); 
    try {
      while (!isStopping()) {
        xThreadPoolTask task;
        try {
          task = this.pool.grabNextTask();
        } catch (InterruptedException ignore) {
          continue;
        } 
        if (task != null) {
          long runIndex = this.runCount.incrementAndGet();
          runTask(task, runIndex);
          continue;
        } 
        log().detail("Idle thread..", new Object[0]);
      } 
    } finally {
      this.stopping.set(true);
      this.running.set(false);
      this.pool.unregisterWorker(this);
    } 
  }
  
  protected void runTask(xThreadPoolTask task, long runIndex) {
    if (task == null)
      throw new RequiredArgumentException("task"); 
    this.active.set(true);
    try {
      task.setWorker(this);
      task.setRunIndex(runIndex);
      task.run();
    } finally {
      this.active.set(false);
    } 
    if (task.hasException())
      log().trace(task.e()); 
  }
  
  protected Thread getThread() {
    if (this.thread.get() == null) {
      Thread thread = newThread();
      if (this.thread.compareAndSet(null, thread)) {
        log().finer("New worker thread..", new Object[0]);
        configureThread(thread);
        return thread;
      } 
    } 
    return this.thread.get();
  }
  
  protected Thread newThread() {
    return new Thread(this);
  }
  
  public void setThread(Thread thread) {
    if (!this.thread.compareAndSet(null, thread)) {
      String threadName = ((Thread)this.thread.get()).getName();
      throw new RuntimeException("Worker thread already set: " + threadName);
    } 
  }
  
  public void configureThread(Thread thread) {
    try {
      thread.setName(getWorkerName());
    } catch (Exception exception) {}
    try {
      thread.setDaemon(false);
    } catch (Exception exception) {}
    try {
      thread.setPriority(this.pool.getThreadPriority());
    } catch (Exception exception) {}
  }
  
  public void join(long timeout) throws InterruptedException {
    Thread thread = this.thread.get();
    if (thread == null)
      return; 
    if (timeout > 0L) {
      thread.join(timeout);
    } else {
      thread.join();
    } 
  }
  
  public void join() throws InterruptedException {
    join(0L);
  }
  
  public boolean isRunning() {
    return this.running.get();
  }
  
  public boolean isActive() {
    return this.active.get();
  }
  
  public boolean isStopping() {
    if (this.pool.isStopping())
      return true; 
    return this.stopping.get();
  }
  
  public boolean isThread(Thread match) {
    Thread thread = this.thread.get();
    if (thread == null)
      return false; 
    return thread.equals(match);
  }
  
  public boolean isCurrentThread() {
    Thread thread = this.thread.get();
    if (thread == null)
      return false; 
    return thread.equals(Thread.currentThread());
  }
  
  public long getWorkerIndex() {
    return this.workerIndex;
  }
  
  public long getRunCount() {
    return this.runCount.get();
  }
  
  public String getWorkerName() {
    if (Utils.notEmpty(this.workerName))
      return this.workerName; 
    if (this.workerNameCached.get() == null) {
      String name = String.format("%s-w%d", new Object[] { this.pool.getPoolName(), 
            Long.valueOf(this.workerIndex) });
      this.workerNameCached.set(name);
    } 
    return this.workerNameCached.get();
  }
  
  public void setPriority(int priority) {
    Thread thread = this.thread.get();
    if (thread != null)
      thread.setPriority(priority); 
  }
  
  public boolean isKeepAlive() {
    return this.keepAlive.get();
  }
  
  public void setKeepAlive(boolean enable) {
    this.keepAlive.set(enable);
  }
  
  public xThreadPoolWorker(xThreadPool pool, Thread thread, String workerName) {
    this._log = new AtomicReference<>(null);
    if (pool == null)
      throw new RequiredArgumentException("pool"); 
    this.pool = pool;
    this.workerIndex = pool.getNextWorkerIndex();
    this.workerName = workerName;
    if (thread != null) {
      this.thread.set(thread);
      configureThread(thread);
    } 
  }
  
  public xLog log() {
    SoftReference<xLog> ref = this._log.get();
    if (ref != null) {
      xLog xLog = ref.get();
      if (xLog != null)
        return xLog; 
    } 
    xLog log = _log();
    SoftReference<xLog> softReference1 = new SoftReference<>(log);
    if (this._log.compareAndSet(null, softReference1))
      return log; 
    return log();
  }
  
  protected xLog _log() {
    String poolName = this.pool.poolName;
    String workerName = getWorkerName();
    StringBuilder name = new StringBuilder();
    name.append("pool:").append(poolName);
    if (!workerName.isEmpty() && 
      !workerName.equalsIgnoreCase(poolName))
      name.append(":").append(workerName); 
    return xLog.Get(name.toString());
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\threadpool\xThreadPoolWorker.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */