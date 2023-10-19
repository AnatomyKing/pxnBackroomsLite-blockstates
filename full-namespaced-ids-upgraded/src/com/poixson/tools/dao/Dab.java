package com.poixson.tools.dao;

import java.io.Serializable;

public class Dab implements Serializable, Cloneable {
  private static final long serialVersionUID = 1L;
  
  public double a;
  
  public double b;
  
  public Dab() {
    this.a = 0.0D;
    this.b = 0.0D;
  }
  
  public Dab(double a, double b) {
    this.a = a;
    this.b = b;
  }
  
  public Dab(Dab dao) {
    this.a = dao.a;
    this.b = dao.b;
  }
  
  public Object clone() {
    return new Dab(this.a, this.b);
  }
  
  public boolean equals(Object obj) {
    if (obj == null)
      return false; 
    if (obj instanceof Dab) {
      Dab dao = (Dab)obj;
      return (this.a == dao.a && this.b == dao.b);
    } 
    return false;
  }
  
  public String toString() {
    return this.a + 
      ", " + this.b;
  }
  
  public int hashCode() {
    long bits = 31L + Double.doubleToLongBits((this.a == 0.0D) ? 0.0D : this.a);
    bits = bits * 31L + Double.doubleToLongBits((this.b == 0.0D) ? 0.0D : this.b);
    return (int)(bits ^ bits >> 32L);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\dao\Dab.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */