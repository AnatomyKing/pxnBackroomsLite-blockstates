package com.poixson.backrooms.gens;

import com.poixson.backrooms.PreGenData;

public class BasementData implements PreGenData {
  public final double valueWall;
  
  public final double valueMoistA;
  
  public final double valueMoistB;
  
  public boolean isWall;
  
  public boolean isWet;
  
  public BasementData(double valueWall, double valueMoistA, double valueMoistB) {
    this.valueWall = valueWall;
    this.valueMoistA = valueMoistA;
    this.valueMoistB = valueMoistB;
    this.isWall = (valueWall > Gen_001.this.thresh_wall.get());
    double thresh_moist = Gen_001.this.thresh_moist.get();
    this.isWet = (valueMoistA > thresh_moist || valueMoistB > thresh_moist);
  }
}
