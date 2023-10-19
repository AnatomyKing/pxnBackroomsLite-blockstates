package com.poixson.tools;

import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.logger.xLog;
import com.poixson.utils.ProcUtils;
import com.poixson.utils.ReflectUtils;
import com.poixson.utils.Utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.ref.SoftReference;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class xLockFile {
  private static final ConcurrentHashMap<String, xLockFile> instances = new ConcurrentHashMap<>();
  
  public final File file;
  
  protected FileLock lock = null;
  
  protected FileChannel channel = null;
  
  protected RandomAccessFile handle = null;
  
  private final AtomicReference<SoftReference<xLog>> _log;
  
  public static xLockFile Get(String filename) {
    if (Utils.isEmpty(filename))
      throw new RequiredArgumentException("filename"); 
    xLockFile lock = instances.get(filename);
    if (lock != null)
      return lock; 
    lock = new xLockFile(filename);
    xLockFile existing = instances.putIfAbsent(filename, lock);
    if (existing != null)
      return existing; 
    Keeper.add(lock);
    return lock;
  }
  
  public static xLockFile Peek(String filename) {
    if (Utils.isEmpty(filename))
      throw new RequiredArgumentException("filename"); 
    return instances.get(filename);
  }
  
  public static xLockFile Lock(String filename) {
    xLockFile lock = Get(filename);
    if (lock == null)
      return null; 
    if (!lock.acquire())
      return null; 
    return lock;
  }
  
  public static boolean Release(String filename) {
    xLockFile lock = Get(filename);
    if (lock != null) {
      Keeper.remove(lock);
      return lock.release();
    } 
    return false;
  }
  
  public boolean isLocked() {
    return (this.lock != null);
  }
  
  public boolean acquire() {
    if (this.lock != null)
      return true; 
    try {
      this.handle = new RandomAccessFile(this.file, "rw");
    } catch (FileNotFoundException e) {
      log().trace(e);
      Utils.SafeClose(this.handle);
      this.handle = null;
      return false;
    } 
    this.channel = this.handle.getChannel();
    try {
      this.lock = this.channel.tryLock();
      if (this.lock == null) {
        Utils.SafeClose(this.handle);
        Utils.SafeClose(this.channel);
        this.handle = null;
        this.channel = null;
        return false;
      } 
      int pid = ProcUtils.getPid();
      this.handle.write(
          Integer.toString(pid).getBytes());
    } catch (OverlappingFileLockException e) {
      log().trace(e);
      Utils.SafeClose(this.lock);
      Utils.SafeClose(this.handle);
      Utils.SafeClose(this.channel);
      this.lock = null;
      this.handle = null;
      this.channel = null;
      return false;
    } catch (IOException e) {
      log().trace(e);
      Utils.SafeClose(this.lock);
      Utils.SafeClose(this.handle);
      Utils.SafeClose(this.channel);
      this.lock = null;
      this.handle = null;
      this.channel = null;
      return false;
    } 
    log().fine("Locked file:", new Object[] { this.file.getName() });
    return true;
  }
  
  public boolean release() {
    if (this.lock == null)
      return false; 
    try {
      this.lock.release();
    } catch (Exception exception) {}
    Utils.SafeClose(this.lock);
    Utils.SafeClose(this.channel);
    Utils.SafeClose(this.handle);
    this.lock = null;
    this.channel = null;
    this.handle = null;
    log().fine("Released file lock:", new Object[] { this.file.getName() });
    try {
      this.file.delete();
    } catch (Exception exception) {}
    Keeper.remove(this);
    return true;
  }
  
  protected xLockFile(String filename) {
    this._log = new AtomicReference<>(null);
    if (Utils.isEmpty(filename))
      throw new RequiredArgumentException("filename"); 
    this.file = new File(filename);
    Runtime.getRuntime().addShutdownHook(new Thread() {
          public void run() {
            xLockFile.this.release();
          }
        });
  }
  
  public xLog log() {
    SoftReference<xLog> ref = this._log.get();
    if (ref != null) {
      xLog xLog = ref.get();
      if (xLog != null)
        return xLog; 
    } 
    xLog log = _log();
    SoftReference<xLog> softReference1 = new SoftReference<>(log);
    if (this._log.compareAndSet(null, softReference1))
      return log; 
    return log();
  }
  
  protected xLog _log() {
    return xLog.Get(ReflectUtils.GetClassName(this));
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\xLockFile.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */