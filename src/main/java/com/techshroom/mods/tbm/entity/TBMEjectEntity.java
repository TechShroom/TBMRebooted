package com.techshroom.mods.tbm.entity;

import static com.techshroom.mods.tbm.TBMMod.store;

import com.techshroom.mods.tbm.TBMKeys;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TBMEjectEntity extends TBMEntity {

    public TBMEjectEntity(World w) {
        super(w, store.get(TBMKeys.Blocks.EJECTOR).get().getDefaultState());
    }

    @Override
    public void onMoveToBlock(BlockPos pos) {
    }

}
