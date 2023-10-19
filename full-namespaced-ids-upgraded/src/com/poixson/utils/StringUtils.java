package com.poixson.utils;

import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.tools.Keeper;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class StringUtils {
  static {
    Keeper.add(new StringUtils());
  }
  
  public static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;
  
  public static final Charset CHARSET_ASCII = StandardCharsets.US_ASCII;
  
  public static final Charset DEFAULT_CHARSET = CHARSET_UTF8;
  
  public static String ToString(Object obj) {
    if (obj == null)
      return null; 
    if (obj.getClass().isArray()) {
      StringBuilder result = new StringBuilder();
      int count = 0;
      for (Object o : (Object[])obj) {
        if (o != null) {
          if (count > 0)
            result.append(' '); 
          count++;
          result.append('{').append(ToString(o)).append('}');
        } 
      } 
      return result.toString();
    } 
    if (obj instanceof String)
      return (String)obj; 
    if (obj instanceof Boolean)
      return ((Boolean)obj).booleanValue() ? "TRUE" : "false"; 
    if (obj instanceof Integer)
      return ((Integer)obj).toString(); 
    if (obj instanceof Long)
      return ((Long)obj).toString(); 
    if (obj instanceof Double)
      return ((Double)obj).toString(); 
    if (obj instanceof Float)
      return ((Float)obj).toString(); 
    if (obj instanceof Exception)
      return ExceptionToString((Exception)obj); 
    return obj.toString();
  }
  
  public static String ExceptionToString(Throwable e) {
    if (e == null)
      return null; 
    StringWriter writer = new StringWriter(256);
    e.printStackTrace(new PrintWriter(writer));
    try {
      return writer.toString().trim();
    } finally {
      Utils.SafeClose(writer);
    } 
  }
  
  public static String decode(String raw) {
    return decode(raw, null, null);
  }
  
  public static String decodeDef(String raw, String defaultStr) {
    return decode(raw, defaultStr, null);
  }
  
  public static String decodeCh(String raw, String charset) {
    return decode(raw, null, charset);
  }
  
  public static String decode(String raw, String defaultStr, String charset) {
    if (charset == null)
      return decode(raw, defaultStr, DEFAULT_CHARSET.name()); 
    try {
      return URLDecoder.decode(raw, charset);
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      return defaultStr;
    } 
  }
  
  public static boolean ArrayContains(String match, String[] array) {
    if (match == null || match.isEmpty())
      return false; 
    if (array == null || array.length == 0)
      return false; 
    for (String entry : array) {
      if (MatchString(match, entry))
        return true; 
    } 
    return false;
  }
  
  public static String[] RemoveTruncatedDuplicates(String[] array) {
    ArrayList<String> result = new ArrayList<>();
    for (String entry : array) {
      if (!entry.isEmpty()) {
        boolean dup = false;
        for (String match : array) {
          if (!match.isEmpty())
            if (!entry.equals(match) && 
              match.startsWith(entry)) {
              dup = true;
              break;
            }  
        } 
        if (!dup)
          result.add(entry); 
      } 
    } 
    return result.<String>toArray(new String[0]);
  }
  
  public static String[] SplitLines(String[] lines) {
    if (lines == null)
      return null; 
    if (lines.length == 0)
      return new String[0]; 
    List<String> result = new ArrayList<>(lines.length);
    for (String line : lines) {
      if (!line.contains("\n")) {
        result.add(line);
      } else {
        String[] split = line.split("\n");
        if (Utils.notEmpty(split))
          for (String str : split)
            result.add(str);  
      } 
    } 
    return result.<String>toArray(new String[0]);
  }
  
  public static String[] Split(String line, char... delims) {
    int len = line.length();
    List<String> result = new ArrayList<>();
    int last = 0;
    while (true) {
      int next = len;
      for (char chr : delims) {
        int pos = line.indexOf(chr, last);
        if (pos != -1) {
          if (next > pos)
            next = pos; 
          if (pos == last || 
            pos == last + 1)
            break; 
        } 
      } 
      if (next == len) {
        if (last < len)
          result.add(line.substring(last)); 
        break;
      } 
      if (last != next)
        result.add(line.substring(last, next)); 
      last = next + 1;
    } 
    return result.<String>toArray(new String[0]);
  }
  
  public static String[] Split(String line, String... delims) {
    int lenLine = line.length();
    List<String> result = new ArrayList<>();
    int last = 0;
    while (true) {
      int next = lenLine;
      int lenNext = 0;
      for (String str : delims) {
        int pos = line.indexOf(str, last);
        if (pos != -1) {
          if (next == pos) {
            if (str.length() > lenNext)
              lenNext = str.length(); 
          } else if (next > pos) {
            next = pos;
            lenNext = str.length();
          } 
          if (pos == last || 
            pos == last + 1)
            break; 
        } 
      } 
      if (next == lenLine) {
        if (last < lenLine)
          result.add(line.substring(last)); 
        break;
      } 
      if (last != next)
        result.add(line.substring(last, next)); 
      last = next + lenNext;
    } 
    return result.<String>toArray(new String[0]);
  }
  
  public static boolean IsAlpha(String str) {
    if (str == null)
      return false; 
    int sz = str.length();
    for (int i = 0; i < sz; i++) {
      if (!Character.isLetter(str.charAt(i)))
        return false; 
    } 
    return true;
  }
  
  public static boolean IsAlphaSpace(String str) {
    if (str == null)
      return false; 
    int sz = str.length();
    for (int i = 0; i < sz; i++) {
      char chr = str.charAt(i);
      if (!Character.isLetter(chr) && 
        !Character.isSpaceChar(chr))
        return false; 
    } 
    return true;
  }
  
  public static boolean IsAlphaNum(String str) {
    if (str == null)
      return false; 
    int sz = str.length();
    for (int i = 0; i < sz; i++) {
      char chr = str.charAt(i);
      if (!Character.isLetterOrDigit(chr))
        return false; 
    } 
    return true;
  }
  
  public static boolean IsAlphaNumSpace(String str) {
    if (str == null)
      return false; 
    int sz = str.length();
    for (int i = 0; i < sz; i++) {
      char chr = str.charAt(i);
      if (!Character.isLetterOrDigit(chr) && 
        !Character.isSpaceChar(chr))
        return false; 
    } 
    return true;
  }
  
  public static boolean MatchString(String expect, String actual) {
    boolean expectEmpty = (expect == null || expect.isEmpty());
    boolean actualEmpty = (actual == null || actual.isEmpty());
    if (expectEmpty || actualEmpty)
      return (expectEmpty == actualEmpty); 
    return expect.equals(actual);
  }
  
  public static boolean MatchStringIgnoreCase(String expect, String actual) {
    boolean expectEmpty = (expect == null || expect.isEmpty());
    boolean actualEmpty = (actual == null || actual.isEmpty());
    if (expectEmpty || actualEmpty)
      return (expectEmpty == actualEmpty); 
    return expect.equalsIgnoreCase(actual);
  }
  
  public static boolean MatchStringExact(String expect, String actual) {
    if (expect == null && actual == null)
      return true; 
    if (expect == null || actual == null)
      return false; 
    return expect.equals(actual);
  }
  
  public static String TrimToNull(String str, String... strip) {
    if (Utils.isEmpty(str))
      return null; 
    String result = doTrim(true, true, false, str, strip);
    if (Utils.isEmpty(result))
      return null; 
    return result;
  }
  
  public static String TrimToNull(String str, char... strip) {
    if (Utils.isEmpty(str))
      return null; 
    String result = doTrim(true, true, false, str, strip);
    if (Utils.isEmpty(result))
      return null; 
    return result;
  }
  
  public static String cTrim(String str, char... strip) {
    return doTrim(true, true, false, str, strip);
  }
  
  public static String ciTrim(String str, char... strip) {
    return doTrim(true, true, true, str, strip);
  }
  
  public static String sTrim(String str, String... strip) {
    return doTrim(true, true, false, str, strip);
  }
  
  public static String siTrim(String str, String... strip) {
    return doTrim(true, true, true, str, strip);
  }
  
  public static String cfTrim(String str, char... strip) {
    return doTrim(true, false, false, str, strip);
  }
  
  public static String cifTrim(String str, char... strip) {
    return doTrim(true, false, true, str, strip);
  }
  
  public static String sfTrim(String str, String... strip) {
    return doTrim(true, false, false, str, strip);
  }
  
  public static String sifTrim(String str, String... strip) {
    return doTrim(true, false, true, str, strip);
  }
  
  public static String ceTrim(String str, char... strip) {
    return doTrim(false, true, false, str, strip);
  }
  
  public static String cieTrim(String str, char... strip) {
    return doTrim(false, true, true, str, strip);
  }
  
  public static String seTrim(String str, String... strip) {
    return doTrim(false, true, false, str, strip);
  }
  
  public static String sieTrim(String str, String... strip) {
    return doTrim(false, true, true, str, strip);
  }
  
  private static String doTrim(boolean trimFront, boolean trimEnd, boolean ignoreCase, String str, char... strip) {
    char[] stripChars;
    String strPrep;
    if (!trimFront && !trimEnd)
      return str; 
    if (Utils.isEmpty(str))
      return str; 
    if (Utils.isEmpty(strip))
      return str; 
    int stripCount = strip.length;
    if (ignoreCase) {
      stripChars = new char[stripCount];
      for (int i = 0; i < stripCount; i++)
        stripChars[i] = Character.toLowerCase(strip[i]); 
      strPrep = str.toLowerCase();
    } else {
      stripChars = strip;
      strPrep = str;
    } 
    int size = str.length();
    int leftIndex = 0;
    int rightIndex = 0;
    boolean changed = true;
    while (changed) {
      changed = false;
      for (int index = 0; index < stripCount; index++) {
        if (trimFront)
          while (true) {
            if (leftIndex + rightIndex >= size)
              return ""; 
            if (strPrep.charAt(leftIndex) != stripChars[index])
              break; 
            leftIndex++;
            changed = true;
          }  
        if (trimEnd)
          while (true) {
            if (leftIndex + rightIndex >= size)
              return ""; 
            if (strPrep.charAt(size - rightIndex + 1) != stripChars[index])
              break; 
            rightIndex++;
            changed = true;
          }  
      } 
    } 
    return str.substring(leftIndex, size - rightIndex);
  }
  
  private static String doTrim(boolean trimFront, boolean trimEnd, boolean ignoreCase, String str, String... strip) {
    String stripStrings[], strPrep;
    if (!trimFront && !trimEnd)
      return str; 
    if (Utils.isEmpty(str))
      return str; 
    if (Utils.isEmpty(strip))
      return str; 
    int stripCount = strip.length;
    int[] stripLen = new int[stripCount];
    if (ignoreCase) {
      stripStrings = new String[stripCount];
      for (int i = 0; i < stripCount; i++) {
        stripStrings[i] = strip[i].toLowerCase();
        stripLen[i] = stripStrings[i].length();
      } 
      strPrep = str.toLowerCase();
    } else {
      stripStrings = strip;
      for (int i = 0; i < stripCount; i++)
        stripLen[i] = stripStrings[i].length(); 
      strPrep = str;
    } 
    int size = str.length();
    int leftIndex = 0;
    int rightIndex = 0;
    boolean changed = true;
    while (changed) {
      changed = false;
      for (int index = 0; index < stripCount; index++) {
        if (trimFront)
          while (true) {
            if (leftIndex + rightIndex >= size)
              return ""; 
            if (!strPrep.substring(leftIndex, leftIndex + stripLen[index]).equals(stripStrings[index]))
              break; 
            leftIndex += stripLen[index];
            changed = true;
          }  
        if (trimEnd)
          while (true) {
            if (leftIndex + rightIndex >= size)
              return ""; 
            int pos = size - rightIndex + stripLen[index];
            if (pos < 0 || 
              leftIndex + rightIndex + stripLen[index] > size || 
              !strPrep.substring(pos, pos + stripLen[index]).equals(stripStrings[index]))
              break; 
            rightIndex += stripLen[index];
            changed = true;
          }  
      } 
    } 
    return str.substring(leftIndex, size - rightIndex);
  }
  
  public static String RemoveFromString(String str, String... strip) {
    if (Utils.isEmpty(str))
      return str; 
    if (Utils.isEmpty(strip))
      return str; 
    String result = str;
    boolean changed = true;
    while (changed) {
      changed = false;
      for (String s : strip) {
        if (result.contains(s)) {
          result = result.replace(s, "");
          changed = true;
        } 
      } 
    } 
    return result;
  }
  
  public static String ForceStarts(char start, String data) {
    if (data == null)
      return null; 
    if (data.isEmpty())
      return Character.toString(start); 
    if (data.charAt(0) == start)
      return data; 
    return (new StringBuilder(data.length() + 1))
      .append(start).append(data).toString();
  }
  
  public static String ForceEnds(char end, String data) {
    if (data == null)
      return null; 
    if (data.isEmpty())
      return Character.toString(end); 
    int len = data.length();
    if (data.charAt(len - 1) == end)
      return data; 
    return (new StringBuilder(data.length() + 1))
      .append(data).append(end).toString();
  }
  
  public static String ForceStartsEnds(char start, char end, String data) {
    return ForceStarts(start, ForceEnds(end, data));
  }
  
  public static String ForceStarts(String start, String data) {
    if (data == null)
      return null; 
    if (data.startsWith(start))
      return data; 
    return (new StringBuilder(start.length() + data.length()))
      .append(start).append(data).toString();
  }
  
  public static String ForceEnds(String end, String data) {
    if (data == null)
      return null; 
    if (data.endsWith(end))
      return data; 
    return (new StringBuilder(data.length() + end.length()))
      .append(data).append(end).toString();
  }
  
  public static String ForceStartsEnds(String start, String end, String data) {
    return ForceStarts(start, ForceEnds(end, data));
  }
  
  public static String UniqueKey(Collection<String> collect, String key) {
    if (collect == null)
      throw new RequiredArgumentException("collect"); 
    if (Utils.isEmpty(key))
      throw new RequiredArgumentException("key"); 
    if (collect.isEmpty())
      return key; 
    if (!collect.contains(key))
      return key; 
    int index = 0;
    while (true) {
      index++;
      String str = key + "_" + index;
      if (!collect.contains(str))
        return str; 
    } 
  }
  
  public static String AddUnique(Collection<String> collect, String key) {
    if (collect == null)
      throw new RequiredArgumentException("collect"); 
    if (Utils.isEmpty(key))
      throw new RequiredArgumentException("key"); 
    if (collect.isEmpty()) {
      collect.add(key);
      return key;
    } 
    if (!collect.contains(key)) {
      collect.add(key);
      return key;
    } 
    int index = 0;
    while (true) {
      index++;
      String str = key + "_" + index;
      if (!collect.contains(str)) {
        collect.add(str);
        return str;
      } 
    } 
  }
  
  public static <T> String PutUnique(ConcurrentHashMap<String, T> map, String key, T value) {
    if (map == null)
      throw new RequiredArgumentException("map"); 
    if (Utils.isEmpty(key))
      throw new RequiredArgumentException("key"); 
    if (value == null)
      throw new RequiredArgumentException("value"); 
    if (map.putIfAbsent(key, value) == null)
      return key; 
    int index = 0;
    while (true) {
      index++;
      String str = key + "_" + index;
      if (map.putIfAbsent(str, value) == null)
        return str; 
    } 
  }
  
  public static <T> String PutUnique(Map<String, T> map, String key, T value) {
    if (map == null)
      throw new RequiredArgumentException("map"); 
    if (Utils.isEmpty(key))
      throw new RequiredArgumentException("key"); 
    if (value == null)
      throw new RequiredArgumentException("value"); 
    if (map.isEmpty()) {
      map.put(key, value);
      return key;
    } 
    if (!map.containsKey(key)) {
      map.put(key, value);
      return key;
    } 
    int index = 0;
    while (true) {
      index++;
      String str = key + "_" + index;
      if (!map.containsKey(str)) {
        map.put(str, value);
        return str;
      } 
    } 
  }
  
  public static String MergeStrings(String delim, String... addThis) {
    if (Utils.isEmpty(addThis))
      return ""; 
    String dlm = Utils.ifEmpty(delim, (String)null);
    StringBuilder buf = new StringBuilder();
    if (dlm == null) {
      for (String part : addThis)
        buf.append(part); 
      return buf.toString();
    } 
    boolean first = true;
    for (String part : addThis) {
      if (!Utils.isEmpty(part)) {
        if (first) {
          first = false;
        } else {
          buf.append(dlm);
        } 
        buf.append(part);
      } 
    } 
    return buf.toString();
  }
  
  public static String MergeStrings(char delim, String... addThis) {
    if (Utils.isEmpty(addThis))
      throw new RequiredArgumentException("addThis"); 
    StringBuilder buf = new StringBuilder();
    boolean first = true;
    for (String line : addThis) {
      if (!Utils.isEmpty(line)) {
        if (!first)
          buf.append(delim); 
        buf.append(line);
        if (first && buf.length() > 0)
          first = false; 
      } 
    } 
    return buf.toString();
  }
  
  public static String MergeObjects(String delim, Object... addThis) {
    if (Utils.isEmpty(addThis))
      throw new RequiredArgumentException("addThis"); 
    String[] addStrings = new String[addThis.length];
    int index = 0;
    for (Object obj : addThis) {
      addStrings[index] = ToString(obj);
      index++;
    } 
    return MergeStrings(delim, addStrings);
  }
  
  public static String MergeObjects(char delim, Object... addThis) {
    if (Utils.isEmpty(addThis))
      throw new RequiredArgumentException("addThis"); 
    String[] addStrings = new String[addThis.length];
    int index = 0;
    for (Object obj : addThis) {
      addStrings[index] = ToString(obj);
      index++;
    } 
    return MergeStrings(delim, addStrings);
  }
  
  public static String WildcardToRegex(String wildcard) {
    if (Utils.isEmpty(wildcard))
      return wildcard; 
    StringBuilder buf = new StringBuilder(wildcard.length());
    buf.append('^');
    int len = wildcard.length();
    for (int i = 0; i < len; i++) {
      char c = wildcard.charAt(i);
      switch (c) {
        case '*':
          buf.append(".*");
          break;
        case '?':
          buf.append(".");
          break;
        case '$':
        case '(':
        case ')':
        case '.':
        case '[':
        case '\\':
        case ']':
        case '^':
        case '{':
        case '|':
        case '}':
          buf.append('\\').append(c);
          break;
        default:
          buf.append(c);
          break;
      } 
    } 
    buf.append('$');
    return buf.toString();
  }
  
  public static int IndexOf(String data, int fromIndex, char... delims) {
    if (Utils.isEmpty(data))
      return -1; 
    int pos = Integer.MAX_VALUE;
    for (char delim : delims) {
      int p = data.indexOf(delim, fromIndex);
      if (p != -1)
        if (p < pos) {
          pos = p;
          if (p == 0)
            return 0; 
        }  
    } 
    return (pos == Integer.MAX_VALUE) ? -1 : pos;
  }
  
  public static int IndexOf(String data, char... delims) {
    return IndexOf(data, 0, delims);
  }
  
  public static int IndexOf(String data, int fromIndex, String... delims) {
    if (Utils.isEmpty(data))
      return -1; 
    int pos = Integer.MAX_VALUE;
    for (String delim : delims) {
      if (!Utils.isEmpty(delim)) {
        int p = data.indexOf(delim, fromIndex);
        if (p != -1)
          if (p < pos) {
            pos = p;
            if (p == 0)
              return 0; 
          }  
      } 
    } 
    return (pos == Integer.MAX_VALUE) ? -1 : pos;
  }
  
  public static int IndexOf(String data, String... delims) {
    return IndexOf(data, 0, delims);
  }
  
  public static int IndexOfLast(String data, char... delims) {
    if (Utils.isEmpty(data))
      return -1; 
    int pos = Integer.MIN_VALUE;
    for (char delim : delims) {
      int p = data.lastIndexOf(delim);
      if (p != -1)
        if (p > pos)
          pos = p;  
    } 
    return (pos == Integer.MIN_VALUE) ? -1 : pos;
  }
  
  public static int IndexOfLast(String data, String... delims) {
    if (Utils.isEmpty(data))
      return -1; 
    int pos = Integer.MIN_VALUE;
    for (String delim : delims) {
      if (!Utils.isEmpty(delim)) {
        int p = data.lastIndexOf(delim);
        if (p != -1)
          if (p > pos)
            pos = p;  
      } 
    } 
    return (pos == Integer.MIN_VALUE) ? -1 : pos;
  }
  
  public static int FindLongestLine(String[] lines) {
    if (Utils.isEmpty(lines))
      return -1; 
    int len = 0;
    for (String line : lines) {
      if (line != null && 
        line.length() > len)
        len = line.length(); 
    } 
    return len;
  }
  
  public static String FirstPart(String data, char... delims) {
    if (Utils.isEmpty(data))
      return ""; 
    if (delims.length == 0)
      throw new RequiredArgumentException("delims"); 
    int pos = IndexOf(data, delims);
    if (pos == -1)
      return data; 
    return data.substring(0, pos);
  }
  
  public static String FirstPart(String data, String... delims) {
    if (Utils.isEmpty(data))
      return ""; 
    if (delims.length == 0)
      throw new RequiredArgumentException("delims"); 
    int pos = IndexOf(data, delims);
    if (pos == -1)
      return data; 
    return data.substring(0, pos);
  }
  
  public static String LastPart(String data, char... delims) {
    if (Utils.isEmpty(data))
      return ""; 
    if (delims.length == 0)
      throw new RequiredArgumentException("delims"); 
    int pos = IndexOfLast(data, delims);
    if (pos == -1)
      return data; 
    return data.substring(pos + 1);
  }
  
  public static String LastPart(String data, String... delims) {
    if (Utils.isEmpty(data))
      return ""; 
    if (delims.length == 0)
      throw new RequiredArgumentException("delims"); 
    int pos = IndexOfLast(data, delims);
    if (pos == -1)
      return data; 
    int delimLen = 0;
    for (String delim : delims) {
      if (!Utils.isEmpty(delim)) {
        int len = delim.length();
        if (len > delimLen && 
          data.substring(pos, len).equals(delim))
          delimLen = len; 
      } 
    } 
    if (delimLen == 0)
      throw new RuntimeException("delim not found second pass"); 
    return data.substring(pos + delimLen);
  }
  
  public static String ReplaceInString(String str, String chunk, int pos) {
    if (str == null)
      throw new RequiredArgumentException("str"); 
    if (str.length() == 0)
      return chunk; 
    StringBuilder result = new StringBuilder();
    result.append(str);
    ReplaceInString(result, chunk, pos);
    return result.toString();
  }
  
  public static void ReplaceInString(StringBuilder str, String chunk, int pos) {
    if (str == null)
      throw new RequiredArgumentException("str"); 
    if (str.length() == 0)
      return; 
    int len_str = str.length();
    int len_chunk = chunk.length();
    for (int i = 0; i < len_chunk; i++) {
      if (pos + i >= len_str) {
        str.append(chunk.substring(i));
        return;
      } 
      str.setCharAt(pos + i, chunk.charAt(i));
    } 
  }
  
  public static String ReplaceWith(String str, String replaceWhat, String[] withWhat) {
    if (Utils.isEmpty(str))
      return str; 
    if (Utils.isEmpty(replaceWhat))
      return str; 
    if (Utils.isEmpty(withWhat))
      return str; 
    StringBuilder result = new StringBuilder();
    int count = withWhat.length;
    int currentPos = 0;
    for (int i = 0; i < count; i++) {
      int thisPos = str.indexOf(replaceWhat, currentPos);
      if (thisPos > 0) {
        result.append(str.substring(currentPos, thisPos));
        result.append(withWhat[i]);
        currentPos = thisPos + replaceWhat.length();
      } 
    } 
    if (str.length() > currentPos)
      result.append(str.substring(currentPos)); 
    return result.toString();
  }
  
  public static void ReplaceWith(StringBuilder str, char replaceWhat, char withWhat) {
    int len = str.length();
    for (int i = 0; i < len; i++) {
      if (str.charAt(i) == replaceWhat)
        str.setCharAt(i, withWhat); 
    } 
  }
  
  public static String ReplaceEnd(String data, char replaceWhat, char replaceWith) {
    if (Utils.isEmpty(data))
      return data; 
    int size = data.length();
    int index = 0;
    while (true) {
      char chr = data.charAt(size - index - 1);
      if (chr != replaceWhat)
        return data.substring(0, size - index) + data.substring(0, size - index); 
      index++;
      if (size == index)
        return Repeat(size, replaceWith); 
    } 
  }
  
  public static String RandomString(int length) {
    if (length < 1)
      return null; 
    StringBuilder buf = new StringBuilder(length);
    while (buf.length() < length) {
      String str = UUID.randomUUID().toString();
      if (str == null)
        throw new RequiredArgumentException("str"); 
      buf.append(str);
    } 
    return buf.toString().substring(0, NumberUtils.MinMax(length, 0, buf.length()));
  }
  
  public static String Repeat(int count, String str) {
    return Repeat(count, str, null);
  }
  
  public static String Repeat(int count, String str, String delim) {
    if (Utils.isEmpty(str))
      throw new RequiredArgumentException("str"); 
    if (count < 1)
      return ""; 
    StringBuilder result = new StringBuilder();
    if (Utils.isEmpty(delim)) {
      for (int i = 0; i < count; i++)
        result.append(str); 
    } else {
      boolean b = false;
      for (int i = 0; i < count; i++) {
        if (b)
          result.append(delim); 
        b = true;
        result.append(str);
      } 
    } 
    return result.toString();
  }
  
  public static String Repeat(int count, char chr) {
    if (count < 1)
      return ""; 
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < count; i++)
      result.append(chr); 
    return result.toString();
  }
  
  public static String PadFront(int width, String text, char padding) {
    if (width < 1)
      return null; 
    int count = width - text.length();
    if (count < 1)
      return text; 
    return (new StringBuilder(width))
      
      .append(Repeat(count, padding))
      .append(text)
      .toString();
  }
  
  public static String PadEnd(int width, String text, char padding) {
    if (width < 1)
      return null; 
    int count = width - text.length();
    if (count < 1)
      return text; 
    return (new StringBuilder(width))
      
      .append(text)
      .append(Repeat(count, padding))
      .toString();
  }
  
  public static String PadCenter(int width, String text, char padding) {
    if (width < 1)
      return null; 
    if (Utils.isEmpty(text))
      return Repeat(width, padding); 
    double count = (width - text.length()) / 2.0D;
    if (Math.ceil(count) < 1.0D)
      return text; 
    return (new StringBuilder(width))
      
      .append(Repeat((int)Math.floor(count), padding))
      .append(text)
      .append(Repeat((int)Math.ceil(count), padding))
      .toString();
  }
  
  public static String PadFront(int width, int value) {
    return PadFront(width, Integer.toString(value), '0');
  }
  
  public static String PadEnd(int width, int value) {
    return PadEnd(width, Integer.toString(value), '0');
  }
  
  public static String PadFront(int width, long value) {
    return PadFront(width, Long.toString(value), '0');
  }
  
  public static String PadEnd(int width, long value) {
    return PadEnd(width, Long.toString(value), '0');
  }
  
  public static double CompareVersions(String versionA, String versionB) {
    if (versionA.endsWith("-SNAPSHOT"))
      return CompareVersions(versionA.substring(0, versionA.length() - 9), versionB) - 0.5D; 
    if (versionB.endsWith("-SNAPSHOT"))
      return CompareVersions(versionA, versionB.substring(0, versionB.length() - 9)) + 0.5D; 
    int pos = versionA.indexOf('-');
    if (pos > 0)
      return CompareVersions(versionA.substring(0, pos), versionB); 
    pos = versionB.indexOf('-');
    if (pos > 0)
      return CompareVersions(versionA, versionB.substring(0, pos)); 
    String[] partsA = versionA.split("[.]");
    String[] partsB = versionB.split("[.]");
    int num_parts = Math.max(partsA.length, partsB.length);
    int sizeA = partsA.length;
    int sizeB = partsB.length;
    int diff = 0;
    for (int i = 0; i < num_parts; i++) {
      int pow = (int)Math.pow(10.0D, (num_parts - i - 1) * 3.0D);
      int valA = (i < sizeA) ? Integer.parseInt(partsA[i]) : 0;
      int valB = (i < sizeB) ? Integer.parseInt(partsB[i]) : 0;
      if (valA > valB || valA < valB)
        diff += (valB - valA) * pow; 
    } 
    return diff;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixso\\utils\StringUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */