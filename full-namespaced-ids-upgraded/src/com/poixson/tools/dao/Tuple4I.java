package com.poixson.tools.dao;

import com.poixson.utils.NumberUtils;

public class Tuple4I extends Iabcd {
  private static final long serialVersionUID = 1L;
  
  public Tuple4I() {
    this.a = 0;
    this.b = 0;
    this.c = 0;
    this.d = 0;
  }
  
  public Tuple4I(int a, int b, int c, int d) {
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
  }
  
  public Tuple4I(Tuple4I tup) {
    this.a = tup.a;
    this.b = tup.b;
    this.c = tup.c;
    this.d = tup.d;
  }
  
  public Object clone() {
    return new Tuple4I(this.a, this.b, this.c, this.d);
  }
  
  public void get(Tuple4I tup) {
    tup.a = this.a;
    tup.b = this.b;
    tup.c = this.c;
    tup.d = this.d;
  }
  
  public void set(int a, int b, int c, int d) {
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
  }
  
  public void set(Tuple4I tup) {
    this.a = tup.a;
    this.b = tup.b;
    this.c = tup.c;
    this.d = tup.d;
  }
  
  public void setW(int a) {
    this.a = a;
  }
  
  public void setX(int b) {
    this.b = b;
  }
  
  public void setY(int c) {
    this.c = c;
  }
  
  public void setZ(int d) {
    this.d = d;
  }
  
  public void add(int a, int b, int c, int d) {
    this.a += a;
    this.b += b;
    this.c += c;
    this.d += d;
  }
  
  public void add(Tuple4I tup) {
    this.a += tup.a;
    this.b += tup.b;
    this.c += tup.c;
    this.d += tup.d;
  }
  
  public void add(Tuple4I tupA, Tuple4I tupB) {
    tupA.a += tupB.a;
    tupA.b += tupB.b;
    tupA.c += tupB.c;
    tupA.d += tupB.d;
  }
  
  public void sub(int a, int b, int c, int d) {
    this.a -= a;
    this.b -= b;
    this.c -= c;
    this.d -= d;
  }
  
  public void sub(Tuple4I tup) {
    this.a -= tup.a;
    this.b -= tup.b;
    this.c -= tup.c;
    this.d -= tup.d;
  }
  
  public void sub(Tuple4I tupA, Tuple4I tupB) {
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
  
  public void neg(Tuple4I tup) {
    this.a = 0 - tup.a;
    this.b = 0 - tup.b;
    this.c = 0 - tup.c;
    this.d = 0 - tup.d;
  }
  
  public void neg() {
    this.a = 0 - this.a;
    this.b = 0 - this.b;
    this.c = 0 - this.c;
    this.d = 0 - this.d;
  }
  
  public void scale(int scale) {
    this.a *= scale;
    this.b *= scale;
    this.c *= scale;
    this.d *= scale;
  }
  
  public void scale(double scale) {
    this.a = (int)(this.a * scale);
    this.b = (int)(this.b * scale);
    this.c = (int)(this.c * scale);
    this.d = (int)(this.d * scale);
  }
  
  public void scale(float scale) {
    this.a = (int)(this.a * scale);
    this.b = (int)(this.b * scale);
    this.c = (int)(this.c * scale);
    this.d = (int)(this.d * scale);
  }
  
  public void clamp(int min, int max) {
    this.a = NumberUtils.MinMax(this.a, min, max);
    this.b = NumberUtils.MinMax(this.b, min, max);
    this.c = NumberUtils.MinMax(this.c, min, max);
    this.d = NumberUtils.MinMax(this.d, min, max);
  }
  
  public void clampMin(int min) {
    if (this.a < min)
      this.a = min; 
    if (this.b < min)
      this.b = min; 
    if (this.c < min)
      this.c = min; 
    if (this.d < min)
      this.d = min; 
  }
  
  public void clampMax(int max) {
    if (this.a > max)
      this.a = max; 
    if (this.b > max)
      this.b = max; 
    if (this.c > max)
      this.c = max; 
    if (this.d > max)
      this.d = max; 
  }
  
  public double vectorLength() {
    double a = this.a;
    double b = this.b;
    double c = this.c;
    double d = this.d;
    return Math.sqrt(a * a + b * b + c * c + d * d);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\dao\Tuple4I.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */