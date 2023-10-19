package com.poixson.tools.dao;

import java.io.Serializable;

public class Iab implements Serializable, Cloneable {
  private static final long serialVersionUID = 1L;
  
  public int a;
  
  public int b;
  
  public Iab() {
    this.a = 0;
    this.b = 0;
  }
  
  public Iab(int a, int b) {
    this.a = a;
    this.b = b;
  }
  
  public Iab(Iab dao) {
    this.a = dao.a;
    this.b = dao.b;
  }
  
  public Object clone() {
    return new Iab(this.a, this.b);
  }
  
  public boolean equals(Object obj) {
    if (obj == null)
      return false; 
    if (obj instanceof Iab) {
      Iab dao = (Iab)obj;
      return (this.a == dao.a && this.b == dao.b);
    } 
    return false;
  }
  
  public String toString() {
    return this.a + 
      ", " + this.b;
  }
  
  public int hashCode() {
    long bits = 31L + this.a;
    bits = bits * 31L + this.b;
    return (int)(bits ^ bits >> 32L);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\dao\Iab.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */