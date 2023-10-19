package com.poixson.tools.abstractions;

public interface xFailable {
  void fail(Throwable paramThrowable);
  
  void fail(String paramString, Object... paramVarArgs);
  
  void fail(int paramInt, String paramString, Object... paramVarArgs);
  
  boolean hasFailed();
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\abstractions\xFailable.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */