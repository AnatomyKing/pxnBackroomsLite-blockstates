package com.poixson.tools.dao;

import com.poixson.utils.NumberUtils;

public class Tuple2L extends Lab {
  private static final long serialVersionUID = 1L;
  
  public Tuple2L() {}
  
  public Tuple2L(long a, long b) {
    super(a, b);
  }
  
  public Tuple2L(Tuple2L tup) {
    super(tup);
  }
  
  public Object clone() {
    return new Tuple2L(this.a, this.b);
  }
  
  public void get(Tuple2L tup) {
    tup.a = this.a;
    tup.b = this.b;
  }
  
  public void set(long a, long b) {
    this.a = a;
    this.b = b;
  }
  
  public void set(Tuple2L tup) {
    this.a = tup.a;
    this.b = tup.b;
  }
  
  public void setX(long a) {
    this.a = a;
  }
  
  public void setY(long b) {
    this.b = b;
  }
  
  public void add(long a, long b) {
    this.a += a;
    this.b += b;
  }
  
  public void add(Tuple2L tup) {
    this.a += tup.a;
    this.b += tup.b;
  }
  
  public void add(Tuple2L tupA, Tuple2L tupB) {
    tupA.a += tupB.a;
    tupA.b += tupB.b;
  }
  
  public void sub(long a, long b) {
    this.a -= a;
    this.b -= b;
  }
  
  public void sub(Tuple2L tup) {
    this.a -= tup.a;
    this.b -= tup.b;
  }
  
  public void sub(Tuple2L tupA, Tuple2L tupB) {
    tupA.a -= tupB.a;
    tupA.b -= tupB.b;
  }
  
  public void abs() {
    this.a = Math.abs(this.a);
    this.b = Math.abs(this.b);
  }
  
  public void neg(Tuple2L tup) {
    this.a = 0L - tup.a;
    this.b = 0L - tup.b;
  }
  
  public void neg() {
    this.a = 0L - this.a;
    this.b = 0L - this.b;
  }
  
  public void scale(long scale) {
    this.a *= scale;
    this.b *= scale;
  }
  
  public void scale(double scale) {
    this.a = (long)(this.a * scale);
    this.b = (long)(this.b * scale);
  }
  
  public void scale(float scale) {
    this.a = (long)((float)this.a * scale);
    this.b = (long)((float)this.b * scale);
  }
  
  public void clamp(long min, long max) {
    this.a = NumberUtils.MinMax(this.a, min, max);
    this.b = NumberUtils.MinMax(this.b, min, max);
  }
  
  public void clampMin(long min) {
    if (this.a < min)
      this.a = min; 
    if (this.b < min)
      this.b = min; 
  }
  
  public void clampMax(long max) {
    if (this.a > max)
      this.a = max; 
    if (this.b > max)
      this.b = max; 
  }
  
  public double vectorLength() {
    double a = this.a;
    double b = this.b;
    return Math.sqrt(a * a + b * b);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\dao\Tuple2L.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */