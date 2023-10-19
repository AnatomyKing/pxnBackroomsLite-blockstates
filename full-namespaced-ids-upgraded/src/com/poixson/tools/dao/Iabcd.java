package com.poixson.tools.dao;

import java.io.Serializable;

public class Iabcd implements Serializable, Cloneable {
  private static final long serialVersionUID = 1L;
  
  public int a;
  
  public int b;
  
  public int c;
  
  public int d;
  
  public Iabcd() {
    this.a = 0;
    this.b = 0;
    this.c = 0;
    this.d = 0;
  }
  
  public Iabcd(int a, int b, int c, int d) {
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
  }
  
  public Iabcd(Iabcd dao) {
    this.a = dao.a;
    this.b = dao.b;
    this.c = dao.c;
    this.d = dao.d;
  }
  
  public Object clone() {
    return new Iabcd(this.a, this.b, this.c, this.d);
  }
  
  public boolean equals(Object obj) {
    if (obj == null)
      return false; 
    if (obj instanceof Iabcd) {
      Iabcd dao = (Iabcd)obj;
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
    long bits = 31L + this.a;
    bits = bits * 31L + this.b;
    bits = bits * 31L + this.c;
    bits = bits * 31L + this.d;
    return (int)(bits ^ bits >> 32L);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\dao\Iabcd.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */