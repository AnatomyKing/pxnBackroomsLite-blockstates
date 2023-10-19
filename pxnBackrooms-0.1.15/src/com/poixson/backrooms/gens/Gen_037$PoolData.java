package com.poixson.backrooms.gens;

import com.poixson.backrooms.PreGenData;

public class PoolData implements PreGenData {
  public final double valueRoom;
  
  public final double valuePortalHotel;
  
  public final double valuePortalLobby;
  
  public final boolean possiblePortalHotel;
  
  public final boolean possiblePortalLobby;
  
  public Gen_037.RoomType type;
  
  public PoolData(int x, int z) {
    this.valueRoom = Gen_037.this.noisePoolRooms.getNoise(x, z);
    this.valuePortalHotel = Gen_037.this.noisePortalHotel.getNoise(x, z);
    this.valuePortalLobby = Gen_037.this.noisePortalLobby.getNoise(x, z);
    if (this.valueRoom < Gen_037.this.thresh_room.get()) {
      this.type = Gen_037.RoomType.SOLID;
      this.possiblePortalHotel = (this.valuePortalHotel > Gen_037.this.thresh_portal.get());
      this.possiblePortalLobby = false;
    } else {
      this.type = Gen_037.RoomType.OPEN;
      this.possiblePortalHotel = false;
      this
        
        .possiblePortalLobby = (this.valuePortalLobby > Gen_037.this.noisePortalLobby.getNoise(x, (z - 1)) && this.valuePortalLobby > Gen_037.this.noisePortalLobby.getNoise(x, (z + 1)) && this.valuePortalLobby > Gen_037.this.noisePortalLobby.getNoise((x + 1), z) && this.valuePortalLobby > Gen_037.this.noisePortalLobby.getNoise((x - 1), z));
    } 
  }
  
  public boolean isSolid() {
    return Gen_037.RoomType.SOLID.equals(this.type);
  }
}
