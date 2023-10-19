package com.poixson.utils;

import com.poixson.tools.Keeper;

public final class SanUtils {
  static {
    Keeper.add(new SanUtils());
  }
  
  public static String AlphaNum(String text) {
    return AlphaNum(text, "");
  }
  
  public static String AlphaNum(String text, String replacement) {
    if (text == null)
      return null; 
    if (text.isEmpty())
      return ""; 
    return text.replaceAll("[^a-zA-Z0-9]+", 
        
        (replacement == null) ? "" : replacement);
  }
  
  public static boolean SafeAlphaNum(String text) {
    if (text == null)
      return true; 
    String safeText = AlphaNum(text, null);
    return text.equals(safeText);
  }
  
  public static String AlphaNumUnderscore(String text) {
    return AlphaNumUnderscore(text, "");
  }
  
  public static String AlphaNumUnderscore(String text, String replacement) {
    if (text == null)
      return null; 
    if (text.isEmpty())
      return ""; 
    return text.replaceAll("[^a-zA-Z0-9\\-\\_\\.]+", 
        
        (replacement == null) ? "" : replacement);
  }
  
  public static boolean SafeAlphaNumUnderscore(String text) {
    if (text == null)
      return true; 
    String safeText = AlphaNumUnderscore(text, null);
    return text.equals(safeText);
  }
  
  public static String FileName(String text) {
    return FileName(text, "_");
  }
  
  public static String FileName(String text, String replacement) {
    if (Utils.isEmpty(text))
      return null; 
    return text.replaceAll("[^a-zA-Z0-9\\._-]+", 
        
        (replacement == null) ? "" : replacement);
  }
  
  public static boolean SafeFileName(String text) {
    if (text == null)
      return true; 
    String safeText = FileName(text, null);
    return text.equals(safeText);
  }
  
  public static String ValidateStringEnum(String value, String... valids) {
    if (Utils.isEmpty(value))
      return null; 
    if (valids.length == 0)
      return null; 
    for (String v : valids) {
      if (v != null && !v.isEmpty() && 
        v.equalsIgnoreCase(value))
        return v; 
    } 
    return null;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixso\\utils\SanUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */