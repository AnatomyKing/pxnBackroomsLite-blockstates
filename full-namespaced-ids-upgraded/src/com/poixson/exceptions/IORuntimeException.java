package com.poixson.exceptions;

public class IORuntimeException extends RuntimeException {
  private static final long serialVersionUID = 1L;
  
  public IORuntimeException() {}
  
  public IORuntimeException(String msg) {
    super(msg);
  }
  
  public IORuntimeException(String msg, Throwable e) {
    super(msg, e);
  }
  
  public IORuntimeException(Throwable e) {
    super(e);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\exceptions\IORuntimeException.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */