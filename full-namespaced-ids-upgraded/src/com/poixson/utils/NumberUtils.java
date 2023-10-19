package com.poixson.utils;

import com.poixson.tools.Keeper;
import java.text.DecimalFormat;

public final class NumberUtils {
  public static final int MAX_PORT = 65535;
  
  public static final double DELTA = 5.0E-16D;
  
  static {
    Keeper.add(new NumberUtils());
  }
  
  public static final String[] TRUE_VALUES = new String[] { "true", "en", "enable", "enabled", "yes", "on" };
  
  public static final char[] T_VALUES = new char[] { '1', 't', 'i', 'e', 'y' };
  
  public static final String[] FALSE_VALUES = new String[] { "false", "dis", "disable", "disabled", "no", "off" };
  
  public static final char[] F_VALUES = new char[] { '0', 'f', 'o', 'd', 'n' };
  
  public static int BitValue(int bit) {
    int result = 1;
    for (int i = 0; i < bit; i++)
      result *= 2; 
    return result;
  }
  
  public static boolean EqualsExact(Integer a, Integer b) {
    if (a == null && b == null)
      return true; 
    if (a == null || b == null)
      return false; 
    return (a.intValue() == b.intValue());
  }
  
  public static boolean EqualsExact(Boolean a, Boolean b) {
    if (a == null && b == null)
      return true; 
    if (a == null || b == null)
      return false; 
    return (a.booleanValue() == b.booleanValue());
  }
  
  public static boolean EqualsExact(Byte a, Byte b) {
    if (a == null && b == null)
      return true; 
    if (a == null || b == null)
      return false; 
    return (a.byteValue() == b.byteValue());
  }
  
  public static boolean EqualsExact(Short a, Short b) {
    if (a == null && b == null)
      return true; 
    if (a == null || b == null)
      return false; 
    return (a.shortValue() == b.shortValue());
  }
  
  public static boolean EqualsExact(Long a, Long b) {
    if (a == null && b == null)
      return true; 
    if (a == null || b == null)
      return false; 
    return (a.longValue() == b.longValue());
  }
  
  public static boolean EqualsExact(Double a, Double b) {
    if (a == null && b == null)
      return true; 
    if (a == null || b == null)
      return false; 
    return (a.doubleValue() == b.doubleValue());
  }
  
  public static boolean EqualsExact(Float a, Float b) {
    if (a == null && b == null)
      return true; 
    if (a == null || b == null)
      return false; 
    return (a.floatValue() == b.floatValue());
  }
  
  public static double Round(double value, double product) {
    double val = Math.round(value / product);
    return val * product;
  }
  
  public static double Floor(double value, double product) {
    double val = Math.floor(value / product);
    return val * product;
  }
  
  public static double Ceil(double value, double product) {
    double val = Math.ceil(value / product);
    return val * product;
  }
  
  public static int Round(int value, int product) {
    return (int)Round(value, product);
  }
  
  public static int Floor(int value, int product) {
    return (int)Floor(value, product);
  }
  
  public static int Ceil(int value, int product) {
    return (int)Ceil(value, product);
  }
  
  public static long Round(long value, int product) {
    return (long)Round(value, product);
  }
  
  public static long Floor(long value, int product) {
    return (long)Floor(value, product);
  }
  
  public static long Ceil(long value, int product) {
    return (long)Ceil(value, product);
  }
  
  public static int SafeLongToInt(long value) {
    if (value < -2147483648L)
      return Integer.MIN_VALUE; 
    if (value > 2147483647L)
      return Integer.MAX_VALUE; 
    return (int)value;
  }
  
  public static boolean IsNumeric(String value) {
    if (Utils.isEmpty(value))
      return false; 
    return (ToLong(value) != null);
  }
  
  public static boolean IsBoolean(String value) {
    return (ToBoolean(value) != null);
  }
  
  public static Integer ToInteger(String value) {
    if (Utils.isEmpty(value))
      return null; 
    try {
      return Integer.valueOf(Integer.parseInt(value));
    } catch (NumberFormatException numberFormatException) {
      return null;
    } 
  }
  
  public static int ToInteger(String value, int def) {
    if (value == null)
      return def; 
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException numberFormatException) {
      return def;
    } 
  }
  
  public static Integer CastInteger(Object obj) {
    if (obj == null)
      return null; 
    if (obj instanceof Integer)
      return (Integer)obj; 
    throw new IllegalArgumentException("Invalid integer value: " + obj.toString());
  }
  
  public static Byte ToByte(String value) {
    if (Utils.isEmpty(value))
      return null; 
    try {
      return Byte.valueOf(Byte.parseByte(value));
    } catch (NumberFormatException numberFormatException) {
      return null;
    } 
  }
  
  public static byte ToByte(String value, byte def) {
    if (value == null)
      return def; 
    try {
      return Byte.parseByte(value);
    } catch (NumberFormatException numberFormatException) {
      return def;
    } 
  }
  
  public static Byte CastByte(Object obj) {
    if (obj == null)
      return null; 
    if (obj instanceof Byte)
      return (Byte)obj; 
    throw new IllegalArgumentException("Invalid byte value: " + obj.toString());
  }
  
  public static Short ToShort(String value) {
    if (Utils.isEmpty(value))
      return null; 
    try {
      return Short.valueOf(Short.parseShort(value));
    } catch (NumberFormatException numberFormatException) {
      return null;
    } 
  }
  
  public static short ToShort(String value, short def) {
    if (value == null)
      return def; 
    try {
      return Short.parseShort(value);
    } catch (NumberFormatException numberFormatException) {
      return def;
    } 
  }
  
  public static Short CastShort(Object obj) {
    if (obj == null)
      return null; 
    if (obj instanceof Short)
      return (Short)obj; 
    throw new IllegalArgumentException("Invalid short value: " + obj.toString());
  }
  
  public static Long ToLong(String value) {
    if (Utils.isEmpty(value))
      return null; 
    try {
      return Long.valueOf(Long.parseLong(value));
    } catch (NumberFormatException numberFormatException) {
      return null;
    } 
  }
  
  public static long ToLong(String value, long def) {
    if (value == null)
      return def; 
    try {
      return Long.parseLong(value);
    } catch (NumberFormatException numberFormatException) {
      return def;
    } 
  }
  
  public static Long CastLong(Object obj) {
    if (obj == null)
      return null; 
    if (obj instanceof Long)
      return (Long)obj; 
    throw new IllegalArgumentException("Invalid long value: " + obj.toString());
  }
  
  public static Double ToDouble(String value) {
    if (Utils.isEmpty(value))
      return null; 
    try {
      return Double.valueOf(Double.parseDouble(value));
    } catch (NumberFormatException numberFormatException) {
      return null;
    } 
  }
  
  public static double ToDouble(String value, double def) {
    if (value == null)
      return def; 
    try {
      return Double.parseDouble(value);
    } catch (NumberFormatException numberFormatException) {
      return def;
    } 
  }
  
  public static Double CastDouble(Object obj) {
    if (obj == null)
      return null; 
    if (obj instanceof Double)
      return (Double)obj; 
    throw new IllegalArgumentException("Invalid double value: " + obj.toString());
  }
  
  public static Float ToFloat(String value) {
    if (Utils.isEmpty(value))
      return null; 
    try {
      return Float.valueOf(Float.parseFloat(value));
    } catch (NumberFormatException numberFormatException) {
      return null;
    } 
  }
  
  public static float ToFloat(String value, float def) {
    if (value == null)
      return def; 
    try {
      return Float.parseFloat(value);
    } catch (NumberFormatException numberFormatException) {
      return def;
    } 
  }
  
  public static Float CastFloat(Object obj) {
    if (obj == null)
      return null; 
    if (obj instanceof Float)
      return (Float)obj; 
    throw new IllegalArgumentException("Invalid float value: " + obj.toString());
  }
  
  public static Boolean ToBoolean(String value) {
    if (Utils.isEmpty(value))
      return null; 
    String val = value.trim().toLowerCase();
    for (String v : TRUE_VALUES) {
      if (val.equals(v))
        return Boolean.TRUE; 
    } 
    for (String v : FALSE_VALUES) {
      if (val.equals(v))
        return Boolean.FALSE; 
    } 
    char chr = val.charAt(0);
    for (char c : T_VALUES) {
      if (chr == c)
        return Boolean.TRUE; 
    } 
    for (char c : F_VALUES) {
      if (chr == c)
        return Boolean.FALSE; 
    } 
    return null;
  }
  
  public static boolean ToBoolean(String value, boolean def) {
    if (value == null)
      return def; 
    Boolean bool = ToBoolean(value);
    if (bool == null)
      return def; 
    return bool.booleanValue();
  }
  
  public static Boolean CastBoolean(Object obj) {
    if (obj == null)
      return null; 
    if (obj instanceof Boolean)
      return (Boolean)obj; 
    if (obj instanceof Integer)
      return Boolean.valueOf((((Integer)obj).intValue() != 0)); 
    if (obj instanceof String) {
      String str = (String)obj;
      for (String entry : TRUE_VALUES) {
        if (entry.equalsIgnoreCase(str))
          return Boolean.TRUE; 
      } 
      for (String entry : FALSE_VALUES) {
        if (entry.equalsIgnoreCase(str))
          return Boolean.FALSE; 
      } 
      char first = str.charAt(0);
      for (char chr : T_VALUES) {
        if (chr == first)
          return Boolean.TRUE; 
      } 
      for (char chr : F_VALUES) {
        if (chr == first)
          return Boolean.FALSE; 
      } 
    } 
    throw new IllegalArgumentException("Invalid boolean value: " + obj.toString());
  }
  
  public static String FormatDecimal(String format, double value) {
    return (new DecimalFormat(format))
      .format(value);
  }
  
  public static String FormatDecimal(String format, float value) {
    return (new DecimalFormat(format))
      .format(value);
  }
  
  public static String PadZeros(int value, int len) {
    String str = Integer.toString(value);
    int zeros = len - str.length();
    if (zeros < 1)
      return str; 
    return StringUtils.Repeat(zeros, '0');
  }
  
  public static int MinMax(int value, int min, int max) {
    if (min > max)
      throw new IllegalArgumentException("min cannot be greater than max"); 
    if (value < min)
      return min; 
    if (value > max)
      return max; 
    return value;
  }
  
  public static byte MinMax(byte value, byte min, byte max) {
    if (min > max)
      throw new IllegalArgumentException("min cannot be greater than max"); 
    if (value < min)
      return min; 
    if (value > max)
      return max; 
    return value;
  }
  
  public static short MinMax(short value, short min, short max) {
    if (min > max)
      throw new IllegalArgumentException("min cannot be greater than max"); 
    if (value < min)
      return min; 
    if (value > max)
      return max; 
    return value;
  }
  
  public static long MinMax(long value, long min, long max) {
    if (min > max)
      throw new IllegalArgumentException("min cannot be greater than max"); 
    if (value < min)
      return min; 
    if (value > max)
      return max; 
    return value;
  }
  
  public static double MinMax(double value, double min, double max) {
    if (min > max)
      throw new IllegalArgumentException("min cannot be greater than max"); 
    if (value < min)
      return min; 
    if (value > max)
      return max; 
    return value;
  }
  
  public static float MinMax(float value, float min, float max) {
    if (min > max)
      throw new IllegalArgumentException("min cannot be greater than max"); 
    if (value < min)
      return min; 
    if (value > max)
      return max; 
    return value;
  }
  
  public static boolean IsMinMax(int value, int min, int max) {
    return (value >= min && value <= max);
  }
  
  public static boolean IsMinMax(byte value, byte min, byte max) {
    return (value >= min && value <= max);
  }
  
  public static boolean IsMinMax(short value, short min, short max) {
    return (value >= min && value <= max);
  }
  
  public static boolean IsMinMax(long value, long min, long max) {
    return (value >= min && value <= max);
  }
  
  public static boolean IsMinMax(double value, double min, double max) {
    return (value >= min && value <= max);
  }
  
  public static boolean IsMinMax(float value, float min, float max) {
    return (value >= min && value <= max);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixso\\utils\NumberUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */