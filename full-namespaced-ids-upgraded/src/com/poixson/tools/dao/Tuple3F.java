package com.poixson.tools.dao;

import com.poixson.utils.NumberUtils;

public class Tuple3F extends Fabc {
  private static final long serialVersionUID = 1L;
  
  public Tuple3F() {}
  
  public Tuple3F(float a, float b, float c) {
    super(a, b, c);
  }
  
  public Tuple3F(Tuple3F tup) {
    super(tup);
  }
  
  public Object clone() {
    return new Tuple3F(this.a, this.b, this.c);
  }
  
  public void get(Tuple3F tup) {
    tup.a = this.a;
    tup.b = this.b;
    tup.c = this.c;
  }
  
  public void set(float a, float b, float c) {
    this.a = a;
    this.b = b;
    this.c = c;
  }
  
  public void set(Tuple3F tup) {
    this.a = tup.a;
    this.b = tup.b;
    this.c = tup.c;
  }
  
  public void setX(float a) {
    this.a = a;
  }
  
  public void setY(float b) {
    this.b = b;
  }
  
  public void setZ(float c) {
    this.c = c;
  }
  
  public void add(float a, float b, float c) {
    this.a += a;
    this.b += b;
    this.c += c;
  }
  
  public void add(Tuple3F tup) {
    this.a += tup.a;
    this.b += tup.b;
    this.c += tup.c;
  }
  
  public void add(Tuple3F tupA, Tuple3F tupB) {
    tupA.a += tupB.a;
    tupA.b += tupB.b;
    tupA.c += tupB.c;
  }
  
  public void sub(float a, float b, float c) {
    this.a -= a;
    this.b -= b;
    this.c -= c;
  }
  
  public void sub(Tuple3F tup) {
    this.a -= tup.a;
    this.b -= tup.b;
    this.c -= tup.c;
  }
  
  public void sub(Tuple3F tupA, Tuple3F tupB) {
    tupA.a -= tupB.a;
    tupA.b -= tupB.b;
    tupA.c -= tupB.c;
  }
  
  public void abs() {
    this.a = Math.abs(this.a);
    this.b = Math.abs(this.b);
    this.c = Math.abs(this.c);
  }
  
  public void neg(Tuple3F tup) {
    this.a = 0.0F - tup.a;
    this.b = 0.0F - tup.b;
    this.c = 0.0F - tup.c;
  }
  
  public void neg() {
    this.a = 0.0F - this.a;
    this.b = 0.0F - this.b;
    this.c = 0.0F - this.c;
  }
  
  public void scale(float scale) {
    this.a *= scale;
    this.b *= scale;
    this.c *= scale;
  }
  
  public void clamp(float min, float max) {
    this.a = NumberUtils.MinMax(this.a, min, max);
    this.b = NumberUtils.MinMax(this.b, min, max);
    this.c = NumberUtils.MinMax(this.c, min, max);
  }
  
  public void clampMin(float min) {
    if (this.a < min)
      this.a = min; 
    if (this.b < min)
      this.b = min; 
    if (this.c < min)
      this.c = min; 
  }
  
  public void clampMax(float max) {
    if (this.a > max)
      this.a = max; 
    if (this.b > max)
      this.b = max; 
    if (this.c > max)
      this.c = max; 
  }
  
  public void normalize(Tuple3F tup) {
    set(tup);
    normalize();
  }
  
  public void normalize() {
    float norm = 1.0F / vectorLength();
    this.a *= norm;
    this.b *= norm;
    this.c *= norm;
  }
  
  public float vectorLength() {
    return (float)Math.sqrt((this.a * this.a + this.b * this.b + this.c * this.c));
  }
  
  public void interpolate(Tuple3F tup, float alpha) {
    this.a = (1.0F - alpha) * this.a + alpha * tup.a;
    this.b = (1.0F - alpha) * this.b + alpha * tup.b;
    this.c = (1.0F - alpha) * this.c + alpha * tup.c;
  }
  
  public void interpolate(Tuple3F tupA, Tuple3F tupB, float alpha) {
    this.a = (1.0F - alpha) * tupA.a + alpha * tupB.a;
    this.b = (1.0F - alpha) * tupA.b + alpha * tupB.b;
    this.c = (1.0F - alpha) * tupA.c + alpha * tupB.c;
  }
  
  public boolean epsilon(Tuple3F tup, float epsilon) {
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
    return true;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\dao\Tuple3F.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */