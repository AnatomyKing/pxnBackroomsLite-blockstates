package com.poixson.utils;

import com.poixson.tools.Keeper;
import java.awt.Color;

public final class MathUtils {
  static {
    Keeper.add(new MathUtils());
  }
  
  public static double RotateX(double x, double y, double angle) {
    double ang = Math.PI * angle;
    return Math.sin(ang) * y - Math.cos(ang) * x;
  }
  
  public static double RotateY(double x, double y, double angle) {
    double ang = Math.PI * angle;
    return Math.sin(ang) * x + Math.cos(ang) * y;
  }
  
  public static float RotateX(float x, float y, float angle) {
    float ang = (float)(Math.PI * angle);
    return (float)(Math.sin(ang) * y - Math.cos(ang) * x);
  }
  
  public static float RotateY(float x, float y, float angle) {
    float ang = (float)(Math.PI * angle);
    return (float)(Math.sin(ang) * x + Math.cos(ang) * y);
  }
  
  public static int Remap(int lowA, int highA, int lowB, int highB, int value) {
    double lA = lowA;
    double hA = highA;
    double lB = lowB;
    double hB = highB;
    double result = (hB - lB) / (hA - lA);
    result *= value - lA;
    result += lB;
    return (int)result;
  }
  
  public static int Remap(int low, int high, double percent) {
    double result = (high - low) * percent;
    return (int)result + low;
  }
  
  public static Color Remap(Color colorA, Color colorB, double percent) {
    return new Color(
        
        NumberUtils.MinMax(Remap(colorA.getRed(), colorB.getRed(), percent), 0, 255), 
        NumberUtils.MinMax(Remap(colorA.getGreen(), colorB.getGreen(), percent), 0, 255), 
        NumberUtils.MinMax(Remap(colorA.getBlue(), colorB.getBlue(), percent), 0, 255));
  }
  
  public static Color Remap8BitColor(int value) {
    int r = value & 0x7;
    int g = (value & 0x38) >> 3;
    int b = (value & 0xC0) >> 6;
    return new Color(
        
        NumberUtils.MinMax(Remap(0, 7, 0, 255, r), 0, 255), 
        NumberUtils.MinMax(Remap(0, 7, 0, 255, g), 0, 255), 
        NumberUtils.MinMax(Remap(0, 3, 0, 255, b), 0, 255));
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixso\\utils\MathUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */