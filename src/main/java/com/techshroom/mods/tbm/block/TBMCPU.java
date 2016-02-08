package com.techshroom.mods.tbm.block;

import com.techshroom.mods.tbm.entity.TBMCPUEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TBMCPU extends TBMBlockBase {

    public TBMCPU() {
        super("cpu");
    }

    @Override
    public Entity spawnEntity(World world, BlockPos pos, IBlockState state) {
        TBMCPUEntity entity = new TBMCPUEntity(world);
        entity.moveToBlockPosAndAngles(pos, 0, 0);
        world.spawnEntityInWorld(entity);
        return entity;
    }

}
