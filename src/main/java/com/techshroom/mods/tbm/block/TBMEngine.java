package com.techshroom.mods.tbm.block;

import com.techshroom.mods.tbm.entity.TBMEngineEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TBMEngine extends TBMBlockBase {

    public TBMEngine() {
        super("engine", "engine-tex");
    }

    @Override
    public Entity spawnEntity(World world, BlockPos pos, IBlockState state) {
        TBMEngineEntity entity = new TBMEngineEntity(world);
        world.spawnEntityInWorld(entity);
        entity.moveToBlockPosAndAngles(pos, 0, 0);
        return entity;
    }

}
