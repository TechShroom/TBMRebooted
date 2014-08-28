package com.techshroom.mods.tbm.entity;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.techshroom.mods.tbm.block.tile.TBMCPUTile;

public class TBMCPUEntity extends TBMEntity<Container, TBMCPUTile> {
    private TBMCPUTile guiSource;

    public TBMCPUEntity(World p_i1582_1_) {
        super(p_i1582_1_);
    }

    public TBMCPUEntity withTile(TBMCPUTile tile) {
        guiSource = tile;
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
    public TBMCPUTile convertToTile() {
        return guiSource;
    }

    @Override
    public boolean providesGUI() {
        return true;
    }

    @Override
    public Container container(EntityPlayer player) {
        return guiSource.container(player);
    }

    @Override
    public GuiScreen guiScreen(Container c) {
        return guiSource.guiScreen(null);
    }
}
