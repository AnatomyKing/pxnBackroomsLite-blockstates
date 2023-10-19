package com.poixson.tools.abstractions;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

public class xCallable<V> extends xRunnable implements Callable<V> {
  public final Callable<V> call;
  
  protected final AtomicReference<V> result = new AtomicReference<>(null);
  
  protected final AtomicReference<Exception> ex = new AtomicReference<>(null);
  
  protected final ThreadLocal<Boolean> callDepth = new ThreadLocal<>();
  
  public xCallable() {
    this.call = null;
  }
  
  public xCallable(String taskName) {
    this((V)null, (Runnable)null, (Callable<V>)null);
    this.taskName.set(taskName);
  }
  
  public xCallable(V result) {
    this(result, (Runnable)null, (Callable<V>)null);
  }
  
  public xCallable(Runnable run) {
    this((V)null, run, (Callable<V>)null);
  }
  
  public xCallable(Callable<V> call) {
    this((V)null, (Runnable)null, call);
  }
  
  public xCallable(V result, Runnable run) {
    this(result, run, (Callable<V>)null);
  }
  
  public xCallable(V result, Callable<V> call) {
    this(result, (Runnable)null, call);
  }
  
  protected xCallable(V result, Runnable run, Callable<V> call) {
    super(run);
    if (run != null && call != null)
      throw new IllegalArgumentException("Cannot set runnable and callable at the same time!"); 
    this.call = call;
    this.result.set(result);
  }
  
  @Deprecated
  public void finalize() throws Throwable {
    super.finalize();
    releaseCallDepth();
  }
  
  public void run() {
    if (this.task != null) {
      try {
        this.task.run();
      } catch (Exception e) {
        this.result.set(null);
        this.ex.set(e);
      } 
      return;
    } 
    try {
      checkCallDepth();
      this.result.set(
          call());
    } catch (Exception e) {
      this.result.set(null);
      this.ex.set(e);
    } finally {
      releaseCallDepth();
    } 
  }
  
  public V call() {
    if (this.call != null) {
      try {
        this.result.set(
            call());
      } catch (Exception e) {
        this.result.set(null);
        this.ex.set(e);
        return null;
      } 
      return this.result.get();
    } 
    try {
      checkCallDepth();
      run();
    } catch (Exception e) {
      this.result.set(null);
      this.ex.set(e);
      return null;
    } finally {
      releaseCallDepth();
    } 
    return this.result.get();
  }
  
  private void checkCallDepth() {
    Boolean depth = this.callDepth.get();
    if (depth == null) {
      this.callDepth.set(Boolean.TRUE);
      return;
    } 
    if (depth.booleanValue())
      throw new UnsupportedOperationException("Must set or override run() or call()"); 
    this.callDepth.set(Boolean.TRUE);
  }
  
  private void releaseCallDepth() {
    this.callDepth.remove();
  }
  
  public V getResult() {
    return this.result.get();
  }
  
  public void setResult(V result) {
    this.result.set(result);
  }
  
  public Exception getException() {
    return this.ex.get();
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\abstractions\xCallable.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */