package com.techshroom.mods.tbm.entity;

import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.techshroom.mods.tbm.block.tile.TBMEjectTile;

public class TBMEjectEntity extends TBMEntity<Container, TBMEjectTile> {
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

    @Override
    public TBMEjectTile convertToTile() {
        return null;
    }

    @Override
    public boolean providesGUI() {
        return false;
    }
}
