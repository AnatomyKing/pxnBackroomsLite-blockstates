package com.poixson.commonmc.tools.wizards.steps;

import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.commonmc.tools.wizards.Wizard;
import com.poixson.commonmc.utils.BukkitUtils;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

public abstract class WizardStep_Ask<T extends xJavaPlugin> extends WizardStep<T> implements Listener {
  protected final String question;
  
  protected final AtomicReference<String> answer = new AtomicReference<>(null);
  
  public WizardStep_Ask(Wizard<T> wizard, String logPrefix, String chatPrefix, String question) {
    super(wizard, logPrefix, chatPrefix);
    this.question = question;
  }
  
  public void run() {
    sendClear();
    sendProgress(this.question);
    Bukkit.getPluginManager()
      .registerEvents(this, (Plugin)this.wizard.getPlugin());
    this.wizard.resetTimeout();
  }
  
  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent event) {
    if (isCompleted()) {
      close();
      return;
    } 
    if (!BukkitUtils.EqualsPlayer(getPlayer(), event.getPlayer()))
      return; 
    event.setCancelled(true);
    this.answer.set(event.getMessage());
    if (!validateAnswer()) {
      this.answer.set(null);
      sendMessage("Invalid answer. Please try again");
      return;
    } 
    sendReply();
    if (this.state.compareAndSet(null, Boolean.TRUE))
      this.wizard.next(); 
  }
  
  public abstract boolean validateAnswer();
  
  public abstract void sendReply();
  
  public void close() {
    super.close();
    HandlerList.unregisterAll(this);
  }
  
  public String getAnswer() {
    return this.answer.get();
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\wizards\steps\WizardStep_Ask.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */