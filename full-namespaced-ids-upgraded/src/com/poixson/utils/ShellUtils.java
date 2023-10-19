package com.poixson.utils;

import com.poixson.tools.Keeper;
import java.util.concurrent.atomic.AtomicReference;

public class ShellUtils {
  protected static final AtomicReference<ShellUtils> instance = new AtomicReference<>(null);
  
  public enum AnsiColor {
    BLACK, RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN, WHITE;
  }
  
  protected static ShellUtils Get() {
    if (instance.get() == null) {
      Class<ShellUtils> clss = ReflectUtils.GetClass("com.poixson.utils.ShellUtils_Extended");
      if (clss != null) {
        ShellUtils shellUtils = ReflectUtils.<ShellUtils>NewInstance(clss, new Object[0]);
        if (shellUtils == null)
          throw new RuntimeException("Unable to initialize ShellUtils instance"); 
        if (instance.compareAndSet(null, shellUtils))
          return shellUtils; 
        return instance.get();
      } 
      ShellUtils utility = new ShellUtils();
      if (instance.compareAndSet(null, utility))
        return utility; 
    } 
    return instance.get();
  }
  
  protected ShellUtils() {
    Keeper.add(this);
  }
  
  public static String RenderAnsi(String line) {
    return Get()._renderAnsi(line);
  }
  
  protected String _renderAnsi(String line) {
    return StripColorTags(line);
  }
  
  public static String[] RenderAnsi(String[] lines) {
    return Get()._renderAnsi(lines);
  }
  
  protected String[] _renderAnsi(String[] lines) {
    return StripColorTags(lines);
  }
  
  public static String StripColorTags(String line) {
    if (Utils.isEmpty(line))
      return line; 
    StringBuilder result = new StringBuilder(line);
    boolean changed = false;
    while (true) {
      int posA = result.indexOf("@|");
      if (posA == -1)
        break; 
      int posB = result.indexOf(" ", posA);
      int posC = result.indexOf("|@", posB);
      if (posB == -1 || 
        posC == -1)
        break; 
      result.replace(posC, posC + 2, "");
      result.replace(posA, posB + 1, "");
      changed = true;
    } 
    if (changed)
      return result.toString(); 
    return line;
  }
  
  public static String[] StripColorTags(String[] lines) {
    if (Utils.isEmpty(lines))
      return lines; 
    String[] result = new String[lines.length];
    for (int index = 0; index < lines.length; index++)
      result[index] = StripColorTags(result[index]); 
    return result;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixso\\utils\ShellUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */