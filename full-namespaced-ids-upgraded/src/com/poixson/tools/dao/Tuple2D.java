package com.poixson.tools.dao;

import com.poixson.utils.NumberUtils;

public class Tuple2D extends Dab {
  private static final long serialVersionUID = 1L;
  
  public Tuple2D() {}
  
  public Tuple2D(double a, double b) {
    super(a, b);
  }
  
  public Tuple2D(Tuple2D tup) {
    super(tup);
  }
  
  public Object clone() {
    return new Tuple2D(this.a, this.b);
  }
  
  public void get(Tuple2D tup) {
    tup.a = this.a;
    tup.b = this.b;
  }
  
  public void set(double a, double b) {
    this.a = a;
    this.b = b;
  }
  
  public void set(Tuple2D tup) {
    this.a = tup.a;
    this.b = tup.b;
  }
  
  public void setX(double a) {
    this.a = a;
  }
  
  public void setY(double b) {
    this.b = b;
  }
  
  public void add(double a, double b) {
    this.a += a;
    this.b += b;
  }
  
  public void add(Tuple2D tup) {
    this.a += tup.a;
    this.b += tup.b;
  }
  
  public void add(Tuple2D tupA, Tuple2D tupB) {
    tupA.a += tupB.a;
    tupA.b += tupB.b;
  }
  
  public void sub(double a, double b) {
    this.a -= a;
    this.b -= b;
  }
  
  public void sub(Tuple2D tup) {
    this.a -= tup.a;
    this.b -= tup.b;
  }
  
  public void sub(Tuple2D tupA, Tuple2D tupB) {
    tupA.a -= tupB.a;
    tupA.b -= tupB.b;
  }
  
  public void abs() {
    this.a = Math.abs(this.a);
    this.b = Math.abs(this.b);
  }
  
  public void neg(Tuple2D tup) {
    this.a = 0.0D - tup.a;
    this.b = 0.0D - tup.b;
  }
  
  public void neg() {
    this.a = 0.0D - this.a;
    this.b = 0.0D - this.b;
  }
  
  public void scale(double scale) {
    this.a *= scale;
    this.b *= scale;
  }
  
  public void clamp(double min, double max) {
    this.a = NumberUtils.MinMax(this.a, min, max);
    this.b = NumberUtils.MinMax(this.b, min, max);
  }
  
  public void clampMin(double min) {
    if (this.a < min)
      this.a = min; 
    if (this.b < min)
      this.b = min; 
  }
  
  public void clampMax(double max) {
    if (this.a > max)
      this.a = max; 
    if (this.b > max)
      this.b = max; 
  }
  
  public void normalize(Tuple2D tup) {
    set(tup);
    normalize();
  }
  
  public void normalize() {
    double norm = 1.0D / vectorLength();
    this.a *= norm;
    this.b *= norm;
  }
  
  public double vectorLength() {
    return Math.sqrt(this.a * this.a + this.b * this.b);
  }
  
  public void interpolate(Tuple2D tup, double alpha) {
    this.a = (1.0D - alpha) * this.a + alpha * tup.a;
    this.b = (1.0D - alpha) * this.b + alpha * tup.b;
  }
  
  public void interpolate(Tuple2D tupA, Tuple2D tupB, double alpha) {
    this.a = (1.0D - alpha) * tupA.a + alpha * tupB.a;
    this.b = (1.0D - alpha) * tupA.b + alpha * tupB.b;
  }
  
  public boolean epsilon(Tuple2D tup, double epsilon) {
    double dif = this.a - tup.a;
    if (Double.isNaN(dif))
      return false; 
    if (Math.abs(dif) > epsilon)
      return false; 
    dif = this.b - tup.b;
    if (Double.isNaN(dif))
      return false; 
    if (Math.abs(dif) > epsilon)
      return false; 
    return true;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\dao\Tuple2D.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */