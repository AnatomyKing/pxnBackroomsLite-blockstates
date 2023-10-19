package com.poixson.utils;

import com.poixson.logger.xLog;
import com.poixson.tools.Keeper;
import java.io.Closeable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public final class Utils {
  static {
    Keeper.add(new Utils());
  }
  
  public static boolean isEmpty(String str) {
    if (str == null)
      return true; 
    return (str.length() == 0);
  }
  
  public static boolean notEmpty(String str) {
    if (str == null)
      return false; 
    return (str.length() > 0);
  }
  
  public static String ifEmpty(String value, String def) {
    if (isEmpty(value))
      return def; 
    return value;
  }
  
  public static boolean isEmpty(StringBuilder str) {
    if (str == null)
      return true; 
    return (str.length() == 0);
  }
  
  public static boolean notEmpty(StringBuilder str) {
    if (str == null)
      return false; 
    return (str.length() > 0);
  }
  
  public static boolean isEmpty(String[] array) {
    if (array == null)
      return true; 
    return (array.length == 0);
  }
  
  public static boolean notEmpty(String[] array) {
    if (array == null)
      return false; 
    return (array.length > 0);
  }
  
  public static String[] ifEmpty(String[] array, String[] def) {
    if (isEmpty(array))
      return def; 
    return array;
  }
  
  public static <T> T ifEmpty(T obj, T def) {
    if (obj == null)
      return def; 
    return obj;
  }
  
  public static boolean isEmpty(Object[] array) {
    if (array == null)
      return true; 
    return (array.length == 0);
  }
  
  public static boolean notEmpty(Object[] array) {
    if (array == null)
      return false; 
    return (array.length > 0);
  }
  
  public static <T> T[] ifEmpty(T[] array, T[] def) {
    if (isEmpty((Object[])array))
      return def; 
    return array;
  }
  
  public static boolean isEmpty(Collection<?> collect) {
    if (collect == null)
      return true; 
    return (collect.size() == 0);
  }
  
  public static boolean notEmpty(Collection<?> collect) {
    if (collect == null)
      return false; 
    return (collect.size() > 0);
  }
  
  public static Collection<?> ifEmpty(Collection<?> collect, Collection<?> def) {
    if (isEmpty(collect))
      return def; 
    return collect;
  }
  
  public static boolean isEmpty(Map<?, ?> map) {
    if (map == null)
      return true; 
    return (map.size() == 0);
  }
  
  public static boolean notEmpty(Map<?, ?> map) {
    if (map == null)
      return false; 
    return (map.size() > 0);
  }
  
  public static Map<?, ?> ifEmpty(Map<?, ?> map, Map<?, ?> def) {
    if (isEmpty(map))
      return def; 
    return map;
  }
  
  public static Byte ifEmpty(Byte byt, Byte def) {
    if (byt == null)
      return def; 
    return byt;
  }
  
  public static boolean isEmpty(byte[] bytes) {
    if (bytes == null)
      return true; 
    return (bytes.length == 0);
  }
  
  public static boolean notEmpty(byte[] bytes) {
    if (bytes == null)
      return false; 
    return (bytes.length > 0);
  }
  
  public static byte[] ifEmpty(byte[] bytes, byte[] def) {
    if (isEmpty(bytes))
      return def; 
    return bytes;
  }
  
  public static char ifEmpty(char chr, char def) {
    if (chr == '\000')
      return def; 
    return chr;
  }
  
  public static boolean isEmpty(char[] chars) {
    if (chars == null)
      return true; 
    return (chars.length == 0);
  }
  
  public static boolean notEmpty(char[] chars) {
    if (chars == null)
      return false; 
    return (chars.length > 0);
  }
  
  public static char[] ifEmpty(char[] chars, char[] def) {
    if (isEmpty(chars))
      return def; 
    return chars;
  }
  
  public static boolean isEmpty(Character chr) {
    return (chr == null);
  }
  
  public static boolean notEmpty(Character chr) {
    return (chr != null);
  }
  
  public static Character ifEmpty(Character chr, Character def) {
    if (isEmpty(chr))
      return def; 
    return chr;
  }
  
  public static boolean isEmpty(Character[] chars) {
    if (chars == null)
      return true; 
    return (chars.length == 0);
  }
  
  public static boolean notEmpty(Character[] chars) {
    if (chars == null)
      return false; 
    return (chars.length > 0);
  }
  
  public static Character[] ifEmpty(Character[] chars, Character[] def) {
    if (isEmpty(chars))
      return def; 
    return chars;
  }
  
  public static Short ifEmpty(Short value, Short def) {
    if (value == null)
      return def; 
    return value;
  }
  
  public static boolean isEmpty(short[] shorts) {
    if (shorts == null)
      return true; 
    return (shorts.length == 0);
  }
  
  public static boolean notEmpty(short[] shorts) {
    if (shorts == null)
      return false; 
    return (shorts.length > 0);
  }
  
  public static short[] ifEmpty(short[] shorts, short[] def) {
    if (isEmpty(shorts))
      return def; 
    return shorts;
  }
  
  public static Integer ifEmpty(Integer value, Integer def) {
    if (value == null)
      return def; 
    return value;
  }
  
  public static boolean isEmpty(int[] ints) {
    if (ints == null)
      return true; 
    return (ints.length == 0);
  }
  
  public static boolean notEmpty(int[] ints) {
    if (ints == null)
      return false; 
    return (ints.length > 0);
  }
  
  public static int[] ifEmpty(int[] ints, int[] def) {
    if (isEmpty(ints))
      return def; 
    return ints;
  }
  
  public static Long ifEmpty(Long value, Long def) {
    if (value == null)
      return def; 
    return value;
  }
  
  public static boolean isEmpty(long[] longs) {
    if (longs == null)
      return true; 
    return (longs.length == 0);
  }
  
  public static boolean notEmpty(long[] longs) {
    if (longs == null)
      return false; 
    return (longs.length > 0);
  }
  
  public static long[] ifEmpty(long[] longs, long[] def) {
    if (isEmpty(longs))
      return def; 
    return longs;
  }
  
  public static Double ifEmpty(Double value, Double def) {
    if (value == null)
      return def; 
    return value;
  }
  
  public static boolean isEmpty(double[] doubles) {
    if (doubles == null)
      return true; 
    return (doubles.length == 0);
  }
  
  public static boolean notEmpty(double[] doubles) {
    if (doubles == null)
      return false; 
    return (doubles.length > 0);
  }
  
  public static double[] ifEmpty(double[] doubles, double[] def) {
    if (isEmpty(doubles))
      return def; 
    return doubles;
  }
  
  public static Float ifEmpty(Float value, Float def) {
    if (value == null)
      return def; 
    return value;
  }
  
  public static boolean isEmpty(float[] floats) {
    if (floats == null)
      return true; 
    return (floats.length == 0);
  }
  
  public static boolean notEmpty(float[] floats) {
    if (floats == null)
      return false; 
    return (floats.length > 0);
  }
  
  public static float[] ifEmpty(float[] floats, float[] def) {
    if (isEmpty(floats))
      return def; 
    return floats;
  }
  
  public static Boolean ifEmpty(Boolean bool, Boolean def) {
    if (bool == null)
      return def; 
    return bool;
  }
  
  public static boolean isEmpty(boolean[] bools) {
    if (bools == null)
      return true; 
    return (bools.length == 0);
  }
  
  public static boolean notEmpty(boolean[] bools) {
    if (bools == null)
      return false; 
    return (bools.length > 0);
  }
  
  public static boolean[] ifEmpty(boolean[] bools, boolean[] def) {
    if (isEmpty(bools))
      return def; 
    return bools;
  }
  
  public static boolean EqualsUUID(UUID uuidA, UUID uuidB) {
    if (uuidA == null || uuidB == null)
      return false; 
    return (uuidA.compareTo(uuidB) == 0);
  }
  
  public static boolean isArray(Object obj) {
    if (obj == null)
      return false; 
    return obj.getClass().isArray();
  }
  
  public static boolean notArray(Object obj) {
    if (obj == null)
      return true; 
    return !obj.getClass().isArray();
  }
  
  public static boolean[] BoolToPrimArray(Collection<Boolean> collect) {
    if (collect == null)
      return null; 
    if (collect.isEmpty())
      return new boolean[0]; 
    int size = collect.size();
    boolean[] result = new boolean[size];
    Iterator<Boolean> it = collect.iterator();
    int index = 0;
    while (it.hasNext())
      result[index++] = ((Boolean)it.next()).booleanValue(); 
    return result;
  }
  
  public static int[] IntToPrimArray(Collection<Integer> collect) {
    if (collect == null)
      return null; 
    if (collect.isEmpty())
      return new int[0]; 
    int size = collect.size();
    int[] result = new int[size];
    Iterator<Integer> it = collect.iterator();
    int index = 0;
    while (it.hasNext()) {
      result[index] = ((Integer)it.next()).intValue();
      index++;
    } 
    return result;
  }
  
  public static byte[] ByteToPrimArray(Collection<Byte> collect) {
    if (collect == null)
      return null; 
    if (collect.isEmpty())
      return new byte[0]; 
    int size = collect.size();
    byte[] result = new byte[size];
    Iterator<Byte> it = collect.iterator();
    int index = 0;
    while (it.hasNext()) {
      result[index] = ((Byte)it.next()).byteValue();
      index++;
    } 
    return result;
  }
  
  public static short[] ShortToPrimArray(Collection<Short> collect) {
    if (collect == null)
      return null; 
    if (collect.isEmpty())
      return new short[0]; 
    int size = collect.size();
    short[] result = new short[size];
    Iterator<Short> it = collect.iterator();
    int index = 0;
    while (it.hasNext()) {
      result[index] = ((Short)it.next()).shortValue();
      index++;
    } 
    return result;
  }
  
  public static long[] LongToPrimArray(Collection<Long> collect) {
    if (collect == null)
      return null; 
    if (collect.isEmpty())
      return new long[0]; 
    int size = collect.size();
    long[] result = new long[size];
    Iterator<Long> it = collect.iterator();
    int index = 0;
    while (it.hasNext()) {
      result[index] = ((Long)it.next()).longValue();
      index++;
    } 
    return result;
  }
  
  public static double[] DoubleToPrimArray(Collection<Double> collect) {
    if (collect == null)
      return null; 
    if (collect.isEmpty())
      return new double[0]; 
    int size = collect.size();
    double[] result = new double[size];
    Iterator<Double> it = collect.iterator();
    int index = 0;
    while (it.hasNext()) {
      result[index] = ((Double)it.next()).doubleValue();
      index++;
    } 
    return result;
  }
  
  public static float[] FloatToPrimArray(Collection<Float> collect) {
    if (collect == null)
      return null; 
    if (collect.isEmpty())
      return new float[0]; 
    int size = collect.size();
    float[] result = new float[size];
    Iterator<Float> it = collect.iterator();
    int index = 0;
    while (it.hasNext()) {
      result[index] = ((Float)it.next()).floatValue();
      index++;
    } 
    return result;
  }
  
  public static void SafeClose(Closeable obj) {
    if (obj == null)
      return; 
    try {
      obj.close();
    } catch (Exception exception) {}
  }
  
  public static void SafeClose(AutoCloseable obj) {
    if (obj == null)
      return; 
    try {
      obj.close();
    } catch (Exception exception) {}
  }
  
  public static Throwable RootCause(Throwable e) {
    Throwable cause = e.getCause();
    if (cause == null)
      return e; 
    return RootCause(cause);
  }
  
  public static long GetMS() {
    return System.currentTimeMillis();
  }
  
  public static xLog log() {
    return xLog.Get();
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixso\\utils\Utils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */