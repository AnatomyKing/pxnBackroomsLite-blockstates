package com.poixson.backrooms.gens;

import com.poixson.commonmc.tools.LineTracer;
import com.poixson.tools.dao.Iab;
import java.util.LinkedList;
import org.bukkit.Material;
import org.bukkit.generator.LimitedRegion;

public class TunnelTracer extends LineTracer {
    public final LinkedList<TunnelTracer> otherTracers;
    public final LimitedRegion region;
    public final int y;
    public int ends = 0;

    public TunnelTracer(LimitedRegion region, LinkedList<TunnelTracer> otherTracers, int x, int z) {
        super(x, z, false);
        this.region = region;
        this.otherTracers = otherTracers;
        this.y = Pop_037.this.gen.level_y + 9;
        Material type = region.getType(x, this.y, z);
        if (Material.AIR.equals(type))
            this.ok = false;
    }

    public void check(Iab from) {
        checkone(from.a, this.y, from.b - 1);
        checkone(from.a, this.y, from.b + 1);
        checkone(from.a + 1, this.y, from.b);
        checkone(from.a - 1, this.y, from.b);
    }

    protected void checkone(int x, int y, int z) {
        if (!this.ok)
            return;
        Iab loc = new Iab(x, z);
        if (this.checked.add(loc) &&
            isValidPoint(x, z)) {
            if (contains(loc))
                return;
            for (TunnelTracer tracer : this.otherTracers) {
                if (tracer.contains(loc)) {
                    this.ok = false;
                    return;
                }
            }
            if (!this.region.isInRegion(x, y, z)) {
                this.ok = false;
                return;
            }
            Material type = this.region.getType(x, y, z);
            if (Material.AIR.equals(type)) {
                this.ends++;
                return;
            }
            if (add(loc))
                this.queued.add(loc);
        }
    }

    public boolean isValidPoint(int x, int y) {
        double value = Pop_037.this.gen.noiseTunnels.getNoiseRot(x, y, 0.25D);
        return (value > 0.95D);
    }
}


