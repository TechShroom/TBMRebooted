package com.techshroom.mods.tbm.entity;

import static com.techshroom.mods.tbm.TBMMod.store_get;
import net.minecraft.block.Block;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.techshroom.mods.tbm.block.tile.TBMEjectTile;

public class TBMEjectEntity extends TBMEntity<Container, TBMEjectTile> {
    private TBMEjectTile back;
    
    public TBMEjectEntity(World p_i1582_1_) {
        super(p_i1582_1_);
    }

    public TBMEjectEntity withTile(TBMEjectTile tile) {
        back = tile;
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
        return back;
    }

    @Override
    public boolean providesGUI() {
        return false;
    }
    
    @Override
    public Block blockBase() {
        return store_get("ejecter");
    }
}
