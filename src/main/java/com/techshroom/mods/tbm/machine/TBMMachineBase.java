package com.techshroom.mods.tbm.machine;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public abstract class TBMMachineBase implements TBMMachine {

    protected final Set<UUID> entities = new HashSet<>();
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
    public boolean isBlockAllowed(IBlockState state) {
        return this.allowedBlocks.contains(state.getBlock());
    }

    @Override
    public void trackEntity(UUID e) {
        this.entities.add(e);
    }

    @Override
    public void untrackEntity(UUID e) {
        this.entities.remove(e);
    }

    @Override
    public Set<UUID> getTrackedEntities() {
        return ImmutableSet.copyOf(this.entities);
    }

}
