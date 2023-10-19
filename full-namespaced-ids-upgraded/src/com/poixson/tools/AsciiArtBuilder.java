package com.poixson.tools;

import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.utils.StringUtils;
import com.poixson.utils.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AsciiArtBuilder {
  protected final String[] lines;
  
  protected final HashMap<Integer, HashMap<Integer, String>> colorLocations = new HashMap<>();
  
  protected String bgColor = null;
  
  protected final HashMap<String, String> colorAliases = new HashMap<>();
  
  protected int indent = 0;
  
  public AsciiArtBuilder(String... lines) {
    if (Utils.isEmpty(lines))
      throw new RequiredArgumentException("lines"); 
    this.lines = lines;
    for (int i = 0; i < lines.length; i++)
      this.colorLocations.put(
          Integer.valueOf(i), new HashMap<>()); 
  }
  
  public String[] build() {
    int lineCount = this.lines.length;
    String[] result = new String[lineCount];
    String bgColor = getBgColor();
    int indent = this.indent;
    StringBuilder buf = new StringBuilder();
    int maxLineSize = StringUtils.FindLongestLine(this.lines);
    boolean withinTag = false;
    for (int lineIndex = 0; lineIndex < lineCount; lineIndex++) {
      String line = this.lines[lineIndex];
      if (Utils.isEmpty(line)) {
        result[lineIndex] = 
          StringUtils.Repeat(indent * 2 + maxLineSize, ' ');
      } else {
        buf.setLength(0);
        if (indent > 0)
          buf.append(
              StringUtils.Repeat(indent, ' ')); 
        withinTag = false;
        HashMap<Integer, String> colorsMap = this.colorLocations.get(
            Integer.valueOf(lineIndex));
        if (Utils.isEmpty(colorsMap)) {
          if (bgColor != null) {
            withinTag = true;
            buf.append("@|")
              .append(bgColor)
              .append(' ');
          } 
          buf.append(line);
        } else {
          List<Integer> ordered = new ArrayList<>(colorsMap.keySet());
          Collections.sort(ordered);
          int lastX = -1;
          for (Integer posX : ordered) {
            int posXX = posX.intValue();
            String colorStr = colorsMap.get(posX);
            if (Utils.isEmpty(colorStr))
              continue; 
            if (lastX == -1) {
              lastX = 0;
              if (posXX > 0 && 
                bgColor != null) {
                withinTag = true;
                buf.append("@|")
                  .append(bgColor)
                  .append(' ');
              } 
            } 
            if (posXX > 0)
              buf.append(line.substring(lastX, posXX)); 
            if (withinTag)
              buf.append("|@"); 
            withinTag = true;
            buf.append("@|");
            if (bgColor != null)
              buf.append(bgColor)
                .append(','); 
            buf.append(colorStr)
              .append(' ');
            lastX = posXX;
          } 
          if (lastX < line.length())
            buf.append(line
                .substring(lastX)); 
        } 
        if (withinTag)
          buf.append("|@"); 
        if (indent > 0)
          buf.append(
              StringUtils.Repeat(maxLineSize - line
                .length() + indent, ' ')); 
        result[lineIndex] = buf.toString();
      } 
    } 
    return result;
  }
  
  public AsciiArtBuilder aliasColor(String alias, String actual) {
    this.colorAliases.put(alias, actual);
    return this;
  }
  
  public String getBgColor() {
    String bgColor = this.bgColor;
    return Utils.ifEmpty(bgColor, null);
  }
  
  public AsciiArtBuilder setBgColor(String bgColor) {
    this.bgColor = StringUtils.ForceStarts("bg_", bgColor);
    return this;
  }
  
  public int getIndent() {
    return this.indent;
  }
  
  public AsciiArtBuilder setIndent(int indent) {
    this.indent = indent;
    return this;
  }
  
  public AsciiArtBuilder setColor(String color, int posX, int posY) {
    if (Utils.isEmpty(color))
      throw new RequiredArgumentException("color"); 
    if (posX < 0)
      throw new IllegalArgumentException("posX is out of range: " + Integer.toString(posX)); 
    if (posY < 0)
      throw new IllegalArgumentException("posY is out of range: " + Integer.toString(posY)); 
    if (posY > this.colorLocations.size())
      throw new IllegalArgumentException(
          String.format("posY is out of range: %d > %d", new Object[] { Integer.valueOf(posY), 
              Integer.valueOf(this.colorLocations.size()) })); 
    HashMap<Integer, String> entry = this.colorLocations.get(Integer.valueOf(posY));
    String existing = entry.get(Integer.valueOf(posX));
    entry.put(
        Integer.valueOf(posX), 
        StringUtils.MergeStrings(',', new String[] { existing, color }));
    return this;
  }
  
  public AsciiArtBuilder setBgColor(String bgColor, int posX, int posY) {
    if (Utils.isEmpty(bgColor))
      throw new RequiredArgumentException("bgColor"); 
    return 
      setColor(
        StringUtils.ForceStarts("bg_", bgColor), posX, posY);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\AsciiArtBuilder.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */