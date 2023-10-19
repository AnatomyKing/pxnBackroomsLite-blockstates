package com.poixson.utils;

import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.tools.Keeper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ObjectUtils {
  static {
    Keeper.add(new ObjectUtils());
  }
  
  public static <T> T Cast(Object object, Class<? extends T> clss) {
    if (clss == null)
      throw new RequiredArgumentException("clss"); 
    if (object == null)
      return null; 
    try {
      if (String.class.equals(clss) && 
        !(object instanceof String))
        return (T)object.toString(); 
      return clss.cast(object);
    } catch (Exception exception) {
      return null;
    } 
  }
  
  public static <T> Set<T> CastSet(Collection<?> data, Class<? extends T> clss) {
    if (clss == null)
      throw new RequiredArgumentException("clss"); 
    if (data == null)
      return null; 
    try {
      Set<T> result = new HashSet<>(data.size());
      for (Object o : data) {
        try {
          result.add(clss.cast(o));
        } catch (Exception exception) {}
      } 
      return result;
    } catch (Exception exception) {
      return null;
    } 
  }
  
  public static <T> Set<T> CastSet(Object data, Class<? extends T> clss) {
    if (clss == null)
      throw new RequiredArgumentException("clss"); 
    if (data == null)
      return null; 
    try {
      return CastSet((Collection)data, clss);
    } catch (Exception exception) {
      return null;
    } 
  }
  
  public static <T> List<T> CastList(Collection<?> data, Class<? extends T> clss) {
    if (clss == null)
      throw new RequiredArgumentException("clss"); 
    if (data == null)
      return null; 
    try {
      List<T> result = new ArrayList<>(data.size());
      for (Object o : data) {
        try {
          result.add(clss.cast(o));
        } catch (Exception exception) {}
      } 
      return result;
    } catch (Exception exception) {
      return null;
    } 
  }
  
  public static <T> List<T> CastList(Object data, Class<? extends T> clss) {
    if (clss == null)
      throw new RequiredArgumentException("clss"); 
    if (data == null)
      return null; 
    try {
      return CastList((Collection)data, clss);
    } catch (Exception exception) {
      return null;
    } 
  }
  
  public static <K, V> Map<K, V> CastMap(Map<?, ?> data, Class<? extends K> keyClss, Class<? extends V> valClss) {
    if (keyClss == null)
      throw new RequiredArgumentException("keyClss"); 
    if (valClss == null)
      throw new RequiredArgumentException("valClss"); 
    if (data == null)
      return null; 
    try {
      Map<K, V> result = new HashMap<>(data.size());
      for (Map.Entry<?, ?> entry : data.entrySet()) {
        try {
          result.put(
              Cast(entry.getKey(), keyClss), 
              Cast(entry.getValue(), valClss));
        } catch (Exception exception) {}
      } 
      return result;
    } catch (Exception exception) {
      return null;
    } 
  }
  
  public static <K, V> Map<K, V> CastMap(Object data, Class<? extends K> keyClss, Class<? extends V> valClss) {
    if (keyClss == null)
      throw new RequiredArgumentException("keyClss"); 
    if (valClss == null)
      throw new RequiredArgumentException("valClss"); 
    if (data == null)
      return null; 
    try {
      return CastMap((Map<?, ?>)data, keyClss, valClss);
    } catch (Exception exception) {
      return null;
    } 
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixso\\utils\ObjectUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */