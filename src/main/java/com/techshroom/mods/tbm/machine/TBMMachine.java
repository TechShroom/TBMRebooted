package com.techshroom.mods.tbm.machine;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;

public interface TBMMachine {

    String getId();

    String getName();

    Set<Block> getBasicAllowedBlocks();

    /**
     * N.B.: This must allow all blocks in {@link #getBasicAllowedBlocks()}.
     */
    boolean isBlockAllowed(Block block, IBlockState state);

    void trackEntity(Entity e);

    Set<Entity> getTrackedEntities();

}
