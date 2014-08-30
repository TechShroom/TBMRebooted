package com.techshroom.mods.tbm.block.tile;

import com.techshroom.mods.tbm.ConvertsToEntity;
import com.techshroom.mods.tbm.entity.TBMEjectEntity;

public class TBMEjectTile extends AlwaysSyncedCPUTile implements
        ConvertsToEntity<TBMEjectEntity> {
    @Override
    public TBMEjectEntity convertToEntity() {
        return new TBMEjectEntity(worldObj).withTile(this);
    }

}
