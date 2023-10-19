package com.poixson.threadpool;

import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.logger.xLog;
import com.poixson.tools.abstractions.xRunnable;
import java.lang.ref.SoftReference;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class xThreadPoolTask extends xRunnable {
  protected final xThreadPool pool;
  
  protected final AtomicReference<xThreadPoolWorker> worker = new AtomicReference<>(null);
  
  protected final AtomicBoolean active = new AtomicBoolean(false);
  
  public final long taskIndex;
  
  protected final AtomicLong runIndex = new AtomicLong(-1L);
  
  protected final AtomicReference<Exception> e = new AtomicReference<>(null);
  
  private final AtomicReference<SoftReference<xLog>> _log;
  
  public xThreadPoolTask(xThreadPool pool, String taskName) {
    this(pool, taskName, (Runnable)null);
  }
  
  public xThreadPoolTask(xThreadPool pool, String taskName, Runnable run) {
    super(taskName, run);
    this._log = new AtomicReference<>(null);
    this.pool = pool;
    this.taskIndex = this.pool.getNextTaskIndex();
  }
  
  public void runTask() {
    if (!this.active.compareAndSet(false, true))
      throw new IllegalStateException("Task already running: " + getTaskName()); 
    log().finest("Running task:", new Object[] { getTaskName() });
    try {
      run();
    } catch (Exception e) {
      setException(e);
    } finally {
      this.active.set(false);
    } 
  }
  
  public boolean isActive() {
    return this.active.get();
  }
  
  public boolean notActive() {
    return !this.active.get();
  }
  
  public int getActive() {
    return isActive() ? 1 : 0;
  }
  
  public xThreadPoolWorker getWorker() {
    xThreadPoolWorker worker = this.worker.get();
    if (worker == null)
      throw new RuntimeException("Worker not registered for task: " + getTaskName()); 
    return worker;
  }
  
  public void setWorker(xThreadPoolWorker worker) {
    if (worker == null)
      throw new RequiredArgumentException("worker"); 
    if (!this.worker.compareAndSet(null, worker) && !worker.equals(this.worker.get()))
      throw new RuntimeException("Task worker already registered: " + ((xThreadPoolWorker)this.worker.get()).getWorkerName()); 
  }
  
  public long getTaskIndex() {
    return this.taskIndex;
  }
  
  public long getRunIndex() {
    return this.runIndex.get();
  }
  
  public void setRunIndex(long index) {
    this.runIndex.set(index);
  }
  
  public Exception e() {
    return this.e.get();
  }
  
  public void setException(Exception e) {
    if (e != null) {
      Exception existing = this.e.getAndSet(e);
      if (existing != null)
        log().trace(existing, "Task has multiple exceptions", new Object[0]); 
    } 
  }
  
  public boolean hasException() {
    return (this.e.get() != null);
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
    xThreadPoolWorker worker = this.worker.get();
    xLog logParent = (worker == null) ? this.pool.log() : worker.log();
    return logParent.getWeak(getTaskName());
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\threadpool\xThreadPoolTask.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */