package com.poixson.commonmc.tools.scripts.exceptions;

public class JSFunctionNotFoundException extends RuntimeException {
  private static final long serialVersionUID = 1L;
  
  public JSFunctionNotFoundException(String fileName, String funcName, Object funcObj) {
    super(
        String.format("Function '%s' not found in script '%s' actual: %s", new Object[] { funcName, fileName, funcObj.getClass().toString() }));
  }
  
  public JSFunctionNotFoundException(String fileName, String funcName) {
    super(
        String.format("Function '%s' not found in script '%s'", new Object[] { funcName, fileName }));
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\scripts\exceptions\JSFunctionNotFoundException.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */