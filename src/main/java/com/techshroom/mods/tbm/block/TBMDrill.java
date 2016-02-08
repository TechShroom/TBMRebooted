package com.techshroom.mods.tbm.block;

import com.techshroom.mods.tbm.entity.TBMDrillEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TBMDrill extends TBMBlockBase {

    public TBMDrill() {
        super("drill");
        setFacingStyle(FacingStyle.ALL);
    }

    @Override
    public Entity spawnEntity(World world, BlockPos pos, IBlockState state) {
        TBMDrillEntity entity = new TBMDrillEntity(world, state);
        entity.moveToBlockPosAndAngles(pos, 0, 0);
        world.spawnEntityInWorld(entity);
        return entity;
    }

}
