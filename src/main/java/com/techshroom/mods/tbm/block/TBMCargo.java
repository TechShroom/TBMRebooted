package com.techshroom.mods.tbm.block;

import com.techshroom.mods.tbm.entity.TBMCargoEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TBMCargo extends TBMBlockBase {

    public TBMCargo() {
        super("cargo");
        setFacingStyle(FacingStyle.HORIZONTAL);
    }

    @Override
    public Entity spawnEntity(World world, BlockPos pos, IBlockState state) {
        TBMCargoEntity entity = new TBMCargoEntity(world, state);
        entity.moveToBlockPosAndAngles(pos, 0, 0);
        world.spawnEntityInWorld(entity);
        return entity;
    }

}
