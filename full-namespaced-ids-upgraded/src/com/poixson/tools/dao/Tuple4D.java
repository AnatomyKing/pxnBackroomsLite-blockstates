package com.poixson.tools.dao;

import com.poixson.utils.NumberUtils;

public class Tuple4D extends Dabcd {
  private static final long serialVersionUID = 1L;
  
  public Tuple4D() {}
  
  public Tuple4D(double a, double b, double c, double d) {
    super(a, b, c, d);
  }
  
  public Tuple4D(Tuple4D tup) {
    super(tup);
  }
  
  public Object clone() {
    return new Tuple4D(this.a, this.b, this.c, this.d);
  }
  
  public void get(Tuple4D tup) {
    tup.a = this.a;
    tup.b = this.b;
    tup.c = this.c;
    tup.d = this.d;
  }
  
  public void set(double a, double b, double c, double d) {
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
  }
  
  public void set(Tuple4D tup) {
    this.a = tup.a;
    this.b = tup.b;
    this.c = tup.c;
    this.d = tup.d;
  }
  
  public void setW(double a) {
    this.a = a;
  }
  
  public void setX(double b) {
    this.b = b;
  }
  
  public void setY(double c) {
    this.c = c;
  }
  
  public void setZ(double d) {
    this.d = d;
  }
  
  public void add(double a, double b, double c, double d) {
    this.a += a;
    this.b += b;
    this.c += c;
    this.d += d;
  }
  
  public void add(Tuple4D tup) {
    this.a += tup.a;
    this.b += tup.b;
    this.c += tup.c;
    this.d += tup.d;
  }
  
  public void add(Tuple4D tupA, Tuple4D tupB) {
    tupA.a += tupB.a;
    tupA.b += tupB.b;
    tupA.c += tupB.c;
    tupA.d += tupB.d;
  }
  
  public void sub(double a, double b, double c, double d) {
    this.a -= a;
    this.b -= b;
    this.c -= c;
    this.d -= d;
  }
  
  public void sub(Tuple4D tup) {
    this.a -= tup.a;
    this.b -= tup.b;
    this.c -= tup.c;
    this.d -= tup.d;
  }
  
  public void sub(Tuple4D tupA, Tuple4D tupB) {
    tupA.a -= tupB.a;
    tupA.b -= tupB.b;
    tupA.c -= tupB.c;
    tupA.d -= tupB.d;
  }
  
  public void abs() {
    this.a = Math.abs(this.a);
    this.b = Math.abs(this.b);
    this.c = Math.abs(this.c);
    this.d = Math.abs(this.d);
  }
  
  public void neg(Tuple4D tup) {
    this.a = 0.0D - tup.a;
    this.b = 0.0D - tup.b;
    this.c = 0.0D - tup.c;
    this.d = 0.0D - tup.d;
  }
  
  public void neg() {
    this.a = 0.0D - this.a;
    this.b = 0.0D - this.b;
    this.c = 0.0D - this.c;
    this.d = 0.0D - this.d;
  }
  
  public void scale(double scale) {
    this.a *= scale;
    this.b *= scale;
    this.c *= scale;
    this.d *= scale;
  }
  
  public void clamp(double min, double max) {
    this.a = NumberUtils.MinMax(this.a, min, max);
    this.b = NumberUtils.MinMax(this.b, min, max);
    this.c = NumberUtils.MinMax(this.c, min, max);
    this.d = NumberUtils.MinMax(this.d, min, max);
  }
  
  public void clampMin(double min) {
    if (this.a < min)
      this.a = min; 
    if (this.b < min)
      this.b = min; 
    if (this.c < min)
      this.c = min; 
    if (this.d < min)
      this.d = min; 
  }
  
  public void clampMax(double max) {
    if (this.a > max)
      this.a = max; 
    if (this.b > max)
      this.b = max; 
    if (this.c > max)
      this.c = max; 
    if (this.d > max)
      this.d = max; 
  }
  
  public void normalize(Tuple4D tup) {
    set(tup);
    normalize();
  }
  
  public void normalize() {
    double norm = 1.0D / vectorLength();
    this.a *= norm;
    this.b *= norm;
    this.c *= norm;
    this.d *= norm;
  }
  
  public double vectorLength() {
    return Math.sqrt(this.a * this.a + this.b * this.b + this.c * this.c + this.d * this.d);
  }
  
  public void interpolate(Tuple4D tup, double alpha) {
    this.a = (1.0D - alpha) * this.a + alpha * tup.a;
    this.b = (1.0D - alpha) * this.b + alpha * tup.b;
    this.c = (1.0D - alpha) * this.c + alpha * tup.c;
    this.d = (1.0D - alpha) * this.d + alpha * tup.d;
  }
  
  public void interpolate(Tuple4D tupA, Tuple4D tupB, double alpha) {
    this.a = (1.0D - alpha) * tupA.a + alpha * tupB.a;
    this.b = (1.0D - alpha) * tupA.b + alpha * tupB.b;
    this.c = (1.0D - alpha) * tupA.c + alpha * tupB.c;
    this.d = (1.0D - alpha) * tupA.d + alpha * tupB.d;
  }
  
  public boolean epsilon(Tuple4D tup, double epsilon) {
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
    dif = this.d - tup.d;
    if (Double.isNaN(dif))
      return false; 
    if (Math.abs(dif) > epsilon)
      return false; 
    return true;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\dao\Tuple4D.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */