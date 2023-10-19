package com.poixson.backrooms.gens;

import com.poixson.backrooms.PreGenData;

public class HotelData implements PreGenData {
  public final double value;
  
  public Gen_005.NodeType type;
  
  public boolean hall_center = false;
  
  public HotelData(double value) {
    this.value = value;
    double thresh_room_hall = Gen_005.this.thresh_room_hall.get();
    this.type = (value > thresh_room_hall) ? Gen_005.NodeType.HALL : Gen_005.NodeType.ROOM;
  }
}
