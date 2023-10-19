package com.poixson.tools.dao;

import com.poixson.utils.NumberUtils;

public class Tuple4L extends Labcd {
  private static final long serialVersionUID = 1L;
  
  public Tuple4L() {
    this.a = 0L;
    this.b = 0L;
    this.c = 0L;
    this.d = 0L;
  }
  
  public Tuple4L(long a, long b, long c, long d) {
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
  }
  
  public Tuple4L(Tuple4L tup) {
    this.a = tup.a;
    this.b = tup.b;
    this.c = tup.c;
    this.d = tup.d;
  }
  
  public Object clone() {
    return new Tuple4L(this.a, this.b, this.c, this.d);
  }
  
  public void get(Tuple4L tup) {
    tup.a = this.a;
    tup.b = this.b;
    tup.c = this.c;
    tup.d = this.d;
  }
  
  public void set(long a, long b, long c, long d) {
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
  }
  
  public void set(Tuple4L tup) {
    this.a = tup.a;
    this.b = tup.b;
    this.c = tup.c;
    this.d = tup.d;
  }
  
  public void setW(long w) {
    this.a = w;
  }
  
  public void setX(long x) {
    this.b = x;
  }
  
  public void setY(long y) {
    this.c = y;
  }
  
  public void setZ(long z) {
    this.d = z;
  }
  
  public void add(long a, long b, long c, long d) {
    this.a += a;
    this.b += b;
    this.c += c;
    this.d += d;
  }
  
  public void add(Tuple4L tup) {
    this.a += tup.a;
    this.b += tup.b;
    this.c += tup.c;
    this.d += tup.d;
  }
  
  public void add(Tuple4L tupA, Tuple4L tupB) {
    tupA.a += tupB.a;
    tupA.b += tupB.b;
    tupA.c += tupB.c;
    tupA.d += tupB.d;
  }
  
  public void sub(long a, long b, long c, long d) {
    this.a -= a;
    this.b -= b;
    this.c -= c;
    this.d -= d;
  }
  
  public void sub(Tuple4L tup) {
    this.a -= tup.a;
    this.b -= tup.b;
    this.c -= tup.c;
    this.d -= tup.d;
  }
  
  public void sub(Tuple4L tupA, Tuple4L tupB) {
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
  
  public void neg(Tuple4L tup) {
    this.a = 0L - tup.a;
    this.b = 0L - tup.b;
    this.c = 0L - tup.c;
    this.d = 0L - tup.d;
  }
  
  public void neg() {
    this.a = 0L - this.a;
    this.b = 0L - this.b;
    this.c = 0L - this.c;
    this.d = 0L - this.d;
  }
  
  public void scale(long scale) {
    this.a *= scale;
    this.b *= scale;
    this.c *= scale;
    this.d *= scale;
  }
  
  public void scale(double scale) {
    this.a = (long)(this.a * scale);
    this.b = (long)(this.b * scale);
    this.c = (long)(this.c * scale);
    this.d = (long)(this.d * scale);
  }
  
  public void scale(float scale) {
    this.a = (long)((float)this.a * scale);
    this.b = (long)((float)this.b * scale);
    this.c = (long)((float)this.c * scale);
    this.d = (long)((float)this.d * scale);
  }
  
  public void clamp(long min, long max) {
    this.a = NumberUtils.MinMax(this.a, min, max);
    this.b = NumberUtils.MinMax(this.b, min, max);
    this.c = NumberUtils.MinMax(this.c, min, max);
    this.d = NumberUtils.MinMax(this.d, min, max);
  }
  
  public void clampMin(long min) {
    if (this.a < min)
      this.a = min; 
    if (this.b < min)
      this.b = min; 
    if (this.c < min)
      this.c = min; 
    if (this.d < min)
      this.d = min; 
  }
  
  public void clampMax(long max) {
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


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\dao\Tuple4L.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */