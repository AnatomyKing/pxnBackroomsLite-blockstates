package com.poixson.logger;

import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.tools.StdIO;
import com.poixson.utils.ReflectUtils;
import com.poixson.utils.Utils;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReference;

public abstract class xLog {
  public static final xLevel DEFAULT_LEVEL = xLevel.ALL;
  
  protected static final AtomicReference<xLog> root = new AtomicReference<>(null);
  
  protected final ConcurrentHashMap<String, SoftReference<xLog>> loggers = new ConcurrentHashMap<>();
  
  public final xLog parent;
  
  public final String logName;
  
  protected final AtomicReference<xLevel> level = new AtomicReference<>(null);
  
  protected final CopyOnWriteArraySet<xLogHandler> handlers = new CopyOnWriteArraySet<>();
  
  protected final AtomicReference<SoftReference<String[]>> cachedNameTree = new AtomicReference<>(null);
  
  public static void init() {
    ReflectUtils.GetClass("com.poixson.logger.xLogRoot");
  }
  
  public static xLog Get() {
    return root.get();
  }
  
  public static xLog Get(String logName) {
    xLog root = Get();
    if (root == null)
      throw new RuntimeException("Root logger not initialized"); 
    return root.get(logName);
  }
  
  public xLog get(String logName) {
    if (Utils.isEmpty(logName))
      return Get(); 
    SoftReference<xLog> ref = this.loggers.get(logName);
    if (ref != null) {
      xLog xLog1 = ref.get();
      if (xLog1 != null)
        return xLog1; 
    } 
    xLog log = ((this.parent == null) ? Get() : this.parent).create(logName);
    SoftReference<xLog> softReference1 = new SoftReference<>(log);
    SoftReference<xLog> existing = this.loggers.putIfAbsent(logName, softReference1);
    if (existing != null) {
      xLog lg = existing.get();
      if (lg != null)
        return lg; 
    } 
    return log;
  }
  
  public xLog getWeak(String logName) {
    if (Utils.isEmpty(logName))
      return clone(); 
    return create(logName);
  }
  
  public xLog clone() {
    return create(this.logName);
  }
  
  protected xLog() {
    StdIO.init();
    this.parent = null;
    this.logName = null;
  }
  
  protected xLog(xLog parent, String logName) {
    if (parent == null)
      throw new RequiredArgumentException("parent"); 
    if (Utils.isEmpty(logName))
      throw new RequiredArgumentException("logName"); 
    this.parent = parent;
    this.logName = logName;
  }
  
  public xLevel getLevel() {
    xLevel level = this.level.get();
    if (level == null)
      return DEFAULT_LEVEL; 
    return level;
  }
  
  public void setLevel(xLevel level) {
    this.level.set(level);
  }
  
  public boolean isLoggable(xLevel level) {
    if (level == null)
      return true; 
    xLevel current = getLevel();
    if (current != null && 
      current.notLoggable(level))
      return false; 
    if (this.parent != null) {
      boolean loggable = this.parent.isLoggable(level);
      return loggable;
    } 
    return true;
  }
  
  public boolean notLoggable(xLevel level) {
    return !isLoggable(level);
  }
  
  public boolean isRoot() {
    return false;
  }
  
  public String[] getNameTree() {
    if (isRoot())
      return new String[0]; 
    SoftReference<String[]> soft = this.cachedNameTree.get();
    if (soft != null) {
      String[] arrayOfString = soft.get();
      if (arrayOfString != null)
        return arrayOfString; 
    } 
    LinkedList<String> list = new LinkedList<>();
    buildNameTree(list);
    String[] result = list.<String>toArray(new String[0]);
    this.cachedNameTree.set((SoftReference)new SoftReference<>(result));
    return result;
  }
  
  protected void buildNameTree(LinkedList<String> list) {
    if (isRoot())
      return; 
    if (this.parent == null)
      return; 
    this.parent.buildNameTree(list);
    if (Utils.notEmpty(this.logName))
      list.add(this.logName); 
  }
  
  public xLogHandler[] getHandlers() {
    return this.handlers.<xLogHandler>toArray(new xLogHandler[0]);
  }
  
  public void addHandler(xLogHandler handler) {
    if (handler == null)
      throw new RequiredArgumentException("handler"); 
    this.handlers.add(handler);
  }
  
  public void replaceHandler(Class<? extends xLogHandler> remove, xLogHandler add) {
    if (remove == null)
      throw new RequiredArgumentException("remove"); 
    if (add == null)
      throw new RequiredArgumentException("add"); 
    List<xLogHandler> removing = new ArrayList<>();
    Iterator<xLogHandler> it = this.handlers.iterator();
    while (it.hasNext()) {
      xLogHandler h = it.next();
      if (h.getClass().isInstance(remove))
        removing.add(h); 
    } 
    for (xLogHandler h : removing)
      this.handlers.remove(h); 
    this.handlers.add(add);
  }
  
  public void replaceHandler(xLogHandler remove, xLogHandler add) {
    if (remove == null)
      throw new RequiredArgumentException("remove"); 
    if (add == null)
      throw new RequiredArgumentException("add"); 
    this.handlers.remove(remove);
    this.handlers.add(add);
  }
  
  public int getHandlerCount() {
    return this.handlers.size();
  }
  
  public boolean hasHandler(Class<? extends xLogHandler> clss) {
    for (xLogHandler h : this.handlers) {
      if (h.getClass().isInstance(clss))
        return true; 
    } 
    return false;
  }
  
  public boolean hasHandler() {
    return !this.handlers.isEmpty();
  }
  
  public void publish() {
    publish((xLogRecord)null);
  }
  
  protected abstract xLog create(String paramString);
  
  public abstract void flush();
  
  public abstract void clearScreen();
  
  public abstract void beep();
  
  public abstract void publish(String paramString);
  
  public abstract void publish(String[] paramArrayOfString);
  
  public abstract void publish(xLogRecord paramxLogRecord);
  
  public abstract void stdout(String paramString);
  
  public abstract void stderr(String paramString);
  
  public abstract void trace(Throwable paramThrowable);
  
  public abstract void trace(Throwable paramThrowable, String paramString, Object... paramVarArgs);
  
  public abstract void title(String paramString, Object... paramVarArgs);
  
  public abstract void detail(String paramString, Object... paramVarArgs);
  
  public abstract void finest(String paramString, Object... paramVarArgs);
  
  public abstract void finer(String paramString, Object... paramVarArgs);
  
  public abstract void fine(String paramString, Object... paramVarArgs);
  
  public abstract void stats(String paramString, Object... paramVarArgs);
  
  public abstract void info(String paramString, Object... paramVarArgs);
  
  public abstract void warning(String paramString, Object... paramVarArgs);
  
  public abstract void notice(String paramString, Object... paramVarArgs);
  
  public abstract void severe(String paramString, Object... paramVarArgs);
  
  public abstract void fatal(String paramString, Object... paramVarArgs);
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\logger\xLog.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */