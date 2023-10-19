package com.poixson.backrooms.listeners;

import com.poixson.utils.Utils;

public class PlayerDamageDAO {
  public long last;
  
  public int count = 1;
  
  public PlayerDamageDAO() {
    this.last = Utils.GetMS();
  }
  
  public int increment() {
    this.last = Utils.GetMS();
    return ++this.count;
  }
}
