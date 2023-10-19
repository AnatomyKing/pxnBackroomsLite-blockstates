package com.poixson.threadpool.types;

import com.poixson.threadpool.xThreadPool;
import com.poixson.threadpool.xThreadPoolWorker;
import com.poixson.tools.Keeper;
import java.util.concurrent.atomic.AtomicReference;

public class xThreadPool_Main extends xThreadPool_Single {
  public static final String MAIN_POOL_NAME = "main";
  
  protected static final AtomicReference<xThreadPool_Main> instance = new AtomicReference<>(null);
  
  public static xThreadPool_Main Get() {
    if (instance.get() == null) {
      xThreadPool_Main pool = new xThreadPool_Main();
      if (instance.compareAndSet(null, pool)) {
        xThreadPool existing = pools.putIfAbsent("main", pool);
        if (existing != null) {
          xThreadPool_Main main = (xThreadPool_Main)existing;
          instance.set(main);
          return main;
        } 
        return pool;
      } 
    } 
    return instance.get();
  }
  
  public xThreadPool_Main() {
    super("main");
    this.keepOneAlive.set(true);
  }
  
  public void unregisterWorker(xThreadPoolWorker worker) {
    super.unregisterWorker(worker);
    log().fine("Main thread pool has stopped", new Object[0]);
  }
  
  public void stopMain() {
    if (this.stopping.compareAndSet(false, true))
      stopWorkers(); 
    Keeper.remove(this);
  }
  
  public boolean isMainPool() {
    return true;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\threadpool\types\xThreadPool_Main.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */