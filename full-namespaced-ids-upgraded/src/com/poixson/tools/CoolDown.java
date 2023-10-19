package com.poixson.tools;

import com.poixson.utils.Utils;
import java.util.concurrent.atomic.AtomicLong;

public class CoolDown {
  protected final AtomicLong duration = new AtomicLong(0L);
  
  protected final AtomicLong last = new AtomicLong(0L);
  
  public CoolDown() {
    this(0L, 0L);
  }
  
  public CoolDown(String duration) {
    this(xTime.Parse(duration));
  }
  
  public CoolDown(xTime timeDuration) {
    this(timeDuration.ms(), 0L);
  }
  
  public CoolDown(long msDuration) {
    this(msDuration, 0L);
  }
  
  public CoolDown(long msDuration, long msLast) {
    this.duration.set(msDuration);
    this.last.set(msLast);
  }
  
  public boolean again() {
    return again(getCurrent());
  }
  
  public boolean again(long current) {
    long duration = this.duration.get();
    if (duration <= 0L)
      return false; 
    long last = this.last.get();
    if (last <= 0L) {
      if (this.last.compareAndSet(last, current))
        return true; 
      return again();
    } 
    if (current - last >= duration) {
      if (this.last.compareAndSet(last, current))
        return true; 
      return again();
    } 
    return false;
  }
  
  public void reset() {
    reset(getCurrent());
  }
  
  public void reset(long time) {
    this.last.set(time);
  }
  
  public long getCurrent() {
    return Utils.GetMS();
  }
  
  public long getLast() {
    return this.last.get();
  }
  
  public long getTimeSinceLast() {
    long last = this.last.get();
    if (last <= 0L)
      return last; 
    return getCurrent() - last;
  }
  
  public long getTimeUntilNext() {
    long last = this.last.get();
    if (last <= 0L)
      return last; 
    long duration = this.duration.get();
    if (duration <= 0L)
      return -1L; 
    return last + duration - getCurrent();
  }
  
  public long getDuration() {
    return this.duration.get();
  }
  
  public void setDuration(String time) {
    setDuration(xTime.Parse(time));
  }
  
  public void setDuration(xTime time) {
    setDuration(time.ms());
  }
  
  public void setDuration(long ms) {
    this.duration.set(ms);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\CoolDown.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */