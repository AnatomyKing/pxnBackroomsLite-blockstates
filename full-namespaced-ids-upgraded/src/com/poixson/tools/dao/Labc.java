package com.poixson.tools.dao;

import java.io.Serializable;

public class Labc implements Serializable, Cloneable {
  private static final long serialVersionUID = 1L;
  
  public long a;
  
  public long b;
  
  public long c;
  
  public Labc() {
    this.a = 0L;
    this.b = 0L;
    this.c = 0L;
  }
  
  public Labc(long a, long b, long c) {
    this.a = a;
    this.b = b;
    this.c = c;
  }
  
  public Labc(Labc dao) {
    this.a = dao.a;
    this.b = dao.b;
    this.c = dao.c;
  }
  
  public Object clone() {
    return new Labc(this.a, this.b, this.c);
  }
  
  public boolean equals(Object obj) {
    if (obj == null)
      return false; 
    if (obj instanceof Labc) {
      Labc dao = (Labc)obj;
      return (this.a == dao.a && this.b == dao.b && this.c == dao.c);
    } 
    return false;
  }
  
  public String toString() {
    return this.a + 
      ", " + this.b + 
      ", " + this.c;
  }
  
  public int hashCode() {
    long bits = ((31L + this.a) * 31L + this.b) * 31L + this.c;
    return (int)(bits ^ bits >> 32L);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\dao\Labc.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */