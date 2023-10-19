package com.poixson.utils;

import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.tools.Keeper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.security.CodeSource;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class FileUtils {
  static {
    Keeper.add(new FileUtils());
  }
  
  private static final AtomicReference<String> cwd = new AtomicReference<>(null);
  
  private static final AtomicReference<String> pwd = new AtomicReference<>(null);
  
  private static final AtomicReference<String> exe = new AtomicReference<>(null);
  
  public static String SearchLocalFile(String[] fileNames, int parents) {
    if (Utils.isEmpty(fileNames))
      throw new RequiredArgumentException("fileNames"); 
    (new String[1])[0] = cwd();
    (new String[2])[0] = cwd();
    (new String[2])[1] = pwd();
    String[] workingPaths = inRunDir() ? new String[1] : new String[2];
    for (int parentIndex = 0; parentIndex < parents + 1; parentIndex++) {
      for (String workPath : workingPaths) {
        for (String fileName : fileNames) {
          String path = MergePaths(new String[] { workPath, StringUtils.Repeat(parentIndex, "../"), fileName });
          File file = new File(path);
          if (file.exists())
            return path; 
        } 
      } 
    } 
    return null;
  }
  
  public static boolean SearchLocalOrResource(Class<?> clss, String fileLocal, String fileRes) throws FileNotFoundException {
    if (Utils.notEmpty(fileLocal)) {
      File path = new File(fileLocal);
      if (path.isFile())
        return true; 
    } 
    if (Utils.notEmpty(fileRes)) {
      URL url = clss.getResource(fileRes);
      if (url != null)
        return false; 
    } 
    throw new FileNotFoundException(String.format("Loc:%s or Res:%s in %s", new Object[] { fileLocal, fileRes, clss.getName() }));
  }
  
  public static boolean inRunDir() {
    String cwd = cwd();
    if (cwd == null)
      return false; 
    String pwd = pwd();
    if (pwd == null)
      return false; 
    return cwd.equals(pwd);
  }
  
  public static String cwd() {
    if (cwd.get() == null)
      populateCwd(); 
    return cwd.get();
  }
  
  private static void populateCwd() {
    if (cwd.get() != null)
      return; 
    String path = System.getProperty("user.dir");
    if (Utils.notEmpty(path)) {
      cwd.compareAndSet(null, path);
      return;
    } 
    try {
      File dir = new File(".");
      cwd.compareAndSet(null, dir.getCanonicalPath());
    } catch (IOException ignore) {
      cwd.set(null);
    } 
  }
  
  public static String pwd() {
    if (pwd.get() == null)
      populatePwdExe(); 
    return pwd.get();
  }
  
  public static String exe() {
    if (exe.get() == null)
      populatePwdExe(); 
    return exe.get();
  }
  
  private static void populatePwdExe() {
    if (pwd.get() != null && exe.get() != null)
      return; 
    CodeSource source = FileUtils.class.getProtectionDomain().getCodeSource();
    String pathRaw = source.getLocation().getPath();
    String path = StringUtils.decodeDef(pathRaw, pathRaw);
    if (Utils.isEmpty(path))
      throw new RuntimeException("Failed to get pwd path"); 
    int pos = path.lastIndexOf('/');
    if (pos < 0)
      throw new RuntimeException("Invalid pwd path: " + path); 
    pwd.compareAndSet(null, StringUtils.ceTrim(path.substring(0, pos), new char[] { '/' }));
    exe.compareAndSet(null, StringUtils.cfTrim(path.substring(pos + 1), new char[] { '/' }));
  }
  
  public static boolean isDir(String pathStr) {
    if (Utils.isEmpty(pathStr))
      return false; 
    File path = new File(pathStr);
    return (path.exists() && path.isDirectory());
  }
  
  public static boolean isFile(String fileStr) {
    if (Utils.isEmpty(fileStr))
      return false; 
    File file = new File(fileStr);
    return (file.exists() && file.isFile());
  }
  
  public static boolean isReadable(String pathStr) {
    if (Utils.isEmpty(pathStr))
      return false; 
    File path = new File(pathStr);
    return (path.exists() && path.canRead());
  }
  
  public static boolean isWritable(String pathStr) {
    if (Utils.isEmpty(pathStr))
      return false; 
    File path = new File(pathStr);
    return (path.exists() && path.canWrite());
  }
  
  public static long GetLastModified(String fileStr) throws IOException {
    if (Utils.isEmpty(fileStr))
      return 0L; 
    return GetLastModified(Paths.get(fileStr, new String[0]));
  }
  
  public static long GetLastModified(File file) throws IOException {
    if (file == null)
      return 0L; 
    return GetLastModified(file.toPath());
  }
  
  public static long GetLastModified(Path path) throws IOException {
    BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class, new java.nio.file.LinkOption[0]);
    if (attr == null)
      throw new IOException("Failed to get file attributes: " + path.toString()); 
    FileTime time = attr.lastModifiedTime();
    return time.to(TimeUnit.SECONDS);
  }
  
  public static File[] ListDirContents(File dir, String[] extensions) {
    if (dir == null)
      throw new RequiredArgumentException("dir"); 
    if (!dir.isDirectory())
      return null; 
    return dir.listFiles((new FileFilter() {
          private String[] exts;
          
          public FileFilter init(String[] extens) {
            this.exts = extens;
            return this;
          }
          
          public boolean accept(File path) {
            if (this.exts == null)
              return true; 
            String pathStr = path.toString();
            for (String ext : this.exts) {
              if (pathStr.endsWith(ext))
                return true; 
            } 
            return false;
          }
        }).init(extensions));
  }
  
  public static File[] ListDirContents(File dir, String extension) {
    return ListDirContents(dir, new String[] { extension });
  }
  
  public static File[] ListDirContents(File dir) {
    return ListDirContents(dir, (String[])null);
  }
  
  public static String MergePaths(String... strings) {
    if (Utils.isEmpty(strings))
      return null; 
    boolean isAbsolute = false;
    for (int index = 0; index < strings.length; ) {
      if (Utils.isEmpty(strings[index])) {
        index++;
        continue;
      } 
      if (strings[index].startsWith("/") || strings[index]
        .startsWith("\\"))
        isAbsolute = true; 
    } 
    LinkedList<String> result = new LinkedList<>();
    int count = 0;
    for (int i = 0; i < strings.length; i++) {
      String[] array = strings[i].split("/");
      for (String str : array) {
        if (!Utils.isEmpty(str)) {
          String s = StringUtils.sTrim(str, new String[] { " ", "\t", "\r", "\n" });
          if (!Utils.isEmpty(s) && (
            count <= 0 || (
            !".".equals(s) && 
            !",".equals(s)))) {
            result.add(s);
            count++;
          } 
        } 
      } 
    } 
    if (result.isEmpty())
      return null; 
    String first = result.getFirst();
    if (".".equals(first)) {
      result.removeFirst();
      isAbsolute = true;
      String[] array = cwd().split("/");
      for (int k = array.length - 1; k >= 0; k--) {
        if (array[k].length() != 0)
          result.addFirst(array[k]); 
      } 
    } else if (",".equals(first)) {
      result.removeFirst();
      isAbsolute = true;
      String[] array = pwd().split("/");
      for (int k = array.length - 1; k >= 0; k--)
        result.addFirst(array[k]); 
    } 
    for (int j = 0; j < result.size(); j++) {
      String entry = result.get(j);
      if ("..".equals(entry)) {
        result.remove(j);
        if (j > 0) {
          j--;
          result.remove(j);
        } 
        j--;
      } 
    } 
    String path = StringUtils.MergeStrings(File.separator, result
        
        .<String>toArray(new String[0]));
    if (isAbsolute)
      return 
        StringUtils.ForceStarts(File.separator, path); 
    return path;
  }
  
  public static InputStream OpenResource(Class<? extends Object> clssRef, String fileStr) {
    if (Utils.isEmpty(fileStr))
      throw new RequiredArgumentException("fileStr"); 
    Class<? extends Object> clss = (clssRef == null) ? (Class)FileUtils.class : clssRef;
    return clss.getResourceAsStream(StringUtils.ForceStarts("/", fileStr));
  }
  
  public static void ExportResource(String targetFileStr, InputStream in) throws IOException {
    if (Utils.isEmpty(targetFileStr))
      throw new RequiredArgumentException("targetFileStr"); 
    if (in == null)
      throw new RequiredArgumentException("in"); 
    File file = new File(targetFileStr);
    try {
      Files.copy(in, file.toPath(), new java.nio.file.CopyOption[0]);
    } finally {
      Utils.SafeClose(in);
    } 
  }
  
  public static String ReadInputStream(InputStream in) {
    if (in == null)
      return null; 
    StringBuilder result = new StringBuilder();
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    try {
      String line;
      while ((line = reader.readLine()) != null)
        result.append(line).append('\n'); 
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } 
    return result.toString();
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixso\\utils\FileUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */