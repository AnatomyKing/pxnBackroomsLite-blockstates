package com.poixson.backrooms.gens;

import com.poixson.backrooms.PreGenData;

public class HillsData implements PreGenData {
  public final double valueHill;
  
  public final double depth;
  
  public final double valueHouse;
  
  public boolean isHouse = false;
  
  public int house_y = 0;
  
  public boolean house_direction;
  
  public HillsData(double valueHill, double valueHouse, double valley_depth, double valley_gain, double hills_gain) {
    this.valueHill = valueHill;
    double depth = 1.0D - valueHill;
    if (depth < valley_depth)
      depth *= valley_gain; 
    this.depth = depth * hills_gain;
    this.valueHouse = valueHouse;
    this.house_direction = ((int)Math.floor(valueHouse * 100000.0D) % 2 == 1);
  }
}
