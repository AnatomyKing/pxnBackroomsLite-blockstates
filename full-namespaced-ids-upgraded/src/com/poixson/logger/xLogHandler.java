package com.poixson.logger;

import com.poixson.utils.StringUtils;
import com.poixson.utils.Utils;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

public abstract class xLogHandler {
  protected final ReentrantLock lock = new ReentrantLock(true);
  
  protected final AtomicReference<xLevel> level = new AtomicReference<>(null);
  
  protected final AtomicReference<xLogFormat> format = new AtomicReference<>(null);
  
  public void publish() {
    publish((String[])null);
  }
  
  public void publish(String msg) {
    if (Utils.isEmpty(msg)) {
      publish();
      return;
    } 
    String trimmed = StringUtils.cTrim(msg, new char[] { '\n' });
    String[] lines = trimmed.split("\n");
    if (Utils.isEmpty(lines)) {
      publish();
      return;
    } 
    publish(lines);
  }
  
  public void publish(xLogRecord record) {
    if (notLoggable(record.getLevel()))
      return; 
    if (record == null || record.isEmpty()) {
      publish();
      return;
    } 
    publish(format(record));
  }
  
  protected abstract void publish(String[] paramArrayOfString);
  
  public abstract void flush();
  
  public abstract void clearScreen();
  
  public abstract void beep();
  
  public void getPublishLock() {
    getPublishLock(this.lock);
  }
  
  public void releasePublishLock() {
    releasePublishLock(this.lock);
  }
  
  protected void getPublishLock(ReentrantLock lock) {
    for (int i = 0; i < 50; i++) {
      try {
        if (lock.tryLock(5L, TimeUnit.MILLISECONDS))
          return; 
      } catch (InterruptedException e) {
        break;
      } 
      if (Thread.interrupted())
        break; 
    } 
  }
  
  protected void releasePublishLock(ReentrantLock lock) {
    try {
      lock.unlock();
    } catch (IllegalMonitorStateException illegalMonitorStateException) {}
  }
  
  public String format(xLogRecord record) {
    xLogFormat format = this.format.get();
    if (format == null)
      return record.toString(); 
    return format.format(record);
  }
  
  public xLogFormat getFormat() {
    return this.format.get();
  }
  
  public void setFormat(xLogFormat format) {
    this.format.set(format);
  }
  
  public xLevel getLevel() {
    return this.level.get();
  }
  
  public void setLevel(xLevel level) {
    this.level.set(level);
  }
  
  public boolean isLoggable(xLevel level) {
    if (level == null)
      return true; 
    xLevel current = getLevel();
    if (current == null)
      return true; 
    return current.isLoggable(level);
  }
  
  public boolean notLoggable(xLevel level) {
    return !isLoggable(level);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\logger\xLogHandler.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */