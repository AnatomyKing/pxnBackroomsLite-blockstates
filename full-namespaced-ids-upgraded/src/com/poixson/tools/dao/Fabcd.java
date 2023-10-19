package com.poixson.tools.dao;

import java.io.Serializable;

public class Fabcd implements Serializable, Cloneable {
  private static final long serialVersionUID = 1L;
  
  public float a;
  
  public float b;
  
  public float c;
  
  public float d;
  
  public Fabcd() {
    this.a = 0.0F;
    this.b = 0.0F;
    this.c = 0.0F;
    this.d = 0.0F;
  }
  
  public Fabcd(float a, float b, float c, float d) {
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
  }
  
  public Fabcd(Fabcd dao) {
    this.a = dao.a;
    this.b = dao.b;
    this.c = dao.c;
    this.d = dao.d;
  }
  
  public Object clone() {
    return new Fabcd(this.a, this.b, this.c, this.d);
  }
  
  public boolean equals(Object obj) {
    if (obj == null)
      return false; 
    if (obj instanceof Fabcd) {
      Fabcd dao = (Fabcd)obj;
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
    long bits = 31L + Float.floatToIntBits(this.a);
    bits = bits * 31L + Float.floatToIntBits(this.b);
    bits = bits * 31L + Float.floatToIntBits(this.c);
    bits = bits * 31L + Float.floatToIntBits(this.d);
    return (int)(bits ^ bits >> 32L);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\dao\Fabcd.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */