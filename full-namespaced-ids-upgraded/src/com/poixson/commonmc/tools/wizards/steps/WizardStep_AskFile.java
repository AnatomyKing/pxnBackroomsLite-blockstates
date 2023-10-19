package com.poixson.commonmc.tools.wizards.steps;

import com.poixson.commonmc.tools.plugin.xJavaPlugin;
import com.poixson.commonmc.tools.wizards.Wizard;
import com.poixson.utils.SanUtils;
import com.poixson.utils.Utils;
import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

public abstract class WizardStep_AskFile<T extends xJavaPlugin> extends WizardStep_Ask<T> {
  protected final File path;
  
  protected final String filePattern;
  
  protected final AtomicReference<File> answerFile = new AtomicReference<>(null);
  
  public WizardStep_AskFile(Wizard<T> wizard, String logPrefix, String chatPrefix, String question, File path, String filePattern) {
    super(wizard, logPrefix, chatPrefix, question);
    if (!filePattern.contains("*"))
      throw new IllegalArgumentException("Invalid file pattern: " + filePattern); 
    this.path = path;
    this.filePattern = filePattern;
  }
  
  public boolean validateAnswer() {
    String answer = getAnswer();
    if (Utils.notEmpty(answer)) {
      String[] parts = this.filePattern.split("[*]");
      String pathStr = parts[0] + SanUtils.FileName(answer) + parts[1];
      File file = new File(this.path, pathStr);
      if (!file.isFile()) {
        sendMessage("File not found: " + file.getName());
        this.answer.set(null);
        return false;
      } 
      if (!file.canRead()) {
        sendMessage("Cannot read file: " + file.getName());
        this.answer.set(null);
        return false;
      } 
      this.answerFile.set(file);
      return true;
    } 
    this.answer.set(null);
    return false;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\wizards\steps\WizardStep_AskFile.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */