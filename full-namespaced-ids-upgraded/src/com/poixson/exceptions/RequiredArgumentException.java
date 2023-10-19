package com.poixson.exceptions;

public class RequiredArgumentException extends IllegalArgumentException {
  private static final long serialVersionUID = 1L;
  
  public RequiredArgumentException(String argName) {
    super(String.format("%s argument is required", new Object[] { argName }));
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\exceptions\RequiredArgumentException.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */