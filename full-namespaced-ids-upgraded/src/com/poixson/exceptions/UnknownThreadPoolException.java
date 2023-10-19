package com.poixson.exceptions;

public class UnknownThreadPoolException extends RuntimeException {
  private static final long serialVersionUID = 1L;
  
  public UnknownThreadPoolException(String poolName) {
    super("Unknown xThreadPool: " + poolName);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\exceptions\UnknownThreadPoolException.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */