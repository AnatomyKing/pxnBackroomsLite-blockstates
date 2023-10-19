package com.poixson.tools;

import com.poixson.exceptions.UnmodifiableObjectException;
import com.poixson.logger.xLog;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class DualKeyMap<K, J, V> {
  protected final Map<K, V> kMap;
  
  protected final Map<J, V> jMap;
  
  private volatile boolean isFinal = false;
  
  public DualKeyMap(DualKeyMap<K, J, V> map) {
    this.kMap = new LinkedHashMap<>();
    this.jMap = new LinkedHashMap<>();
    putAll(map);
  }
  
  public DualKeyMap(Map<K, V> kMap, Map<J, V> jMap) {
    this.kMap = kMap;
    this.jMap = jMap;
  }
  
  public DualKeyMap<K, J, V> setFinal() {
    this.isFinal = true;
    return this;
  }
  
  public boolean isFinal() {
    return this.isFinal;
  }
  
  public Map<K, V> getMapK() {
    return this.kMap;
  }
  
  public Map<J, V> getMapJ() {
    return this.jMap;
  }
  
  public int size() {
    return this.kMap.size();
  }
  
  public boolean isEmpty() {
    return this.kMap.isEmpty();
  }
  
  public void clear() {
    if (this.isFinal)
      throw new UnmodifiableObjectException(); 
    this.kMap.clear();
    this.jMap.clear();
  }
  
  public V remove(K kKey, J jKey) {
    if (this.isFinal)
      throw new UnmodifiableObjectException(); 
    V resultK = this.kMap.remove(kKey);
    V resultJ = this.jMap.remove(jKey);
    if (resultK != null)
      return resultK; 
    return resultJ;
  }
  
  public boolean containsKeyK(K kKey) {
    return this.kMap.containsKey(kKey);
  }
  
  public boolean containsKeyJ(J jKey) {
    return this.jMap.containsKey(jKey);
  }
  
  public boolean containsValue(V value) {
    return this.kMap.containsValue(value);
  }
  
  public Set<Map.Entry<K, V>> entrySetK() {
    return this.kMap.entrySet();
  }
  
  public Set<Map.Entry<J, V>> entrySetJ() {
    return this.jMap.entrySet();
  }
  
  public Set<K> keySetK() {
    return this.kMap.keySet();
  }
  
  public Set<J> keySetJ() {
    return this.jMap.keySet();
  }
  
  public Collection<V> values() {
    return this.kMap.values();
  }
  
  public V getK(K kKey) {
    return this.kMap.get(kKey);
  }
  
  public V getJ(J jKey) {
    return this.jMap.get(jKey);
  }
  
  public V put(K kKey, J jKey, V value) {
    if (this.isFinal)
      throw new UnmodifiableObjectException(); 
    this.kMap.put(kKey, value);
    this.jMap.put(jKey, value);
    return value;
  }
  
  public void putAll(DualKeyMap<K, J, V> map) {
    if (this.isFinal)
      throw new UnmodifiableObjectException(); 
    Iterator<Map.Entry<K, V>> itK = this.kMap.entrySet().iterator();
    Iterator<Map.Entry<J, V>> itJ = this.jMap.entrySet().iterator();
    while (itK.hasNext() && itJ.hasNext()) {
      Map.Entry<K, V> entryK = itK.next();
      Map.Entry<J, V> entryJ = itJ.next();
      if (!entryK.getValue().equals(entryJ.getValue())) {
        xLog.Get().severe("Missmatched values in DualKeyMap object! [ %s : %s ] != [ %s : %s ]", new Object[] { entryK
              
              .getKey().toString(), entryK
              .getValue().toString(), entryJ
              .getKey().toString(), entryJ
              .getValue().toString() });
        continue;
      } 
      put(entryK
          .getKey(), entryJ
          .getKey(), entryK
          .getValue());
    } 
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\DualKeyMap.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */