package com.poixson.backrooms.gens;

import com.poixson.backrooms.PreGenData;

public class BasementData implements PreGenData {
  public final double valueWall;
  public final double valueMoistA;
  public final double valueMoistB;
  public boolean isWall;
  public boolean isWet;

  public BasementData(double valueWall, double valueMoistA, double valueMoistB, Gen_001 gen) {
    this.valueWall = valueWall;
    this.valueMoistA = valueMoistA;
    this.valueMoistB = valueMoistB;
    this.isWall = (valueWall > gen.thresh_wall.get());
    double thresh_moist = gen.thresh_moist.get();
    this.isWet = (valueMoistA > thresh_moist || valueMoistB > thresh_moist);
  }
}
