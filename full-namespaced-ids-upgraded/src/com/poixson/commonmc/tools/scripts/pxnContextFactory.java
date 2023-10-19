package com.poixson.commonmc.tools.scripts;

import com.poixson.tools.Keeper;
import java.util.concurrent.atomic.AtomicBoolean;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;

public class pxnContextFactory extends ContextFactory {
  protected static final AtomicBoolean inited = new AtomicBoolean(false);
  
  public static void init() {
    if (inited.compareAndSet(false, true)) {
      pxnContextFactory factory = new pxnContextFactory();
      ContextFactory.initGlobal(factory);
      Keeper.add(factory);
    } 
  }
  
  protected Context makeContext() {
    Context context = super.makeContext();
    context.setLanguageVersion(200);
    context.setOptimizationLevel(9);
    return context;
  }
  
  protected boolean hasFeature(Context context, int featureIndex) {
    switch (featureIndex) {
      case 8:
      case 10:
      case 11:
      case 21:
        return true;
    } 
    return super.hasFeature(context, featureIndex);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\scripts\pxnContextFactory.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */