package com.techshroom.mods.tbm.block;

import com.techshroom.mods.tbm.entity.TBMEngineEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TBMEngine extends TBMBlockBase {

    public TBMEngine() {
        super("engine");
        setFacingStyle(FacingStyle.HORIZONTAL);
    }

    @Override
    public Entity spawnEntity(World world, BlockPos pos, IBlockState state) {
        TBMEngineEntity entity = new TBMEngineEntity(world, state);
        entity.moveToBlockPosAndAngles(pos, 0, 0);
        world.spawnEntityInWorld(entity);
        return entity;
    }

}
