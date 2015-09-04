package com.techshroom.mods.tbm.entity;

import static com.techshroom.mods.tbm.TBMMod.store_get;
import net.minecraft.block.Block;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

import com.techshroom.mods.tbm.block.tile.TBMEjectTile;

public class TBMEjectEntity
        extends TBMEntity<Container, TBMEjectTile, TBMEjectEntity> {
    public TBMEjectEntity(World p_i1582_1_) {
        super(p_i1582_1_);
    }

    @Override
    public Block blockBase() {
        return store_get("ejecter");
    }
}
