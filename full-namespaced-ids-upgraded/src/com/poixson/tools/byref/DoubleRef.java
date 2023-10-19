package com.poixson.tools.byref;

import com.poixson.exceptions.RequiredArgumentException;

public class DoubleRef {
  public volatile double value = 0.0D;
  
  public DoubleRef(double value) {
    this.value = value;
  }
  
  public DoubleRef() {}
  
  public void value(double value) {
    this.value = value;
  }
  
  public void value(Double value) {
    if (value == null)
      throw new RequiredArgumentException("value"); 
    this.value = value.doubleValue();
  }
  
  public double value() {
    return this.value;
  }
  
  public void increment() {
    this.value++;
  }
  
  public void decrement() {
    this.value--;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\byref\DoubleRef.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */