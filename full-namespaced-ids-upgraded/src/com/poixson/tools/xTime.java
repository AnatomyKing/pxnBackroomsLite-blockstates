package com.poixson.tools;

import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.utils.StringUtils;
import com.poixson.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class xTime {
  public final AtomicLong value = new AtomicLong(0L);
  
  public xTime() {
    set(0L);
  }
  
  public xTime(long ms) {
    set(ms);
  }
  
  public xTime(long value, xTimeU xunit) {
    set(value, xunit);
  }
  
  public xTime(long value, TimeUnit unit) {
    set(value, unit);
  }
  
  public xTime(String value) {
    set(value);
  }
  
  public xTime(xTime time) {
    set(time);
  }
  
  public xTime clone() {
    return new xTime(this);
  }
  
  public long get(xTimeU xunit) {
    if (xunit == null)
      throw new RequiredArgumentException("xunit"); 
    return xunit.convertTo(this.value.get());
  }
  
  public long get(TimeUnit unit) {
    if (unit == null)
      throw new RequiredArgumentException("unit"); 
    xTimeU xunit = xTimeU.GetUnit(unit);
    if (xunit == null)
      throw new RuntimeException("Unknown time unit: " + unit.toString()); 
    return xunit.convertTo(this.value.get());
  }
  
  public long ms() {
    return this.value.get();
  }
  
  public long ticks(long tick_length) {
    return this.value.get() / tick_length;
  }
  
  public xTime set(long ms) {
    this.value.set(ms);
    return this;
  }
  
  public xTime set(long value, xTimeU xunit) {
    if (xunit == null)
      throw new RequiredArgumentException("unit"); 
    this.value.set(xunit.convertTo(value));
    return this;
  }
  
  public xTime set(long value, TimeUnit unit) {
    if (unit == null)
      throw new RequiredArgumentException("unit"); 
    xTimeU xunit = xTimeU.GetUnit(unit);
    if (xunit == null)
      throw new RuntimeException("Unknown time unit: " + unit.toString()); 
    this.value.set(xunit.convertTo(value));
    return this;
  }
  
  public xTime set(String str) {
    if (Utils.isEmpty(str))
      throw new RequiredArgumentException("str"); 
    this.value.set(ParseToLong(str));
    return this;
  }
  
  public xTime set(xTime time) {
    if (time == null)
      throw new RequiredArgumentException("time"); 
    this.value.set(time.ms());
    return this;
  }
  
  public xTime reset() {
    this.value.set(0L);
    return this;
  }
  
  public void add(long ms) {
    this.value.addAndGet(ms);
  }
  
  public void add(long value, xTimeU xunit) {
    if (xunit == null)
      throw new RequiredArgumentException("unit"); 
    this.value.addAndGet(xunit.convertTo(value));
  }
  
  public void add(long value, TimeUnit unit) {
    if (unit == null)
      throw new RequiredArgumentException("unit"); 
    xTimeU xunit = xTimeU.GetUnit(unit);
    if (xunit == null)
      throw new RuntimeException("Unknown time unit: " + unit.toString()); 
    this.value.addAndGet(xunit.convertTo(value));
  }
  
  public void add(String str) {
    if (Utils.isEmpty(str))
      throw new RequiredArgumentException("str"); 
    this.value.addAndGet(ParseToLong(str));
  }
  
  public void add(xTime time) {
    if (time == null)
      throw new RequiredArgumentException("time"); 
    this.value.addAndGet(time.ms());
  }
  
  public static xTime Parse(String str) {
    if (Utils.isEmpty(str))
      return null; 
    return new xTime(ParseToLong(str));
  }
  
  public static long ParseToLong(String str) {
    if (Utils.isEmpty(str))
      throw new RequiredArgumentException("str"); 
    long value = 0L;
    StringBuilder bufValue = new StringBuilder();
    StringBuilder bufUnit = new StringBuilder();
    boolean moreNumbers = true;
    boolean decimal = false;
    for (char chr : (str + "  ").toCharArray()) {
      if (moreNumbers) {
        if (chr == '.') {
          decimal = true;
          bufValue.append(chr);
          continue;
        } 
        if (chr == ',')
          continue; 
        if (chr == '-' && 
          bufValue.length() == 0) {
          bufValue.append('-');
          continue;
        } 
        if (Character.isDigit(chr)) {
          bufValue.append(chr);
          continue;
        } 
      } 
      if (chr == ' ') {
        if (moreNumbers) {
          moreNumbers = false;
        } else {
          xTimeU xunit;
          if (bufUnit.length() == 0) {
            xunit = xTimeU.MS;
          } else {
            xunit = xTimeU.GetUnit(bufUnit.toString());
            if (xunit == null)
              throw new NumberFormatException("Unknown unit: " + bufUnit.toString()); 
          } 
          if (bufValue.length() > 0)
            if (decimal) {
              Double val = Double.valueOf(bufValue.toString());
              if (val == null)
                throw new NumberFormatException("Invalid number: " + bufValue.toString()); 
              value += (long)xunit.convertFrom(val.doubleValue());
            } else {
              Long val = Long.valueOf(bufValue.toString());
              if (val == null)
                throw new NumberFormatException("Invalid number: " + bufValue.toString()); 
              value += xunit.convertTo(val.longValue());
            }  
          bufValue.setLength(0);
          bufUnit.setLength(0);
          moreNumbers = true;
          decimal = false;
        } 
      } else if (Character.isLetter(chr)) {
        moreNumbers = false;
        bufUnit.append(chr);
      } else {
        throw new NumberFormatException("Unknown character " + 
            
            chr + " in value: " + 
            str);
      } 
      continue;
    } 
    return value;
  }
  
  public String toString() {
    return ToString(ms(), false, false);
  }
  
  public String toRoundedString() {
    return ToString(ms(), true, true);
  }
  
  public String toFullString() {
    return ToString(ms(), true, false);
  }
  
  public static String ToString(xTime time, boolean fullFormat, boolean roundedFormat) {
    if (time == null)
      return null; 
    return ToString(time.ms(), fullFormat, roundedFormat);
  }
  
  public static String ToString(long ms, boolean fullFormat, boolean roundedFormat) {
    long tmp = ms;
    List<String> result = new ArrayList<>();
    for (xTimeU xunit : xTimeU.xunits) {
      if (xTimeU.T.equals(xunit) || 
        xTimeU.W.equals(xunit) || 
        tmp < xunit.value)
        continue; 
      long val = Math.floorDiv(tmp, xunit.value);
      if (val == 0L)
        continue; 
      tmp -= val * xunit.value;
      StringBuilder str = new StringBuilder();
      if (fullFormat) {
        str.append(val)
          .append(' ')
          .append(xunit.name);
        if (val != 1L)
          str.append('s'); 
      } else {
        str.append(val);
        if (xTimeU.MS.equals(xunit)) {
          str.append(xunit.name);
        } else {
          str.append(xunit.chr);
        } 
      } 
      if (roundedFormat)
        return str.toString(); 
      result.add(str.toString());
    } 
    return StringUtils.MergeStrings(' ', result.<String>toArray(new String[0]));
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\xTime.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */