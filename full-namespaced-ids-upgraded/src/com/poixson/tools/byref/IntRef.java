package com.poixson.tools.byref;

import com.poixson.exceptions.RequiredArgumentException;

public class IntRef {
  public volatile int value = 0;
  
  public IntRef(int value) {
    this.value = value;
  }
  
  public IntRef() {}
  
  public void value(int value) {
    this.value = value;
  }
  
  public void value(Integer value) {
    if (value == null)
      throw new RequiredArgumentException("value"); 
    this.value = value.intValue();
  }
  
  public int value() {
    return this.value;
  }
  
  public void increment() {
    this.value++;
  }
  
  public void decrement() {
    this.value--;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\byref\IntRef.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */