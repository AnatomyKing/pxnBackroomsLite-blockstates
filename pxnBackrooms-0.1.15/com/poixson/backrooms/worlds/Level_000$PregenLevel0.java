package com.poixson.backrooms.worlds;

import com.poixson.backrooms.PreGenData;
import com.poixson.backrooms.gens.Gen_000;
import com.poixson.backrooms.gens.Gen_001;
import com.poixson.backrooms.gens.Gen_005;
import com.poixson.backrooms.gens.Gen_037;
import com.poixson.tools.dao.Iab;
import java.util.HashMap;

public class PregenLevel0 implements PreGenData {
  public final HashMap<Iab, Gen_000.LobbyData> lobby = new HashMap<>();
  
  public final HashMap<Iab, Gen_001.BasementData> basement = new HashMap<>();
  
  public final HashMap<Iab, Gen_005.HotelData> hotel = new HashMap<>();
  
  public final HashMap<Iab, Gen_037.PoolData> pools = new HashMap<>();
}
