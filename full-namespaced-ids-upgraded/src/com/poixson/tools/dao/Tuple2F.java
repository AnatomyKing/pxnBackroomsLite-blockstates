package com.poixson.tools.dao;

import com.poixson.utils.NumberUtils;

public class Tuple2F extends Fab {
  private static final long serialVersionUID = 1L;
  
  public Tuple2F() {}
  
  public Tuple2F(float a, float b) {
    super(a, b);
  }
  
  public Tuple2F(Tuple2F tup) {
    super(tup);
  }
  
  public Object clone() {
    return new Tuple2F(this.a, this.b);
  }
  
  public void get(Tuple2F tup) {
    tup.a = this.a;
    tup.b = this.b;
  }
  
  public void set(float a, float b) {
    this.a = a;
    this.b = b;
  }
  
  public void set(Tuple2F tup) {
    this.a = tup.a;
    this.b = tup.b;
  }
  
  public void setX(float a) {
    this.a = a;
  }
  
  public void setY(float b) {
    this.b = b;
  }
  
  public void add(float a, float b) {
    this.a += a;
    this.b += b;
  }
  
  public void add(Tuple2F tup) {
    this.a += tup.a;
    this.b += tup.b;
  }
  
  public void add(Tuple2F tupA, Tuple2F tupB) {
    tupA.a += tupB.a;
    tupA.b += tupB.b;
  }
  
  public void sub(float a, float b) {
    this.a -= a;
    this.b -= b;
  }
  
  public void sub(Tuple2F tup) {
    this.a -= tup.a;
    this.b -= tup.b;
  }
  
  public void sub(Tuple2F tupA, Tuple2F tupB) {
    tupA.a -= tupB.a;
    tupA.b -= tupB.b;
  }
  
  public void abs() {
    this.a = Math.abs(this.a);
    this.b = Math.abs(this.b);
  }
  
  public void neg(Tuple2F tup) {
    this.a = 0.0F - tup.a;
    this.b = 0.0F - tup.b;
  }
  
  public void neg() {
    this.a = 0.0F - this.a;
    this.b = 0.0F - this.b;
  }
  
  public void scale(float scale) {
    this.a *= scale;
    this.b *= scale;
  }
  
  public void clamp(float min, float max) {
    this.a = NumberUtils.MinMax(this.a, min, max);
    this.b = NumberUtils.MinMax(this.b, min, max);
  }
  
  public void clampMin(float min) {
    if (this.a < min)
      this.a = min; 
    if (this.b < min)
      this.b = min; 
  }
  
  public void clampMax(float max) {
    if (this.a > max)
      this.a = max; 
    if (this.b > max)
      this.b = max; 
  }
  
  public void normalize(Tuple2F tup) {
    set(tup);
    normalize();
  }
  
  public void normalize() {
    float norm = 1.0F / vectorLength();
    this.a *= norm;
    this.b *= norm;
  }
  
  public float vectorLength() {
    return (float)Math.sqrt((this.a * this.a + this.b * this.b));
  }
  
  public void interpolate(Tuple2F tup, float alpha) {
    this.a = (1.0F - alpha) * this.a + alpha * tup.a;
    this.b = (1.0F - alpha) * this.b + alpha * tup.b;
  }
  
  public void interpolate(Tuple2F tupA, Tuple2F tupB, float alpha) {
    this.a = (1.0F - alpha) * tupA.a + alpha * tupB.a;
    this.b = (1.0F - alpha) * tupA.b + alpha * tupB.b;
  }
  
  public boolean epsilon(Tuple2F tup, float epsilon) {
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
    return true;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\dao\Tuple2F.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */