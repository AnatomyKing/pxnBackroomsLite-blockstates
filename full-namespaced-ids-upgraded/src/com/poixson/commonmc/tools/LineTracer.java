package com.poixson.commonmc.tools;

import com.poixson.tools.dao.Iab;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import org.bukkit.Location;

public abstract class LineTracer implements Runnable {
  public final LinkedList<Iab> points = new LinkedList<>();
  
  public final HashSet<Iab> pointSet = new HashSet<>();
  
  public final HashSet<Iab> queued = new HashSet<>();
  
  public final HashSet<Iab> checked = new HashSet<>();
  
  public final boolean allow_forking;
  
  public boolean ok = false;
  
  public LineTracer(Iab loc) {
    this(loc, false);
  }
  
  public LineTracer(Iab loc, boolean allow_forking) {
    this(loc.a, loc.b, allow_forking);
  }
  
  public LineTracer(int x, int y, boolean allow_forking) {
    this.allow_forking = allow_forking;
    Iab loc = new Iab(x, y);
    this.checked.add(loc);
    if (isValidPoint(x, y)) {
      this.points.add(loc);
      this.pointSet.add(loc);
      this.queued.add(loc);
      this.ok = true;
    } 
  }
  
  public abstract boolean isValidPoint(int paramInt1, int paramInt2);
  
  public abstract void check(Iab paramIab);
  
  public void run() {
    if (!this.ok || this.queued.size() == 0)
      return; 
    while (this.ok && 
      this.queued.size() != 0) {
      Iab loc = null;
      Iterator<Iab> iterator = this.queued.iterator();
      if (iterator.hasNext()) {
        Iab dao = iterator.next();
        loc = dao;
      } 
      if (loc == null)
        break; 
      this.queued.remove(loc);
      check(loc);
    } 
  }
  
  public boolean add(int x, int y) {
    return add(new Iab(x, y));
  }
  
  public boolean add(Location loc) {
    return add(new Iab(loc.getBlockX(), loc.getBlockZ()));
  }
  
  public boolean add(Iab loc) {
    if (contains(loc))
      return false; 
    if (1 == getDistance(this.points.getLast(), loc)) {
      this.points.addLast(loc);
      this.pointSet.add(loc);
      return true;
    } 
    if (1 == getDistance(this.points.getFirst(), loc)) {
      this.points.addFirst(loc);
      this.pointSet.add(loc);
      return true;
    } 
    if (this.allow_forking) {
      Iab near = getNear(loc);
      if (near == null)
        return false; 
      int index = this.points.indexOf(near);
      if (index == -1)
        return false; 
      this.points.add(index + 1, loc);
      this.pointSet.add(loc);
      return true;
    } 
    return false;
  }
  
  public boolean contains(int x, int y) {
    return contains(new Iab(x, y));
  }
  
  public boolean contains(Location loc) {
    return contains(new Iab(loc.getBlockX(), loc.getBlockZ()));
  }
  
  public boolean contains(Iab loc) {
    return this.pointSet.contains(loc);
  }
  
  public Iab getNear(Location loc) {
    return getNear(loc.getBlockX(), loc.getBlockZ());
  }
  
  public Iab getNear(Iab loc) {
    return getNear(loc.a, loc.b);
  }
  
  public Iab getNear(int x, int y) {
    return getNear(x, y, 1);
  }
  
  public Iab getNear(Location loc, int distance) {
    return getNear(loc.getBlockX(), loc.getBlockZ(), distance);
  }
  
  public Iab getNear(Iab loc, int distance) {
    return getNear(loc.a, loc.b, distance);
  }
  
  public Iab getNear(int x, int y, int distance) {
    Iab near = null;
    int nearest = distance;
    for (Iab point : this.points) {
      int dist = getDistance(point.a, point.b, x, y);
      if (nearest >= dist) {
        nearest = dist;
        near = point;
        if (nearest == 0)
          break; 
      } 
    } 
    return near;
  }
  
  public int getDistance(Location locA, Location locB) {
    return 
      getDistance(locA
        .getBlockX(), locA.getBlockZ(), locB
        .getBlockX(), locB.getBlockZ());
  }
  
  public int getDistance(Iab locA, Iab locB) {
    return 
      getDistance(locA.a, locA.b, locB.a, locB.b);
  }
  
  public int getDistance(int x1, int y1, int x2, int y2) {
    return Math.abs(x1 - x2) + Math.abs(y1 - y2);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tools\LineTracer.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */