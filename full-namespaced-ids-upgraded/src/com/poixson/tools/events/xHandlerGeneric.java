package com.poixson.tools.events;

import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.utils.Utils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class xHandlerGeneric extends xHandler<xEvent> {
  protected final CopyOnWriteArrayList<xEventListenerDAO> listeners = new CopyOnWriteArrayList<>();
  
  public xHandlerGeneric() {
    super(xEvent.class);
  }
  
  protected boolean registerMethod(Object object, Method method, xEvent anno) {
    if (object == null)
      throw new RequiredArgumentException("object"); 
    if (method == null)
      throw new RequiredArgumentException("method"); 
    if (anno == null)
      throw new RequiredArgumentException("anno"); 
    xEventListenerDAO dao = new xEventListenerDAO(object, method);
    this.listeners.add(dao);
    return true;
  }
  
  public void unregisterObject(Object object) {
    if (object == null)
      return; 
    Set<xEventListenerDAO> remove = new HashSet<>();
    Iterator<xEventListenerDAO> it = this.listeners.iterator();
    while (it.hasNext()) {
      xEventListenerDAO dao = it.next();
      if (dao.isObject(object))
        remove.add(dao); 
    } 
    if (!remove.isEmpty())
      for (xEventListenerDAO dao : remove)
        this.listeners.remove(dao);  
  }
  
  public void unregisterMethod(Object object, String methodName) {
    if (object == null || Utils.isEmpty(methodName))
      return; 
    Set<xEventListenerDAO> remove = new HashSet<>();
    Iterator<xEventListenerDAO> it = this.listeners.iterator();
    while (it.hasNext()) {
      xEventListenerDAO dao = it.next();
      if (dao.isMethod(object, methodName))
        remove.add(dao); 
    } 
    if (!remove.isEmpty())
      for (xEventListenerDAO dao : remove)
        this.listeners.remove(dao);  
  }
  
  public void unregisterAll() {
    this.listeners.clear();
  }
  
  public void trigger() {
    Iterator<xEventListenerDAO> it = this.listeners.iterator();
    while (it.hasNext()) {
      xEventListenerDAO dao = it.next();
      dao.invoke();
    } 
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\events\xHandlerGeneric.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */