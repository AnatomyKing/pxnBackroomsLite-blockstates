package com.poixson.tools.dao;

import java.io.Serializable;

public class Lab implements Serializable, Cloneable {
  private static final long serialVersionUID = 1L;
  
  public long a;
  
  public long b;
  
  public Lab() {
    this.a = 0L;
    this.b = 0L;
  }
  
  public Lab(long a, long b) {
    this.a = a;
    this.b = b;
  }
  
  public Lab(Lab dao) {
    this.a = dao.a;
    this.b = dao.b;
  }
  
  public Object clone() {
    return new Lab(this.a, this.b);
  }
  
  public boolean equals(Object obj) {
    if (obj == null)
      return false; 
    if (obj instanceof Lab) {
      Lab dao = (Lab)obj;
      return (this.a == dao.a && this.b == dao.b);
    } 
    return false;
  }
  
  public String toString() {
    return this.a + 
      ", " + this.b;
  }
  
  public int hashCode() {
    long bits = (31L + this.a) * 31L + this.b;
    return (int)(bits ^ bits >> 32L);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\dao\Lab.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */