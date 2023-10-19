package com.poixson.threadpool;

class null extends xThreadPoolTask {
  null(xThreadPool pool, String taskName) {
    super(pool, taskName);
  }
  
  public void run() {
    xThreadPool.this.log()
      .fine("Thread queue is running..", new Object[0]);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\threadpool\xThreadPool$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */