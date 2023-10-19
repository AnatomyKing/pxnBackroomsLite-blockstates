package com.poixson.tools.byref;

import com.poixson.exceptions.RequiredArgumentException;

public class LongRef {
  public volatile long value = 0L;
  
  public LongRef(long value) {
    this.value = value;
  }
  
  public LongRef() {}
  
  public void value(long value) {
    this.value = value;
  }
  
  public void value(Long value) {
    if (value == null)
      throw new RequiredArgumentException("value"); 
    this.value = value.longValue();
  }
  
  public long value() {
    return this.value;
  }
  
  public void increment() {
    this.value++;
  }
  
  public void decrement() {
    this.value--;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\byref\LongRef.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */