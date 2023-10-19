package com.poixson.commonmc.tools.wizards.steps;

import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.commonmc.tools.wizards.Wizard;
import com.poixson.utils.Utils;
import java.io.Closeable;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class WizardStep<T extends xJavaPlugin> implements Runnable, Closeable {
  protected final String logPrefix;
  
  protected final String chatPrefix;
  
  protected final Wizard<T> wizard;
  
  protected final int stepIndex;
  
  protected final AtomicReference<Boolean> state = new AtomicReference<>(null);
  
  public WizardStep(Wizard<T> wizard, String logPrefix, String chatPrefix) {
    this.wizard = wizard;
    this.stepIndex = wizard.getStepsCount() + 1;
    this.logPrefix = logPrefix;
    this.chatPrefix = chatPrefix;
  }
  
  public abstract void run();
  
  public void close() {
    this.state.compareAndSet(null, Boolean.FALSE);
    this.wizard.resetTimeout();
  }
  
  public boolean isCompleted() {
    return (this.state.get() != null);
  }
  
  public boolean isSuccess() {
    Boolean result = this.state.get();
    if (result == null)
      return false; 
    return result.booleanValue();
  }
  
  public boolean isCanceled() {
    Boolean result = this.state.get();
    if (result == null)
      return false; 
    return !result.booleanValue();
  }
  
  public JavaPlugin getPlugin() {
    return (JavaPlugin)this.wizard.getPlugin();
  }
  
  public Player getPlayer() {
    return this.wizard.getPlayer();
  }
  
  public void sendMessage(String msg) {
    if (Utils.isEmpty(msg)) {
      this.wizard.sendMessage("");
    } else {
      this.wizard.sendMessage(this.chatPrefix + this.chatPrefix);
    } 
  }
  
  public void sendProgress(String msg) {
    this.wizard.sendMessage(
        String.format("[%d%%] %s", new Object[] { Long.valueOf(Math.round(this.stepIndex / this.wizard.getStepsCount() * 100.0D)), msg }));
  }
  
  public void sendClear() {
    this.wizard.sendMessage("");
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\wizards\steps\WizardStep.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */