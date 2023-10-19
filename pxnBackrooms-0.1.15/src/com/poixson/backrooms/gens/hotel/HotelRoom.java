package com.poixson.backrooms.gens.hotel;

import com.poixson.commonmc.tools.plotter.BlockPlotter;
import com.poixson.tools.dao.Iabcd;
import java.util.LinkedList;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.LimitedRegion;

public interface HotelRoom {
  void build(Iabcd paramIabcd, int paramInt, BlockFace paramBlockFace, LimitedRegion paramLimitedRegion, LinkedList<BlockPlotter> paramLinkedList);
}
