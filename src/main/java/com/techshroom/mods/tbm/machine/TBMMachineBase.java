package com.techshroom.mods.tbm.machine;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;

public abstract class TBMMachineBase implements TBMMachine {

    protected final Set<Entity> entities = new HashSet<>();
    private final Set<Block> allowedBlocks;
    private final String id;
    private final String name;

    protected TBMMachineBase(String id, String name,
            Collection<? extends Block> allowedBlocks) {
        this.id = id;
        this.name = name;
        this.allowedBlocks = ImmutableSet.copyOf(allowedBlocks);
    }

    protected TBMMachineBase(String id, String name, Block... allowedBlocks) {
        this(id, name, ImmutableSet.copyOf(allowedBlocks));
    }

    @Override
    public String getId() {
        return this.id;
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
