package com.techshroom.mods.tbm.machine;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import gnu.trove.set.hash.TCustomHashSet;
import gnu.trove.strategy.HashingStrategy;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;

public abstract class TBMMachineBase implements TBMMachine {

    protected final Set<Entity> entities =
            new TCustomHashSet<>(new HashingStrategy<Entity>() {

                private static final long serialVersionUID =
                        -3688873446668328375L;

                @Override
                public int computeHashCode(Entity object) {
                    return object.getUniqueID().hashCode();
                }

                @Override
                public boolean equals(Entity o1, Entity o2) {
                    return o1.getUniqueID().equals(o2.getUniqueID());
                }

            });
    private final Set<Block> allowedBlocks;
    private final String name;

    protected TBMMachineBase(String name,
            Collection<? extends Block> allowedBlocks) {
        this.name = name;
        this.allowedBlocks = ImmutableSet.copyOf(allowedBlocks);
    }

    protected TBMMachineBase(String name, Block... allowedBlocks) {
        this(name, ImmutableSet.copyOf(allowedBlocks));
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Set<Block> getBasicAllowedBlocks() {
        return this.allowedBlocks;
    }

    @Override
    public boolean isBlockAllowed(Block block, IBlockState state) {
        return this.allowedBlocks.contains(block);
    }

    @Override
    public void trackEntity(Entity e) {
        this.entities.add(e);
    }

    @Override
    public Set<Entity> getTrackedEntities() {
        return ImmutableSet.copyOf(this.entities);
    }

}
