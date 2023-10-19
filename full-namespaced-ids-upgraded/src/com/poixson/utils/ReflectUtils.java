package com.poixson.utils;

import com.poixson.exceptions.RequiredArgumentException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ReflectUtils {
  public static String GetClassName(Object obj) {
    if (obj == null)
      return null; 
    if (obj instanceof Class)
      return ((Class)obj).getSimpleName(); 
    return obj.getClass().getSimpleName();
  }
  
  public static <T> Class<T> GetClass(String classStr) {
    try {
      Class<T> clss = (Class)Class.forName(classStr);
      return clss;
    } catch (ClassNotFoundException classNotFoundException) {
      return null;
    } 
  }
  
  public static <T> T NewInstance(Class<T> clss, Object... args) {
    Constructor<T> construct;
    T instance;
    try {
      if (args.length == 0) {
        construct = clss.getConstructor(new Class[0]);
      } else {
        construct = clss.getConstructor(ArgsToClasses(args));
      } 
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException(e);
    } catch (SecurityException e) {
      throw new IllegalArgumentException(e);
    } 
    try {
      if (args.length == 0) {
        instance = construct.newInstance(new Object[0]);
      } else {
        instance = construct.newInstance(args);
      } 
    } catch (InstantiationException e) {
      throw new IllegalArgumentException(e);
    } catch (IllegalAccessException e) {
      throw new IllegalArgumentException(e);
    } catch (InvocationTargetException e) {
      throw new IllegalArgumentException(e);
    } 
    return instance;
  }
  
  public static Method getMethodByName(String className, String methodName, Class<?>... args) {
    Class<?> clss;
    try {
      clss = Class.forName(className);
    } catch (ClassNotFoundException ignore) {
      return null;
    } 
    if (clss == null)
      return null; 
    return getMethodByName(clss, methodName, args);
  }
  
  public static Method getMethodByName(Object container, String methodName, Class<?>... args) {
    if (container == null)
      throw new IllegalArgumentException("container"); 
    if (Utils.isEmpty(methodName))
      throw new IllegalArgumentException("methodName"); 
    Class<?> clss = (container instanceof Class) ? (Class)container : container.getClass();
    if (clss == null)
      return null; 
    try {
      return clss.getMethod(methodName, args);
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException("Invalid method: " + methodName, e);
    } catch (SecurityException e) {
      throw new IllegalArgumentException("Error accessing method: " + methodName, e);
    } 
  }
  
  public static Object InvokeMethod(String className, String methodName, Object... args) {
    Class<?> clss;
    try {
      clss = Class.forName(className);
    } catch (ClassNotFoundException ignore) {
      return null;
    } 
    if (clss == null)
      return null; 
    return InvokeMethod(clss, methodName, args);
  }
  
  public static Object InvokeMethod(Object container, String methodName, Object... args) {
    return InvokeMethod(container, 
        
        getMethodByName(container, methodName, 
          
          ArgsToClasses(args)), args);
  }
  
  public static Object InvokeMethod(Object container, Method method, Object... args) {
    if (container == null)
      throw new IllegalArgumentException("container"); 
    if (method == null)
      throw new IllegalArgumentException("method"); 
    try {
      return method.invoke(container, args);
    } catch (IllegalAccessException e) {
      throw new IllegalArgumentException("Failed to call method: " + method.getName(), e);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Failed to call method: " + method.getName(), e);
    } catch (InvocationTargetException e) {
      throw new IllegalArgumentException("Failed to call method: " + method.getName(), e);
    } 
  }
  
  public static Class<?>[] ArgsToClasses(Object... args) {
    if (Utils.isEmpty(args))
      return null; 
    Class<?>[] classes = new Class[args.length];
    for (int i = 0; i < args.length; i++)
      classes[i] = (args[i] instanceof Class) ? (Class)args[i] : args[i].getClass(); 
    return classes;
  }
  
  public static String getStaticString(Class<?> clss, String name) {
    String value;
    if (clss == null)
      throw new RequiredArgumentException("clss"); 
    if (Utils.isEmpty(name))
      throw new RequiredArgumentException("name"); 
    try {
      Field field = clss.getField(name);
      Object o = field.get(null);
      value = (String)o;
    } catch (NoSuchFieldException e) {
      throw new IllegalArgumentException("Invalid field: " + name, e);
    } catch (SecurityException e) {
      throw new IllegalArgumentException("Error accessing field: " + name, e);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Failed to get field: " + name, e);
    } catch (IllegalAccessException e) {
      throw new IllegalArgumentException("Error accessing field: " + name, e);
    } 
    return value;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixso\\utils\ReflectUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */