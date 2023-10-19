package com.poixson.tools.dao;

import com.poixson.utils.NumberUtils;

public class Tuple2I extends Iab {
  private static final long serialVersionUID = 1L;
  
  public Tuple2I() {}
  
  public Tuple2I(int a, int b) {
    super(a, b);
  }
  
  public Tuple2I(Tuple2I tup) {
    super(tup);
  }
  
  public Object clone() {
    return new Tuple2I(this.a, this.b);
  }
  
  public void get(Tuple2I tup) {
    tup.a = this.a;
    tup.b = this.b;
  }
  
  public void set(int a, int b) {
    this.a = a;
    this.b = b;
  }
  
  public void set(Tuple2I tup) {
    this.a = tup.a;
    this.b = tup.b;
  }
  
  public void setX(int a) {
    this.a = a;
  }
  
  public void setY(int b) {
    this.b = b;
  }
  
  public void add(int a, int b) {
    this.a += a;
    this.b += b;
  }
  
  public void add(Tuple2I tup) {
    this.a += tup.a;
    this.b += tup.b;
  }
  
  public void add(Tuple2I tupA, Tuple2I tupB) {
    tupA.a += tupB.a;
    tupA.b += tupB.b;
  }
  
  public void sub(int a, int b) {
    this.a -= a;
    this.b -= b;
  }
  
  public void sub(Tuple2I tup) {
    this.a -= tup.a;
    this.b -= tup.b;
  }
  
  public void sub(Tuple2I tupA, Tuple2I tupB) {
    tupA.a -= tupB.a;
    tupA.b -= tupB.b;
  }
  
  public void abs() {
    this.a = Math.abs(this.a);
    this.b = Math.abs(this.b);
  }
  
  public void neg(Tuple2I tup) {
    this.a = 0 - tup.a;
    this.b = 0 - tup.b;
  }
  
  public void neg() {
    this.a = 0 - this.a;
    this.b = 0 - this.b;
  }
  
  public void scale(int scale) {
    this.a *= scale;
    this.b *= scale;
  }
  
  public void scale(double scale) {
    this.a = (int)(this.a * scale);
    this.b = (int)(this.b * scale);
  }
  
  public void scale(float scale) {
    this.a = (int)(this.a * scale);
    this.b = (int)(this.b * scale);
  }
  
  public void clamp(int min, int max) {
    this.a = NumberUtils.MinMax(this.a, min, max);
    this.b = NumberUtils.MinMax(this.b, min, max);
  }
  
  public void clampMin(int min) {
    if (this.a < min)
      this.a = min; 
    if (this.b < min)
      this.b = min; 
  }
  
  public void clampMax(int max) {
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


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\dao\Tuple2I.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */