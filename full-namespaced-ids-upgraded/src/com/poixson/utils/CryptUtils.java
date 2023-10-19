package com.poixson.utils;

import com.poixson.logger.xLog;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Formatter;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class CryptUtils {
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
  
  public static String MD5(String data) {
    return Crypt(CryptType.MD5, data);
  }
  
  public static String SHA1(String data) {
    return Crypt(CryptType.SHA1, data);
  }
  
  public static String SHA256(String data) {
    return Crypt(CryptType.SHA256, data);
  }
  
  public static String SHA512(String data) {
    return Crypt(CryptType.SHA512, data);
  }
  
  public static String Crypt(CryptType type, String data) {
    return Crypt(type.toString(), data);
  }
  
  public static String Crypt(String typeStr, String data) {
    try {
      MessageDigest md = MessageDigest.getInstance(typeStr);
      if (md == null)
        throw new NoSuchAlgorithmException(typeStr); 
      md.update(data.getBytes());
      return toHex(md.digest());
    } catch (NoSuchAlgorithmException e) {
      log().trace(e);
      return null;
    } 
  }
  
  public static String Crypt(String typeStr, String key, String data) {
    try {
      Mac mac = Mac.getInstance(typeStr);
      if (mac == null)
        throw new NoSuchAlgorithmException(typeStr); 
      mac.init(new SecretKeySpec(key.getBytes(), typeStr));
      return toHex(mac.doFinal(data.getBytes()));
    } catch (NoSuchAlgorithmException e) {
      log().trace(e);
    } catch (InvalidKeyException e) {
      log().trace(e);
    } 
    return null;
  }
  
  public static String HMacMD5(String key, String data) {
    return HMac(CryptType.HMAC_MD5, key, data);
  }
  
  public static String HMacSHA1(String key, String data) {
    return HMac(CryptType.HMAC_SHA1, key, data);
  }
  
  public static String HMacSHA256(String key, String data) {
    return HMac(CryptType.HMAC_SHA256, key, data);
  }
  
  public static String HMac(CryptType type, String key, String data) {
    return HMac(type.toString(), key, data);
  }
  
  public static String HMac(String typeStr, String key, String data) {
    try {
      Mac mac = Mac.getInstance(typeStr);
      if (mac == null)
        throw new NoSuchAlgorithmException(typeStr); 
      mac.init(new SecretKeySpec(key.getBytes(), typeStr));
      return toHex(mac.doFinal(data.getBytes()));
    } catch (NoSuchAlgorithmException e) {
      log().trace(e);
    } catch (InvalidKeyException e) {
      log().trace(e);
    } 
    return null;
  }
  
  public static String Base64Encode(String data) {
    if (Utils.isEmpty(data))
      return data; 
    return new String(Base64Encode(data.getBytes()));
  }
  
  public static byte[] Base64Encode(byte[] data) {
    if (Utils.isEmpty(data))
      return data; 
    return Base64.getEncoder().encode(data);
  }
  
  public static String Base64Decode(String data) {
    if (Utils.isEmpty(data))
      return data; 
    return new String(Base64Decode(data.getBytes()));
  }
  
  public static byte[] Base64Decode(byte[] data) {
    if (Utils.isEmpty(data))
      return data; 
    return Base64.getDecoder().decode(data);
  }
  
  public static String toHex(String data) {
    return toHex(data.getBytes());
  }
  
  public static String toHex(byte[] data) {
    if (data == null || data.length == 0)
      return null; 
    StringBuilder str = new StringBuilder(data.length * 2);
    Formatter formatter = new Formatter(str);
    for (byte b : data) {
      formatter.format("%02x", new Object[] { Byte.valueOf(b) });
    } 
    Utils.SafeClose(formatter);
    return str.toString();
  }
  
  public static byte[] fromHex(String hex) {
    return fromHex(hex.toCharArray());
  }
  
  public static byte[] fromHex(char[] hex) {
    if (hex == null || hex.length == 0)
      return null; 
    int length = hex.length / 2;
    byte[] out = new byte[length];
    for (int i = 0; i < length; i++) {
      int high = Character.digit(hex[i * 2], 16);
      int low = Character.digit(hex[i * 2 + 1], 16);
      int value = high << 4 | low;
      if (value > 127)
        value -= 256; 
      out[i] = (byte)value;
    } 
    return out;
  }
  
  public static xLog log() {
    return Utils.log();
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixso\\utils\CryptUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */