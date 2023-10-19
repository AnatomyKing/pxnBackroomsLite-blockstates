package com.poixson.tools.events;

import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.utils.Utils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public abstract class xHandler<T extends Annotation> {
  protected final Class<T> type;
  
  public xHandler(Class<T> type) {
    if (type == null)
      throw new RequiredArgumentException("type"); 
    this.type = type;
  }
  
  public int register(Object... objects) {
    if (Utils.isEmpty(objects))
      return -1; 
    int count = 0;
    for (Object obj : objects) {
      if (obj != null)
        count += registerObject(obj); 
    } 
    return count;
  }
  
  protected int registerObject(Object object) {
    if (object == null)
      return -1; 
    Method[] methods = object.getClass().getMethods();
    if (Utils.isEmpty((Object[])methods))
      return -1; 
    int count = 0;
    for (Method m : methods) {
      if (m != null) {
        T anno = m.getAnnotation(this.type);
        if (anno != null)
          if (registerMethod(object, m, anno))
            count++;  
      } 
    } 
    return count;
  }
  
  protected abstract boolean registerMethod(Object paramObject, Method paramMethod, T paramT);
  
  public abstract void unregisterObject(Object paramObject);
  
  public abstract void unregisterMethod(Object paramObject, String paramString);
  
  public abstract void unregisterAll();
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\events\xHandler.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */