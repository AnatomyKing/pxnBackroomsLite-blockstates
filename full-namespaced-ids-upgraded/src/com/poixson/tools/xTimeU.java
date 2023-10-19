package com.poixson.tools;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public enum xTimeU {
  T('t', "tick", null, -1L),
  MS('n', "ms", TimeUnit.MILLISECONDS, 1L),
  S('s', "second", TimeUnit.SECONDS, 1000L),
  M('m', "minute", TimeUnit.MINUTES, 60000L),
  H('h', "hour", TimeUnit.HOURS, 3600000L),
  D('d', "day", TimeUnit.DAYS, 86400000L),
  W('w', "week", null, 604800000L),
  MO('o', "month", null, 2592000000L),
  Y('y', "year", null, 31557600000L);
  
  public final AtomicInteger ticksPerSecond = new AtomicInteger(20);
  
  public final char chr;
  
  public final String name;
  
  public final TimeUnit timeUnit;
  
  public final long value;
  
  public static final List<xTimeU> xunits;
  
  static {
    xunits = Collections.unmodifiableList(
        Arrays.asList(new xTimeU[] { Y, MO, W, D, H, M, S, MS, T }));
  }
  
  xTimeU(char chr, String name, TimeUnit timeUnit, long value) {
    this.chr = chr;
    this.name = name;
    this.timeUnit = timeUnit;
    this.value = value;
  }
  
  public static xTimeU[] GetUnits() {
    return xunits.<xTimeU>toArray(new xTimeU[0]);
  }
  
  public static xTimeU GetUnit(TimeUnit timeUnit) {
    Iterator<xTimeU> it = xunits.iterator();
    while (it.hasNext()) {
      xTimeU xunit = it.next();
      if (timeUnit.equals(xunit.timeUnit))
        return xunit; 
    } 
    return null;
  }
  
  public static xTimeU GetUnit(char chr) {
    Iterator<xTimeU> it = xunits.iterator();
    while (it.hasNext()) {
      xTimeU xunit = it.next();
      if (xunit.chr == Character.toLowerCase(chr))
        return xunit; 
    } 
    return null;
  }
  
  public static xTimeU GetUnit(String str) {
    String match = str.toUpperCase();
    switch (match) {
      case "T":
      case "TK":
      case "TCK":
      case "TICK":
      case "TICKS":
        return T;
      case "N":
      case "MS":
        return MS;
      case "S":
      case "SEC":
      case "SECS":
      case "SECOND":
      case "SECONDS":
        return S;
      case "M":
      case "MIN":
      case "MINUTE":
      case "MINUTES":
        return M;
      case "H":
      case "HR":
      case "HOUR":
      case "HOURS":
        return H;
      case "D":
      case "DY":
      case "DAY":
      case "DAYS":
        return D;
      case "W":
      case "WK":
      case "WEEK":
      case "WEEKS":
        return W;
      case "O":
      case "MN":
      case "MTH":
      case "MONTH":
      case "MONTHS":
        return MO;
      case "Y":
      case "YR":
      case "YEAR":
      case "YEARS":
        return Y;
    } 
    return null;
  }
  
  public static xTimeU GetUnit(long value) {
    Iterator<xTimeU> it = xunits.iterator();
    while (it.hasNext()) {
      xTimeU xunit = it.next();
      if (xunit.getUnitValue() == value)
        return xunit; 
    } 
    return null;
  }
  
  public long convertTo(long value) {
    return value * getUnitValue();
  }
  
  public double convertTo(double value) {
    return value * getUnitValue();
  }
  
  public long convertFrom(long value) {
    return Math.floorDiv(value, getUnitValue());
  }
  
  public double convertFrom(double value) {
    return value / getUnitValue();
  }
  
  public TimeUnit getTimeUnit() {
    return this.timeUnit;
  }
  
  public long getUnitValue() {
    if (T.equals(this))
      return 1000L / this.ticksPerSecond.get(); 
    return this.value;
  }
  
  public int getTicksPerSecond() {
    return this.ticksPerSecond.get();
  }
  
  public void setTicksPerSecond(int value) {
    this.ticksPerSecond.set(value);
  }
  
  public String toString() {
    return this.name;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\xTimeU.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */