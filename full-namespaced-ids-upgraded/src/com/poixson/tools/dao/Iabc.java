package com.poixson.tools.dao;

import java.io.Serializable;

public class Iabc implements Serializable, Cloneable {
  private static final long serialVersionUID = 1L;
  
  public int a;
  
  public int b;
  
  public int c;
  
  public Iabc() {
    this.a = 0;
    this.b = 0;
    this.c = 0;
  }
  
  public Iabc(int a, int b, int c) {
    this.a = a;
    this.b = b;
    this.c = c;
  }
  
  public Iabc(Iabc dao) {
    this.a = dao.a;
    this.b = dao.b;
    this.c = dao.c;
  }
  
  public Object clone() {
    return new Iabc(this.a, this.b, this.c);
  }
  
  public boolean equals(Object obj) {
    if (obj == null)
      return false; 
    if (obj instanceof Iabc) {
      Iabc dao = (Iabc)obj;
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
    long bits = 31L + this.a;
    bits = bits * 31L + this.b;
    bits = bits * 31L + this.c;
    return (int)(bits ^ bits >> 32L);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\dao\Iabc.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */