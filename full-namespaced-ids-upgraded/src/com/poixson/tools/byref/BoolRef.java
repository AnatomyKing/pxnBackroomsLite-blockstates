package com.poixson.tools.byref;

import com.poixson.exceptions.RequiredArgumentException;

public class BoolRef {
  public volatile boolean value = false;
  
  public BoolRef(boolean value) {
    this.value = value;
  }
  
  public BoolRef() {}
  
  public void value(boolean value) {
    this.value = value;
  }
  
  public void value(Boolean value) {
    if (value == null)
      throw new RequiredArgumentException("value"); 
    this.value = value.booleanValue();
  }
  
  public boolean value() {
    return this.value;
  }
  
  public boolean invert() {
    this.value = !this.value;
    return this.value;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\byref\BoolRef.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */