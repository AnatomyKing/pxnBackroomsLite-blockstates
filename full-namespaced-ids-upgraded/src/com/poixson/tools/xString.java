package com.poixson.tools;

import com.poixson.utils.StringUtils;
import com.poixson.utils.Utils;

public class xString {
  protected String data = null;
  
  public static xString getNew() {
    return new xString("");
  }
  
  public static xString getNew(String data) {
    return new xString(data);
  }
  
  public static xString getNew(Object obj) {
    return new xString(obj);
  }
  
  public xString(String data) {
    this.data = data;
  }
  
  public xString(Object obj) {
    this.data = StringUtils.ToString(obj);
  }
  
  public String toString() {
    return this.data;
  }
  
  public xString set(String data) {
    this.data = data;
    return this;
  }
  
  public boolean isEmpty() {
    return Utils.isEmpty(this.data);
  }
  
  public boolean notEmpty() {
    return Utils.notEmpty(this.data);
  }
  
  public xString append(String append) {
    this
      
      .data = (this.data == null) ? append : (this.data + this.data);
    return this;
  }
  
  public xString remove(String... strip) {
    String data = this.data;
    if (data != null)
      this.data = StringUtils.RemoveFromString(data, strip); 
    return this;
  }
  
  public xString upper() {
    String data = this.data;
    if (data != null)
      this.data = data.toUpperCase(); 
    return this;
  }
  
  public xString lower() {
    String data = this.data;
    if (data != null)
      this.data = data.toLowerCase(); 
    return this;
  }
  
  public xString trim() {
    String data = this.data;
    if (data != null)
      this.data = data.trim(); 
    return this;
  }
  
  public xString trims(String... strip) {
    String data = this.data;
    if (data != null)
      this.data = StringUtils.sTrim(data, strip); 
    return this;
  }
  
  public boolean startsWith(String prefix) {
    String data = this.data;
    if (data == null)
      return false; 
    return data.startsWith(prefix);
  }
  
  public boolean endsWith(String suffix) {
    String data = this.data;
    if (data == null)
      return false; 
    return data.endsWith(suffix);
  }
  
  public boolean contains(CharSequence str) {
    String data = this.data;
    if (data == null)
      return false; 
    return data.contains(str);
  }
  
  public int indexOf(String... delims) {
    String data = this.data;
    if (data == null)
      return -1; 
    return StringUtils.IndexOf(data, delims);
  }
  
  public int indexOf(int fromIndex, String... delims) {
    String data = this.data;
    if (data == null)
      return -1; 
    return StringUtils.IndexOf(data, fromIndex, delims);
  }
  
  public int indexOfLast(String... delims) {
    String data = this.data;
    if (data == null)
      return -1; 
    return StringUtils.IndexOfLast(data, delims);
  }
  
  public xString ensureStarts(String start) {
    String data = this.data;
    if (data != null)
      this.data = StringUtils.ForceStarts(start, data); 
    return this;
  }
  
  public xString ensureEnds(String end) {
    String data = this.data;
    if (data != null)
      this.data = StringUtils.ForceEnds(end, data); 
    return this;
  }
  
  public xString replaceWith(String replaceWhat, String[] withWhat) {
    String data = this.data;
    if (data != null)
      this.data = StringUtils.ReplaceWith(data, replaceWhat, withWhat); 
    return this;
  }
  
  public xString padFront(int width, char padding) {
    String data = this.data;
    if (data != null)
      this.data = StringUtils.PadFront(width, data, padding); 
    return this;
  }
  
  public xString padEnd(int width, char padding) {
    String data = this.data;
    if (data != null)
      this.data = StringUtils.PadEnd(width, data, padding); 
    return this;
  }
  
  public xString padCenter(int width, char padding) {
    String data = this.data;
    if (data != null)
      this.data = StringUtils.PadCenter(width, data, padding); 
    return this;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\xString.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */