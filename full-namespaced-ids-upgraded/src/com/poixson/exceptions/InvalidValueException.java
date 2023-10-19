package com.poixson.exceptions;

import com.poixson.utils.StringUtils;

public class InvalidValueException extends IllegalArgumentException {
  private static final long serialVersionUID = 1L;
  
  public InvalidValueException(String name) {
    super("Invalid value: " + name);
  }
  
  public InvalidValueException(String name, Object value) {
    super("Invalid value: " + name + " = " + StringUtils.ToString(value));
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\exceptions\InvalidValueException.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */