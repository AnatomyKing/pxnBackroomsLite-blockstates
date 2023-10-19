package com.poixson.utils;

import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.logger.xLog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CopyOnWriteArraySet;

public final class NativeUtils {
  private static final int EXTRACT_BUFFER_SIZE = 4096;
  
  private static final CopyOnWriteArraySet<String> libsLoaded = new CopyOnWriteArraySet<>();
  
  public static boolean SafeLoad(String fileStr) {
    try {
      LoadLibrary(new String[] { fileStr });
    } catch (SecurityException e) {
      log().severe(e.getMessage(), new Object[0]);
      log().severe("Failed to load library: %s  %s", new Object[] { fileStr, e
            
            .getMessage() });
      return false;
    } catch (UnsatisfiedLinkError e) {
      log().severe(e.getMessage(), new Object[0]);
      log().severe("Failed to load library: %s  %s", new Object[] { fileStr, e
            
            .getMessage() });
      return false;
    } 
    return true;
  }
  
  public static void LoadLibrary(String... filePath) throws SecurityException, UnsatisfiedLinkError {
    String pathStr = FileUtils.MergePaths(filePath);
    if (!libsLoaded.add(pathStr)) {
      log().detail("Library already loaded:", new Object[] { pathStr });
      return;
    } 
    log().detail("NativeUtils::LoadLibrary(path=%s)", new Object[] { pathStr });
    System.load(pathStr);
  }
  
  public static void ExtractLibrary(String outputDir, String resourcePath, String fileName, Class<?> classRef) throws IOException {
    if (Utils.isEmpty(fileName))
      throw new RequiredArgumentException("fileName"); 
    if (classRef == null)
      throw new RequiredArgumentException("classRef"); 
    String resPath = FileUtils.MergePaths(new String[] { resourcePath, fileName });
    String outFilePath = FileUtils.MergePaths(new String[] { outputDir, fileName });
    File outFile = new File(outFilePath);
    InputStream in = null;
    FileOutputStream out = null;
    log().detail("NativeUtils::ExtractLibrary(outFilePath=%s,resPath=%s,fileName=%s,classRef=%s)", new Object[] { outFilePath, resPath, fileName, classRef
          
          .getName() });
    in = classRef.getResourceAsStream(resPath);
    if (in == null)
      throw new IOException("Resource file not found: " + resPath); 
    if (outFile.isFile()) {
      log().info("Removing existing library file:", new Object[] { outFilePath });
      if (!outFile.delete())
        throw new IOException("Failed to remove library file: " + outFilePath); 
    } 
    try {
      out = new FileOutputStream(outFile);
      byte[] buf = new byte[4096];
      while (true) {
        int read = in.read(buf);
        if (read == -1)
          break; 
        out.write(buf, 0, read);
      } 
      log().info("Extracted library file:", new Object[] { outFilePath });
    } catch (FileNotFoundException e) {
      throw new IOException("Cannot write to file: " + outFilePath, e);
    } finally {
      Utils.SafeClose(out);
      Utils.SafeClose(in);
    } 
  }
  
  public static xLog log() {
    return Utils.log();
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixso\\utils\NativeUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */