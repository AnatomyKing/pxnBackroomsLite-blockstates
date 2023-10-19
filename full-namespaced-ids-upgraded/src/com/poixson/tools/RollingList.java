package com.poixson.tools;

import java.util.LinkedList;

public class RollingList<E> extends LinkedList<E> {
  private static final long serialVersionUID = 1L;
  
  private final int maxSize;
  
  private final boolean autoPrune;
  
  public RollingList(int size) {
    this(size, true);
  }
  
  public RollingList(int size, boolean autoPrune) {
    if (size <= 0)
      throw new IllegalArgumentException("size argument must be greater than zero."); 
    this.maxSize = size;
    this.autoPrune = autoPrune;
  }
  
  public boolean add(E entry) {
    boolean result = super.add(entry);
    if (this.autoPrune)
      prune(); 
    return result;
  }
  
  public int prune() {
    int maxSize = this.maxSize;
    int curSize = size();
    int remSize = curSize - maxSize;
    if (remSize <= 0)
      return 0; 
    removeRange(0, remSize);
    return remSize;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\RollingList.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */