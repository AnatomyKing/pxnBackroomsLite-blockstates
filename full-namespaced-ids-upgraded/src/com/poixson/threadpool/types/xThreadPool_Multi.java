package com.poixson.threadpool.types;

import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.threadpool.xThreadPool;
import com.poixson.threadpool.xThreadPoolWorker;
import com.poixson.utils.NumberUtils;
import com.poixson.utils.ThreadUtils;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public abstract class xThreadPool_Multi extends xThreadPool {
  protected final CopyOnWriteArraySet<xThreadPoolWorker> workers = new CopyOnWriteArraySet<>();
  
  protected final AtomicInteger maxWorkers = new AtomicInteger(0);
  
  protected final AtomicLong workerIndexCount = new AtomicLong(0L);
  
  public xThreadPool_Multi(String poolName) {
    super(poolName);
    this.maxWorkers.set(ThreadUtils.getSystemCoresPlus(1));
  }
  
  public void go() {
    if (!okStart())
      return; 
    if (!this.running.compareAndSet(false, true))
      return; 
    xThreadPoolWorker worker = new xThreadPoolWorker(this, Thread.currentThread(), getPoolName());
    this.workers.add(worker);
    worker.run();
  }
  
  protected void startNewWorkerIfNeededAndAble() {
    if (isStopping())
      return; 
    if (!isRunning())
      return; 
    throw new RuntimeException("UNFINISHED CODE");
  }
  
  public xThreadPoolWorker[] getWorkers() {
    return this.workers.<xThreadPoolWorker>toArray(new xThreadPoolWorker[0]);
  }
  
  protected void stopWorkers() {
    boolean changed = true;
    while (changed) {
      changed = false;
      Iterator<xThreadPoolWorker> it = this.workers.iterator();
      while (it.hasNext()) {
        xThreadPoolWorker worker = it.next();
        if (!worker.isStopping()) {
          worker.stop();
          changed = true;
        } 
      } 
      if (changed)
        ThreadUtils.Sleep(10L); 
    } 
  }
  
  public void join(long timeout) {
    while (!this.workers.isEmpty()) {
      int count = this.workers.size();
      Iterator<xThreadPoolWorker> it = this.workers.iterator();
      while (it.hasNext()) {
        xThreadPoolWorker worker = it.next();
        try {
          if (timeout > 0L) {
            long timot = (long)Math.ceil(timeout / count);
            worker.join(timot);
            continue;
          } 
          worker.join();
        } catch (InterruptedException e) {
          log().trace(e);
        } 
      } 
      if (timeout > 0L)
        break; 
    } 
  }
  
  public void unregisterWorker(xThreadPoolWorker worker) {
    if (worker == null)
      throw new RequiredArgumentException("worker"); 
    if (!this.workers.remove(worker))
      throw new RuntimeException(
          String.format("Cannot unregister worker not owned by pool:", new Object[] { getPoolName(), worker
              .getWorkerName() })); 
  }
  
  public boolean isRunning() {
    if (!super.isRunning())
      return false; 
    return (this.workers.size() > 0);
  }
  
  public int getActiveCount() {
    int count = 0;
    Iterator<xThreadPoolWorker> it = this.workers.iterator();
    while (it.hasNext()) {
      if (((xThreadPoolWorker)it.next()).isActive())
        count++; 
    } 
    return count;
  }
  
  public long getNextWorkerIndex() {
    return this.workerIndexCount
      .incrementAndGet();
  }
  
  public xThreadPoolWorker getCurrentThreadWorker() {
    if (this.workers.isEmpty())
      return null; 
    Thread current = Thread.currentThread();
    Iterator<xThreadPoolWorker> it = this.workers.iterator();
    while (it.hasNext()) {
      xThreadPoolWorker worker = it.next();
      if (worker.isThread(current))
        return worker; 
    } 
    return null;
  }
  
  public boolean isCurrentThread() {
    if (this.workers.isEmpty())
      return false; 
    Thread current = Thread.currentThread();
    Iterator<xThreadPoolWorker> it = this.workers.iterator();
    while (it.hasNext()) {
      xThreadPoolWorker worker = it.next();
      if (worker.isThread(current))
        return true; 
    } 
    return false;
  }
  
  public int getWorkerCount() {
    return this.workers.size();
  }
  
  public int getActiveWorkerCount() {
    return 0;
  }
  
  public int getInactiveWorkerCount() {
    return 0;
  }
  
  public int getMaxWorkers() {
    return this.maxWorkers.get();
  }
  
  public void setMaxWorkers(int maxWorkers) {
    this.maxWorkers.set(
        NumberUtils.MinMax(maxWorkers, 0, 100));
  }
  
  public void setThreadPriority(int priority) {
    super.setThreadPriority(priority);
    ThreadUtils.Sleep(10L);
    Iterator<xThreadPoolWorker> it = this.workers.iterator();
    while (it.hasNext())
      ((xThreadPoolWorker)it.next()).setPriority(priority); 
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\threadpool\types\xThreadPool_Multi.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */