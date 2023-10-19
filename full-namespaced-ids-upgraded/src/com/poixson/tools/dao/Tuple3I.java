package com.poixson.tools.dao;

import com.poixson.utils.NumberUtils;

public class Tuple3I extends Iabc {
  private static final long serialVersionUID = 1L;
  
  public Tuple3I() {
    this.a = 0;
    this.b = 0;
    this.c = 0;
  }
  
  public Tuple3I(int a, int b, int c) {
    this.a = a;
    this.b = b;
    this.c = c;
  }
  
  public Tuple3I(Tuple3I tup) {
    this.a = tup.a;
    this.b = tup.b;
    this.c = tup.c;
  }
  
  public Object clone() {
    return new Tuple3I(this.a, this.b, this.c);
  }
  
  public void get(Tuple3I tup) {
    tup.a = this.a;
    tup.b = this.b;
    tup.c = this.c;
  }
  
  public void set(int a, int b, int c) {
    this.a = a;
    this.b = b;
    this.c = c;
  }
  
  public void set(Tuple3I tup) {
    this.a = tup.a;
    this.b = tup.b;
    this.c = tup.c;
  }
  
  public void setX(int a) {
    this.a = a;
  }
  
  public void setY(int b) {
    this.b = b;
  }
  
  public void setZ(int c) {
    this.c = c;
  }
  
  public void add(int a, int b, int c) {
    this.a += a;
    this.b += b;
    this.c += c;
  }
  
  public void add(Tuple3I tup) {
    this.a += tup.a;
    this.b += tup.b;
    this.c += tup.c;
  }
  
  public void add(Tuple3I tupA, Tuple3I tupB) {
    tupA.a += tupB.a;
    tupA.b += tupB.b;
    tupA.c += tupB.c;
  }
  
  public void sub(int a, int b, int c) {
    this.a -= a;
    this.b -= b;
    this.c -= c;
  }
  
  public void sub(Tuple3I tup) {
    this.a -= tup.a;
    this.b -= tup.b;
    this.c -= tup.c;
  }
  
  public void sub(Tuple3I tupA, Tuple3I tupB) {
    tupA.a -= tupB.a;
    tupA.b -= tupB.b;
    tupA.c -= tupB.c;
  }
  
  public void abs() {
    this.a = Math.abs(this.a);
    this.b = Math.abs(this.b);
    this.c = Math.abs(this.c);
  }
  
  public void neg(Tuple3I tup) {
    this.a = 0 - tup.a;
    this.b = 0 - tup.b;
    this.c = 0 - tup.c;
  }
  
  public void neg() {
    this.a = 0 - this.a;
    this.b = 0 - this.b;
    this.c = 0 - this.c;
  }
  
  public void scale(int scale) {
    this.a *= scale;
    this.b *= scale;
    this.c *= scale;
  }
  
  public void scale(double scale) {
    this.a = (int)(this.a * scale);
    this.b = (int)(this.b * scale);
    this.c = (int)(this.c * scale);
  }
  
  public void scale(float scale) {
    this.a = (int)(this.a * scale);
    this.b = (int)(this.b * scale);
    this.c = (int)(this.c * scale);
  }
  
  public void clamp(int min, int max) {
    this.a = NumberUtils.MinMax(this.a, min, max);
    this.b = NumberUtils.MinMax(this.b, min, max);
    this.c = NumberUtils.MinMax(this.c, min, max);
  }
  
  public void clampMin(int min) {
    if (this.a < min)
      this.a = min; 
    if (this.b < min)
      this.b = min; 
    if (this.c < min)
      this.c = min; 
  }
  
  public void clampMax(int max) {
    if (this.a > max)
      this.a = max; 
    if (this.b > max)
      this.b = max; 
    if (this.c > max)
      this.c = max; 
  }
  
  public double vectorLength() {
    double a = this.a;
    double b = this.b;
    double c = this.c;
    return Math.sqrt(a * a + b * b + c * c);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\dao\Tuple3I.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */