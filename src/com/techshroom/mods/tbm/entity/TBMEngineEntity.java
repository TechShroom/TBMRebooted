package com.techshroom.mods.tbm.entity;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.techshroom.mods.tbm.block.tile.TBMEngineTile;
import com.techshroom.mods.tbm.gui.container.ContainerTBMEngine;

public class TBMEngineEntity extends
        TBMEntity<ContainerTBMEngine, TBMEngineTile> {
    private TBMEngineTile guiSource;

    public TBMEngineEntity(World w) {
        super(w);
    }

    public TBMEngineEntity withTile(TBMEngineTile tile) {
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
    public boolean providesGUI() {
        return true;
    }

    @Override
    public int getGUIId() {
        return guiSource.getGUIId();
    }

    @Override
    public GuiScreen guiScreen(ContainerTBMEngine c) {
        return guiSource.guiScreen(c);
    }

    @Override
    public Container container(EntityPlayer player) {
        return guiSource.container(player);
    }

    @Override
    public TBMEngineTile convertToTile() {
        return guiSource;
    }
}
