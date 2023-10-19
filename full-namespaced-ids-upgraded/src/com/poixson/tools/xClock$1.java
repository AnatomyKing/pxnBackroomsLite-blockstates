package com.poixson.tools;

class null extends Thread {
  private volatile boolean blocking = false;
  
  public Thread init(boolean blocking) {
    this.blocking = blocking;
    return this;
  }
  
  public void run() {
    xClock.this
      .doUpdate(this.blocking);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\xClock$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */