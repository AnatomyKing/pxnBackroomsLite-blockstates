package com.poixson.tools.dao;

import java.io.Serializable;

public class Dabc implements Serializable, Cloneable {
  private static final long serialVersionUID = 1L;
  
  public double a;
  
  public double b;
  
  public double c;
  
  public Dabc() {
    this.a = 0.0D;
    this.b = 0.0D;
    this.c = 0.0D;
  }
  
  public Dabc(double x, double y, double z) {
    this.a = x;
    this.b = y;
    this.c = z;
  }
  
  public Dabc(Dabc dao) {
    this.a = dao.a;
    this.b = dao.b;
    this.c = dao.c;
  }
  
  public Object clone() {
    return new Dabc(this.a, this.b, this.c);
  }
  
  public boolean equals(Object obj) {
    if (obj == null)
      return false; 
    if (obj instanceof Dabc) {
      Dabc dao = (Dabc)obj;
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
    long bits = 31L + Double.doubleToLongBits((this.a == 0.0D) ? 0.0D : this.a);
    bits = bits * 31L + Double.doubleToLongBits((this.b == 0.0D) ? 0.0D : this.b);
    bits = bits * 31L + Double.doubleToLongBits((this.c == 0.0D) ? 0.0D : this.c);
    return (int)(bits ^ bits >> 32L);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\dao\Dabc.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */