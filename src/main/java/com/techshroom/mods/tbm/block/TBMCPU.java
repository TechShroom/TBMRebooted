package com.techshroom.mods.tbm.block;

import java.util.function.Supplier;

import com.techshroom.mods.tbm.entity.TBMCPUEntity;
import com.techshroom.mods.tbm.machine.provider.IronGlassTBMMachineProvider;
import com.techshroom.mods.tbm.machine.provider.TBMMachineProviders;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TBMCPU extends TBMBlockBase {

    private static final Supplier<IllegalStateException> NO_DEFAULT_PROVIDER =
            () -> new IllegalStateException("No provider found for the CPU");

    public TBMCPU() {
        super("cpu");
    }

    @Override
    public Entity spawnEntity(World world, BlockPos pos, IBlockState state) {
        TBMCPUEntity entity = new TBMCPUEntity(world,
                TBMMachineProviders.getProvider(IronGlassTBMMachineProvider.ID)
                        .orElseThrow(NO_DEFAULT_PROVIDER).provideMachine());
        entity.moveToBlockPosAndAngles(pos, 0, 0);
        world.spawnEntityInWorld(entity);
        return entity;
    }

}
