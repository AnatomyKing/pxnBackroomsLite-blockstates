package com.poixson.backrooms.gens;

import com.poixson.backrooms.PreGenData;

public class PoolData implements PreGenData {
    public final double valueRoom;
    public final double valuePortalHotel;
    public final double valuePortalLobby;
    public final boolean possiblePortalHotel;
    public final boolean possiblePortalLobby;
    public RoomType type;

    public PoolData(int x, int z, Gen_037 gen_037) {
        this.valueRoom = gen_037.noisePoolRooms.getNoise(x, z);
        this.valuePortalHotel = gen_037.noisePortalHotel.getNoise(x, z);
        this.valuePortalLobby = gen_037.noisePortalLobby.getNoise(x, z);

        if (this.valueRoom < gen_037.thresh_room.get()) {
            this.type = RoomType.SOLID;
            this.possiblePortalHotel = (this.valuePortalHotel > gen_037.thresh_portal.get());
            this.possiblePortalLobby = false;
        } else {
            this.type = RoomType.OPEN;
            this.possiblePortalHotel = false;
            this.possiblePortalLobby = (
                this.valuePortalLobby > gen_037.noisePortalLobby.getNoise(x, (z - 1)) &&
                this.valuePortalLobby > gen_037.noisePortalLobby.getNoise(x, (z + 1)) &&
                this.valuePortalLobby > gen_037.noisePortalLobby.getNoise((x + 1), z) &&
                this.valuePortalLobby > gen_037.noisePortalLobby.getNoise((x - 1), z)
            );
        }
    }

    public boolean isSolid() {
        return RoomType.SOLID.equals(this.type);
    }
}
