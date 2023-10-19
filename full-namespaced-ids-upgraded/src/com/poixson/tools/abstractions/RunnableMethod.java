package com.poixson.tools.abstractions;

import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.utils.ReflectUtils;
import com.poixson.utils.Utils;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

public class RunnableMethod<V> extends xRunnable {
  public final Object container;
  
  public final Method method;
  
  public final Object[] args;
  
  protected final AtomicReference<V> result = new AtomicReference<>(null);
  
  public RunnableMethod(Object container, String methodName, Object... args) {
    if (container == null)
      throw new RequiredArgumentException("container"); 
    if (Utils.isEmpty(methodName))
      throw new RequiredArgumentException("method"); 
    this
      .method = ReflectUtils.getMethodByName(container, methodName, 
        
        ReflectUtils.ArgsToClasses(args));
    this.container = container;
    this.args = args;
  }
  
  public RunnableMethod(Object container, Method method, Object... args) {
    if (container == null)
      throw new RequiredArgumentException("container"); 
    if (method == null)
      throw new RequiredArgumentException("method"); 
    this.container = container;
    this.method = method;
    this.args = args;
  }
  
  public RunnableMethod(String taskName, Object container, String methodName, Object... args) {
    if (container == null)
      throw new RequiredArgumentException("container"); 
    if (Utils.isEmpty(methodName))
      throw new RequiredArgumentException("method"); 
    this
      .method = ReflectUtils.getMethodByName(container, methodName, 
        
        ReflectUtils.ArgsToClasses(args));
    this.container = container;
    this.args = args;
    setTaskName(taskName);
  }
  
  public RunnableMethod(String taskName, Object container, Method method, Object... args) {
    if (container == null)
      throw new RequiredArgumentException("container"); 
    if (method == null)
      throw new RequiredArgumentException("method"); 
    this.container = container;
    this.method = method;
    this.args = args;
    setTaskName(taskName);
  }
  
  public void run() {
    this.runCount.getAndIncrement();
    this.result.set(
        (V)ReflectUtils.InvokeMethod(this.container, this.method, this.args));
  }
  
  public String getFullName() {
    return 
      
      ReflectUtils.GetClassName(this.container) + "->" + 
      this.method
      .getName() + "()";
  }
  
  public V getResult() {
    if (notRun())
      return null; 
    return this.result.get();
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\abstractions\RunnableMethod.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */