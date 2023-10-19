package com.poixson.tools.dao;

import java.io.Serializable;

public class Dabcd implements Serializable, Cloneable {
  private static final long serialVersionUID = 1L;
  
  public double a;
  
  public double b;
  
  public double c;
  
  public double d;
  
  public Dabcd() {
    this.a = 0.0D;
    this.b = 0.0D;
    this.c = 0.0D;
    this.d = 0.0D;
  }
  
  public Dabcd(double a, double b, double c, double d) {
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
  }
  
  public Dabcd(Dabcd dao) {
    this.a = dao.a;
    this.b = dao.b;
    this.c = dao.c;
    this.d = dao.d;
  }
  
  public Object clone() {
    return new Dabcd(this.a, this.b, this.c, this.d);
  }
  
  public boolean equals(Object obj) {
    if (obj == null)
      return false; 
    if (obj instanceof Dabcd) {
      Dabcd dao = (Dabcd)obj;
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
    long bits = 31L + Double.doubleToLongBits((this.a == 0.0D) ? 0.0D : this.a);
    bits = bits * 31L + Double.doubleToLongBits((this.b == 0.0D) ? 0.0D : this.b);
    bits = bits * 31L + Double.doubleToLongBits((this.c == 0.0D) ? 0.0D : this.c);
    bits = bits * 31L + Double.doubleToLongBits((this.d == 0.0D) ? 0.0D : this.d);
    return (int)(bits ^ bits >> 32L);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\dao\Dabcd.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */