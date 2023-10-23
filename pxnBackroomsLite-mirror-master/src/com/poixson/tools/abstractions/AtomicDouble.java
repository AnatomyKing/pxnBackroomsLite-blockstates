package com.poixson.tools.abstractions;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicDouble {
  protected final AtomicLong value;
  
  public AtomicDouble(double initialValue) {
    this.value = new AtomicLong(Double.doubleToLongBits(initialValue));
  }
  
  public AtomicDouble() {
    this.value = new AtomicLong();
  }
  
  public double get() {
    return Double.longBitsToDouble(this.value.get());
  }
  
  public void set(double value) {
    this.value.set(Double.doubleToLongBits(value));
  }
  
  public void lazySet(double value) {
    this.value.lazySet(Double.doubleToLongBits(value));
  }
  
  public double getAndSet(double value) {
    return 
      Double.longBitsToDouble(this.value
        .getAndSet(Double.doubleToLongBits(value)));
  }
  
  public boolean compareAndSet(double expect, double update) {
    return this.value
      .compareAndSet(
        Double.doubleToLongBits(expect), 
        Double.doubleToLongBits(update));
  }
  
  public double getAndIncrement() {
    while (true) {
      double value = get();
      boolean result = this.value.compareAndSet(
          Double.doubleToLongBits(value), 
          Double.doubleToLongBits(value + 1.0D));
      if (result)
        return value; 
    } 
  }
  
  public double getAndDecrement() {
    while (true) {
      double value = get();
      boolean result = this.value.compareAndSet(
          Double.doubleToLongBits(value), 
          Double.doubleToLongBits(value - 1.0D));
      if (result)
        return value; 
    } 
  }
  
  public double incrementAndGet() {
    while (true) {
      double value = get();
      boolean result = this.value.compareAndSet(
          Double.doubleToLongBits(value), 
          Double.doubleToLongBits(value + 1.0D));
      if (result)
        return value + 1.0D; 
    } 
  }
  
  public double decrementAndGet() {
    while (true) {
      double value = get();
      boolean result = this.value.compareAndSet(
          Double.doubleToLongBits(value), 
          Double.doubleToLongBits(value - 1.0D));
      if (result)
        return value - 1.0D; 
    } 
  }
  
  public double getAndAdd(double delta) {
    while (true) {
      double value = get();
      boolean result = this.value.compareAndSet(
          Double.doubleToLongBits(value), 
          Double.doubleToLongBits(value + 1.0D));
      if (result)
        return value; 
    } 
  }
  
  public double addAndGet(double delta) {
    while (true) {
      double value = get();
      boolean result = this.value.compareAndSet(
          Double.doubleToLongBits(value), 
          Double.doubleToLongBits(value + delta));
      if (result)
        return value + delta; 
    } 
  }
  
  public String toString() {
    return Double.toString(get());
  }
}
