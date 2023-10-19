package com.poixson.tools.dao;

import com.poixson.utils.NumberUtils;

public class Tuple3L extends Labc {
  private static final long serialVersionUID = 1L;
  
  public Tuple3L() {
    this.a = 0L;
    this.b = 0L;
    this.c = 0L;
  }
  
  public Tuple3L(long a, long b, long c) {
    this.a = a;
    this.b = b;
    this.c = c;
  }
  
  public Tuple3L(Tuple3L tup) {
    this.a = tup.a;
    this.b = tup.b;
    this.c = tup.c;
  }
  
  public Object clone() {
    return new Tuple3L(this.a, this.b, this.c);
  }
  
  public void get(Tuple3L tup) {
    tup.a = this.a;
    tup.b = this.b;
    tup.c = this.c;
  }
  
  public void set(long a, long b, long c) {
    this.a = a;
    this.b = b;
    this.c = c;
  }
  
  public void set(Tuple3L tup) {
    this.a = tup.a;
    this.b = tup.b;
    this.c = tup.c;
  }
  
  public void setX(long a) {
    this.a = a;
  }
  
  public void setY(long b) {
    this.b = b;
  }
  
  public void setZ(long c) {
    this.c = c;
  }
  
  public void add(long a, long b, long c) {
    this.a += a;
    this.b += b;
    this.c += c;
  }
  
  public void add(Tuple3L tup) {
    this.a += tup.a;
    this.b += tup.b;
    this.c += tup.c;
  }
  
  public void add(Tuple3L tupA, Tuple3L tupB) {
    tupA.a += tupB.a;
    tupA.b += tupB.b;
    tupA.c += tupB.c;
  }
  
  public void sub(long a, long b, long c) {
    this.a -= a;
    this.b -= b;
    this.c -= c;
  }
  
  public void sub(Tuple3L tup) {
    this.a -= tup.a;
    this.b -= tup.b;
    this.c -= tup.c;
  }
  
  public void sub(Tuple3L tupA, Tuple3L tupB) {
    tupA.a -= tupB.a;
    tupA.b -= tupB.b;
    tupA.c -= tupB.c;
  }
  
  public void abs() {
    this.a = Math.abs(this.a);
    this.b = Math.abs(this.b);
    this.c = Math.abs(this.c);
  }
  
  public void neg(Tuple3L tup) {
    this.a = 0L - tup.a;
    this.b = 0L - tup.b;
    this.c = 0L - tup.c;
  }
  
  public void neg() {
    this.a = 0L - this.a;
    this.b = 0L - this.b;
    this.c = 0L - this.c;
  }
  
  public void scale(long scale) {
    this.a *= scale;
    this.b *= scale;
    this.c *= scale;
  }
  
  public void scale(double scale) {
    this.a = (long)(this.a * scale);
    this.b = (long)(this.b * scale);
    this.c = (long)(this.c * scale);
  }
  
  public void scale(float scale) {
    this.a = (long)((float)this.a * scale);
    this.b = (long)((float)this.b * scale);
    this.c = (long)((float)this.c * scale);
  }
  
  public void clamp(long min, long max) {
    this.a = NumberUtils.MinMax(this.a, min, max);
    this.b = NumberUtils.MinMax(this.b, min, max);
    this.c = NumberUtils.MinMax(this.c, min, max);
  }
  
  public void clampMin(long min) {
    if (this.a < min)
      this.a = min; 
    if (this.b < min)
      this.b = min; 
    if (this.c < min)
      this.c = min; 
  }
  
  public void clampMax(long max) {
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


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\dao\Tuple3L.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */