package com.techshroom.mods.tbm.block;

import com.techshroom.mods.tbm.entity.TBMEjectEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TBMEject extends TBMBlockBase {

    public TBMEject() {
        super("ejector", "eject-tex");
        setFacingStyle(FacingStyle.ALL);
    }

    @Override
    public Entity spawnEntity(World world, BlockPos pos, IBlockState state) {
        TBMEjectEntity entity = new TBMEjectEntity(world);
        world.spawnEntityInWorld(entity);
        entity.moveToBlockPosAndAngles(pos, 0, 0);
        return entity;
    }

}
