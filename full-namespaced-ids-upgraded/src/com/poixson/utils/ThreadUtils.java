package com.poixson.utils;

import com.poixson.tools.Keeper;
import com.poixson.tools.abstractions.xCallable;
import com.poixson.tools.xTime;
import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public final class ThreadUtils {
  static {
    Keeper.add(new ThreadUtils());
  }
  
  public static final String[] ignoreThreadNames = new String[] { "EndThread", "DestroyJavaVM" };
  
  public static String[] GetThreadNames() {
    return GetThreadNames(true);
  }
  
  public static String[] GetThreadNames(boolean includeDaemon) {
    Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
    if (threadSet.isEmpty())
      return null; 
    Set<String> list = new HashSet<>();
    label30: for (Thread thread : threadSet) {
      if (!includeDaemon && thread.isDaemon())
        continue; 
      String name = thread.getName();
      if (Utils.isEmpty(name))
        continue; 
      if (!includeDaemon && name.startsWith("main:"))
        continue; 
      for (String str : ignoreThreadNames) {
        if (name.equals(str))
          continue label30; 
      } 
      list.add(thread.getName());
    } 
    if (list.isEmpty())
      return null; 
    return list.<String>toArray(new String[0]);
  }
  
  public static int CountStillRunning() {
    String[] threadNames = GetThreadNames(false);
    if (threadNames == null)
      return -1; 
    return threadNames.length;
  }
  
  public static Thread getDispatchThreadSafe() {
    try {
      return getDispatchThread();
    } catch (InvocationTargetException invocationTargetException) {
    
    } catch (InterruptedException interruptedException) {}
    return null;
  }
  
  public static Thread getDispatchThread() throws InvocationTargetException, InterruptedException {
    xCallable<Thread> call = new xCallable<Thread>() {
        public Thread call() {
          return Thread.currentThread();
        }
      };
    EventQueue.invokeAndWait((Runnable)call);
    return (Thread)call.getResult();
  }
  
  public static void Sleep(long ms) {
    if (ms < 1L)
      return; 
    try {
      Thread.sleep(ms);
    } catch (InterruptedException interruptedException) {}
  }
  
  public static void Sleep(String time) {
    Sleep(new xTime(time));
  }
  
  public static void Sleep(xTime time) {
    if (time == null)
      return; 
    Sleep(time.ms());
  }
  
  public static int getSystemCores() {
    return Runtime.getRuntime().availableProcessors();
  }
  
  public static int getSystemCoresPlus(int add) {
    return getSystemCores() + add;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixso\\utils\ThreadUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */