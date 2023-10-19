package com.poixson.tools.dao;

import java.io.Serializable;

public class Labcd implements Serializable, Cloneable {
  private static final long serialVersionUID = 1L;
  
  public long a;
  
  public long b;
  
  public long c;
  
  public long d;
  
  public Labcd() {
    this.a = 0L;
    this.b = 0L;
    this.c = 0L;
    this.d = 0L;
  }
  
  public Labcd(long a, long b, long c, long d) {
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
  }
  
  public Labcd(Labcd dao) {
    this.a = dao.a;
    this.b = dao.b;
    this.c = dao.c;
    this.d = dao.d;
  }
  
  public Object clone() {
    return new Labcd(this.a, this.b, this.c, this.d);
  }
  
  public boolean equals(Object obj) {
    if (obj == null)
      return false; 
    if (obj instanceof Labcd) {
      Labcd dao = (Labcd)obj;
      return (this.a == dao.a && this.b == dao.b && this.c == dao.c && this.d == dao.d);
    } 
    return false;
  }
  
  public String toString() {
    return this.a + 
      ", " + this.b + 
      ", " + this.c + 
      ", " + this.d;
  }
  
  public int hashCode() {
    long bits = (((31L + this.a) * 31L + this.b) * 31L + this.c) * 31L + this.d;
    return (int)(bits ^ bits >> 32L);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\dao\Labcd.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */