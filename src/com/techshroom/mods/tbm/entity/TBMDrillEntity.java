package com.techshroom.mods.tbm.entity;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.techshroom.mods.tbm.block.tile.TBMDrillTile;
import com.techshroom.mods.tbm.gui.container.ContainerTBMDrill;

public class TBMDrillEntity extends TBMEntity<ContainerTBMDrill, TBMDrillTile> {
    private TBMDrillTile guiSource;

    public TBMDrillEntity(World w) {
        super(w);
    }

    public TBMDrillEntity withTile(TBMDrillTile tile) {
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
    public GuiScreen guiScreen(ContainerTBMDrill c) {
        return guiSource.guiScreen(c);
    }

    @Override
    public Container container(EntityPlayer player) {
        return guiSource.container(player);
    }

    @Override
    public TBMDrillTile convertToTile() {
        return guiSource;
    }
}
