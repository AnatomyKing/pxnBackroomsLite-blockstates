package com.poixson.backrooms;

import com.poixson.commonmc.tools.plotter.BlockPlotter;
import java.util.LinkedList;
import org.bukkit.generator.LimitedRegion;

public interface BackroomsPop {
  void populate(int paramInt1, int paramInt2, LimitedRegion paramLimitedRegion, LinkedList<BlockPlotter> paramLinkedList);
}
