package com.techshroom.mods.tbm.entity;

import java.util.Optional;

import net.minecraft.util.EnumFacing;

public enum MovingState {
    DOWN(EnumFacing.DOWN), UP(EnumFacing.UP), NORTH(EnumFacing.NORTH),
    SOUTH(EnumFacing.SOUTH), WEST(EnumFacing.WEST), EAST(EnumFacing.EAST),
    HALTED(null);

    private final Optional<EnumFacing> facing;

    MovingState(EnumFacing facing) {
        this.facing = Optional.ofNullable(facing);
    }

    public Optional<EnumFacing> getFacing() {
        return this.facing;
    }

    public boolean hasMotion() {
        return this.facing.isPresent();
    }

}
