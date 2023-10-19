package com.poixson.tools.byref;

import com.poixson.utils.StringUtils;
import com.poixson.utils.Utils;

public class StringRef implements StringRefInterface {
  public volatile String value = null;
  
  public static StringRef getNew(String val) {
    return new StringRef(val);
  }
  
  public StringRef(String val) {
    this.value = val;
  }
  
  public StringRef() {}
  
  public void value(String val) {
    this.value = val;
  }
  
  public String value() {
    return this.value;
  }
  
  public boolean isEmpty() {
    return Utils.isEmpty(this.value);
  }
  
  public boolean notEmpty() {
    return Utils.notEmpty(this.value);
  }
  
  public int length() {
    String val = this.value;
    return 
      (val == null) ? 
      0 : 
      val.length();
  }
  
  public int indexOf(char delim) {
    String val = this.value;
    return 
      (val == null) ? 
      -1 : 
      val.indexOf(delim);
  }
  
  public int indexOf(String delim) {
    String val = this.value;
    return 
      (val == null) ? 
      -1 : 
      val.indexOf(delim);
  }
  
  public int indexOf(char... delims) {
    String val = this.value;
    return 
      (val == null) ? 
      -1 : 
      StringUtils.IndexOf(val, delims);
  }
  
  public int indexOf(String... delims) {
    String val = this.value;
    return 
      (val == null) ? 
      -1 : 
      StringUtils.IndexOf(val, delims);
  }
  
  public int indexOfLast(char delim) {
    String val = this.value;
    return 
      (val == null) ? 
      -1 : 
      val.lastIndexOf(delim);
  }
  
  public int indexOfLast(String delim) {
    String val = this.value;
    return 
      (val == null) ? 
      -1 : 
      val.lastIndexOf(delim);
  }
  
  public int indexOfLast(char... delims) {
    String val = this.value;
    return 
      (val == null) ? 
      -1 : 
      StringUtils.IndexOfLast(val, delims);
  }
  
  public int indexOfLast(String... delims) {
    String val = this.value;
    return 
      (val == null) ? 
      -1 : 
      StringUtils.IndexOfLast(val, delims);
  }
  
  public String cutFirstPart(char delim) {
    String val = this.value;
    if (Utils.isEmpty(val))
      return val; 
    int pos = val.indexOf(delim);
    if (pos == -1) {
      this.value = "";
      return val;
    } 
    String result = val.substring(0, pos);
    this.value = val.substring(pos + 1);
    return result;
  }
  
  public String cutFirstPart(String delim) {
    String val = this.value;
    if (Utils.isEmpty(val))
      return val; 
    int pos = val.indexOf(delim);
    if (pos == -1) {
      this.value = "";
      return val;
    } 
    String result = val.substring(0, pos);
    this.value = val.substring(pos + delim.length());
    return result;
  }
  
  public String cutFirstPart(char... delims) {
    String val = this.value;
    if (Utils.isEmpty(val))
      return val; 
    int pos = Integer.MAX_VALUE;
    for (char delim : delims) {
      int p = val.indexOf(delim);
      if (p != -1)
        if (p < pos) {
          pos = p;
          if (p == 0)
            break; 
        }  
    } 
    if (pos == Integer.MAX_VALUE) {
      this.value = "";
      return val;
    } 
    String result = val.substring(0, pos);
    this.value = val.substring(pos + 1);
    return result;
  }
  
  public String cutFirstPart(String... delims) {
    String val = this.value;
    if (Utils.isEmpty(val))
      return val; 
    int pos = Integer.MAX_VALUE;
    int delimSize = 0;
    for (String delim : delims) {
      if (!Utils.isEmpty(delim)) {
        int p = val.indexOf(delim);
        if (p != -1)
          if (p == pos) {
            if (delim.length() > delimSize)
              delimSize = delim.length(); 
          } else if (p < pos) {
            pos = p;
            delimSize = delim.length();
          }  
      } 
    } 
    if (pos == Integer.MAX_VALUE) {
      this.value = "";
      return val;
    } 
    String result = val.substring(0, pos);
    this.value = val.substring(pos + delimSize);
    return result;
  }
  
  public String cutLastPart(char delim) {
    String val = this.value;
    if (Utils.isEmpty(val))
      return val; 
    int pos = val.lastIndexOf(delim);
    if (pos == -1) {
      this.value = "";
      return val;
    } 
    String result = val.substring(pos + 1);
    this.value = val.substring(0, pos);
    return result;
  }
  
  public String cutLastPart(String delim) {
    String val = this.value;
    if (Utils.isEmpty(val))
      return val; 
    int pos = val.lastIndexOf(delim);
    if (pos == -1) {
      this.value = "";
      return val;
    } 
    String result = val.substring(pos + delim.length());
    this.value = val.substring(0, pos);
    return result;
  }
  
  public String cutLastPart(char... delims) {
    String val = this.value;
    if (Utils.isEmpty(val))
      return val; 
    int pos = Integer.MIN_VALUE;
    for (char delim : delims) {
      int p = val.lastIndexOf(delim);
      if (p != -1)
        if (p > pos) {
          pos = p;
          if (p == 0)
            break; 
        }  
    } 
    if (pos == Integer.MIN_VALUE) {
      this.value = "";
      return val;
    } 
    String result = val.substring(pos + 1);
    this.value = val.substring(0, pos);
    return result;
  }
  
  public String cutLastPart(String... delims) {
    String val = this.value;
    if (Utils.isEmpty(val))
      return val; 
    int pos = Integer.MIN_VALUE;
    int delimSize = 0;
    for (String delim : delims) {
      if (!Utils.isEmpty(delim)) {
        int p = val.lastIndexOf(delim);
        if (p != -1)
          if (p == pos) {
            if (delim.length() > delimSize)
              delimSize = delim.length(); 
          } else if (p > pos) {
            pos = p;
            delimSize = delim.length();
          }  
      } 
    } 
    if (pos == Integer.MIN_VALUE) {
      this.value = "";
      return val;
    } 
    String result = val.substring(pos + delimSize);
    this.value = val.substring(0, pos);
    return result;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\byref\StringRef.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */