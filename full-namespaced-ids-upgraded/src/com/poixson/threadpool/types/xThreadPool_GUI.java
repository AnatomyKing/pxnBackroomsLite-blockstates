package com.poixson.threadpool.types;

import com.poixson.threadpool.xThreadPool;
import com.poixson.threadpool.xThreadPoolWorker;
import com.poixson.threadpool.xThreadPoolWorker_GUI;
import java.awt.EventQueue;
import java.util.concurrent.atomic.AtomicReference;

public class xThreadPool_GUI extends xThreadPool_Single {
  public static final String DISPATCH_POOL_NAME = "gui";
  
  private static final AtomicReference<xThreadPool_GUI> instance = new AtomicReference<>(null);
  
  public static xThreadPool_GUI Get() {
    if (instance.get() == null) {
      xThreadPool_GUI pool = new xThreadPool_GUI();
      if (instance.compareAndSet(null, pool)) {
        xThreadPool existing = pools.putIfAbsent("gui", pool);
        if (existing != null) {
          xThreadPool_GUI gui = (xThreadPool_GUI)existing;
          instance.set(gui);
          return gui;
        } 
        return pool;
      } 
    } 
    return instance.get();
  }
  
  protected xThreadPool_GUI() {
    super("gui");
    this.keepOneAlive.set(true);
  }
  
  public void start() {
    throw new UnsupportedOperationException();
  }
  
  public void go() {
    throw new UnsupportedOperationException();
  }
  
  public void stop() {
    throw new UnsupportedOperationException();
  }
  
  protected void startNewWorkerIfNeededAndAble() {
    EventQueue.invokeLater((Runnable)
        getWorker());
  }
  
  protected final xThreadPoolWorker getWorker() {
    if (this.worker.get() == null) {
      xThreadPoolWorker_GUI xThreadPoolWorker_GUI = new xThreadPoolWorker_GUI(this, getPoolName());
      if (this.worker.compareAndSet(null, xThreadPoolWorker_GUI))
        return (xThreadPoolWorker)xThreadPoolWorker_GUI; 
    } 
    return this.worker.get();
  }
  
  public boolean isRunning() {
    return true;
  }
  
  public boolean isGraphicsPool() {
    return true;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\threadpool\types\xThreadPool_GUI.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */