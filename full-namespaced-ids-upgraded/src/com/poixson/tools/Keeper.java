package com.poixson.tools;

import com.poixson.exceptions.RequiredArgumentException;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReference;

public class Keeper {
  private static final AtomicReference<Keeper> instance = new AtomicReference<>(null);
  
  private static final CopyOnWriteArraySet<Object> holder = new CopyOnWriteArraySet();
  
  public static Keeper get() {
    if (instance.get() == null) {
      Keeper keeper = new Keeper();
      if (instance.compareAndSet(null, keeper))
        return keeper; 
    } 
    return instance.get();
  }
  
  public static void add(Object obj) {
    if (obj == null)
      throw new RequiredArgumentException("obj"); 
    holder.add(obj);
  }
  
  public static void remove(Object obj) {
    if (obj == null)
      throw new RequiredArgumentException("obj"); 
    holder.remove(obj);
  }
  
  public static void removeAll() {
    holder.clear();
  }
  
  public static int removeAll(Class<? extends Object> clss) {
    if (holder.isEmpty())
      return 0; 
    int count = 0;
    String expect = clss.getName();
    Iterator<Object> it = holder.iterator();
    while (it.hasNext()) {
      Object obj = it.next();
      String actual = obj.getClass().getName();
      if (expect.equals(actual)) {
        count++;
        remove(obj);
      } 
    } 
    return count;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\Keeper.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */