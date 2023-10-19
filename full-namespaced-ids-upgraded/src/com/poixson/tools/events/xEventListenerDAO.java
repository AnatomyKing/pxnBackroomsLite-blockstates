package com.poixson.tools.events;

import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.logger.xLog;
import com.poixson.threadpool.types.xThreadPool_Main;
import com.poixson.utils.Utils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

public class xEventListenerDAO {
  private static final AtomicLong nextIndex = new AtomicLong(0L);
  
  public final long index;
  
  protected final Object object;
  
  protected final Method method;
  
  public xEventListenerDAO(Object object, Method method) {
    if (object == null)
      throw new RequiredArgumentException("object"); 
    if (method == null)
      throw new RequiredArgumentException("method"); 
    this.index = nextIndex.incrementAndGet();
    this.object = object;
    this.method = method;
  }
  
  public void invoke() {
    if (xThreadPool_Main.Get().proper(this, "invoke", new Object[0]))
      return; 
    xLog.Get()
      .finest("Invoking event: %s->%s", new Object[] { this.object.getClass().getName(), this.method
          .getName() });
    try {
      this.method.invoke(this.object, new Object[0]);
      return;
    } catch (IllegalAccessException illegalAccessException) {
    
    } catch (IllegalArgumentException illegalArgumentException) {
    
    } catch (InvocationTargetException invocationTargetException) {}
    throw new RuntimeException("Method arguments not supported: " + 
        
        this.method
        .getName());
  }
  
  public long getIndex() {
    return this.index;
  }
  
  public boolean isObject(Object object) {
    if (object == null)
      return false; 
    return object.equals(this.object);
  }
  
  public boolean isMethod(Object object, String methodName) {
    if (object == null)
      return false; 
    if (Utils.isEmpty(methodName))
      return false; 
    if (!object.equals(this.object))
      return false; 
    if (!methodName.equals(this.method.getName()))
      return false; 
    return true;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\events\xEventListenerDAO.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */