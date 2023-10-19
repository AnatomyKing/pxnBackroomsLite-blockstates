package com.poixson.tools.dao;

import com.poixson.utils.NumberUtils;

public class Tuple3D extends Dabc {
  private static final long serialVersionUID = 1L;
  
  public Tuple3D() {}
  
  public Tuple3D(double a, double b, double c) {
    super(a, b, c);
  }
  
  public Tuple3D(Tuple3D tup) {
    super(tup);
  }
  
  public Object clone() {
    return new Tuple3D(this.a, this.b, this.c);
  }
  
  public void get(Tuple3D tup) {
    tup.a = this.a;
    tup.b = this.b;
    tup.c = this.c;
  }
  
  public void set(double a, double b, double c) {
    this.a = a;
    this.b = b;
    this.c = c;
  }
  
  public void set(Tuple3D tup) {
    this.a = tup.a;
    this.b = tup.b;
    this.c = tup.c;
  }
  
  public void setX(double a) {
    this.a = a;
  }
  
  public void setY(double b) {
    this.b = b;
  }
  
  public void setZ(double c) {
    this.c = c;
  }
  
  public void add(double a, double b, double c) {
    this.a += a;
    this.b += b;
    this.c += c;
  }
  
  public void add(Tuple3D tup) {
    this.a += tup.a;
    this.b += tup.b;
    this.c += tup.c;
  }
  
  public void add(Tuple3D tupA, Tuple3D tupB) {
    tupA.a += tupB.a;
    tupA.b += tupB.b;
    tupA.c += tupB.c;
  }
  
  public void sub(double a, double b, double c) {
    this.a -= a;
    this.b -= b;
    this.c -= c;
  }
  
  public void sub(Tuple3D tup) {
    this.a -= tup.a;
    this.b -= tup.b;
    this.c -= tup.c;
  }
  
  public void sub(Tuple3D tupA, Tuple3D tupB) {
    tupA.a -= tupB.a;
    tupA.b -= tupB.b;
    tupA.c -= tupB.c;
  }
  
  public void abs() {
    this.a = Math.abs(this.a);
    this.b = Math.abs(this.b);
    this.c = Math.abs(this.c);
  }
  
  public void neg(Tuple3D tup) {
    this.a = 0.0D - tup.a;
    this.b = 0.0D - tup.b;
    this.c = 0.0D - tup.c;
  }
  
  public void neg() {
    this.a = 0.0D - this.a;
    this.b = 0.0D - this.b;
    this.c = 0.0D - this.c;
  }
  
  public void scale(double scale) {
    this.a *= scale;
    this.b *= scale;
    this.c *= scale;
  }
  
  public void clamp(double min, double max) {
    this.a = NumberUtils.MinMax(this.a, min, max);
    this.b = NumberUtils.MinMax(this.b, min, max);
    this.c = NumberUtils.MinMax(this.c, min, max);
  }
  
  public void clampMin(double min) {
    if (this.a < min)
      this.a = min; 
    if (this.b < min)
      this.b = min; 
    if (this.c < min)
      this.c = min; 
  }
  
  public void clampMax(double max) {
    if (this.a > max)
      this.a = max; 
    if (this.b > max)
      this.b = max; 
    if (this.c > max)
      this.c = max; 
  }
  
  public void normalize(Tuple3D tup) {
    set(tup);
    normalize();
  }
  
  public void normalize() {
    double norm = 1.0D / vectorLength();
    this.a *= norm;
    this.b *= norm;
    this.c *= norm;
  }
  
  public double vectorLength() {
    return Math.sqrt(this.a * this.a + this.b * this.b + this.c * this.c);
  }
  
  public void interpolate(Tuple3D tup, double alpha) {
    this.a = (1.0D - alpha) * this.a + alpha * tup.a;
    this.b = (1.0D - alpha) * this.b + alpha * tup.b;
    this.c = (1.0D - alpha) * this.c + alpha * tup.c;
  }
  
  public void interpolate(Tuple3D tupA, Tuple3D tupB, double alpha) {
    this.a = (1.0D - alpha) * tupA.a + alpha * tupB.a;
    this.b = (1.0D - alpha) * tupA.b + alpha * tupB.b;
    this.c = (1.0D - alpha) * tupA.c + alpha * tupB.c;
  }
  
  public boolean epsilon(Tuple3D tup, double epsilon) {
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
    dif = this.c - tup.c;
    if (Double.isNaN(dif))
      return false; 
    if (Math.abs(dif) > epsilon)
      return false; 
    return true;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\dao\Tuple3D.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */