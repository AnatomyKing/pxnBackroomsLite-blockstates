package com.poixson.threadpool;

import com.poixson.threadpool.types.xThreadPool_GUI;
import com.poixson.utils.ThreadUtils;

public class xThreadPoolWorker_GUI extends xThreadPoolWorker {
  public xThreadPoolWorker_GUI(xThreadPool_GUI pool, String poolName) {
    super((xThreadPool)pool, (Thread)null, poolName);
    Thread thread = ThreadUtils.getDispatchThreadSafe();
    if (thread == null)
      throw new RuntimeException("Failed to get dispatch thread"); 
    if (!this.thread.compareAndSet(null, thread))
      throw new RuntimeException("Dispatch thread already set"); 
  }
  
  public void run() {
    if (isStopping())
      return; 
    boolean b = true;
    throw new RuntimeException("UNFINISHED CODE");
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\threadpool\xThreadPoolWorker_GUI.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */