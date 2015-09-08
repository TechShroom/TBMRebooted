package com.techshroom.mods.tbm.entity;

import static com.techshroom.mods.tbm.TBMMod.store;

import com.techshroom.mods.tbm.TBMKeys;
import com.techshroom.mods.tbm.block.tile.TBMEjectTile;

import net.minecraft.block.Block;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

public class TBMEjectEntity
        extends TBMEntity<Container, TBMEjectTile, TBMEjectEntity> {

    public TBMEjectEntity(World p_i1582_1_) {
        super(p_i1582_1_);
    }

    @Override
    public Block blockBase() {
        return store.get(TBMKeys.Blocks.EJECTOR).get();
    }
}
