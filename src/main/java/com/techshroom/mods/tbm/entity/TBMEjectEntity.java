package com.techshroom.mods.tbm.entity;

import static com.techshroom.mods.tbm.TBMMod.store;

import com.techshroom.mods.tbm.TBMKeys;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TBMEjectEntity extends TBMEntity {

    public TBMEjectEntity(World w) {
        super(w);
    }

    @Override
    public Block blockBase() {
        return store.get(TBMKeys.Blocks.EJECTOR).get();
    }

    @Override
    public void onMoveToBlock(BlockPos pos) {
    }

}
