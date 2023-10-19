package com.poixson.logger;

import com.poixson.utils.NumberUtils;
import com.poixson.utils.Utils;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;

public enum xLevel {
  OFF('x', "off", 2147483647),
  ALL('a', "all", -2147483648),
  TITLE('t', "title", 9000),
  DETAIL('0', "detail", 100),
  FINEST('1', "finest", 200),
  FINER('2', "finer", 300),
  FINE('3', "fine", 400),
  STATS('s', "stats", 500),
  INFO('i', "info", 600),
  WARNING('w', "warning", 700),
  NOTICE('n', "notice", 800),
  SEVERE('s', "severe", 900),
  FATAL('f', "fatal", 1000);
  
  public final char chr;
  
  public final String name;
  
  public final int value;
  
  private static final CopyOnWriteArrayList<xLevel> levels;
  
  static {
    levels = new CopyOnWriteArrayList<xLevel>() {
        private static final long serialVersionUID = 1L;
      };
  }
  
  xLevel(char chr, String name, int value) {
    this.chr = chr;
    this.name = name;
    this.value = value;
  }
  
  public static xLevel[] Levels() {
    return levels.<xLevel>toArray(new xLevel[0]);
  }
  
  public static xLevel GetLevel(String name) {
    if (Utils.isEmpty(name))
      return null; 
    if (NumberUtils.IsNumeric(name))
      return GetLevel(NumberUtils.ToInteger(name)); 
    Iterator<xLevel> it = levels.iterator();
    while (it.hasNext()) {
      xLevel level = it.next();
      if (name.equalsIgnoreCase(level.name))
        return level; 
    } 
    return null;
  }
  
  public static xLevel GetLevel(Integer value) {
    if (value == null)
      return null; 
    int val = value.intValue();
    if (val == ALL.value)
      return ALL; 
    if (val == OFF.value)
      return OFF; 
    xLevel found = OFF;
    int offset = OFF.value;
    Iterator<xLevel> it = levels.iterator();
    while (it.hasNext()) {
      xLevel lvl = it.next();
      if (!OFF.equals(lvl) && 
        !ALL.equals(lvl) && 
        val >= lvl.value && 
        val - lvl.value < offset) {
        offset = val - lvl.value;
        found = lvl;
      } 
    } 
    return found;
  }
  
  public static xLevel FromJavaLevel(Level lvl) {
    if (lvl.equals(Level.ALL))
      return ALL; 
    if (lvl.equals(Level.FINEST))
      return FINEST; 
    if (lvl.equals(Level.FINER))
      return FINER; 
    if (lvl.equals(Level.FINE))
      return FINE; 
    if (lvl.equals(Level.CONFIG))
      return STATS; 
    if (lvl.equals(Level.INFO))
      return INFO; 
    if (lvl.equals(Level.WARNING))
      return WARNING; 
    if (lvl.equals(Level.SEVERE))
      return SEVERE; 
    if (lvl.equals(Level.OFF))
      return OFF; 
    return null;
  }
  
  public boolean isLoggable(xLevel level) {
    if (level == null)
      return false; 
    if (this.value == OFF.value)
      return false; 
    if (this.value == ALL.value)
      return true; 
    if (level.value == ALL.value)
      return true; 
    return (this.value <= level.value);
  }
  
  public boolean notLoggable(xLevel level) {
    if (level == null)
      return false; 
    return !isLoggable(level);
  }
  
  public boolean equals(xLevel level) {
    if (level == null)
      return false; 
    return (level.value == this.value);
  }
  
  public String toString() {
    return this.name;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\logger\xLevel.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */