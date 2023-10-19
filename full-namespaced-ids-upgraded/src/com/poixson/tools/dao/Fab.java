package com.poixson.tools.dao;

import java.io.Serializable;

public class Fab implements Serializable, Cloneable {
  private static final long serialVersionUID = 1L;
  
  public float a;
  
  public float b;
  
  public Fab() {
    this.a = 0.0F;
    this.b = 0.0F;
  }
  
  public Fab(float a, float b) {
    this.a = a;
    this.b = b;
  }
  
  public Fab(Fab dao) {
    this.a = dao.a;
    this.b = dao.b;
  }
  
  public Object clone() {
    return new Fab(this.a, this.b);
  }
  
  public boolean equals(Object obj) {
    if (obj == null)
      return false; 
    if (obj instanceof Fab) {
      Fab dao = (Fab)obj;
      return (this.a == dao.a && this.b == dao.b);
    } 
    return false;
  }
  
  public String toString() {
    return this.a + 
      ", " + this.b;
  }
  
  public int hashCode() {
    long bits = 31L + Float.floatToIntBits(this.a);
    bits = bits * 31L + Float.floatToIntBits(this.b);
    return (int)(bits ^ bits >> 32L);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\dao\Fab.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */