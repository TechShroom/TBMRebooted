package com.techshroom.mods.tbm.render.entity;

import com.techshroom.mods.tbm.Tutils.SetBlockFlag;
import com.techshroom.mods.tbm.entity.TBMEntity;
import com.techshroom.mods.tbm.machine.TBMMachine;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;

/**
 * Helper for providing fake data where entities exist.
 * Needs some way to provide good fake lighting data.
 */
public class FilterByEntitiesOnMachine implements IBlockAccess {

    private final World delegate;
    private final TBMMachine machine;

    private class TemporaryPlacementOfBlock implements AutoCloseable {

        private TemporaryPlacementOfBlock() {
            FilterByEntitiesOnMachine.this.machine.getTrackedEntities()
                    .forEach(e -> {
                        if (!(e instanceof TBMEntity)) {
                            return;
                        }
                        BlockPos pos = new BlockPos(e);
                        FilterByEntitiesOnMachine.this.delegate.setBlockState(
                                pos, ((TBMEntity) e).getState(),
                                SetBlockFlag.DONT_RE_RENDER);
                    });
        }

        @Override
        public void close() {
            FilterByEntitiesOnMachine.this.machine.getTrackedEntities()
                    .forEach(e -> {
                        if (!(e instanceof TBMEntity)) {
                            return;
                        }
                        BlockPos pos = new BlockPos(e);
                        FilterByEntitiesOnMachine.this.delegate.setBlockState(
                                pos, Blocks.air.getDefaultState(),
                                SetBlockFlag.DONT_RE_RENDER);
                    });
        }
    }

    public FilterByEntitiesOnMachine(World world, TBMMachine machine) {
        this.delegate = world;
        this.machine = machine;
    }

    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        try (
                TemporaryPlacementOfBlock blocks =
                        new TemporaryPlacementOfBlock()) {
            return this.delegate.getTileEntity(pos);
        }
    }

    @Override
    public int getCombinedLight(BlockPos pos, int lightValue) {
        try (
                TemporaryPlacementOfBlock blocks =
                        new TemporaryPlacementOfBlock()) {
            return this.delegate.getCombinedLight(pos, lightValue);
        }
    }

    @Override
    public IBlockState getBlockState(BlockPos pos) {
        try (
                TemporaryPlacementOfBlock blocks =
                        new TemporaryPlacementOfBlock()) {
            return this.delegate.getBlockState(pos);
        }
    }

    @Override
    public boolean isAirBlock(BlockPos pos) {
        try (
                TemporaryPlacementOfBlock blocks =
                        new TemporaryPlacementOfBlock()) {
            return this.delegate.isAirBlock(pos);
        }
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(BlockPos pos) {
        try (
                TemporaryPlacementOfBlock blocks =
                        new TemporaryPlacementOfBlock()) {
            return this.delegate.getBiomeGenForCoords(pos);
        }
    }

    @Override
    public boolean extendedLevelsInChunkCache() {
        try (
                TemporaryPlacementOfBlock blocks =
                        new TemporaryPlacementOfBlock()) {
            return this.delegate.extendedLevelsInChunkCache();
        }
    }

    @Override
    public int getStrongPower(BlockPos pos, EnumFacing direction) {
        try (
                TemporaryPlacementOfBlock blocks =
                        new TemporaryPlacementOfBlock()) {
            return this.delegate.getStrongPower(pos, direction);
        }
    }

    @Override
    public WorldType getWorldType() {
        return this.delegate.getWorldType();
    }

    @Override
    public boolean isSideSolid(BlockPos pos, EnumFacing side,
            boolean _default) {
        try (
                TemporaryPlacementOfBlock blocks =
                        new TemporaryPlacementOfBlock()) {
            return this.delegate.isSideSolid(pos, side, _default);
        }
    }

}
