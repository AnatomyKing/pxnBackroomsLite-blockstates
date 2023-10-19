package com.poixson.utils;

public enum CryptType {
  MD5("MD5"),
  SHA1("SHA1"),
  SHA256("SHA256"),
  SHA512("SHA512"),
  HMAC_MD5("HmacMD5"),
  HMAC_SHA1("HmacSHA1"),
  HMAC_SHA256("HmacSHA256");
  
  public final String key;
  
  CryptType(String key) {
    this.key = key;
  }
  
  public String toString() {
    return this.key;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixso\\utils\CryptUtils$CryptType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */