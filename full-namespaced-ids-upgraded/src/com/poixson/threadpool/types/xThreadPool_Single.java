package com.poixson.threadpool.types;

import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.threadpool.xThreadPool;
import com.poixson.threadpool.xThreadPoolWorker;
import java.util.concurrent.atomic.AtomicReference;

public abstract class xThreadPool_Single extends xThreadPool {
  protected final AtomicReference<xThreadPoolWorker> worker = new AtomicReference<>(null);
  
  public xThreadPool_Single(String poolName) {
    super(poolName);
  }
  
  public void go() {
    if (!okStart())
      return; 
    if (!this.running.compareAndSet(false, true))
      return; 
    if (this.worker.get() == null) {
      xThreadPoolWorker worker = new xThreadPoolWorker(this, Thread.currentThread(), getPoolName());
      if (this.worker.compareAndSet(null, worker))
        worker.run(); 
    } 
  }
  
  public xThreadPoolWorker[] getWorkers() {
    xThreadPoolWorker worker = this.worker.get();
    if (worker == null)
      return new xThreadPoolWorker[0]; 
    return new xThreadPoolWorker[] { worker };
  }
  
  protected void startNewWorkerIfNeededAndAble() {
    if (isStopping())
      return; 
    if (!isRunning())
      return; 
    if (this.worker.get() == null) {
      xThreadPoolWorker worker = new xThreadPoolWorker(this, (Thread)null, getPoolName());
      if (this.worker.compareAndSet(null, worker)) {
        worker.start();
        worker.waitForStart();
      } 
    } 
  }
  
  protected void stopWorkers() {
    xThreadPoolWorker worker = this.worker.get();
    if (worker != null)
      worker.stop(); 
  }
  
  public void unregisterWorker(xThreadPoolWorker worker) {
    if (worker == null)
      throw new RequiredArgumentException("worker"); 
    if (!this.worker.compareAndSet(worker, null))
      throw new RuntimeException(
          String.format("Cannot unregister worker not owned by pool: ", new Object[] { getPoolName(), worker.getWorkerName() })); 
    worker.stop();
  }
  
  public void join(long timeout) {
    xThreadPoolWorker worker = this.worker.get();
    if (worker == null)
      return; 
    try {
      worker.join(timeout);
    } catch (InterruptedException e) {
      log().trace(e);
    } 
  }
  
  public boolean isRunning() {
    if (!super.isRunning())
      return false; 
    return (this.worker.get() != null);
  }
  
  public int getActiveCount() {
    xThreadPoolWorker worker = this.worker.get();
    if (worker != null && 
      worker.isActive())
      return 1; 
    return 0;
  }
  
  public long getNextWorkerIndex() {
    return 0L;
  }
  
  public xThreadPoolWorker getCurrentThreadWorker() {
    xThreadPoolWorker worker = this.worker.get();
    if (worker != null && 
      worker.isCurrentThread())
      return worker; 
    return null;
  }
  
  public boolean isCurrentThread() {
    xThreadPoolWorker worker = this.worker.get();
    if (worker != null)
      return worker.isCurrentThread(); 
    return false;
  }
  
  public int getWorkerCount() {
    if (this.worker.get() == null)
      return 0; 
    return 1;
  }
  
  public int getActiveWorkerCount() {
    xThreadPoolWorker worker = this.worker.get();
    if (worker == null)
      return 0; 
    if (worker.isActive())
      return 1; 
    return 0;
  }
  
  public int getInactiveWorkerCount() {
    xThreadPoolWorker worker = this.worker.get();
    if (worker == null)
      return 0; 
    if (worker.isActive())
      return 0; 
    return 1;
  }
  
  public int getMaxWorkers() {
    return 1;
  }
  
  public void setMaxWorkers(int maxWorkers) {
    throw new UnsupportedOperationException();
  }
  
  public void setThreadPriority(int priority) {
    super.setThreadPriority(priority);
    xThreadPoolWorker worker = this.worker.get();
    if (worker != null)
      worker.setPriority(priority); 
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\threadpool\types\xThreadPool_Single.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */