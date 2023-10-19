package com.poixson.commonmc.tools.scripts;

import com.poixson.commonmc.tools.scripts.loader.ScriptLoader;
import com.poixson.commonmc.tools.scripts.loader.ScriptSourceDAO;
import com.poixson.tools.CoolDown;
import com.poixson.utils.Utils;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;

public class CraftScript {
  protected static final Logger LOG = Logger.getLogger("Minecraft");
  
  protected final ScriptLoader loader;
  
  protected final Scriptable scope;
  
  protected final ConcurrentHashMap<Thread, ScriptInstance> instances = new ConcurrentHashMap<>();
  
  protected final AtomicReference<Script[]> compiled = (AtomicReference)new AtomicReference<>(null);
  
  protected final CoolDown reloadCool = new CoolDown("5s");
  
  private static final AtomicBoolean inited = new AtomicBoolean(false);
  
  static {
    if (inited.compareAndSet(false, true))
      pxnContextFactory.init(); 
  }
  
  public CraftScript(ScriptLoader loader) {
    this(loader, true);
  }
  
  public CraftScript(ScriptLoader loader, boolean safe) {
    this.loader = loader;
    Context context = Context.enter();
    try {
      if (safe) {
        this.scope = (Scriptable)context.initStandardObjects(null, true);
      } else {
        this.scope = (Scriptable)new ImporterTopLevel(context);
      } 
      this.scope.put("out", this.scope, System.out);
    } finally {
      Utils.SafeClose((Closeable)context);
    } 
  }
  
  public Object getVariable(String name) {
    return this.scope.get(name, this.scope);
  }
  
  public void setVariable(String name, Object value) {
    this.scope.put(name, this.scope, value);
  }
  
  public void run() {
    getScriptInstance();
  }
  
  public Object call(String funcName, Object... args) {
    ScriptInstance instance = getScriptInstance();
    return instance.call(funcName, args);
  }
  
  public ScriptInstance getScriptInstance() {
    ScriptSourceDAO[] sources;
    synchronized (this.reloadCool) {
      if (this.reloadCool.again() && this.loader
        .hasChanged()) {
        LOG.info(String.format("%sReloading script: %s", new Object[] { "[pxnCommon] ", this.loader.getName() }));
        reload();
      } 
    } 
    Thread thread = Thread.currentThread();
    ScriptInstance script = this.instances.get(thread);
    if (script != null)
      return script; 
    try {
      sources = this.loader.getSources();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    } 
    Script[] compiled = getCompiledScripts(sources);
    ScriptInstance instance = new ScriptInstance(this, this.loader, this.scope, compiled);
    ScriptInstance existing = this.instances.putIfAbsent(thread, instance);
    if (existing == null) {
      instance.run();
      return instance;
    } 
    return existing;
  }
  
  protected Script[] getCompiledScripts(ScriptSourceDAO[] sources) {
    Script[] compiled = this.compiled.get();
    if (compiled != null)
      return compiled; 
    Context context = Context.enter();
    context.setOptimizationLevel(9);
    context.setLanguageVersion(200);
    try {
      LinkedList<Script> list = new LinkedList<>();
      for (ScriptSourceDAO src : sources) {
        Script script = context.compileString(src.code, src.filename, 1, null);
        list.add(script);
      } 
      Script[] arrayOfScript = list.<Script>toArray(new Script[0]);
      if (this.compiled.compareAndSet(null, arrayOfScript))
        return arrayOfScript; 
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      Utils.SafeClose((Closeable)context);
    } 
    return getCompiledScripts(sources);
  }
  
  public void reload() {
    synchronized (this.instances) {
      this.reloadCool.reset();
      this.loader.reload();
      this.compiled.set(null);
      Iterator<Thread> it = this.instances.keySet().iterator();
      while (it.hasNext()) {
        Thread key = it.next();
        ScriptInstance instance = this.instances.remove(key);
        Utils.SafeClose(instance);
      } 
    } 
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\scripts\CraftScript.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */