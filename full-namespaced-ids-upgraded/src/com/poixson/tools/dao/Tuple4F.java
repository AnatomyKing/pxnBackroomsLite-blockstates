package com.poixson.tools.dao;

import com.poixson.utils.NumberUtils;

public class Tuple4F extends Fabcd {
  private static final long serialVersionUID = 1L;
  
  public Tuple4F() {}
  
  public Tuple4F(float a, float b, float c, float d) {
    super(a, b, c, d);
  }
  
  public Tuple4F(Tuple4F tup) {
    super(tup);
  }
  
  public Object clone() {
    return new Tuple4F(this.a, this.b, this.c, this.d);
  }
  
  public void get(Tuple4F tup) {
    tup.a = this.a;
    tup.b = this.b;
    tup.c = this.c;
    tup.d = this.d;
  }
  
  public void set(float a, float b, float c, float d) {
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
  }
  
  public void set(Tuple4F tup) {
    this.a = tup.a;
    this.b = tup.b;
    this.c = tup.c;
    this.d = tup.d;
  }
  
  public void setW(float a) {
    this.a = a;
  }
  
  public void setX(float b) {
    this.b = b;
  }
  
  public void setY(float c) {
    this.c = c;
  }
  
  public void setZ(float d) {
    this.d = d;
  }
  
  public void add(float a, float b, float c, float d) {
    this.a += a;
    this.b += b;
    this.c += c;
    this.d += d;
  }
  
  public void add(Tuple4F tup) {
    this.a += tup.a;
    this.b += tup.b;
    this.c += tup.c;
    this.d += tup.d;
  }
  
  public void add(Tuple4F tupA, Tuple4F tupB) {
    tupA.a += tupB.a;
    tupA.b += tupB.b;
    tupA.c += tupB.c;
    tupA.d += tupB.d;
  }
  
  public void sub(float a, float b, float c, float d) {
    this.a -= a;
    this.b -= b;
    this.c -= c;
    this.d -= d;
  }
  
  public void sub(Tuple4F tup) {
    this.a -= tup.a;
    this.b -= tup.b;
    this.c -= tup.c;
    this.d -= tup.d;
  }
  
  public void sub(Tuple4F tupA, Tuple4F tupB) {
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
  
  public void neg(Tuple4F tup) {
    this.a = 0.0F - tup.a;
    this.b = 0.0F - tup.b;
    this.c = 0.0F - tup.c;
    this.d = 0.0F - tup.d;
  }
  
  public void neg() {
    this.a = 0.0F - this.a;
    this.b = 0.0F - this.b;
    this.c = 0.0F - this.c;
    this.d = 0.0F - this.d;
  }
  
  public void scale(float scale) {
    this.a *= scale;
    this.b *= scale;
    this.c *= scale;
    this.d *= scale;
  }
  
  public void clamp(float min, float max) {
    this.a = NumberUtils.MinMax(this.a, min, max);
    this.b = NumberUtils.MinMax(this.b, min, max);
    this.c = NumberUtils.MinMax(this.c, min, max);
    this.d = NumberUtils.MinMax(this.d, min, max);
  }
  
  public void clampMin(float min) {
    if (this.a < min)
      this.a = min; 
    if (this.b < min)
      this.b = min; 
    if (this.c < min)
      this.c = min; 
    if (this.d < min)
      this.d = min; 
  }
  
  public void clampMax(float max) {
    if (this.a > max)
      this.a = max; 
    if (this.b > max)
      this.b = max; 
    if (this.c > max)
      this.c = max; 
    if (this.d > max)
      this.d = max; 
  }
  
  public void normalize(Tuple4F tup) {
    set(tup);
    normalize();
  }
  
  public void normalize() {
    float norm = 1.0F / vectorLength();
    this.a *= norm;
    this.b *= norm;
    this.c *= norm;
    this.d *= norm;
  }
  
  public float vectorLength() {
    return (float)Math.sqrt((this.a * this.a + this.b * this.b + this.c * this.c + this.d * this.d));
  }
  
  public void interpolate(Tuple4F tup, float alpha) {
    this.a = (1.0F - alpha) * this.a + alpha * tup.a;
    this.b = (1.0F - alpha) * this.b + alpha * tup.b;
    this.c = (1.0F - alpha) * this.c + alpha * tup.c;
    this.d = (1.0F - alpha) * this.d + alpha * tup.d;
  }
  
  public void interpolate(Tuple4F tupA, Tuple4F tupB, float alpha) {
    this.a = (1.0F - alpha) * tupA.a + alpha * tupB.a;
    this.b = (1.0F - alpha) * tupA.b + alpha * tupB.b;
    this.c = (1.0F - alpha) * tupA.c + alpha * tupB.c;
    this.d = (1.0F - alpha) * tupA.d + alpha * tupB.d;
  }
  
  public boolean epsilon(Tuple4F tup, float epsilon) {
    float dif = this.a - tup.a;
    if (Float.isNaN(dif))
      return false; 
    if (Math.abs(dif) > epsilon)
      return false; 
    dif = this.b - tup.b;
    if (Float.isNaN(dif))
      return false; 
    if (Math.abs(dif) > epsilon)
      return false; 
    dif = this.c - tup.c;
    if (Float.isNaN(dif))
      return false; 
    if (Math.abs(dif) > epsilon)
      return false; 
    dif = this.d - tup.d;
    if (Float.isNaN(dif))
      return false; 
    if (Math.abs(dif) > epsilon)
      return false; 
    return true;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\dao\Tuple4F.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */