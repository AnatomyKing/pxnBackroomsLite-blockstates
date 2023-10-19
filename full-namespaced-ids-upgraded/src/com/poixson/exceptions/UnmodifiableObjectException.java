package com.poixson.exceptions;

import com.poixson.utils.StringUtils;
import java.util.Arrays;
import java.util.Iterator;

public class UnmodifiableObjectException extends UnsupportedOperationException {
  private static final long serialVersionUID = 1L;
  
  private static String BuildMsg() {
    StackTraceElement[] trace = (new Exception()).getStackTrace();
    Iterator<StackTraceElement> it = Arrays.<StackTraceElement>asList(trace).iterator();
    while (it.hasNext()) {
      StackTraceElement e = it.next();
      String className = e.getClassName();
      if (!className.endsWith("UnmodifiableObjectException"))
        return 
          String.format("Object cannot be modified! %s->%s()", new Object[] { StringUtils.LastPart(className, new char[] { '.' }), StringUtils.cTrim(e.getMethodName(), new char[] { '<', '>' }) }); 
    } 
    return null;
  }
  
  public UnmodifiableObjectException() {
    super(BuildMsg());
  }
  
  public UnmodifiableObjectException(String msg) {
    super(msg);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\exceptions\UnmodifiableObjectException.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */