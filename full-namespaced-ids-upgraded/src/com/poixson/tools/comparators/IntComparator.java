package com.poixson.tools.comparators;

import com.poixson.exceptions.RequiredArgumentException;
import java.util.Comparator;

public class IntComparator implements Comparator<Integer> {
  protected final boolean reverse;
  
  public IntComparator() {
    this(false);
  }
  
  public IntComparator(boolean reverse) {
    this.reverse = reverse;
  }
  
  public int compare(Integer valA, Integer valB) {
    if (valA == null)
      throw new RequiredArgumentException("valA"); 
    if (valB == null)
      throw new RequiredArgumentException("valB"); 
    if (this.reverse)
      return valB.intValue() - valA.intValue(); 
    return valA.intValue() - valB.intValue();
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\comparators\IntComparator.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */