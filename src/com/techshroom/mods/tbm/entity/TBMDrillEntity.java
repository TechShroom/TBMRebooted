package com.techshroom.mods.tbm.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.techshroom.mods.tbm.block.tile.TBMDrillTile;

public class TBMDrillEntity extends Entity {

    public TBMDrillEntity(World p_i1582_1_) {
        super(p_i1582_1_);
    }

    public TBMDrillEntity withTile(TBMDrillTile tile) {
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
