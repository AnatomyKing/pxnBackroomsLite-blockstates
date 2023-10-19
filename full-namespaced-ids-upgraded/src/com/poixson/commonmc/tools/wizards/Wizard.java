package com.poixson.commonmc.tools.wizards;

import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.commonmc.tools.wizards.steps.WizardStep;
import com.poixson.commonmc.utils.BukkitUtils;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class Wizard<T extends xJavaPlugin> {
  protected final String logPrefix;
  
  protected final String chatPrefix;
  
  protected final T plugin;
  
  protected final Player player;
  
  protected final LinkedList<WizardStep<T>> steps = new LinkedList<>();
  
  protected final BukkitTask timeoutTask;
  
  protected final AtomicInteger timeoutCount = new AtomicInteger(0);
  
  protected final int timeoutSeconds;
  
  public Wizard(T plugin, Player player, String logPrefix, String chatPrefix) {
    this.logPrefix = logPrefix;
    this.chatPrefix = chatPrefix;
    this.plugin = plugin;
    this.player = player;
    this.timeoutSeconds = 30;
    Runnable run = new Runnable() {
        public void run() {
          Wizard.this.timeout();
        }
      };
    this
      
      .timeoutTask = Bukkit.getScheduler().runTaskTimer((Plugin)plugin, run, 20L, 20L);
  }
  
  public void start() {
    next();
  }
  
  public void next() {
    Runnable run = new Runnable() {
        public void run() {
          Wizard.this.doNext();
        }
      };
    Bukkit.getScheduler()
      .runTask((Plugin)this.plugin, run);
  }
  
  protected void doNext() {
    for (WizardStep<T> step : this.steps) {
      if (!step.isCompleted()) {
        try {
          step.run();
        } catch (Exception e) {
          sendMessage(this.chatPrefix + "ERROR: " + this.chatPrefix);
          throw e;
        } 
        return;
      } 
    } 
    finished();
  }
  
  public void finished() {
    try {
      this.timeoutTask.cancel();
    } catch (IllegalStateException illegalStateException) {}
  }
  
  public void cancel() {
    try {
      this.timeoutTask.cancel();
    } catch (IllegalStateException illegalStateException) {}
    for (WizardStep<T> step : this.steps)
      step.close(); 
  }
  
  public void timeout() {
    int count = this.timeoutCount.incrementAndGet();
    if (count >= this.timeoutSeconds) {
      sendMessage(this.chatPrefix + "Wizard timeout.");
      sendMessage("");
      cancel();
    } 
  }
  
  public void resetTimeout() {
    this.timeoutCount.set(0);
  }
  
  public void addStep(WizardStep<T> step) {
    this.steps.addLast(step);
    resetTimeout();
  }
  
  public WizardStep<T>[] getSteps() {
    return this.steps.<WizardStep<T>>toArray((WizardStep<T>[])new WizardStep[0]);
  }
  
  public int getStepsCount() {
    return this.steps.size();
  }
  
  public T getPlugin() {
    return this.plugin;
  }
  
  public Player getPlayer() {
    return this.player;
  }
  
  public boolean isPlayer(Player player) {
    return BukkitUtils.EqualsPlayer(player, this.player);
  }
  
  public void sendMessage(String msg) {
    this.player.sendMessage(msg);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\wizards\Wizard.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */