package com.techshroom.mods.tbm.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.techshroom.mods.tbm.block.tile.TBMEjectTile;

public class TBMEjectEntity extends Entity {
    public TBMEjectEntity(World p_i1582_1_) {
        super(p_i1582_1_);
    }

    public TBMEjectEntity withTile(TBMEjectTile tile) {
        return this;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
    }
}
