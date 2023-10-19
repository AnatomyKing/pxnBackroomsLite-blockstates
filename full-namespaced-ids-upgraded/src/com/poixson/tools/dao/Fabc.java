package com.poixson.tools.dao;

import java.io.Serializable;

public class Fabc implements Serializable, Cloneable {
  private static final long serialVersionUID = 1L;
  
  public float a;
  
  public float b;
  
  public float c;
  
  public Fabc() {
    this.a = 0.0F;
    this.b = 0.0F;
    this.c = 0.0F;
  }
  
  public Fabc(float a, float b, float c) {
    this.a = a;
    this.b = b;
    this.c = c;
  }
  
  public Fabc(Fabc dao) {
    this.a = dao.a;
    this.b = dao.b;
    this.c = dao.c;
  }
  
  public Object clone() {
    return new Fabc(this.a, this.b, this.c);
  }
  
  public boolean equals(Object obj) {
    if (obj == null)
      return false; 
    if (obj instanceof Fabc) {
      Fabc dao = (Fabc)obj;
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
    long bits = 31L + Float.floatToIntBits(this.a);
    bits = bits * 31L + Float.floatToIntBits(this.b);
    bits = bits * 31L + Float.floatToIntBits(this.c);
    return (int)(bits ^ bits >> 32L);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\dao\Fabc.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */