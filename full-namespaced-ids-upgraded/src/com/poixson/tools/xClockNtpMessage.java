package com.poixson.tools;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class xClockNtpMessage {
  public byte leapIndicator = 0;
  
  public byte version = 3;
  
  public byte mode = 0;
  
  public short stratum = 0;
  
  public byte pollInterval = 0;
  
  public byte precision = 0;
  
  public double rootDelay = 0.0D;
  
  public double rootDispersion = 0.0D;
  
  public byte[] referenceIdentifier = new byte[] { 0, 0, 0, 0 };
  
  public double referenceTimestamp = 0.0D;
  
  public double originateTimestamp = 0.0D;
  
  public double receiveTimestamp = 0.0D;
  
  public double transmitTimestamp = 0.0D;
  
  public xClockNtpMessage(byte[] array) {
    this.leapIndicator = (byte)(array[0] >> 6 & 0x3);
    this.version = (byte)(array[0] >> 3 & 0x7);
    this.mode = (byte)(array[0] & 0x7);
    this.stratum = unsignedByteToShort(array[1]);
    this.pollInterval = array[2];
    this.precision = array[3];
    this
      
      .rootDelay = array[4] * 256.0D + unsignedByteToShort(array[5]) + unsignedByteToShort(array[6]) / 256.0D + unsignedByteToShort(array[7]) / 65536.0D;
    this
      
      .rootDispersion = unsignedByteToShort(array[8]) * 256.0D + unsignedByteToShort(array[9]) + unsignedByteToShort(array[10]) / 256.0D + unsignedByteToShort(array[11]) / 65536.0D;
    this.referenceIdentifier[0] = array[12];
    this.referenceIdentifier[1] = array[13];
    this.referenceIdentifier[2] = array[14];
    this.referenceIdentifier[3] = array[15];
    this.referenceTimestamp = decodeTimestamp(array, 16);
    this.originateTimestamp = decodeTimestamp(array, 24);
    this.receiveTimestamp = decodeTimestamp(array, 32);
    this.transmitTimestamp = decodeTimestamp(array, 40);
  }
  
  public xClockNtpMessage() {
    this.mode = 3;
    this.transmitTimestamp = System.currentTimeMillis() / 1000.0D + 2.2089888E9D;
  }
  
  public byte[] toByteArray() {
    byte[] p = new byte[48];
    p[0] = (byte)(this.leapIndicator << 6 | this.version << 3 | this.mode);
    p[1] = (byte)this.stratum;
    p[2] = this.pollInterval;
    p[3] = this.precision;
    int l = (int)(this.rootDelay * 65536.0D);
    p[4] = (byte)(l >> 24 & 0xFF);
    p[5] = (byte)(l >> 16 & 0xFF);
    p[6] = (byte)(l >> 8 & 0xFF);
    p[7] = (byte)(l & 0xFF);
    long ul = (long)(this.rootDispersion * 65536.0D);
    p[8] = (byte)(int)(ul >> 24L & 0xFFL);
    p[9] = (byte)(int)(ul >> 16L & 0xFFL);
    p[10] = (byte)(int)(ul >> 8L & 0xFFL);
    p[11] = (byte)(int)(ul & 0xFFL);
    p[12] = this.referenceIdentifier[0];
    p[13] = this.referenceIdentifier[1];
    p[14] = this.referenceIdentifier[2];
    p[15] = this.referenceIdentifier[3];
    encodeTimestamp(p, 16, this.referenceTimestamp);
    encodeTimestamp(p, 24, this.originateTimestamp);
    encodeTimestamp(p, 32, this.receiveTimestamp);
    encodeTimestamp(p, 40, this.transmitTimestamp);
    return p;
  }
  
  public String toString() {
    String precisionStr = (new DecimalFormat("0.#E0")).format(Math.pow(2.0D, this.precision));
    StringBuilder str = new StringBuilder();
    str.append("Leap indicator: ").append(this.leapIndicator).append("\n");
    str.append("Version: ").append(this.version).append("\n");
    str.append("Mode: ").append(this.mode).append("\n");
    str.append("Stratum: ").append(this.stratum).append("\n");
    str.append("Poll: ").append(this.pollInterval).append("\n");
    str.append("Precision: ").append(this.precision).append(" (").append(precisionStr).append(" seconds)\n");
    str.append("Root delay: ").append((new DecimalFormat("0.00")).format(this.rootDelay * 1000.0D)).append(" ms\n");
    str.append("Root dispersion: ").append((new DecimalFormat("0.00")).format(this.rootDispersion * 1000.0D)).append(" ms\n");
    str.append("Reference identifier: ").append(referenceIdentifierToString(this.referenceIdentifier, this.stratum, this.version)).append("\n");
    str.append("Reference timestamp: ").append(timestampToString(this.referenceTimestamp)).append("\n");
    str.append("Originate timestamp: ").append(timestampToString(this.originateTimestamp)).append("\n");
    str.append("Receive timestamp:   ").append(timestampToString(this.receiveTimestamp)).append("\n");
    str.append("Transmit timestamp:  ").append(timestampToString(this.transmitTimestamp));
    return str.toString();
  }
  
  public static short unsignedByteToShort(byte b) {
    if ((b & 0x80) == 128)
      return (short)(128 + (b & Byte.MAX_VALUE)); 
    return (short)b;
  }
  
  public static double unsignedByteToDouble(byte b) {
    return unsignedByteToShort(b);
  }
  
  public static double decodeTimestamp(byte[] array, int pointer) {
    double r = 0.0D;
    for (int i = 0; i < 8; i++)
      r += unsignedByteToShort(array[pointer + i]) * Math.pow(2.0D, ((3 - i) * 8)); 
    return r;
  }
  
  public static void encodeTimestamp(byte[] array, int pointer, double timestamp) {
    double stamp = timestamp;
    for (int i = 0; i < 8; i++) {
      double base = Math.pow(2.0D, ((3 - i) * 8));
      array[pointer + i] = (byte)(int)(stamp / base);
      stamp -= unsignedByteToDouble(array[pointer + i]) * base;
    } 
    array[7] = (byte)(int)(Math.random() * 255.0D);
  }
  
  public static String timestampToString(double timestamp) {
    if (timestamp == 0.0D)
      return "0"; 
    double utc = timestamp - 2.2089888E9D;
    long ms = (long)(utc * 1000.0D);
    String date = (new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss")).format(new Date(ms));
    double fraction = timestamp - (long)timestamp;
    String fractionSting = (new DecimalFormat(".000000")).format(fraction);
    return date + date;
  }
  
  public static String referenceIdentifierToString(byte[] ref, short stratum, byte version) {
    if (stratum == 0 || stratum == 1)
      return new String(ref); 
    if (version == 3) {
      StringBuilder str = new StringBuilder();
      str.append(unsignedByteToShort(ref[0])).append(".");
      str.append(unsignedByteToShort(ref[1])).append(".");
      str.append(unsignedByteToShort(ref[2])).append(".");
      str.append(unsignedByteToShort(ref[3]));
      return str.toString();
    } 
    if (version == 4) {
      StringBuilder str = new StringBuilder();
      str.append(unsignedByteToShort(ref[0]) / 256.0D);
      str.append(unsignedByteToShort(ref[1]) / 65536.0D);
      str.append(unsignedByteToShort(ref[2]) / 1.6777216E7D);
      str.append(unsignedByteToShort(ref[3]) / 4.294967296E9D);
      return str.toString();
    } 
    return "";
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\xClockNtpMessage.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */