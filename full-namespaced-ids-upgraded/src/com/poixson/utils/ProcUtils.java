package com.poixson.utils;

import com.poixson.tools.Keeper;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class ProcUtils {
  static {
    Keeper.add(new ProcUtils());
  }
  
  private static final AtomicInteger pid = new AtomicInteger(-2147483648);
  
  private static final boolean debugWireEnabled = initDebugWire();
  
  public static boolean isDebugWireEnabled() {
    return debugWireEnabled;
  }
  
  private static boolean initDebugWire() {
    RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
    if (bean != null) {
      List<String> args = bean.getInputArguments();
      if (args != null) {
        String argsStr = args.toString();
        if (argsStr.indexOf("jdwp") >= 0)
          return true; 
      } 
    } 
    return false;
  }
  
  public static int getPid() {
    if (pid.get() == Integer.MIN_VALUE) {
      int value = -1;
      RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
      if (bean != null) {
        String procName = bean.getName();
        if (Utils.notEmpty(procName)) {
          String[] parts = procName.split("@", 2);
          if (parts != null && 
            parts.length == 2)
            value = NumberUtils.ToInteger(parts[0], -1); 
        } 
      } 
      pid.set(value);
    } 
    return pid.get();
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixso\\utils\ProcUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */