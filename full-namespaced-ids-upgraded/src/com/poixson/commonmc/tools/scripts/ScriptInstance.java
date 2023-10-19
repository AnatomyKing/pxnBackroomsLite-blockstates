package com.poixson.commonmc.tools.scripts;

import com.poixson.commonmc.tools.scripts.exceptions.JSFunctionNotFoundException;
import com.poixson.commonmc.tools.scripts.loader.ScriptLoader;
import com.poixson.tools.xTime;
import com.poixson.utils.Utils;
import java.io.Closeable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;

public class ScriptInstance implements Closeable {
  protected final CraftScript craftscript;
  
  protected final ScriptLoader loader;
  
  protected final Script[] compiled;
  
  protected final Scriptable scope;
  
  protected final long STALE_TIMEOUT = xTime.ParseToLong("5m");
  
  protected final AtomicLong lastUsed = new AtomicLong(0L);
  
  protected final AtomicBoolean crashed = new AtomicBoolean(false);
  
  public ScriptInstance(CraftScript craftscript, ScriptLoader loader, Scriptable scope, Script[] compiled) {
    this.craftscript = craftscript;
    this.loader = loader;
    this.compiled = compiled;
    Context context = Context.enter();
    try {
      this.scope = context.newObject(scope);
      this.scope.setPrototype(scope);
      this.scope.setParentScope(null);
    } finally {
      Utils.SafeClose((Closeable)context);
    } 
    resetLastUsed();
  }
  
  public void close() {
    setCrashed();
  }
  
  public Object run() {
    if (isCrashed())
      return null; 
    resetLastUsed();
    Context context = Context.enter();
    Object result = null;
    try {
      for (Script src : this.compiled) {
        Object res = src.exec(context, this.scope);
        if (res != null && 
          result == null)
          result = res; 
        resetLastUsed();
      } 
      return result;
    } catch (Exception e) {
      setCrashed();
      e.printStackTrace();
      return e;
    } finally {
      Utils.SafeClose((Closeable)context);
    } 
  }
  
  public Object call(String funcName, Object... args) {
    if (isCrashed())
      return null; 
    if (Utils.isEmpty(funcName))
      throw new RuntimeException("Cannot call function, no name provided"); 
    resetLastUsed();
    Context context = Context.enter();
    try {
      Object funcObj = this.scope.get(funcName, this.scope);
      if (funcObj == null)
        throw new JSFunctionNotFoundException(this.loader.getName(), funcName, funcObj); 
      Function func = (Function)funcObj;
      return func.call(context, this.scope, this.scope, args);
    } catch (Exception e) {
      setCrashed();
      e.printStackTrace();
      return e;
    } finally {
      Utils.SafeClose((Closeable)context);
    } 
  }
  
  public void resetLastUsed() {
    this.lastUsed.set(Utils.GetMS());
  }
  
  public boolean isStale() {
    return isStale(Utils.GetMS());
  }
  
  public boolean isStale(long time) {
    long since = getSinceLastUsed(time);
    if (since == -1L)
      return false; 
    return (since > this.STALE_TIMEOUT);
  }
  
  public long getSinceLastUsed() {
    return getSinceLastUsed(Utils.GetMS());
  }
  
  public long getSinceLastUsed(long time) {
    long last = this.lastUsed.get();
    if (last <= 0L)
      return -1L; 
    return time - last;
  }
  
  public boolean isCrashed() {
    return this.crashed.get();
  }
  
  public boolean setCrashed() {
    return setCrashed(true);
  }
  
  public boolean setCrashed(boolean crashed) {
    return this.crashed.getAndSet(crashed);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\scripts\ScriptInstance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */