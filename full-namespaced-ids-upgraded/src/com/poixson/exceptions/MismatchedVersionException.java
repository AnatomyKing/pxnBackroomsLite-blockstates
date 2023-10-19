package com.poixson.exceptions;

public class MismatchedVersionException extends Exception {
  private static final long serialVersionUID = 1L;
  
  public final String versionExpected;
  
  public final String versionActual;
  
  public MismatchedVersionException(String versionExpected, String versionActual) {
    super(String.format("Expected version: %s Found version: %s", new Object[] { versionExpected, versionActual }));
    this.versionExpected = versionExpected;
    this.versionActual = versionActual;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\exceptions\MismatchedVersionException.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */